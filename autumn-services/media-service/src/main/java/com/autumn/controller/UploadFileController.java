package com.autumn.controller;

import com.autumn.model.vo.UploadFileVo;
import com.autumn.service.IUploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author autumn
 * @desc 文件上传
 * @date 2025年05月19日
 */
@RestController
@RequestMapping("/upload/file")
@RequiredArgsConstructor
public class UploadFileController {

    private final IUploadFileService uploadFileService;

    @PostMapping("/img")
    public UploadFileVo uploadImgFile(@RequestParam("file") MultipartFile file) {
        return uploadFileService.uploadImgFile(file);
    }

    @PostMapping("/imgBase64")
    public UploadFileVo uploadImgFileBase64(@RequestParam("file") String base64) {
        return uploadFileService.uploadImgBase64(base64);
    }
}
