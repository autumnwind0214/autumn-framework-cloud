package com.autumn.mybatis.split.aspect;

import com.autumn.mybatis.split.annotation.NeedSplitParam;
import com.autumn.mybatis.split.annotation.SplitWorkAnnotation;
import com.autumn.mybatis.split.enums.ThreadPoolEnum;
import com.autumn.mybatis.split.handle.HandleReturn;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author autumn
 * @desc 使用AOP实现拆分多线程并发调用合并逻辑
 * @date 2025/5/25 15:41
 **/
@Aspect
@Component
@Slf4j
public class SplitWorkAspect {

    @Pointcut("@annotation(com.autumn.mybatis.split.annotation.SplitWorkAnnotation)")
    public void needSplit() {
    }

    @Around("needSplit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        SplitWorkAnnotation splitWorkAnnotation = targetMethod.getAnnotation(SplitWorkAnnotation.class);
        Object[] args = joinPoint.getArgs();

        assert splitWorkAnnotation != null;
        int splitLimit = splitWorkAnnotation.splitLimit();
        int splitGroupNum = splitWorkAnnotation.splitGroupNum();
        if (args == null || args.length == 0 || splitLimit <= splitGroupNum) {
            return joinPoint.proceed();
        }

        int needSplitParamIndex = -1;
        for (int i = 0; i < targetMethod.getParameters().length; i++) {
            Parameter parameter = targetMethod.getParameters()[i];
            NeedSplitParam needSplitParam = parameter.getAnnotation(NeedSplitParam.class);
            if (needSplitParam != null) {
                needSplitParamIndex = i;
                break;
            }
        }

        if (needSplitParamIndex == -1) {
            return joinPoint.proceed();
        }
        Object needSplitParam = args[needSplitParamIndex];


        //只能处理Object[] 和 Collection
        if (!(needSplitParam instanceof Object[]) && !(needSplitParam instanceof List) && !(needSplitParam instanceof Set)) {
            return joinPoint.proceed();
        }
        //如果目标参数长度小于拆分下限跳过
        boolean notMeetSplitLen = (needSplitParam instanceof Object[] && ((Object[]) needSplitParam).length <= splitLimit)
                || (needSplitParam instanceof List && ((List<?>) needSplitParam).size() <= splitLimit)
                || (needSplitParam instanceof Set && ((Set<?>) needSplitParam).size() <= splitLimit);
        if (notMeetSplitLen) {
            return joinPoint.proceed();
        }

        // 去重，这一步看情况也可以不要
        if (needSplitParam instanceof List<?> list) {
            if (list.size() > 1) {
                needSplitParam = new ArrayList<>(new HashSet<>(list));
            }
        }
        //算出拆分成几批次
        int batchNum = getBatchNum(needSplitParam, splitGroupNum);
        if (batchNum == 1) {
            return joinPoint.proceed();
        }
        CompletableFuture<?>[] futures = new CompletableFuture[batchNum];
        ThreadPoolEnum threadPool = splitWorkAnnotation.setThreadPool();
        if (threadPool == null) {
            return joinPoint.proceed();
        }

        try {
            for (int currentBatch = 0; currentBatch < batchNum; currentBatch++) {
                int finalNeedSplitParamIndex = needSplitParamIndex;
                int finalCurrentBatch = currentBatch;
                Object finalNeedSplitParam = needSplitParam;
                futures[currentBatch] = CompletableFuture.supplyAsync(() -> {
                    Object[] dest = new Object[args.length];
                    //这一步很重要！！！因为多线程运行不能用原理的参数列表了，不然会导致混乱
                    System.arraycopy(args, 0, dest, 0, args.length);
                    try {
                        //将其他参数保持不变，将需要拆分的参数替换为part参数
                        dest[finalNeedSplitParamIndex] = getPartParam(finalNeedSplitParam, splitGroupNum, finalCurrentBatch);
                        return joinPoint.proceed(dest);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, threadPool.getThreadPoolExecutor());
            }
            CompletableFuture<Void> all = CompletableFuture.allOf(futures);
            all.get();
            Class<? extends HandleReturn<?>> handleReturn = splitWorkAnnotation.handlerReturnClass();

            List<Object> resultList = new ArrayList<>(futures.length);
            for (CompletableFuture<?> future : futures) {
                resultList.add(future.get());
            }
            //获取到每个part的结果然后调用处理函数
            return handleReturn.getDeclaredMethods()[0].invoke(handleReturn.getDeclaredConstructor().newInstance(), resultList);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取批次数目
     */
    public Integer getBatchNum(Object needSplitParam, Integer splitGroupNum){
        if (needSplitParam instanceof Object[] splitParam) {
            return splitParam.length % splitGroupNum == 0 ? splitParam.length / splitGroupNum : splitParam.length / splitGroupNum + 1;
        } else if (needSplitParam instanceof Collection<?> splitParam) {
            return splitParam.size() % splitGroupNum == 0 ? splitParam.size() / splitGroupNum : splitParam.size() / splitGroupNum + 1;
        } else {
            return 1;
        }
    }

    /**
     * 获取当前批次参数
     */
    public Object getPartParam(Object needSplitParam, Integer splitGroupNum, Integer batch)throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (needSplitParam instanceof Object[] splitParam) {
            int end = Math.min((batch + 1) * splitGroupNum, splitParam.length);
            return Arrays.copyOfRange(splitParam, batch * splitGroupNum, end);
        } else if (needSplitParam instanceof List<?> splitParam) {
            int end = Math.min((batch + 1) * splitGroupNum, splitParam.size());
            return splitParam.subList(batch * splitGroupNum, end);
        } else if (needSplitParam instanceof Set) {
            List splitParam = new ArrayList<>((Set) needSplitParam);
            int end = Math.min((batch + 1) * splitGroupNum, splitParam.size());
            //参数具体化了
            Set<?> set = (Set<?>) needSplitParam.getClass().getDeclaredConstructor().newInstance();
            set.addAll(splitParam.subList(batch * splitGroupNum, end));
            return set;
        } else {
            return null;
        }
    }
}
