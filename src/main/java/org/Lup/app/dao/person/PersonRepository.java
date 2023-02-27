package org.Lup.app.dao.person;

import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonDto, Integer> {

    @Query("SELECT b FROM persons p JOIN p.books b WHERE p.id = ?1")
    List<BookDto> booksBorrowedByPersonId(Integer personId);
}
