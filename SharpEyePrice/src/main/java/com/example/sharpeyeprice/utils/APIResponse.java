package com.example.sharpeyeprice.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse {
    private String message;   //返回给客户端的描述信息
    private int status_code;  //存储HTTP状态码，表示请求的处理结果：200 400等
    private Object payload;   //存储实际返回的数据，任何类型

    public APIResponse(String message, int status_code, Object payload) {
        this.message = message;
        this.status_code = status_code;
        this.payload = payload;
    }

    public APIResponse(String message, int status_code) {
        this.message = message;
        this.status_code = status_code;
    }
}
