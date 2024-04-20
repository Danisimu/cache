package com.example.simplerestapi.web.model;

import lombok.Data;

@Data
public class BookResponse {

    private long id;

    private String name;

    private String author;

    private String release;

    private String categoryName;
}
