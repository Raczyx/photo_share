package com.example.photo_share.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBody <T>{
    private Integer code;
    private String msg;
    private T data;

    public static final Integer SUCCESS = 600;
    public static final Integer ERROR = 700;

    public ResponseBody success(String msg, T data){
        this.code = SUCCESS;
        this.msg = msg ;
        this.data = data;
        return this;
    }

    public ResponseBody error(String msg, T data){
        this.code = ERROR;
        this.msg = msg ;
        this.data = data;
        return this;
    }

    public ResponseBody(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResponseBody success(String msg){
        this.code = SUCCESS;
        this.msg = msg ;
        return this;
    }

    public ResponseBody error(String msg){
        this.code = ERROR;
        this.msg = msg;
        return this;
    }
}
