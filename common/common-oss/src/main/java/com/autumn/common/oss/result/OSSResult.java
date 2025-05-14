package com.autumn.common.oss.result;

import lombok.Data;

/**
 * @author autumn
 * @desc oss返回结果
 * @date 2025年05月14日
 */
@Data
public class OSSResult {

    /**
     * 文件浏览地址
     */
    String reviewUrl;

    /**
     * 文件名
     */
    String filename;
}
