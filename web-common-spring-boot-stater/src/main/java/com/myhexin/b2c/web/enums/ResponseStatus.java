package com.myhexin.b2c.web.enums;

/**
 * @author baoyh
 * @since 2023/10/14
 */
public interface ResponseStatus {

    /**
     * 获取响应对象{@link com.myhexin.b2c.web.vo.Response}的状态的描述信息
     *
     * @return 状态的描述信息
     */
    String getMessage();

    /**
     * 获取响应对象{@link com.myhexin.b2c.web.vo.Response}的状态码
     *
     * @return 状态码
     */
    Integer getCode();
}
