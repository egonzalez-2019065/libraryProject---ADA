package com.alexandergonzalez.libraryProject.models.book;


import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;

    private String title;
    private String author;
    private String description;
    private String category;
    private String isbn;
    private boolean available;
}
