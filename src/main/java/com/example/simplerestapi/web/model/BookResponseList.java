package com.example.simplerestapi.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookResponseList {

    List<BookResponse> bookResponseList = new ArrayList<>();
}
