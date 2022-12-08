package org.Lup.app.dao;

import org.Lup.app.dto.BookDto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookDao extends AbstractDao{

    public void store(BookDto book){
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("INSERT INTO books(title, author) VALUES (?, ?)");) {
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BookDto get(Integer id){
        BookDto dto = new BookDto();
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM books WHERE id=?");) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                dto.setId(rs.getInt("id"));
                dto.setAuthor(rs.getString("author"));
                dto.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    public List<BookDto> getAll(){
        List<BookDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM books");) {
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                BookDto dto = new BookDto();
                dto.setId(rs.getInt("id"));
                dto.setAuthor(rs.getString("author"));
                dto.setTitle(rs.getString("title"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void delete(Integer id){
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE id=?");) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(BookDto book){
        Connection conn = super.getConnection();
        String query = "UPDATE books SET title=? author=? WHERE id=?";
        try(PreparedStatement pst = conn.prepareStatement(query);) {
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setInt(3, book.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookDto> getByAuthor(String author){
        List<BookDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM books WHERE author=?");) {
            pst.setString(1, author);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                BookDto dto = new BookDto();
                dto.setId(rs.getInt("id"));
                dto.setAuthor(rs.getString("author"));
                dto.setTitle(rs.getString("title"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void closeConnection(){
        super.closeConnection();
    }

}
