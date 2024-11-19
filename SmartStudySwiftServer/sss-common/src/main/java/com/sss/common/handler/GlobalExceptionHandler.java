package com.sss.common.handler;

import com.sss.common.api.RException;
import com.sss.common.api.RResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
