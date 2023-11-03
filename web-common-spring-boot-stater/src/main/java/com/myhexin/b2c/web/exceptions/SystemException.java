package com.myhexin.b2c.web.exceptions;

import com.myhexin.b2c.web.enums.ResponseStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baoyh
 * @since 2023/10/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemException extends RuntimeException {

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误状态码
     */
    private Integer code;

    public SystemException() {
        super();
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public SystemException(String message, Integer code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SystemException(ResponseStatusEnum statusEnum, Throwable e) {
        this(statusEnum.getMessage(), statusEnum.getCode(), e);
    }

    public SystemException(ResponseStatusEnum statusEnum) {
        this(statusEnum.getMessage(), statusEnum.getCode());
    }

    public SystemException(String message) {
        this.message = message;
        this.code = ResponseStatusEnum.SYSTEM_ERROR.getCode();
    }

    public SystemException(String message, Integer code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }
}
