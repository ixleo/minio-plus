package org.liuxp.minioplus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 文件元数据信息VO
 *
 * @author contact@liuxp.me
 * @since 2023-06-26
 **/
@Getter
@Setter
@ApiModel("文件元数据信息")
public class FileMetadataInfoVo {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("文件KEY")
    private String fileKey;

    @ApiModelProperty("文件md5")
    private String fileMd5;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("MIME类型")
    private String fileMimeType;

    @ApiModelProperty("文件后缀")
    private String fileSuffix;

    @ApiModelProperty("文件长度")
    private Long fileSize;

    @ApiModelProperty("存储引擎")
    private String storageEngine;

    @ApiModelProperty("存储桶")
    private String storageBucket;

    @ApiModelProperty("存储路径")
    private String storagePath;

    @ApiModelProperty("minio切片任务id")
    private String uploadTaskId;

    @ApiModelProperty("状态 0:未完成 1:已完成")
    private Boolean isFinished;

    @ApiModelProperty("是否分块 0:否 1:是")
    private Boolean isPart;

    @ApiModelProperty("分块数量")
    private Integer partNumber;

    @ApiModelProperty("预览图 0:无 1:有")
    private Boolean isPreview;

    @ApiModelProperty("是否私有 0:否 1:是")
    private Boolean isPrivate;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}