package com.autumn.auth.local;

/**
 * @author autumn
 * @desc 登录线程变量
 * @date 2025年05月14日
 */
public class GrantThreadLocal {

    private static final ThreadLocal<String> GRANT_TYPE = new ThreadLocal<>();

    public static String getGrantType() {
        return GRANT_TYPE.get();
    }

    public static void setGrantType(String type) {
        GRANT_TYPE.set(type);
    }

    public static void clear() {
        GRANT_TYPE.remove();
    }
}
