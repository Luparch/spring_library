package org.Lup.app.service;

import org.Lup.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto getBookById(Integer id);

    void deleteBookById(Integer id);

    void updateBookById(Integer id, BookDto book);

    void createBook(BookDto dto);

    List<BookDto> getAllBooks();

    List<BookDto> getBooksByAuthor(String name);

}
