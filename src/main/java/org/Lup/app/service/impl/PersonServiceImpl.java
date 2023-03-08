package org.Lup.app.service.impl;

import org.Lup.app.dao.book.BookRepository;
import org.Lup.app.dao.person.PersonRepository;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final BookRepository bookRepository;

    public PersonServiceImpl(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<PersonDto> getPersonById(Integer id){
        return personRepository.findById(id);
    }

    @Override
    public void deletePersonById(Integer id) {
        personRepository.deleteById(id);
    }

    @Override
    public void createPerson(PersonDto dto){
        personRepository.saveAndFlush(dto);
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public void updatePersonById(Integer personId, PersonDto dto){
        dto.setId(personId);
        personRepository.saveAndFlush(dto);
    }

    @Override
    public void borrowBook(Integer personId, Integer bookId){
        Optional<BookDto> book = bookRepository.findById(bookId);
        book.ifPresent((b) -> {
            Optional<PersonDto> person = personRepository.findById(personId);
            person.ifPresent((p) -> {
                p.getBooks().add(b);
                personRepository.saveAndFlush(p);
            });
        });
    }

    @Override
    public void returnBook(Integer personId, Integer bookId){
        Optional<PersonDto> person = personRepository.findById(personId);
        person.ifPresent((p) -> {
                p.getBooks().removeIf((b) -> b.getId().equals(bookId));
                personRepository.saveAndFlush(p);
            }
        );
    }

    @Override
    public List<BookDto> getBooksBorrowedByPerson(Integer personId){
        return personRepository.booksBorrowedByPersonId(personId);
    }
    
}
