package org.Lup.app.service.impl;

import org.Lup.app.dao.book.BookRepository;
import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<BookDto> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void updateBookById(Integer id, BookDto dto) {
        dto.setId(id);
        bookRepository.saveAndFlush(dto);
    }

    @Override
    public void createBook(BookDto dto) {
        bookRepository.saveAndFlush(dto);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<BookDto> getBooksByAuthor(AuthorDto dto) {
        return bookRepository.getByAuthor(dto);
    }

}
