package com.autumn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author autumn
 * @desc 文件实体
 * @date 2025年05月19日
 */
@Data
@TableName("upload_file")
public class UploadFile {

    /**
     * 文件md5
     */
    @TableId(value = "file_md5", type = IdType.INPUT)
    String fileMd5;

    /**
     * 文件名
     */
    String fileName;

    /**
     * 文件路径
     */
    String filePath;

    /**
     * 文件类型
     */
    String fileType;

    /**
     * 文件浏览地址
     */
    String reviewUrl;


}
