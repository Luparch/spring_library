package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookDto, Integer> {
    @Query("SELECT b FROM books b JOIN b.authors a WHERE a.name = :#{#dto.name} AND " +
            "a.secondName = :#{#dto.secondName} AND a.patronymic = :#{#dto.patronymic}")
    List<BookDto> getByAuthor(@Param("dto") AuthorDto dto);
}
