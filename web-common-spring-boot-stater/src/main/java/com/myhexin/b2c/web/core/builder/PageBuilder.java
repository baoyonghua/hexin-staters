package com.myhexin.b2c.web.core.builder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.myhexin.b2c.web.dto.QueryPageDTO;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author baoyh
 * @since 2023/10/18
 */
public class PageBuilder<T> implements Build<PageDTO<T>> {

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 排序列
     */
    private String sortBy;

    /**
     * 正序 or 倒序
     */
    private Boolean sortAsc;

    public PageBuilder<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageBuilder<T> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PageBuilder<T> setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public PageBuilder<T> setSortAsc(Boolean sortAsc) {
        this.sortAsc = sortAsc;
        return this;
    }

    public static <T> PageBuilder<T> create() {
        return new PageBuilder<>();
    }

    public PageDTO<T> build() {
        return build(this);
    }

    public PageDTO<T> build(QueryPageDTO dto) {
        PageBuilder<T> builder = PageBuilder.<T>create().setCurrentPage(dto.getCurrentPage())
                .setPageSize(dto.getPageSize())
                .setSortAsc(dto.getSortAsc())
                .setSortBy(dto.getSortBy());
        return build(builder);
    }

    private PageDTO<T> build(PageBuilder<T> builder) {
        PageDTO<T> pageDTO = new PageDTO<>();
        Assert.isTrue(builder.currentPage > 0, "当前页不能小于0");
        Assert.isTrue(builder.pageSize > 0, "每页大小不能小于0");
        pageDTO.setCurrent(Math.toIntExact(builder.currentPage));
        pageDTO.setSize(Math.toIntExact(builder.pageSize));
        if (Objects.isNull(builder.sortBy)) {
            return pageDTO;
        }
        // 设置排序相关的信息
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(StrUtil.toUnderlineCase(builder.sortBy));
        // 设置倒叙/正序
        if (Objects.nonNull(builder.sortAsc)) {
            orderItem.setAsc(builder.sortAsc);
        }
        orderItems.add(orderItem);
        pageDTO.setOrders(orderItems);
        return pageDTO;
    }
}
