package org.liuxp.minioplus.common.exception;

import org.liuxp.minioplus.common.enums.MinioPlusErrorCode;

/**
 * MinioPlus专用异常定义
 * @author contact@liuxp.me
 * @since  2024/05/26
 */
public class MinioPlusException extends RuntimeException {

    private static final long serialVersionUID = 772046747932011086L;

    private int errorCode;

    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public MinioPlusException() {
        super();
    }

    public MinioPlusException(String message) {
        super(message);
    }

    public MinioPlusException(MinioPlusErrorCode minioPlusErrorCode){
        this.errorCode = minioPlusErrorCode.getCode();
        this.errorMessage = minioPlusErrorCode.getMessage();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MinioPlusException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
