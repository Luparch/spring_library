package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void store(BookDto dto);
    Optional<BookDto> get(Integer id);
    List<BookDto> getAll();
    void delete(Integer id);
    void update(BookDto dto);
    List<BookDto> getByAuthor(AuthorDto dto);
}
