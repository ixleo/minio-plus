package org.liuxp.minioplus.core.common.enums;

/**
 * 返回给前端的code编码对应内容的枚举
 */
public enum ResponseCodeEnum {

    /**
     * 调用成功
     */
    SUCCESS(0, "调用成功"),
    /**
     * 调用失败
     */
    FAIL(-1, "调用失败");

    /**
     * 返回给前端的状态编码   0表示成功
     */
    private final Integer code;
    /**
     * 编码对应的解释
     */
    private final String message;

    /**
     * 构造方法
     *
     * @param code
     * @param message
     */
    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
