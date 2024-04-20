package com.example.simplerestapi.service;

import com.example.simplerestapi.model.Book;
import com.example.simplerestapi.model.Category;
import com.example.simplerestapi.repository.DatabaseCategoryRepository;
import com.example.simplerestapi.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseCategoryService {

    private final DatabaseCategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findCategoryById(long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    public Category saveCategory(Category category){

        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category){
        Category existedCategory = findCategoryById(category.getId());
        BeanUtils.copyNonNullProperties(category, existedCategory);

        return categoryRepository.save(existedCategory);
    }

    public void deleteBook(long categoryId){

        categoryRepository.deleteById(categoryId);
    }
}
