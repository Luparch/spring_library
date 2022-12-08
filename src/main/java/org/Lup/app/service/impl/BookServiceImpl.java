package org.Lup.app.service.impl;

import org.Lup.app.dao.BookDao;
import org.Lup.app.dto.BookDto;
import org.Lup.app.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao){
        this.bookDao = bookDao;
    }


    @Override
    public BookDto getBookById(Integer id) {
        return bookDao.get(id);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookDao.delete(id);
    }

    @Override
    public void updateBookById(Integer id, BookDto book) {
        book.setId(id);
        bookDao.update(book);
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
    public List<BookDto> getBooksByAuthor(String name) {
        return bookDao.getByAuthor(name);
    }
}
