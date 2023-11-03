package com.myhexin.b2c.web.core.builder;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myhexin.b2c.web.vo.PageResponse;
import com.myhexin.b2c.web.vo.Response;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

/**
 * @author baoyh
 * @since 2023/10/14
 */

/**
 * 返回给前端的通用分页包装
 */
@Getter
public class PageResponseBuilder<T extends Serializable> implements Build<PageResponse<T>> {

    /**
     * 当前页的记录数
     */
    private Integer pageSize;

    /**
     * 当前为第几页
     */
    private Integer currentPage;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 具体返回给前端的该页的记录数据
     */
    private List<T> records;


    public PageResponseBuilder<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageResponseBuilder<T> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PageResponseBuilder<T> setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public PageResponseBuilder<T> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PageResponseBuilder<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public static <T extends Serializable> PageResponseBuilder<T> create() {
        return new PageResponseBuilder<>();
    }

    public PageResponse<T> build(Page<T> page) {
        pageSize = Math.toIntExact(page.getSize());
        currentPage = Math.toIntExact(page.getCurrent());
        total = Math.toIntExact(page.getTotal());
        totalPage = Math.toIntExact(page.getPages());
        if (Objects.isNull(records) || records.isEmpty()) {
            records = page.getRecords();
        }
        return build(this);
    }

    @Override
    public PageResponse<T> build() {
        return build(this);
    }

    private PageResponse<T> build(PageResponseBuilder<T> builder) {
        records = Objects.isNull(records) ? Collections.emptyList() : records;
        pageSize = Objects.isNull(pageSize) ? 0 : pageSize;
        currentPage = Objects.isNull(currentPage) ? 0 : currentPage;
        total = Objects.isNull(total) ? 0 : total;
        totalPage = Objects.isNull(totalPage) ? 0 : totalPage;
        return new PageResponse<>(
                builder.pageSize, builder.currentPage, builder.total, builder.totalPage, builder.records
        );
    }
}
