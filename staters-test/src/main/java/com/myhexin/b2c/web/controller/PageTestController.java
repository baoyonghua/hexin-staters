package com.myhexin.b2c.web.controller;

import com.myhexin.b2c.web.dto.PageBaseDTO;
import com.myhexin.b2c.web.pojo.UserEntity;
import com.myhexin.b2c.web.service.UserEntityService;
import com.myhexin.b2c.web.vo.PageResponse;
import com.myhexin.b2c.web.vo.Response;
import com.myhexin.b2c.web.vo.UserVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baoyh
 * @since 2023/10/17
 */
@RestController
@Validated
public class PageTestController {

    @Resource
    private UserEntityService userEntityService;

    @GetMapping("/insert")
    public Response<List<UserEntity>> ready() {
        return Response.success(userEntityService.saveBatchUsers());
    }

    @GetMapping("/page_get")
    public Response<PageResponse<UserEntity>> getPage(
            @RequestParam("page_size") Integer pageSize, @RequestParam("current_page") Integer currentPage) {
        PageBaseDTO dto = PageBaseDTO.builder().pageSize(pageSize).currentPage(currentPage).build();
        return Response.success(userEntityService.pageGet(dto));
    }

    @GetMapping("/page_get_vo")
    public Response<PageResponse<UserVO>> getPageVos(
            @RequestParam("page_size") Integer pageSize, @RequestParam("current_page") Integer currentPage) {
        PageBaseDTO dto = PageBaseDTO.builder().pageSize(pageSize).currentPage(currentPage).build();
        return Response.success(userEntityService.pageGetVO(dto));
    }
}
