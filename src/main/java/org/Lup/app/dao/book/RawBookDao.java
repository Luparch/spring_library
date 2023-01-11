package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
class RawBookDao{

    DataSource ds;
    private Map<String, Class<?>> map;
    public RawBookDao(DataSource ds){
        this.ds = ds;
        map = new HashMap<>();
        map.put("author", AuthorDto.class);
    }
    public void store(BookDto dto) throws SQLException{
        Connection conn = ds.getConnection();
        PreparedStatement pst = conn.prepareStatement("CALL insert_book_with_authors(?, ?)");
        pst.setString(1, dto.getTitle());
        pst.setArray(2, conn.createArrayOf("author", dto.getAuthors()));
        pst.executeUpdate();
        pst.close();
    }
    public Optional<BookDto> get(Integer id) throws SQLException{
        Connection conn = ds.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM select_book_by_id(?)");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        BookDto dto = null;
        if(rs.next()) {
            dto = new BookDto();
            dto.setId(rs.getInt("book_id"));
            dto.setTitle(rs.getString("title"));
            dto.setAuthors((AuthorDto[]) rs.getArray("authors_of_book").getArray(map));
        }
        return Optional.ofNullable(dto);
    }
    public List<BookDto> getAll() throws SQLException{
        List<BookDto> list = new ArrayList<>();
        Connection conn = ds.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM select_all_books()");
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            BookDto dto = new BookDto();
            dto.setId(rs.getInt("book_id"));
            dto.setTitle(rs.getString("title"));
            dto.setAuthors((AuthorDto[]) rs.getArray("authors_of_book").getArray(map));
            list.add(dto);
        }
        pst.close();
        return list;
    }
    public void delete(Integer id) throws SQLException{
        Connection conn = ds.getConnection();
        PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE book_id=?");
        pst.setInt(1, id);
        pst.executeUpdate();
        pst.close();
    }
    public void update(BookDto dto) throws SQLException{
        Connection conn = ds.getConnection();
        String query = "CALL update_book_with_authors(?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, dto.getId());
        pst.setString(2, dto.getTitle());
        pst.setArray(3, conn.createArrayOf("author", dto.getAuthors()));
        pst.executeUpdate();
    }

    public List<BookDto> getByAuthor(AuthorDto author) throws SQLException{
        List<BookDto> list = new ArrayList<>();
        Connection conn = ds.getConnection();
        Map<String, Class<?>> typeMap = conn.getTypeMap();
        typeMap.putAll(map);
        conn.setTypeMap(typeMap);
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM select_books_by_author(?)");
        pst.setObject(1, author);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            BookDto dto = new BookDto();
            dto.setId(rs.getInt("book_id"));
            dto.setAuthors((AuthorDto[]) rs.getArray("authors_of_book").getArray(map));
            dto.setTitle(rs.getString("title"));
            list.add(dto);
        }
        return list;
    }

}
