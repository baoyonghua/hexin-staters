package com.myhexin.b2c.web.vo;

/**
 * @author baoyh
 * @since 2023/10/17
 */

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myhexin.b2c.web.core.builder.PageResponseBuilder;
import com.myhexin.b2c.web.exceptions.SystemException;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回给前端的通用分页包装
 */
@Data
public class PageResponse<T extends Serializable> implements Serializable {

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

    public PageResponse(Integer pageSize, Integer currentPage, Integer total, Integer totalPage, List<T> records) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.total = total;
        this.totalPage = totalPage;
        this.records = records;
    }

    /**
     * 构建pageResponse对象
     *
     * @param collection 指定分页数据，将会替换原有的分页数据
     */
    public PageResponse<T> convert(List<T> collection) {
        this.setRecords(collection);
        return this;
    }

    /**
     * 作用：将entity类型转换为对应的VO类型
     * 用法：当完成分页查询后，调用此方法即可完成vo的初始化操作，然后返回给前端
     *
     * @param clz vo的Class对象
     * @param <U> vo类
     * @return PageResponseVO对象
     */
    public <U extends Serializable> PageResponse<U> convert(Class<U> clz) {
        U instance;
        List<U> us = new ArrayList<>(records.size());
        // 迭代该页的记录并进行类型转换
        for (T record : records) {
            try {
                // 通过反射创建对象
                instance = clz.newInstance();
            } catch (Exception e) {
                throw new SystemException("创建" + clz.getName() + "对象失败", e);
            }
            // 完成类型转换
            BeanUtil.copyProperties(record, instance);
            us.add(instance);
        }
        return PageResponseBuilder.<U>create()
                .setRecords(us)
                .setCurrentPage(Math.toIntExact(currentPage))
                .setPageSize(Math.toIntExact(pageSize))
                .setTotal(Math.toIntExact(total))
                .setTotalPage(Math.toIntExact(totalPage))
                .build();
    }
}
