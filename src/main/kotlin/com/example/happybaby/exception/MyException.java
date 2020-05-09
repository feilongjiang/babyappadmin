package com.example.happybaby.exception;


import java.util.UUID;

public class MyException extends RuntimeException {
    private static final long serialVersionUID = -7864604160297181941L;
    /**
     * 错误码
     */
    protected final ErrorCode errorCode;

    /**
     * 这个是和谐一些不必要的地方,冗余的字段
     * * 尽量不要用
     */
    private String code;

    public MyException() {
        super(BaseHttpStatus.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = BaseHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 指定错误码构造通用异常
     *
     * @param errorCode
     */
    public MyException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailMessage
     */
    public MyException(final String detailMessage) {
        super(detailMessage);
        this.errorCode = BaseHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param t 导火索
     */
    public MyException(final Throwable t) {
        super(t);
        this.errorCode = BaseHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode     错误码
     * @param detailMessage 详细描述
     */
    public MyException(final ErrorCode errorCode, final String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode 错误码
     * @param t         导火索
     */
    public MyException(final ErrorCode errorCode, final Throwable t) {
        super(errorCode.getMessage(), t);
        this.errorCode = errorCode;
    }

    /**
     * 构造通用异常
     *
     * @param detailMessage 详细描述
     * @param t             导火索
     */
    public MyException(final String detailMessage, final Throwable t) {
        super(detailMessage, t);
        this.errorCode = BaseHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 构造通用异常
     *
     * @param errorCode     错误码
     * @param detailMessage 详细描述
     * @param t             导火索
     */
    public MyException(final ErrorCode errorCode, final String detailMessage, final Throwable t) {
        super(detailMessage, t);
        this.errorCode = errorCode;
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
