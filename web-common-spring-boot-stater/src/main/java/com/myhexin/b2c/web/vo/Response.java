package com.myhexin.b2c.web.vo;

import com.myhexin.b2c.web.enums.ResponseStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baoyh
 * @since 2023/10/14
 */
@Data
public class Response<T> implements Serializable {
    private String msg;

    private Integer code;

    private T data;

    private Response(String msg, Integer code, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Response(String msg, Integer code) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    /**
     * 通用的不带数据的失败返回
     */
    public static Response<Void> error(ResponseStatusEnum statusEnum) {
        return new Response<>(statusEnum.getMessage(), statusEnum.getCode());
    }

    public static Response<Void> error(Integer code, String msg) {
        return new Response<>(msg, code);
    }

    public static Response<Void> error(String msg) {
        return new Response<>(msg, ResponseStatusEnum.SYSTEM_ERROR.getCode());
    }

    /**
     * 通用的不带数据的成功返回
     */
    public static Response<Void> success() {
        ResponseStatusEnum statusEnum = ResponseStatusEnum.SUCCESS;
        return new Response<>(statusEnum.getMessage(), statusEnum.getCode());
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(
                ResponseStatusEnum.SUCCESS.getMessage(),
                ResponseStatusEnum.SUCCESS.getCode(),
                data);
    }
}