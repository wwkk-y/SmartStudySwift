package com.sss.common.api;


/**
 * 通用返回结果封装类
 */
public class RResult<T> {
    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据封装
     */
    private T data;

    protected RResult() {
    }

    protected RResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> RResult<T> success(T data) {
        return new RResult<T>(RCode.SUCCESS.getCode(), RCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> RResult<T> success(T data, String message) {
        return new RResult<T>(RCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> RResult<T> failed(RCode errorCode) {
        return new RResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> RResult<T> failed(RCode errorCode, String message) {
        return new RResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> RResult<T> failed(String message) {
        return new RResult<T>(RCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> RResult<T> failed() {
        return failed(RCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> RResult<T> validateFailed() {
        return failed(RCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> RResult<T> validateFailed(String message) {
        return new RResult<T>(RCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> RResult<T> unauthorized(T data) {
        return new RResult<T>(RCode.UNAUTHORIZED.getCode(), RCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> RResult<T> forbidden(T data) {
        return new RResult<T>(RCode.FORBIDDEN.getCode(), RCode.FORBIDDEN.getMessage(), data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
