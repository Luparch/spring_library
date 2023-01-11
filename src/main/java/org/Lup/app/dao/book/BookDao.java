package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.exception.DomainException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private RawBookDao dao;

    public BookDao(RawBookDao dao){
        this.dao = dao;
    }

    public void store(BookDto dto){
        try{
            if(dto.getAuthors()==null)
                throw new DomainException("Список авторов не должен быть null");
            boolean hasNullAuthor = Arrays.stream(dto.getAuthors()).anyMatch(authorDto -> authorDto==null);
            if(hasNullAuthor)
                throw new DomainException("Автор не должен быть null");
            if(dto.getAuthors().length==0)
                throw new DomainException("У книги должен быть автор. Если автор неизвестен, его имя, фамилия " +
                        "и отчество должны быть пустыми строками");
            dao.store(dto);
        }catch(SQLException e){
            if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }

    public Optional<BookDto> get(Integer id){
        try{
            return dao.get(id);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<BookDto> getAll() {
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

    public void update(BookDto dto){
        try {
            dao.update(dto);
        } catch (SQLException e) {
            if("23503".equals(e.getSQLState())){
                throw new DomainException("Книги с таким id нет");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }

    public List<BookDto> getByAuthor(AuthorDto dto){
        try {
            return dao.getByAuthor(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
