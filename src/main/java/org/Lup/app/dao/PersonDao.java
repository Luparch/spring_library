package org.Lup.app.dao;

import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PersonDao extends AbstractDao{

    public void store(PersonDto dto){
        try {
            doStore(dto);
        } catch (SQLException e) {
            if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }
    private void doStore(PersonDto dto) throws SQLException{
        Connection conn = super.getConnection();
        String query = "INSERT INTO persons(name, second_name, patronymic, birth_date) VALUES (?,?,?,?)";
        try(PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setString(1, dto.getName());
            pst.setString(2, dto.getSecondName());
            pst.setString(3, dto.getPatronymic());
            pst.setDate(4, new Date(dto.getBirthDay()));
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DomainException(e.getMessage());
        }
    }

    public Optional<PersonDto> get(Integer id){
        try {
            return doGet(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Optional<PersonDto> doGet(Integer id) throws SQLException{
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM persons WHERE id=?");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        PersonDto dto = null;
        if(rs.next()){
            dto = new PersonDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString( "name"));
            dto.setSecondName(rs.getString("second_name"));
            dto.setPatronymic(rs.getString("patronymic"));
            dto.setBirthDay(rs.getDate("birth_date").getTime());
        }
        pst.close();
        return Optional.ofNullable(dto);
    }

    public List<PersonDto> getAll(){
        try {
            return doGetAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private List<PersonDto> doGetAll() throws SQLException{
        List<PersonDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM persons");
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            PersonDto dto = new PersonDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setSecondName(rs.getString("second_name"));
            dto.setPatronymic(rs.getString("patronymic"));
            dto.setBirthDay(rs.getDate("birth_date").getTime());
            list.add(dto);
        }
        pst.close();
        return list;
    }

    public void delete(Integer id){
        try {
            doDelete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void doDelete(Integer id) throws SQLException{
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("DELETE FROM persons WHERE id=?");
        pst.setInt(1, id);
        pst.executeUpdate();
        pst.close();
    }

    public void update(PersonDto dto){
        try {
            doUpdate(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void doUpdate(PersonDto dto) throws SQLException{
        Connection conn = super.getConnection();
        String query = "UPDATE persons SET name=?, second_name=?, patronymic=?, birth_date=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, dto.getName());
        pst.setString(2, dto.getSecondName());
        pst.setString(3, dto.getPatronymic());
        pst.setDate(4, new Date(dto.getBirthDay()));
        pst.setInt(5, dto.getId());
        pst.executeUpdate();
        pst.close();
    }

    public void borrowBook(Integer personId, Integer bookId){
        try {
            doBorrowBook(personId, bookId);
        } catch (SQLException e) {
            if("23000".equals(e.getSQLState())){
                throw new DomainException("Пользователь или книга отсутствуют в базе данных");
            }
            else if("23505".equals(e.getSQLState())){
                throw new DomainException("Попытка пользователя взять книгу, которую он уже брал, но еще не вернул");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }
    private void doBorrowBook(Integer personId, Integer bookId) throws SQLException{
        Connection conn = super.getConnection();
        String query = "INSERT INTO persons_borrowed_books (person_id, book_id) VALUES (?,?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, personId);
        pst.setInt(2, bookId);
        pst.executeUpdate();
        pst.close();
    }

    public void returnBook(Integer personId, Integer bookId){
        try {
            doReturnBook(personId, bookId);
        } catch (SQLException e) {
            if("09000".equals(e.getSQLState())){
                throw new DomainException("Нельзя вернуть книгу, если пользователь ее не брал");
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }
    private void doReturnBook(Integer personId, Integer bookId) throws SQLException{
        Connection conn = super.getConnection();
        String query = "DELETE FROM persons_borrowed_books WHERE person_id=? AND book_id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, personId);
        pst.setInt(2, bookId);
        pst.executeUpdate();
        pst.close();
    }

    @Override
    public void closeConnection(){
        super.closeConnection();
    }

}
