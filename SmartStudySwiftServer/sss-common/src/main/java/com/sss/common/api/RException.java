package com.sss.common.api;


import lombok.Getter;

@Getter
public class RException extends RuntimeException{
    private RResult rResult;

    public RException(){
        this.rResult = RResult.failed();
    }

    public RException(RCode rCode){
        this.rResult = RResult.failed(rCode);
    }

    public RException(String message){
        this.rResult = RResult.failed(message);
    }

    public RException(RCode rCode, String message){
        this.rResult = RResult.failed(rCode, message);
    }

    @Override
    public String getMessage() {
        return rResult.getMessage();
    }
}
