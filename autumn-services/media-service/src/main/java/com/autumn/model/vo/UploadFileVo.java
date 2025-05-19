package com.autumn.model.vo;

import lombok.Data;

/**
 * @author autumn
 * @desc 文件VO
 * @date 2025年05月19日
 */
@Data
public class UploadFileVo {

    /**
     * 文件浏览路径
     */
    private String reviewUrl;

    /**
     * 原始文件名
     */
    private String originName;

    /**
     * 文件名
     */
    private String filename;
}
