package com.lzz.wangpan.service.impl;

import com.lzz.wangpan.mapper.CategoryMapper;
import com.lzz.wangpan.entity.Category;
import com.lzz.wangpan.service.ICategoryService;
import com.lzz.wangpan.modules.constant.DefaultValues;
import com.zhazhapan.util.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {this.categoryMapper=categoryMapper;}

    @Override
    public boolean insert(String name) {
        return Checker.isNotNull(name) && categoryMapper.insertCategory(name);
    }

    @Override
    public boolean remove(int id) {
        return isCategorized(id) && categoryMapper.removeCategoryById(id);
    }

    @Override
    public boolean update(int id, String name) {
        return Checker.isNotEmpty(name) && isCategorized(id) && categoryMapper.updateNameById(id, name);
    }

    private boolean isCategorized(int id) {
        return !DefaultValues.UNCATEGORIZED.equals(getById(id).getName());
    }

    @Override
    public Category getById(int id) {
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public List<Category> list() {
        return categoryMapper.listCategory();
    }

    @Override
    public int getIdByName(String name) {
        try {
            return categoryMapper.getIdByName(name);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }
}
