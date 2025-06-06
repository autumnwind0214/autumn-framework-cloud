package com.autumn.service;

import com.autumn.entity.MediaFile;
import com.autumn.model.vo.UploadFileVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author autumn
 * @desc 上传文件接口
 * @date 2025年05月19日
 */
public interface IMediaFileService extends IService<MediaFile> {
    UploadFileVo uploadFile(MultipartFile file);

    UploadFileVo uploadImgBase64(String base64);

    Boolean checkFile(String fileMd5);

    Boolean checkChunk(String fileMd5, int chunk);

    Boolean uploadChunk(String localFilePath, String fileMd5, int chunk);

    Boolean mergeChunk(String fileMd5, String fileName, long chunkTotal);
}
