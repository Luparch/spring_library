package org.Lup.app.service;

import org.Lup.app.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> getBookById(Integer id);

    void deleteBookById(Integer id);

    void updateBookById(Integer id, BookDto book);

    void createBook(BookDto dto);

    List<BookDto> getAllBooks();

    List<BookDto> getBooksByAuthor(String name);

}
