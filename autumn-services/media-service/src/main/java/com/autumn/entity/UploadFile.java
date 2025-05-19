package com.autumn.entity;

import com.autumn.mybatis.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author autumn
 * @desc 文件实体
 * @date 2025年05月19日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("upload_file")
public class UploadFile extends BaseEntity {

    String fileName;

    String filePath;

    String fileType;

    String fileMd5;
}
