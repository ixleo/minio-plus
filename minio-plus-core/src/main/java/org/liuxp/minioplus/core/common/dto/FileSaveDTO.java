package org.liuxp.minioplus.core.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件保存DTO
 * @author contact@liuxp.me
 * @since  2023-06-26
 **/
@Getter
@Setter
@ToString
@ApiModel("文件保存入参DTO")
public class FileSaveDTO {

    @ApiModelProperty(value = "文件名（含扩展名）",required = true)
    private String fullFileName;
    
    @ApiModelProperty("是否私有 false:否 true:是")
    private Boolean isPrivate;

    @ApiModelProperty("创建人")
    private String createUser;

}