package com.myhexin.b2c.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用于接收前端传入的分页参数
 *
 * @author baohh
 * @since 2023/10/14
 */
@Data
public class QueryPageDTO<T> implements Serializable {

    /**
     * 每页数量
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 0, message = "每页大小不能小于0")
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * 当前页
     */
    @JsonProperty("current_page")
    @NotNull(message = "当前页不能为空")
    @Min(value = 0, message = "当前页不能小于0")
    private Integer currentPage;

    /**
     * 排序列
     */
    @JsonProperty("sort_by")
    private String sortBy;

    /**
     * 正序 or 倒序
     */
    @JsonProperty("sort_asc")
    private Boolean sortAsc;

    /**
     * 查询参数
     */
    @Valid
    private T params;
}
