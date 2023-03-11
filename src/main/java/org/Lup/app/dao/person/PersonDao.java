package org.Lup.app.dao.person;

import org.Lup.app.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface PersonDao {
    void store(PersonDto dto);
    Optional<PersonDto> get(Integer id);
    List<PersonDto> getAll();
    void delete(Integer id);
    void update(PersonDto dto);
    void borrowBook(Integer personId, Integer bookId);
    List<Integer> booksBorrowedByPerson(Integer person_id);
    void returnBook(Integer personId, Integer bookId);
}
