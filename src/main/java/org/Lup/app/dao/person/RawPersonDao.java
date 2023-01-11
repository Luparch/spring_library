package org.Lup.app.dao.person;

import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class RawPersonDao {

    DataSource ds;

    public RawPersonDao(DataSource ds){
        this.ds = ds;
    }

    public void store(PersonDto dto) throws SQLException {
        Connection conn = ds.getConnection();
        String query = "INSERT INTO persons(name, second_name, patronymic, birth_date) VALUES (?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, dto.getName());
        pst.setString(2, dto.getSecondName());
        pst.setString(3, dto.getPatronymic());
        pst.setDate(4, new Date(dto.getBirthDay()));
        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    public Optional<PersonDto> get(Integer id) throws SQLException{
        Connection conn = ds.getConnection();
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
        conn.close();
        return Optional.ofNullable(dto);
    }

    public List<PersonDto> getAll() throws SQLException{
        List<PersonDto> list = new ArrayList<>();
        Connection conn = ds.getConnection();
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
        conn.close();
        return list;
    }

    public void delete(Integer id) throws SQLException{
        Connection conn = ds.getConnection();
        PreparedStatement pst = conn.prepareStatement("DELETE FROM persons WHERE id=?");
        pst.setInt(1, id);
        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    public void update(PersonDto dto) throws SQLException{
        Connection conn = ds.getConnection();
        String query = "UPDATE persons SET name=?, second_name=?, patronymic=?, birth_date=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, dto.getName());
        pst.setString(2, dto.getSecondName());
        pst.setString(3, dto.getPatronymic());
        pst.setDate(4, new Date(dto.getBirthDay()));
        pst.setInt(5, dto.getId());
        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    public void borrowBook(Integer personId, Integer bookId) throws SQLException{
        Connection conn = ds.getConnection();
        String query = "INSERT INTO persons_borrowed_books (person_id, book_id) VALUES (?,?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, personId);
        pst.setInt(2, bookId);
        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    public List<Integer> booksBorrowedByPerson(Integer person_id) throws SQLException {
        Connection conn = ds.getConnection();
        String query = "SELECT array_agg(book_id) FROM persons_borrowed_books " +
                "WHERE person_id=? GROUP BY book_id";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, person_id);
        ResultSet rs = pst.executeQuery();
        List<Integer> list = new ArrayList<>();
        if(rs.next()){
            ResultSet arrRs = rs.getArray(1).getResultSet();
            while(arrRs.next()){
                list.add(arrRs.getInt(2));
            }
        }
        pst.close();
        conn.close();
        return list;
    }

    public void returnBook(Integer personId, Integer bookId) throws SQLException{
        Connection conn = ds.getConnection();
        String query = "call delete_record(?,?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, personId);
        pst.setInt(2, bookId);
        pst.executeUpdate();
        pst.close();
        conn.close();
    }
}
