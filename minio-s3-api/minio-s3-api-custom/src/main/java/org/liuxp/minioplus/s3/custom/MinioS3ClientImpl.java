package org.liuxp.minioplus.s3.custom;

import lombok.extern.slf4j.Slf4j;
import org.liuxp.minioplus.common.config.MinioPlusProperties;
import org.liuxp.minioplus.s3.def.ListParts;
import org.liuxp.minioplus.s3.def.MinioS3Client;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Repository
public class MinioS3ClientImpl implements MinioS3Client {

    @Resource
    private MinioPlusProperties properties;

    @Override
    public Boolean bucketExists(String bucketName) {
        return null;
    }

    @Override
    public void makeBucket(String bucketName) {

    }

    @Override
    public String createMultipartUpload(String bucketName, String objectName) {
        return null;
    }

    @Override
    public Boolean completeMultipartUpload(String bucketName, String objectName, String uploadId, List<ListParts.Part> parts) {
        return null;
    }

    @Override
    public ListParts listParts(String bucketName, String objectName, Integer maxParts, String uploadId) {

        // 获取失败时，拼一个空的返回值
        return null;
    }

    @Override
    public String getUploadObjectUrl(String bucketName, String objectName, String uploadId, String partNumber) {
        return null;
    }

    @Override
    public String getDownloadUrl(String fileName, String contentType, String bucketName, String objectName) {
        return null;
    }

    @Override
    public String getPreviewUrl(String contentType, String bucketName, String objectName) {
        return null;
    }

    @Override
    public Boolean putObject(String bucketName, String objectName, InputStream stream, long size, String contentType) {
        return null;
    }

    @Override
    public byte[] getObject(String bucketName, String objectName) {
        return new byte[0];
    }

    @Override
    public void removeObject(String bucketName, String objectName) {

    }
}
