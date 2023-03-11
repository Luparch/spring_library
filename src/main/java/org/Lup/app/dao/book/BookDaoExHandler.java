package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.exception.DomainException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class BookDaoExHandler implements BookDao {

    private final RawBookDao dao;

    public BookDaoExHandler(RawBookDao dao){
        this.dao = dao;
    }

    @Override
    public void store(BookDto dto){
        try{
            correctBook(dto);
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
    @Override
    public Optional<BookDto> get(Integer id){
        try{
            return dao.get(id);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<BookDto> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(Integer id){
        try {
            dao.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(BookDto dto){
        try {
            correctBook(dto);
            dao.update(dto);
        } catch (SQLException e) {
            if("23503".equals(e.getSQLState())){
                throw new DomainException("Книги с таким id нет");
            }
            else if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }

    }
    @Override
    public List<BookDto> getByAuthor(AuthorDto dto){
        try {
            return dao.getByAuthor(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void correctBook(BookDto dto){
       AuthorDto[] arr = dto.getAuthors();
       if(arr == null || arr.length == 0){
           arr = new AuthorDto[] {new AuthorDto()};
       }
       for(int i = 0; i < arr.length; i++){
           if(arr[i] == null)
               arr[i] = new AuthorDto();
           correctAuthor(arr[i]);
       }
       dto.setAuthors(arr);
    }

    private void correctAuthor(AuthorDto dto){
        if(dto.getName() == null)
            dto.setName("");
        if(dto.getSecondName() == null)
            dto.setSecondName("");
        if(dto.getPatronymic() == null)
            dto.setPatronymic("");
    }

}
