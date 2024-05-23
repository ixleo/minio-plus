package org.liuxp.minioplus.core.common.dto.minio;

import com.google.common.collect.Multimap;
import io.minio.messages.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建分片上传需要的参数
 *
 * @author contact@liuxp.me
 * @date 2023/06/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipartUploadCreateDTO {
    /**
     * 桶名字
     */
    private String bucketName;
    /**
     * 区名字
     */
    private String region;
    /**
     * 对象名字
     */
    private String objectName;
    /**
     * 请求头
     */
    private Multimap<String, String> headers;
    /**
     * 查询参数
     */
    private Multimap<String, String> extraQueryParams;
    /**
     * minio的id
     */
    private String uploadId;
    /**
     * 最大块数量
     */
    private Integer maxParts;
    /**
     * 块信息
     */
    private Part[] parts;
    /**
     * 块编号
     */
    private Integer partNumberMarker;


}