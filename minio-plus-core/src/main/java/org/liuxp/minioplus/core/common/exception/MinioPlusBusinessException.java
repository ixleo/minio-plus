package org.liuxp.minioplus.core.common.exception;

/**
 * MinioPlus专用异常定义
 * @author contact@liuxp.me
 * @since  2023/08/14
 */
public class MinioPlusBusinessException extends RuntimeException {

    private static final long serialVersionUID = 772046747932011086L;

    private int errorCode;

    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public MinioPlusBusinessException() {
        super();
    }

    public MinioPlusBusinessException(String message) {
        super(message);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MinioPlusBusinessException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
