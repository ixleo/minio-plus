package org.liuxp.minioplus.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.liuxp.minioplus.model.vo.FileCheckResultVo;

import java.util.List;

/**
 * 创建上传链接请求参数
 *
 * @author contact@liuxp.me
 * @date 2023/6/29
 */
@Getter
@Setter
@ToString
public class CreateUploadUrlRespBO {

    /**
     * 文件id-必填
     */
    private String fileKey;
    /**
     * 分块数量-可选,分片后必须重新赋值
     * 默认1
     */
    private Integer partCount = 1;
    /**
     * 切片上传任务id
     */
    private String uploadTaskId;
    /**
     * minio存储信息-可选,使用minio存储引擎必填
     */
    private MinioStorageBO minioStorage;
    /**
     * 分片信息-必填
     */
    List<FileCheckResultVo.Part> parts;

    /**
     * minio存储信息
     *
     * @author contact@liuxp.me
     * @date 2023/06/29
     */
    @Setter
    @Getter
    @ToString
    public static class MinioStorageBO {
        /**
         * 桶名字
         */
        private String bucketName;
        /**
         * 文件存储路径
         */
        private String storagePath;
    }
}
