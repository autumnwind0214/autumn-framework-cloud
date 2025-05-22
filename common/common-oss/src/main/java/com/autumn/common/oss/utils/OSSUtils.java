package com.autumn.common.oss.utils;

import com.autumn.common.core.utils.UuidUtils;
import com.autumn.common.oss.constant.OSSConstant;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author autumn
 * @desc oss工具类
 * @date 2025年05月14日
 */
public class OSSUtils {

    /**
     * 获取文件后缀
     */
    public static String getExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    // 根据扩展名获取mineType
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

    // 生成唯一文件名
    public static String getFilename(String extension) {
        return UuidUtils.getUuid() + "." + extension;
    }

    // 获取图片文件目录
    public static String getImgFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return OSSConstant.IMG + sdf.format(new Date()).replace("-", "/") + "/";
    }

}
