package com.nakajima.nkjwebapp.service;

import com.nakajima.nkjwebapp.model.Category;
import java.util.List;

public interface CategoryService {
    Category createCategory(String name);
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    void deleteCategory(Integer id);
    void updateCategory(Integer id, String name);
}
