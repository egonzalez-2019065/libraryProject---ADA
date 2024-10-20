package com.alexandergonzalez.libraryProject.models.book;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String title;
    private String author;
    private String description;
    private String category;
    private String isbn;
    private boolean available;
}
