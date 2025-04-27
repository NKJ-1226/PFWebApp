package com.nakajima.nkjwebapp.service;

import com.nakajima.nkjwebapp.model.Category;
import com.nakajima.nkjwebapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);  // もし存在しない場合はnullを返す
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);  // 指定されたIDのカテゴリを削除
    }

    @Override
    public void updateCategory(Integer id, String name) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(name);
            categoryRepository.save(category);  // 名前を更新して保存
        }
    }
}
