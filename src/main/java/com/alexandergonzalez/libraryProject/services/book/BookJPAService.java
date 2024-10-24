package com.alexandergonzalez.libraryProject.services.book;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.repositories.book.BookJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("jpaBookService")
public class BookJPAService implements BookService {

    private final BookJPARepository bookJPARepository;

    @Autowired
    public BookJPAService(BookJPARepository bookJPARepository) {
        this.bookJPARepository = bookJPARepository;
    }


    public BookDto toDto(BookEntity book){
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
        BookEntity book = new BookEntity();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());
        book.setIsbn(bookDto.getIsbn());
        book.setAvailable(true);
        bookJPARepository.save(book);
        return this.toDto(book);
    }

    @Override
    public BookDocument findById(String id) {
        return null;
    }

    @Override
    public BookEntity findByIdJPA(Long id) {
        BookEntity bookFound = bookJPARepository.findById(id).orElse(null);
        if(bookFound != null){
            return bookFound;
        }
        return null;
    }

    @Override
    public BookDto findByIdDto(String id) {
        BookEntity bookFound = findByIdJPA(Long.valueOf(id));
        if(bookFound != null){
            return this.toDto(bookFound);
        }
        return null;
    }

    @Override
    public BookDto updateBook(String id, BookDto bookDto) {
        BookEntity bookFound = findByIdJPA(Long.valueOf(id));
        if(bookFound != null){
            bookFound.setTitle(bookDto.getTitle());
            bookFound.setDescription(bookDto.getDescription());
            bookFound.setAuthor(bookDto.getAuthor());
            bookFound.setCategory(bookDto.getCategory());
            //bookFound.setIsbn(bookDto.getIsbn());
            bookFound.setAvailable(bookDto.isAvailable());
            bookJPARepository.save(bookFound);
            return this.toDto(bookFound);
        }
        return null;
    }

    @Override
    public List<BookDto> getBooks() {
        return bookJPARepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BookDto deleteBook(String id) {
        BookEntity bookFound = findByIdJPA(Long.valueOf(id));
        if(bookFound != null){
            bookJPARepository.delete(bookFound);
            return this.toDto(bookFound);
        }
        return null;
    }
}
