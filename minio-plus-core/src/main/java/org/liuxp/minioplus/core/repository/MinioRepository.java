package org.liuxp.minioplus.core.repository;

import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.ObjectWriteResponse;
import org.liuxp.minioplus.core.common.context.MultipartUploadCreateDTO;
import org.liuxp.minioplus.core.repository.impl.CustomMinioClient;

import java.io.InputStream;
import java.util.Map;

/**
 * MinIO文件存储引擎接口定义
 *
 * @author contact@liuxp.me
 * @since  2023/07/06
 */
public interface MinioRepository {

    /**
     * 取得MinioClient
     * @return CustomMinioClient
     */
    CustomMinioClient getClient();

    /**
     * 创建桶
     * @param bucketName 桶名称
     */
    void createBucket(String bucketName);

    /**
     * 创建分片请求,获取uploadId
     * @param multipartUploadCreate 创建分片上传需要的参数
     * @return 分片结果
     */
    CreateMultipartUploadResponse createMultipartUpload(MultipartUploadCreateDTO multipartUploadCreate);

    /**
     * 合并分片
     * @param multipartUploadCreate 分片参数
     * @return 是否成功
     */
    ObjectWriteResponse completeMultipartUpload(MultipartUploadCreateDTO multipartUploadCreate);

    /**
     * 获取分片信息列表
     * @param multipartUploadCreate 分片参数
     * @return 分片信息
     */
    ListPartsResponse listMultipart(MultipartUploadCreateDTO multipartUploadCreate);

    /**
     * 获得对象上传的url
     * @param bucketName  桶名称
     * @param objectName  对象名称
     * @param queryParams 查询参数
     * @return {@link String}
     */
    String getPresignedObjectUrl(String bucketName, String objectName, Map<String, String> queryParams);

    /**
     * 取得下载链接
     * @param fileName 文件全名含扩展名
     * @param contentType 数据类型
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     * @return 下载地址
     */
    String getDownloadUrl(String fileName, String contentType, String bucketName, String objectName);

    /**
     * 取得图片预览链接
     * @param contentType 数据类型
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     * @return 图片预览链接
     */
    String getPreviewUrl(String contentType, String bucketName, String objectName);

    /**
     * 写入文件
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     * @param stream 文件流
     * @param size 文件长度
     * @param contentType 文件类型
     * @return 是否成功
     */
    Boolean write(String bucketName, String objectName, InputStream stream, long size, String contentType);

    /**
     * 读取文件
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     * @return 文件流
     */
    byte[] read(String bucketName, String objectName);

    /**
     * 删除文件
     * @param bucketName 桶名称
     * @param objectName 对象名称含路径
     */
    void remove(String bucketName, String objectName);

}