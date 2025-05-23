package com.autumn.controller;

import com.autumn.model.vo.UploadFileVo;
import com.autumn.service.IMediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author autumn
 * @desc 文件上传
 * @date 2025年05月19日
 */
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadFileController {

    private final IMediaFileService uploadFileService;

    @PostMapping("/file")
    public UploadFileVo uploadFile(@RequestParam("file") MultipartFile file) {
        return uploadFileService.uploadFile(file);
    }

    @PostMapping("/imgBase64")
    public UploadFileVo uploadImgFileBase64(@RequestParam("base64") String base64) {
        return uploadFileService.uploadImgBase64(base64);
    }

    /*========================================= 大文件上传 =========================================*/

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件md5
     * @return true:存在 false:不存在
     */
    @PostMapping("/checkFile")
    public Boolean checkFile(@RequestParam("fileMd5") String fileMd5) {
        return uploadFileService.checkFile(fileMd5);
    }

    /**
     * 检查分块是否存在
     *
     * @param fileMd5 文件md5
     * @return true:存在 false:不存在
     */
    @PostMapping("/checkChunk")
    public Boolean checkChunk(@RequestParam("fileMd5") String fileMd5,
                              @RequestParam("chunk") int chunk) {
        return uploadFileService.checkChunk(fileMd5, chunk);
    }

    /**
     * 上传分块
     *
     * @param file    文件
     * @param fileMd5 文件md5
     * @param chunk   分块索引
     * @return true:成功 false:失败
     */
    @PostMapping("/uploadChunk")
    public Boolean uploadChunk(@RequestParam("file") MultipartFile file,
                               @RequestParam("fileMd5") String fileMd5,
                               @RequestParam("chunk") int chunk) throws Exception{
        // 创建临时文件
        File tempFile = File.createTempFile("minio", ".temp");
        file.transferTo(tempFile);
        // 文件路径
        String localFilePath = tempFile.getAbsolutePath();
        return uploadFileService.uploadChunk(localFilePath, fileMd5, chunk);
    }

    /**
     * 合并分块
     *
     * @param fileMd5    文件md5
     * @param fileName   文件名
     * @param chunkTotal 分块总数
     * @return true:成功 false:失败
     */
    @PostMapping("/mergeChunk")
    public Boolean mergeChunk(@RequestParam("fileMd5") String fileMd5,
                              @RequestParam("fileName") String fileName,
                              @RequestParam("chunkTotal") long chunkTotal) {

        return uploadFileService.mergeChunk(fileMd5, fileName, chunkTotal);
    }
}
