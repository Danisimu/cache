package com.example.simplerestapi.web.controller;

import com.example.simplerestapi.mapper.BookMapper;
import com.example.simplerestapi.model.Book;
import com.example.simplerestapi.model.Category;
import com.example.simplerestapi.service.DatabaseBookService;
import com.example.simplerestapi.service.DatabaseCategoryService;
import com.example.simplerestapi.web.model.BookResponse;
import com.example.simplerestapi.web.model.BookResponseList;
import com.example.simplerestapi.web.model.UpsertBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final DatabaseBookService bookService;

    private final DatabaseCategoryService categoryService;

    private final BookMapper bookMapper;




    @GetMapping
    public ResponseEntity<BookResponseList> findAllBooks(){

        System.out.println(categoryService.findAll());
        return ResponseEntity.ok(
                bookMapper.bookResponseToBookListResponse(bookService.findAll()));
    }

    @GetMapping("bookNameAndAuthor/{name_author}")
    public ResponseEntity<BookResponse> findByIdBook(@PathVariable String name_author){
        return  ResponseEntity.ok(
                bookMapper.bookToResponse(bookService.findBookByNameAndAuthor(name_author))
        );
    }

    @GetMapping("/categoryName/{categoryName}")
    public ResponseEntity<BookResponseList> findBooksByCategoryName(@PathVariable String categoryName){
        return ResponseEntity.ok(
                bookMapper.bookResponseToBookListResponse(bookService.findBookByCategoryName(categoryName))
        );
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody UpsertBookRequest request){

        Category category = new Category();
        category.setName(request.getCategoryName());
        categoryService.saveCategory(category);

        Book book = bookMapper.requestToBook(request);
        book.setCategory(category);
        bookService.saveBook(book);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookMapper.bookToResponse(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable long id,
                                                   @RequestBody UpsertBookRequest request){
        Category category = categoryService.findCategoryById(id);
        category.setName(request.getCategoryName());
        categoryService.updateCategory(category);

        Book book = bookMapper.requestToBook(request, id);
        book.setCategory(category);
        bookService.updateBook(book);

        return ResponseEntity.ok(bookMapper.bookToResponse(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id){

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
