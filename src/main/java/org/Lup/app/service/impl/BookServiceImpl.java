package org.Lup.app.service.impl;

import org.Lup.app.dao.BookDao;
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
    public Optional<BookDto> getBookById(Integer id) throws DomainException {
        return bookDao.get(id);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookDao.delete(id);
    }

    @Override
    public void updateBookById(Integer id, BookDto dto) throws DomainException {
        dto.setId(id);
        bookDao.update(dto);
    }

    @Override
    public void createBook(BookDto dto) throws DomainException {
        bookDao.store(dto);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public List<BookDto> getBooksByAuthor(String name) {
        return bookDao.getByAuthor(name);
    }

}