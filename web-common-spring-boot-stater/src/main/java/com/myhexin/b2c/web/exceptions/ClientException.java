package com.myhexin.b2c.web.exceptions;

import com.myhexin.b2c.web.enums.ResponseStatusEnum;
import lombok.Getter;

/**
 * @author baoyh
 * @since 2023/10/14
 */
@Getter
public class ClientException extends RuntimeException {

    /**
     * 错误状态码
     */
    private Integer code;

    private String message;

    public ClientException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ClientException(ResponseStatusEnum statusEnum) {
        this(statusEnum.getCode(), statusEnum.getMessage());
    }

    public ClientException(String message) {
        this.message = message;
        this.code = ResponseStatusEnum.CLIENT.getCode();
    }
}
