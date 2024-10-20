package com.alexandergonzalez.libraryProject.dto.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {

    private  Long idEntity;
    private String idDocument;
    private String title;
    private String author;
    private String description;
    private String category;
    private String isbn;
    private boolean available;

    public BookDto(String title, String author, String description, String category, String isbn, boolean available, Long idEntity) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.category = category;
        this.isbn = isbn;
        this.available = available;
        this.idEntity = idEntity;
    }

    public BookDto(String idDocument, String title, String author, String description, String category, boolean available) {
        this.idDocument = idDocument;
        this.title = title;
        this.author = author;
        this.description = description;
        this.category = category;
        this.available = available;
    }
}
