package com.imooc.mall.service.Impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.responseVo.CategoryVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        // List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categoryList = categoryMapper.selectAll();

        //先查出来 父集ID parent_id = 0
        /*for (Category category : categoryList) {
            if (category.getParentId().equals(ROOT_PARENT_ID)) {
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category, categoryVo);
                categoryVoList.add(categoryVo);
            }

        }*/
        List<CategoryVo> categoryVoList = categoryList.stream()
                .filter(category -> category.getParentId().equals(ROOT_PARENT_ID))
                .map(category -> category2CategoryVo(category))
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());
        findSubCatregory(categoryVoList, categoryList);
        return ResponseVo.success(categoryVoList);
    }

    /*商品列表
     * 先把所有可能的父子类ID查出来
     * */
    @Override
    public void findBySubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categoryList = categoryMapper.selectAll();
        findBySubCategoryId(id,resultSet,categoryList);

    }

    private void findBySubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categoryList) {
        for (Category category : categoryList) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                //递归
                findBySubCategoryId(category.getId(), resultSet,categoryList);
            }

        }
    }

    //接着查父集合中的子集合
    private void findSubCatregory(List<CategoryVo> categoryVoList, List<Category> categoryList) {
        //先把父集合遍历出来找到ID 这时候已经拿到的是categoryVo中的父类数据
        for (CategoryVo categoryVo : categoryVoList) {
            //再查询子类中的数据 把子类的数据放到categoryVo的subCategories中
            List<CategoryVo> subCategoriesList = new ArrayList<>();
            //从数据源中查数据
            for (Category category : categoryList) {
                //当第一步查出来的ID 与 现在查询出来的parentID相同时 则是父类的子类
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo categoryVo1 = new CategoryVo();
                    BeanUtils.copyProperties(category, categoryVo1);
                    subCategoriesList.add(categoryVo1);
                }
                //排序 从大到小
                subCategoriesList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoriesList);
                //递归接着查
                findSubCatregory(subCategoriesList, categoryList);
            }
        }
    }

    //把category中的数据复制到categoryvo 中
    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
