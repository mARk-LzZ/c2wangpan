package com.lzz.wangpan.web.controller;

import com.lzz.wangpan.annotation.AuthInterceptor;
import com.lzz.wangpan.entity.Category;
import com.lzz.wangpan.enums.InterceptorLevel;
import com.lzz.wangpan.service.ICategoryService;
import com.lzz.wangpan.util.ControllerUtils;
import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Formatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@CrossOrigin
@Api(value = "/category", description = "文件分类相关操作")
public class CategoryController {

    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {this.categoryService = categoryService;}

    @ApiOperation(value = "新增一个分类")
    @AuthInterceptor(InterceptorLevel.ADMIN)
    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public String add(@PathVariable("name") String name) {
        return ControllerUtils.getResponse(categoryService.insert(name));
    }

    @ApiOperation(value = "更新分类名称")
    @ApiImplicitParam(name = "name", value = "新的名称", required = true)
    @AuthInterceptor(InterceptorLevel.ADMIN)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable("id") int id, String name) {
        boolean isSuccess = Checker.isNotEmpty(name) && categoryService.update(id, name);
        return ControllerUtils.getResponse(isSuccess);
    }

    @ApiOperation(value = "删除一个分类")
    @AuthInterceptor(InterceptorLevel.ADMIN)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String remove(@PathVariable("id") int id) {
        return ControllerUtils.getResponse(categoryService.remove(id));
    }

    @ApiOperation(value = "获取一个分类")
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getById(@PathVariable("id") int id) {
        Category category = categoryService.getById(id);
        if (Checker.isNull(category)) {
            return ControllerUtils.getResponse(ValueConsts.FALSE);
        } else {
            return category.toString();
        }
    }

    @ApiOperation(value = "获取所有分类")
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAll() {
        return Formatter.listToJson(categoryService.list());
    }
}
