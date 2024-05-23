package org.liuxp.minioplus.core.repository.impl;

import cn.hutool.core.io.IoUtil;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.liuxp.minioplus.config.MinioPlusProperties;
import org.liuxp.minioplus.core.common.dto.minio.MultipartUploadCreateDTO;
import org.liuxp.minioplus.core.common.enums.ResponseCodeEnum;
import org.liuxp.minioplus.core.common.exception.MinioPlusBusinessException;
import org.liuxp.minioplus.core.repository.MinioRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MinIO文件存储引擎接口定义实现类
 *
 * @author contact@liuxp.me
 * @since  2023/07/06
 */
@Slf4j
@Repository
public class MinioRepositoryImpl implements MinioRepository {

    private CustomMinioClient minioClient = null;

    @Resource
    private MinioPlusProperties properties;

    /**
     * 取得MinioClient
     * @return CustomMinioClient
     */
    @Override
    public CustomMinioClient getClient(){

        if(null==this.minioClient){
            MinioClient client = MinioClient.builder()
                    .endpoint(properties.getBackend())
                    .credentials(properties.getKey(), properties.getSecret())
                    .build();
            this.minioClient = new CustomMinioClient(client);
        }

        return this.minioClient;
    }

    /**
     * 创建桶
     *
     * @param bucketName bucket名称
     */
    @Override
    @SneakyThrows(Exception.class)
    public void createBucket(String bucketName) {
        boolean found = this.getClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            log.info("create bucket: [{}]", bucketName);
            this.getClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @Override
    public CreateMultipartUploadResponse createMultipartUpload(MultipartUploadCreateDTO multipartUploadCreate) {
        try {
            return this.getClient().createMultipartUpload(multipartUploadCreate.getBucketName(), multipartUploadCreate.getRegion(), multipartUploadCreate.getObjectName(), multipartUploadCreate.getHeaders(), multipartUploadCreate.getExtraQueryParams());
        } catch (Exception e) {
            log.error("文件分片获取上传编号失败:{}", e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "获取上传编号失败");
        }
    }

    /**
     * 合并分片
     *
     * @param multipartUploadCreate 分片参数
     * @return 是否成功
     */
    @Override
    public ObjectWriteResponse completeMultipartUpload(MultipartUploadCreateDTO multipartUploadCreate) {
        try {
            return this.getClient().completeMultipartUpload(multipartUploadCreate.getBucketName(), multipartUploadCreate.getRegion(), multipartUploadCreate.getObjectName(), multipartUploadCreate.getUploadId(), multipartUploadCreate.getParts(), multipartUploadCreate.getHeaders(), multipartUploadCreate.getExtraQueryParams());
        } catch (Exception e) {
            log.error("合并分片失败,uploadId:{},ObjectName:{},失败原因:{},", multipartUploadCreate.getUploadId(), multipartUploadCreate.getObjectName(), e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "合并分片失败");
        }
    }


    /**
     * 获取分片信息列表
     *
     * @param multipartUploadCreate 请求参数
     * @return {@link ListPartsResponse}
     */
    @Override
    public ListPartsResponse listMultipart(MultipartUploadCreateDTO multipartUploadCreate) {
        try {
            return this.getClient().listParts(multipartUploadCreate.getBucketName(), multipartUploadCreate.getRegion(), multipartUploadCreate.getObjectName(), multipartUploadCreate.getMaxParts(), multipartUploadCreate.getPartNumberMarker(), multipartUploadCreate.getUploadId(), multipartUploadCreate.getHeaders(), multipartUploadCreate.getExtraQueryParams());
        } catch (Exception e) {
            log.error("查询分片失败:{}", e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "查询分片失败");
        }
    }


    /**
     * 获得对象上传的url
     *
     * @param bucketName  桶名称
     * @param objectName  对象名称
     * @param queryParams 查询参数
     * @return {@link String}
     */
    @Override
    public String getPresignedObjectUrl(String bucketName, String objectName, Map<String, String> queryParams) {
        try {
            return this.getClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(properties.getUploadExpiry(), TimeUnit.MINUTES)
                            .extraQueryParams(queryParams)
                            .build());
        } catch (Exception e) {
            log.error("获取预签名URL失败:{}", e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "获取预签名URL失败");
        }
    }

    /**
     * 取得下载链接
     * @param fileName 文件全名含扩展名
     * @param contentType 数据类型
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     * @return 下载地址
     */
    @Override
    public String getDownloadUrl(String fileName, String contentType, String bucketName, String objectName) {

        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-disposition", "attachment;filename=\""+fileName+"\"");
        reqParams.put("response-content-type", contentType);

        try {
            return this.getClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(properties.getDownloadExpiry(), TimeUnit.MINUTES)
                            .extraQueryParams(reqParams)
                            .build());
        } catch (Exception e) {
            log.error("获取预签名下载URL失败:{}", e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "获取预签名下载URL失败");
        }
    }

    @Override
    public String getPreviewUrl(String contentType, String bucketName, String objectName) {

        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-type", contentType);
        reqParams.put("response-content-disposition", "inline");

        try {
            return this.getClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(properties.getDownloadExpiry(), TimeUnit.MINUTES)
                            .extraQueryParams(reqParams)
                            .build());
        } catch (Exception e) {
            log.error("获取预签名预览URL失败:{}", e.getMessage(), e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(), "获取预签名预览URL失败");
        }
    }

    @Override
    public Boolean write(String bucketName,String objectName, InputStream stream, long size, String contentType) {

        try{

            // 检查存储桶是否已经存在
            boolean isExist = this.getClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                // 创建存储桶。
                this.getClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            }

            // 使用putObject上传一个文件到存储桶中。
            this.getClient().putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream,size,0L)
                    .contentType(contentType)
                    .build());

        } catch (Exception e) {
            log.error("文件写入失败",e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(),"文件写入失败");
        }

        return true;
    }

    @Override
    public byte[] read(String bucketName,String objectName) {

        // 从远程MinIO服务读取文件流
        try (InputStream inputStream = this.getClient().getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build())) {
            // 文件流转换为字节码
            return IoUtil.readBytes(inputStream);
        } catch (Exception e) {
            log.error("文件读取失败",e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(),"文件读取失败");
        }

    }

    @Override
    public void remove(String bucketName, String objectName) {
        try {
            this.getClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            log.error("删除失败",e);
            throw new MinioPlusBusinessException(ResponseCodeEnum.FAIL.getCode(),"删除失败");
        }
    }


}