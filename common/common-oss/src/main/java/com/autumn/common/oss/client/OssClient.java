package com.autumn.common.oss.client;

import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.oss.constant.OSSConstant;
import com.autumn.common.oss.result.OSSResult;
import com.autumn.common.oss.utils.OSSUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.autumn.common.oss.properties.MinioProperties;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author autumn
 * @desc 对象存储client
 * @date 2025年05月14日
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssClient {

    private final MinioProperties minioProperties;

    private final MinioClient minioClient;

    /**
     * 判断bucket是否存在，不存在就创建
     *
     * @param bucket 存储桶
     */
    public void existBucket(String bucket) {
        BucketExistsArgs build = BucketExistsArgs.builder().bucket(bucket).build();
        try {
            boolean exists = minioClient.bucketExists(build);
            if (!exists) {
                this.makeBucket(bucket);
            }
        } catch (Exception e) {
            log.error("bucket判断失败", e);
            throw new AutumnException(ResultCodeEnum.INTERNAL_SERVER_ERROR, "bucket判断失败: " + bucket);
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucket 存储桶名称
     */
    private void makeBucket(String bucket) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            log.error("bucket创建失败", e);
            throw new AutumnException(ResultCodeEnum.INTERNAL_SERVER_ERROR, "bucket创建失败: " + bucket);
        }
    }

    /**
     * 删除bucket
     */
    public Boolean removeBucket(String bucket) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            log.error("bucket删除失败", e);
            throw new AutumnException(ResultCodeEnum.INTERNAL_SERVER_ERROR, "bucket删除失败: " + bucket);
        }
        return true;
    }

    /**
     * 图片上传
     * base64格式图片上传
     */
    public OSSResult uploadImg(String base64) throws Exception {
        OSSResult result = new OSSResult();
        String[] parts = base64.split(",");
        // 提取图片类型
        String imageType = parts[0].split("/")[1].split(";")[0];
        byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put(OSSConstant.TAG_KEY, DigestUtils.md5Hex(imageBytes));

        // 生成唯一文件名
        String filename = OSSUtils.getFilename(imageType);
        // 获取图片上传目录
        String folderPath = OSSUtils.getImgFolderPath();

        // 构建PutObjectArgs对象
        PutObjectArgs build = PutObjectArgs.builder()
                .bucket(minioProperties.getMediaBucket())
                .object(folderPath + filename)
                .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                .contentType("image/" + imageType)
                .tags(tagMap)
                .build();
        // 上传文件
        minioClient.putObject(build);

        result.setFilename(filename);
        result.setReviewUrl(minioProperties.getMediaUrl() + folderPath + filename);
        result.setFilePath(folderPath + filename);
        return result;
    }

    public OSSResult uploadImg(MultipartFile file, String extension) throws Exception {
        OSSResult result = new OSSResult();
        // 生成唯一文件名
        String filename = OSSUtils.getFilename(extension);
        // 获取图片上传目录
        String folderPath = OSSUtils.getImgFolderPath();

        Map<String, String> tagMap = new HashMap<>();
        tagMap.put(OSSConstant.TAG_KEY, DigestUtils.md5Hex(file.getBytes()));

        // 构建PutObjectArgs对象
        PutObjectArgs build = PutObjectArgs.builder()
                .bucket(minioProperties.getMediaBucket())
                .object(folderPath + filename)
                .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                .contentType(file.getContentType())
                .tags(tagMap)
                .build();

        minioClient.putObject(build);

        result.setFilename(filename);
        result.setReviewUrl(minioProperties.getMediaUrl() + folderPath + filename);
        result.setFilePath(folderPath + filename);
        return result;
    }

    public String getFileMd5(String filepath) throws Exception {
        Tags tags = minioClient.getObjectTags(
                GetObjectTagsArgs.builder().bucket(minioProperties.getMediaBucket()).object(filepath).build());
        return tags.get().get(OSSConstant.TAG_KEY);
    }


}
