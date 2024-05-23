package org.liuxp.minioplus.application.context;

import java.util.Objects;

/**
 * 返回值枚举类
 * @author contact@liuxp.me
 * @since  2024/05/22
 */
public enum ResponseCodeEnum {

    /**
     * 调用成功
     */
    SUCCESS(0, "调用成功"),
    /**
     * 调用失败
     */
    FAIL(-1, "系统异常，请重试");

    /**
     * 返回给前端的状态编码   0表示成功
     */
    private Integer code;
    /**
     * 编码对应的解释
     */
    private String message;

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

    public static ResponseCodeEnum getByCode(Integer code) {
        for (ResponseCodeEnum e : ResponseCodeEnum.values()) {
            if (Objects.equals(e.code, code)) {
                return e;
            }
        }
        return ResponseCodeEnum.FAIL;
    }

}
