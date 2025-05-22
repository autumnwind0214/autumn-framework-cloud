package com.autumn.service.impl;

import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.oss.client.OssClient;
import com.autumn.common.oss.result.OSSResult;
import com.autumn.common.oss.utils.OSSUtils;
import com.autumn.constant.FileTypeConstant;
import com.autumn.entity.MediaFile;
import com.autumn.mapper.MediaFileMapper;
import com.autumn.model.vo.UploadFileVo;
import com.autumn.service.IMediaFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFile> implements IMediaFileService {

    private final MediaFileMapper mediaFileMapper;

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
            uploadFileVo.setOriginName(file.getOriginalFilename());
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
        return mediaFileMapper.selectById(fileMd5) != null;
    }

    @Override
    public Boolean checkChunk(String fileMd5, int chunk) {
        // 1.根据md5得到分块文件所在目录的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // 2.检查分块文件是否存在
        return ossClient.checkFile(chunkFileFolderPath + chunk);
    }

    @Override
    public Boolean uploadChunk(String localFilePath, String fileMd5, int chunk) {
        // 1.获取mimeType
        String mimeType = getMimeType(null);
        // 2.构建文件分块路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;

        // 3.文件上传
        return ossClient.uploadFile(localFilePath, chunkFilePath, mimeType);
    }

    @Override
    public Boolean mergeChunk(String fileMd5, String fileName, long chunkTotal) {
        // 1.获取分块文件所在目录
        String path = getChunkFileFolderPath(fileMd5);

        // 2.获取合并后文件的路径
        String filePath = getFilePathByMd5(fileMd5, OSSUtils.getExtension(fileName));
        // 3.找到分开文件调用minio的sdk进行文件合并
        String reviewUrl = ossClient.mergeChunk(path, filePath, fileName, chunkTotal);

        // 4.获取合并后文件的md5值 并比较
        String md5Hex = ossClient.getFileMd5(filePath);
        if (!md5Hex.equals(fileMd5)) {
            throw new AutumnException(ResultCodeEnum.FILE_MERGE_ERROR);
        }

        // 5.构造保存文件信息
        buildMediaFile(fileMd5, fileName, filePath, reviewUrl);

        // 6.删除分块文件
        ossClient.removeChunkFiles(path, chunkTotal);
        return true;
    }

    private void buildMediaFile(String fileMd5, String fileName, String filePath, String reviewUrl) {
        MediaFile mediaFile = mediaFileMapper.selectById(fileMd5);
        if (mediaFile == null) {
            mediaFile = new MediaFile();
            mediaFile.setFileMd5(fileMd5);
            mediaFile.setFileName(fileName);
            mediaFile.setFilePath(filePath);
            mediaFile.setReviewUrl(reviewUrl);
            mediaFileMapper.insert(mediaFile);
        } else {
            mediaFile.setFileName(fileName);
            mediaFile.setFilePath(filePath);
            mediaFile.setReviewUrl(reviewUrl);
            mediaFileMapper.updateById(mediaFile);
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

    /**
     * 获取分块文件的目录
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + File.separator + fileMd5.charAt(1) + File.separator + fileMd5 + File.separator + "chunk" + File.separator;
    }

    /**
     * 得到合并后的文件的地址
     */
    public static String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + extension;
    }

    // 根据扩展名获取mimeType
    private String getMimeType(String extension) {
        if (extension == null) {
            extension = "";
        }
        // 根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        // 通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }
}
