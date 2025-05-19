package com.autumn.service.impl;

import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.oss.client.OssClient;
import com.autumn.common.oss.result.OSSResult;
import com.autumn.common.oss.utils.OSSUtils;
import com.autumn.constant.FileTypeConstant;
import com.autumn.entity.UploadFile;
import com.autumn.mapper.UploadFileMapper;
import com.autumn.model.vo.UploadFileVo;
import com.autumn.service.IUploadFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * @author autumn
 * @desc 文件上传服务实现类
 * @date 2025年05月19日
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    private final UploadFileMapper uploadFileMapper;

    private final OssClient ossClient;

    @Override
    public UploadFileVo uploadImgFile(MultipartFile file) {
        String extension = OSSUtils.getExtension(file);
        checkType(extension, "img");
        try {
            UploadFileVo uploadFileVo = new UploadFileVo();
            // 获取文件扩展名

            OSSResult ossResult = ossClient.uploadImg(file, extension);
            uploadFileVo.setFilename(ossResult.getFilename());
            uploadFileVo.setReviewUrl(ossResult.getReviewUrl());
            return uploadFileVo;
        } catch (Exception e) {
            throw new AutumnException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public UploadFileVo uploadImgBase64(String base64) {
        try {
            OSSResult ossResult = ossClient.uploadImg(base64);
            UploadFileVo uploadFileVo = new UploadFileVo();
            uploadFileVo.setFilename(ossResult.getFilename());
            uploadFileVo.setReviewUrl(ossResult.getReviewUrl());
            return uploadFileVo;
        } catch (Exception e) {
            throw new AutumnException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    private void checkType(String extension, String type) {
        switch (type) {
            case "img" -> {
                if (!Arrays.asList(FileTypeConstant.IMG_TYPE).contains(extension)) {
                    throw new AutumnException(ResultCodeEnum.IMG_TYPE_ERROR);
                }
            }
            case "video" -> {
                if (!Arrays.asList(FileTypeConstant.VIDEO_TYPE).contains(extension)) {
                    throw new AutumnException(ResultCodeEnum.VIDEO_TYPE_ERROR);
                }
            }
            case "audio" -> {
                if (!Arrays.asList(FileTypeConstant.AUDIO_TYPE).contains(extension)) {
                    throw new AutumnException(ResultCodeEnum.AUDIO_TYPE_ERROR);
                }
            }
            case "doc" -> {
                if (!Arrays.asList(FileTypeConstant.DOC_TYPE).contains(extension)) {
                    throw new AutumnException(ResultCodeEnum.DOC_TYPE_ERROR);
                }
            }
        }

    }
}
