package org.Lup.app.dao;

import org.Lup.app.dto.PersonDto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PersonDao extends AbstractDao{

    public PersonDto get(Integer id){
        Connection conn = super.getConnection();
        PersonDto dto = new PersonDto();
        try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM persons WHERE id=?");) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString( "name"));
                dto.setSecondName(rs.getString("second_name"));
                dto.setPatronymic(rs.getString("patronymic"));
                dto.setBirthDay(rs.getDate("birth_date").getTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    public void delete(Integer id) {
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("DELETE FROM persons WHERE id=?");) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(PersonDto person){
        Connection conn = super.getConnection();
        String query = "UPDATE persons SET name=?, second_name=?, patronymic=?, birth_date=? WHERE id=?";
        try(PreparedStatement pst = conn.prepareStatement(query);){
            pst.setString(1, person.getName());
            pst.setString(2, person.getSecondName());
            pst.setString(3, person.getPatronymic());
            pst.setDate(4, new Date(person.getBirthDay()));
            pst.setInt(5, person.getId());
            pst.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<PersonDto> getAll(){
        List<PersonDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM persons");) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void store(PersonDto person){
        Connection conn = super.getConnection();
        String query = "INSERT INTO persons(name, second_name, patronymic, birth_date) VALUES (?,?,?,?)";
        try(PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setString(1, person.getName());
            pst.setString(2, person.getSecondName());
            pst.setString(3, person.getPatronymic());
            pst.setDate(4, new Date(person.getBirthDay()));
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void borrowBook(Integer personId, Integer bookId) {
        Connection conn = super.getConnection();
        String query = "INSERT INTO persons_borrowed_books (person_id, book_id) VALUES (?,?)";
        try(PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setInt(1, personId);
            pst.setInt(2, bookId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBook(Integer personId, Integer bookId) {
        Connection conn = super.getConnection();
        String query = "DELETE FROM persons_borrowed_books WHERE person_id=? AND book_id=?";
        try(PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setInt(1, personId);
            pst.setInt(2, bookId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection(){
        super.closeConnection();
    }

}
