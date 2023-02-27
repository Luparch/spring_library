package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<BookDto, Integer> {
    @Query("SELECT b FROM books b JOIN b.authors a WHERE a = ?1")
    List<BookDto> getByAuthor(AuthorDto dto);
}
