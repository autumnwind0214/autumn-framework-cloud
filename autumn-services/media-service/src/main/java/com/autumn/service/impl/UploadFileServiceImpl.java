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

    @Override
    public Boolean checkFile(String fileMd5) {
        return uploadFileMapper.selectById(fileMd5) != null;
    }

    @Override
    public Boolean checkChunk(String fileMd5, int chunk) {
        // 1.得到分块文件得存储目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // todo 2.检查分块文件是否存在
        return null;
    }

    @Override
    public Boolean uploadChunk(MultipartFile file, String fileMd5, int chunk) {
        // 1.得到分块文件得存储目录

        // 2.构建文件分块路径

        // 3.文件上传
        return null;
    }

    @Override
    public Boolean mergeChunk(String fileMd5, String fileName, long chunkTotal) {
        // 1.获取分块文件所在目录
        String chunkDirPath = getChunkFileFolderPath(fileMd5);

        // 2.检查分块文件得完整性

        // 3.合并文件

        // 4.检查合并文件得完整性

        // 5.删除分块文件

        // 6.保存文件信息
        return true;
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

    /**
     * 获取分块文件的目录
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + File.separator + fileMd5.charAt(1) + File.separator + fileMd5 + File.separator + "chunk" + File.separator;
    }
}
