package com.alexandergonzalez.libraryProject.services.book;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
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

    @Override
    public BookDocument findById(String id) {
        BookDocument bookFound = mongoRepository.findById(id).orElse(null);
        if(bookFound != null){
           return bookFound;
        }
        return null;
    }

    @Override
    public BookEntity findByIdJPA(Long id) {
        return null;
    }

    @Override
    public BookDto findByIdDtoJPA(Long id) {
        return null;
    }

    @Override
    public BookDto updateBookJPA(Long id, BookDto bookDto) {
        return null;
    }

    @Override
    public BookDto findByIdDto(String id) {
       BookDocument bookFoundDto = findById(id);
       if(bookFoundDto != null){
           return this.toDto(bookFoundDto);
       }
       return null;
    }

    @Override
    public BookDto updateBook(String id, BookDto bookDto) {
        BookDocument bookFound = findById(id);
        if(bookFound != null){

            bookFound.setTitle(bookDto.getTitle());
            bookFound.setDescription(bookDto.getDescription());
            bookFound.setAuthor(bookDto.getAuthor());
            bookFound.setCategory(bookDto.getCategory());
            //bookFound.setIsbn(bookDto.getIsbn());
            bookFound.setAvailable(bookDto.isAvailable());
            mongoRepository.save(bookFound);
            return this.toDto(bookFound);
        }
        return null;
    }

}
