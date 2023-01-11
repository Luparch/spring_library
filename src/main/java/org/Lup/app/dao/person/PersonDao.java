package org.Lup.app.dao.person;

import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class PersonDao{

    RawPersonDao dao;
    public PersonDao(RawPersonDao dao){
        this.dao = dao;
    }
    public void store(PersonDto dto){
        try {
            if(dto.getBirthDay() == null)
                throw new DomainException("Все поля должны быть не null");
            dao.store(dto);
        } catch (SQLException e) {
            if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }
    public Optional<PersonDto> get(Integer id){
        try {
            return dao.get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<PersonDto> getAll(){
        try {
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id){
        try {
            dao.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(PersonDto dto){
        try {
            if(dto.getBirthDay() == null)
                throw new DomainException("Все поля должны быть не null");
            dao.update(dto);
        } catch (SQLException e) {
            if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }


    public void borrowBook(Integer personId, Integer bookId){
        try {
            dao.borrowBook(personId, bookId);
        } catch (SQLException e) {
            if("23000".equals(e.getSQLState())){
                throw new DomainException("Пользователь или книга отсутствуют в базе данных");
            }
            else if("23505".equals(e.getSQLState())){
                throw new DomainException("Попытка пользователя взять книгу, которую он уже брал, но еще не вернул");
            }
            else if("23503".equals(e.getSQLState())){
                throw new DomainException("Несуществующий пользователь не может взять книгу и наоборот");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }

    public List<Integer> booksBorrowedByPerson(Integer person_id){
        try{
            return dao.booksBorrowedByPerson(person_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBook(Integer personId, Integer bookId){
        try {
            dao.returnBook(personId, bookId);
        } catch (SQLException e) {
            if("09000".equals(e.getSQLState())){
                throw new DomainException("Нельзя вернуть книгу, если пользователь ее не брал");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }

}
