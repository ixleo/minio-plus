package org.liuxp.minioplus.core.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件完整性校验结果VO
 *
 * @author contact@liuxp.me
 * @since 2023-06-26
 **/
@Getter
@Setter
@ApiModel("文件完整性校验结果")
public class CompleteResultVo {

    @ApiModelProperty("是否完成")
    private Boolean isComplete;

    @ApiModelProperty("上传任务编号")
    private String uploadTaskId;

    @ApiModelProperty("补传的分块信息")
    private List<FileCheckResultVo.Part> partList = new ArrayList<>();

}