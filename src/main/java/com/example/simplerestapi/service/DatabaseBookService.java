package com.example.simplerestapi.service;

import com.example.simplerestapi.configuration.properties.AppCacheProperties;
import com.example.simplerestapi.model.Book;
import com.example.simplerestapi.repository.DatabaseBookRepository;
import com.example.simplerestapi.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class DatabaseBookService {

    private final DatabaseBookRepository bookRepository;



    public List<Book> findAll(){
        return bookRepository.findAll();
    }


    public Book findBookById(long bookId){
        return bookRepository.findById(bookId).orElseThrow();
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DATABASE_BOOK_BY_NAME_AND_AUTHOR, key = "#name_author")
    public Book findBookByNameAndAuthor(String name_author){
        String[] array = name_author.split("_");
        Book probe = new Book();
        probe.setName(array[0]);
        probe.setAuthor(array[1]);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id","release", "category");

        Example<Book> example = Example.of(probe, matcher);

        return bookRepository.findOne(example).orElseThrow();
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_NAME_CATEGORY, key = "#categoryName")
    public List<Book> findBookByCategoryName(String categoryName){

        return bookRepository.findAll().stream()
                .filter(book -> book.getCategory().getName().equals(categoryName))
                .toList();
    }

    @Caching(evict = {
            @CacheEvict(value = "bookByNameAndAuthor", beforeInvocation = true),
            @CacheEvict(value = "listBookByCategoryName", beforeInvocation = true)
    })
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    @Caching(evict = {
            @CacheEvict(value = "bookByNameAndAuthor", beforeInvocation = true),
            @CacheEvict(value = "listBookByCategoryName", beforeInvocation = true)
    })
    public Book updateBook(Book book){
        Book existedBook = findBookById(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);

        return bookRepository.save(book);
    }

    @Caching(evict = {
            @CacheEvict(value = "bookByNameAndAuthor", beforeInvocation = true),
            @CacheEvict(value = "listBookByCategoryName", beforeInvocation = true)
    })
    public void deleteBook(long bookId){

        bookRepository.deleteById(bookId);
    }



}
