package com.myhexin.b2c.web.enums;

import lombok.Getter;

/**
 * @author baoyh
 * @since 2023/10/14
 */
@Getter
public enum ResponseStatusEnum implements ResponseStatus {

    /**
     * 响应状态枚举
     */
    SUCCESS(200, "成功"),

    // -------------------------------------------

    SYSTEM_ERROR(500, "系统内部错误"),

    // ---------------------------------------------------------

    CLIENT(400, "请求错误"),
    PARAM_VALID_ERROR(400, "请求参数错误");

    private final Integer code;

    private final String message;

    ResponseStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
