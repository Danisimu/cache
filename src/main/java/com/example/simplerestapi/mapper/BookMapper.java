package com.example.simplerestapi.mapper;

import com.example.simplerestapi.model.Book;
import com.example.simplerestapi.web.model.BookResponse;
import com.example.simplerestapi.web.model.BookResponseList;
import com.example.simplerestapi.web.model.UpsertBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface BookMapper {

    Book requestToBook(UpsertBookRequest request);
    @Mapping(source = "bookId", target = "id")
    Book requestToBook(UpsertBookRequest request, Long bookId);

    @Mapping(source = "book", target = "categoryName",qualifiedByName = "categoryName")
    BookResponse bookToResponse(Book book);

    default BookResponseList bookResponseToBookListResponse(List<Book> bookList){

        BookResponseList bookResponseList = new BookResponseList();

       bookResponseList.setBookResponseList(bookList.stream()
                .map(this::bookToResponse)
                .collect(Collectors.toList()));

       return bookResponseList;
    }

    @Named("categoryName")
    default String setCategoryName(Book book){
        return book.getCategory().getName();
    }


}
