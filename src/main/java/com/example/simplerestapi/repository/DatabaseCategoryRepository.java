package com.example.simplerestapi.repository;

import com.example.simplerestapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseCategoryRepository  extends JpaRepository<Category, Long> {
}
