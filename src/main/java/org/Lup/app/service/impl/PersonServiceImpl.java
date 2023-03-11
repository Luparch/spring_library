package org.Lup.app.service.impl;

import org.Lup.app.dao.person.PersonDao;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;

    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public Optional<PersonDto> getPersonById(Integer id){
        return personDao.get(id);
    }

    @Override
    public void deletePersonById(Integer id) {
        personDao.delete(id);
    }

    @Override
    public void createPerson(PersonDto dto){
        personDao.store(dto);
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personDao.getAll();
    }

    @Override
    public void updatePersonById(Integer personId, PersonDto dto){
        dto.setId(personId);
        personDao.update(dto);
    }

    @Override
    public void borrowBook(Integer personId, Integer bookId){
        personDao.borrowBook(personId, bookId);
    }

    @Override
    public void returnBook(Integer personId, Integer bookId){
        personDao.returnBook(personId, bookId);
    }

    @Override
    public List<Integer> borrowedBooks(Integer personId) {
        return personDao.booksBorrowedByPerson(personId);
    }

}
