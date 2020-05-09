package com.example.happybaby.exception;

/**
 * 错误码接口
 */
public interface ErrorCode {
    /**
     * 获取错误码
     * @return
     */
    Integer getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getMessage();
}
