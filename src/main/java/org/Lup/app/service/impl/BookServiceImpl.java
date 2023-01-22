package org.Lup.app.service.impl;

import org.Lup.app.dao.book.BookDao;
import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.exception.DomainException;
import org.Lup.app.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao){
        this.bookDao = bookDao;
    }


    @Override
    public Optional<BookDto> getBookById(Integer id) {
        return bookDao.get(id);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookDao.delete(id);
    }

    @Override
    public void updateBookById(Integer id, BookDto dto) {
        dto.setId(id);
        bookDao.update(dto);
    }

    @Override
    public void createBook(BookDto dto) {
        bookDao.store(dto);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public List<BookDto> getBooksByAuthor(AuthorDto dto) {
        return bookDao.getByAuthor(dto);
    }

}
