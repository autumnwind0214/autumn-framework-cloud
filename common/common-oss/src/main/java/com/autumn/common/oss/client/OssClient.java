package com.autumn.common.oss.client;

import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.oss.result.OSSResult;
import com.autumn.common.oss.utils.OSSUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .build();
        // 上传文件
        minioClient.putObject(build);

        result.setFilename(filename);
        result.setReviewUrl(minioProperties.getMediaUrl() + folderPath + filename);
        result.setFilePath(folderPath + filename);
        return result;
    }

    public OSSResult uploadFile(MultipartFile file, String extension) throws Exception {
        OSSResult result = new OSSResult();
        // 生成唯一文件名
        String filename = OSSUtils.getFilename(extension);
        // 获取文件上传目录
        String folderPath = OSSUtils.getFileFolderPath();

        // 构建PutObjectArgs对象
        PutObjectArgs build = PutObjectArgs.builder()
                .bucket(minioProperties.getMediaBucket())
                .object(folderPath + filename)
                .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();

        minioClient.putObject(build);

        result.setFilename(filename);
        result.setReviewUrl(minioProperties.getMediaUrl() + folderPath + filename);
        result.setFilePath(folderPath + filename);
        return result;
    }

    public Boolean uploadFile(String localFilePath, String filePath, String contentType) {
        UploadObjectArgs build = null;
        try {
            build = UploadObjectArgs.builder()
                    .bucket(minioProperties.getMediaBucket())
                    .contentType(contentType)
                    .filename(localFilePath)
                    .object(filePath).build();
            minioClient.uploadObject(build);
            return true;
        } catch (Exception e) {
            log.error("文件上传失败", e);
        }
        return false;
    }

    public String getFileMd5(String filepath) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getMediaBucket())
                            .object(filepath)
                            .build()
            );
            return stat.etag();

        } catch (Exception e) {
            log.error("获取文件md5失败", e);
            return "";
        }
    }


    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return true 存在 false 不存在
     */
    public Boolean checkFile(String filePath) {
        // 获取对象的元数据以确认其是否存在
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(minioProperties.getMediaBucket())
                    .object(filePath)
                    .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String mergeChunk(String path, String filePath, String fileName, long chunkTotal) {
        List<ComposeSource> sources =
                Stream.iterate(0, item -> ++item)
                        .limit(chunkTotal)
                        .map(item -> ComposeSource.builder().bucket(minioProperties.getMediaBucket())
                                .object(path + item).build())
                        .toList();
        // 指定合并后的objectName等信息
        ComposeObjectArgs build = ComposeObjectArgs.builder()
                .bucket(minioProperties.getMediaBucket())
                .object(filePath)
                .sources(sources)
                .build();
        try {
            minioClient.composeObject(build);
            log.info("文件合并成功,filePath:{}", filePath);
            return minioProperties.getMediaUrl() + filePath;
        } catch (Exception e) {
            log.error("文件合并失败", e);
            throw new AutumnException(ResultCodeEnum.FILE_MERGE_ERROR);
        }
    }

    public void removeChunkFiles(String chunkFileFolderPath, long chunkTotal) {
        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i))))
                    .collect(Collectors.toList());

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("video").objects(deleteObjects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            results.forEach(r -> {
                try {
                    DeleteError deleteError = r.get();
                } catch (Exception e) {
                    log.error("删除分块文件失败,chunkFileFolderPath:{}", chunkFileFolderPath, e);
                    throw new RuntimeException(e);
                }

            });
        } catch (Exception e) {
            log.error("清楚分块文件失败,chunkFileFolderPath:{}", chunkFileFolderPath, e);
            throw new RuntimeException(e);
        }
    }
}
