package com.example.simplerestapi.repository;

import com.example.simplerestapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseBookRepository extends JpaRepository<Book, Long> {
}
