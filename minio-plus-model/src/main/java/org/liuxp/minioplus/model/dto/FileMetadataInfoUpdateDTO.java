package org.liuxp.minioplus.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件元数据信息修改入参
 * @author contact@liuxp.me
 * @since  2023-06-26
 **/
@Getter
@Setter
@ToString
@ApiModel("文件元数据信息修改入参")
public class FileMetadataInfoUpdateDTO {

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

    @ApiModelProperty("上传任务id,用于合并切片")
    private String uploadTaskId;
    
    @ApiModelProperty("状态 false:未完成 true:已完成")
    private Boolean isFinished;

    @ApiModelProperty("是否分块 false:否 true:是")
    private Boolean isPart;

    @ApiModelProperty("分块数量")
    private Integer partNumber;
    
    @ApiModelProperty("预览图 false:无 true:有")
    private Boolean isPreview;
    
    @ApiModelProperty("是否私有 false:否 true:是")
    private Boolean isPrivate;

    @ApiModelProperty("修改人")
    private String updateUser;

}