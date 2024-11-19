package com.sss.common.handler;

import com.sss.common.api.RException;
import com.sss.common.api.RResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RResult<String> exception(Exception e){
        log.error("全局异常 error = {}", e.getMessage(), e);
        return RResult.failed(e.getMessage());
    }

    @ExceptionHandler(RException.class)
    public RResult<Object> responseException(RException e){
        log.error("返回异常值 error = {}", e.getRResult().toString());
        return e.getRResult();
    }

    /**
     * 参数校验错误: <a href="https://juejin.cn/post/7066352669228531726#heading-11">...</a>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RResult<Object> onConstraintValidationException(
            ConstraintViolationException e) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            if(msg.length() != 0){
                msg.append("; ");
            }
            msg.append(violation.getPropertyPath().toString()).append(": ").append(violation.getMessage());
        }
        log.error("参数校验错误 error = {}", msg);
        return RResult.failed(msg.toString());
    }

    /**
     * 参数校验错误: <a href="https://juejin.cn/post/7066352669228531726#heading-11">...</a>
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RResult<Object> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        StringBuilder msg = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            if(msg.length() != 0){
                msg.append("; ");
            }
            msg.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage());
        }
        log.error("参数校验错误 error = {}", msg);
        return RResult.failed(msg.toString());
    }
}
