package com.alexandergonzalez.libraryProject.services.book;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.repositories.book.BookJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public BookDto findByIdDto(String id) {
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
    public BookDto findByIdDtoJPA(Long id) {
        BookEntity bookFound = findByIdJPA(id);
        if(bookFound != null){
            return this.toDto(bookFound);
        }
        return null;
    }



}
