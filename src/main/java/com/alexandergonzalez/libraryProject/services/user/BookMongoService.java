package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.repositories.book.BookMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("mongoBookService")
public class BookMongoService implements BookService {

    private final BookMongoRepository mongoRepository;

    @Autowired
    public BookMongoService(BookMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    public BookDto toDto(BookDocument book){
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getCategory(),
                book.isAvailable()
        );
    }

    @Override
    public BookDto saveBook(BookDto bookDto){
        BookDocument book = new BookDocument();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());
        book.setIsbn(bookDto.getIsbn());
        book.setAvailable(true);
        mongoRepository.save(book);
        return this.toDto(book);
    }
}
