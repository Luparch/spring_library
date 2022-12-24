package org.Lup.app.service;

import org.Lup.app.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<PersonDto> getPersonById(Integer id);

    void deletePersonById(Integer id);

    void createPerson(PersonDto dto);

    List<PersonDto> getAllPersons();

    void updatePersonById(Integer personId, PersonDto dto);

    void borrowBook(Integer personId, Integer bookId);

    void returnBook(Integer personId, Integer bookId);

}
