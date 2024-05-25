package org.liuxp.minioplus.model.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;

import java.util.Arrays;

/**
 * 桶策略枚举类
 *
 * @author contact@liuxp.me
 * @since 2023/06/26
 */
@Getter
public enum StorageBucketEnums {

    /**
     * 桶策略枚举类
     * 文档（document）：txt、rtf、ofd、doc、docx、xls、xlsx、ppt、pptx、pdf
     * 压缩包（package）：zip、rar、7z、tar、wim、gz、bz2
     * 音频（ audio ） ：mp3、wav、flac、acc、ogg、aiff、m4a、wma、midi
     * 视频（ video ） ：mp4、avi、mov、wmv、flv、mkv、mpeg、mpg 、rmvb
     * 图片 – 原始（ image ） ：jpeg、jpg、png、bmp、webp、gif
     * 图片 – 缩略图（ image-preview ） ：按照宽度 300 像素压缩
     * 其他（ other ） ：未在上述格式中的文件
     * 其他规则：文件在桶中存储时，按照 /年/月 划分路径
     * 用以规避Linux ext3文件系统下单个目录最多创建32000个目录的问题，参考了阿里云的处理办法
     */
    DOCUMENT("document", "文档文件桶",new String[]{"txt", "rtf", "ofd", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf"}),
    PACKAGE("package","压缩文件桶",new String[]{"zip", "rar", "7z", "tar", "wim", "gz", "bz2"}),
    AUDIO("audio", "音频文件桶",new String[]{"mp3", "wav", "flac", "acc", "ogg", "aiff", "m4a", "wma", "midi"}),
    VIDEO("video", "视频文件桶",new String[]{"mp4", "avi", "mov", "wmv", "flv", "mkv", "mpeg", "mpg", "rmvb"}),
    IMAGE("image", "图片文件桶",new String[]{"jpeg", "jpg", "png", "bmp", "webp", "gif"}),
    IMAGE_PREVIEW("image-preview", "缩略图文件桶",new String[]{"jpeg_large", "jpg_large", "png_large", "bmp_large", "webp_large", "gif_large"}),
    OTHER("other", "其他文件桶",new String[]{"*"});

    private final String code;

    private final String name;

    private final String[] types;

    /**
     * 构造方法
     *
     * @param code 编码
     * @param name 名称
     */
    StorageBucketEnums(String code, String name,String[] types) {
        this.code = code;
        this.name = name;
        this.types = types;
    }

    /**
     * 根据编码取得枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static StorageBucketEnums getByCode(String code) {
        for (StorageBucketEnums fileDomain : StorageBucketEnums.values()) {
            if (code.equals(fileDomain.getCode())) {
                return fileDomain;
            }
        }
        return null;
    }

    /**
     * 根据编码取得名称
     *
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(String code) {
        for (StorageBucketEnums fileDomain : StorageBucketEnums.values()) {
            if (code.equals(fileDomain.getCode())) {
                return fileDomain.getName();
            }
        }
        return "";
    }

    /**
     * 根据扩展名取得桶编码
     * @param suffix 扩展名
     * @return 桶编码
     */
    public static String getBucketByFileSuffix(String suffix) {

        return Arrays.stream(StorageBucketEnums.values())
                .filter(item -> ArrayUtil.contains(item.types, suffix))
                .findFirst()
                .orElse(StorageBucketEnums.OTHER)
                .code;

    }

}
