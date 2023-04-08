package com.example.xiaosage.exception;

import lombok.Getter;

import javax.lang.model.type.ErrorType;

@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code, String msg){
        super(msg);
        this.code = code;
    }

}
