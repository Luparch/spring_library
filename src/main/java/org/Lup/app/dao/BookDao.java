package org.Lup.app.dao;

import org.Lup.app.dto.BookDto;
import org.Lup.app.exception.DomainException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookDao extends AbstractDao {

    public void store(BookDto dto){
        try{
            doStore(dto);
        }catch(SQLException e){
            if("23502".equals(e.getSQLState())){
                throw new DomainException("Все поля должны быть не null");
            }
            else{
                throw new RuntimeException(e);
            }
        }
}
    private void doStore(BookDto book) throws SQLException{
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("INSERT INTO books(title, author) VALUES (?, ?)");
        pst.setString(1, book.getTitle());
        pst.setString(2, book.getAuthor());
        pst.executeUpdate();
        pst.close();
    }

    public Optional<BookDto> get(Integer id){
        try{
            return doGet(id);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    private Optional<BookDto> doGet(Integer id) throws SQLException{
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM books WHERE id=?");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        BookDto dto = null;
        if(rs.next()) {
            dto = new BookDto();
            dto.setId(rs.getInt("id"));
            dto.setAuthor(rs.getString("author"));
            dto.setTitle(rs.getString("title"));
        }
        return Optional.ofNullable(dto);
    }

    public List<BookDto> getAll() {
        try {
            return doGetAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private List<BookDto> doGetAll() throws SQLException{
        List<BookDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM books");
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            BookDto dto = new BookDto();
            dto.setId(rs.getInt("id"));
            dto.setAuthor(rs.getString("author"));
            dto.setTitle(rs.getString("title"));
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
        PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE id=?");
        pst.setInt(1, id);
        pst.executeUpdate();
        pst.close();
    }

    public void update(BookDto dto){
        try {
            doUpdate(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void doUpdate(BookDto dto) throws SQLException{
        Connection conn = super.getConnection();
        String query = "UPDATE books SET title=? author=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, dto.getTitle());
        pst.setString(2, dto.getAuthor());
        pst.setInt(3, dto.getId());
        pst.executeUpdate();
    }

    public List<BookDto> getByAuthor(String author){
        try {
            return doGetByAuthor(author);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<BookDto> doGetByAuthor(String author) throws SQLException{
        List<BookDto> list = new ArrayList<>();
        Connection conn = super.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM books WHERE author=?");
        pst.setString(1, author);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            BookDto dto = new BookDto();
            dto.setId(rs.getInt("id"));
            dto.setAuthor(rs.getString("author"));
            dto.setTitle(rs.getString("title"));
            list.add(dto);
        }
        return list;
    }

    @Override
    public void closeConnection(){
        super.closeConnection();
    }

}
