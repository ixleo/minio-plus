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
 * @since  2024/05/29
 */
@Getter
@Setter
@ToString
public class CreateUploadUrlRespBO {

    /**
     * 桶名字
     */
    private String bucketName;
    /**
     * 文件存储路径
     */
    private String storagePath;

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
     * 分片信息-必填
     */
    List<FileCheckResultVo.Part> parts;

}