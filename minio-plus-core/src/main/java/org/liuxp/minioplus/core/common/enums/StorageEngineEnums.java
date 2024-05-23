package org.liuxp.minioplus.core.common.enums;

import lombok.Getter;

/**
 * 存储引擎枚举类
 *
 * @author contact@liuxp.me
 * @since  2023/06/26
 */
@Getter
public enum StorageEngineEnums {

    /**
     * 存储引擎枚举
     */
    MINIO("minio","MinIO存储引擎"),
    LOCAL("local","本地文件存储"),
    TIS("tis","TIS存储引擎");

    private final String code;

    private final String name;

    /**
     * 构造方法
     * @param code 编码
     * @param name 名称
     */
    StorageEngineEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据编码取得枚举
     * @param code 编码
     * @return 枚举
     */
    public static StorageEngineEnums getByCode(String code) {
        for (StorageEngineEnums fileDomain : StorageEngineEnums.values()) {
            if (code.equals(fileDomain.getCode())) {
                return fileDomain;
            }
        }
        return null;
    }

    /**
     * 根据编码取得名称
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(String code) {
        for (StorageEngineEnums fileDomain : StorageEngineEnums.values()) {
            if (code.equals(fileDomain.getCode())) {
                return fileDomain.getName();
            }
        }
        return "";
    }

}
