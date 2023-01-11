package org.Lup.app.dao.book;


import com.impossibl.postgres.jdbc.PGDataSource;
import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.exception.DomainException;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class BookDaoTest {

    private final String host = "localhost";
    private final int port = 5432;
    private final String db = "library_test";
    private final String user = "postgres";
    private final String password = "Ytp1288";
    private PGDataSource ds;
    private BookDao dao;
    private Map<String, Class<?>> map;
    private AuthorDto author1, author2, author3;
    private BookDto book1;

    @BeforeAll
    void init() throws SQLException {
        ds = new PGDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(db);
        ds.setUser(user);
        ds.setPassword(password);
        dao = new BookDao(new RawBookDao(ds));

        author1 = new AuthorDto();
        author1.setName("n1");
        author1.setSecondName("f1");
        author1.setPatronymic("p1");

        author2 = new AuthorDto();
        author2.setName("n2");
        author2.setSecondName("f2");
        author2.setPatronymic("p2");

        author3 = new AuthorDto();
        author3.setName("n3");
        author3.setSecondName("f3");
        author3.setPatronymic("p3");

        book1 = new BookDto();
        book1.setAuthors(new AuthorDto[] {author1,author2});
        book1.setTitle("The 1");
    }

    @BeforeEach
    @AfterAll
    void truncateTables() throws SQLException {
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("TRUNCATE books CASCADE");
        st.close();
        conn.close();
    }

    @Test
    void storedBookIsPresent() throws SQLException {
        dao.store(book1);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(book1), setBookIdToNull(list));
    }

    @Test
    void deletedBookIsAbsent(){
        dao.store(book1);

        Integer id = dao.getAll().get(0).getId();
        dao.delete(id);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(), setBookIdToNull(list));
    }

    @Test
    void updatedBookIsReplaced(){
        dao.store(book1);
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle() + " updated");
        other.setAuthors(new AuthorDto[]{author1, author3});

        Integer id = dao.getAll().get(0).getId();
        other.setId(id);
        dao.update(other);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(other), new HashSet<>(list));
    }

    @Test
    void getBookById(){
        dao.store(book1);

        Integer id = dao.getAll().get(0).getId();
        BookDto book = dao.get(id).get();

        book.setId(null);
        assertEquals(book1, book);
    }

    @Test
    void updatingOfAbsentBookForbidden(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle() + " updated");
        other.setAuthors(new AuthorDto[]{author1, author3});
        other.setId(1);

        assertThrows(DomainException.class, () -> dao.update(other));
    }

    @Test
    void nullTitleForbidden(){
        BookDto other = new BookDto();
        other.setTitle(null);
        other.setAuthors(book1.getAuthors());

        assertThrows(DomainException.class, () -> dao.store(other));
    }

    @Test
    void nullAuthorForbidden(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        other.setAuthors(new AuthorDto[] {null});

        assertThrows(DomainException.class, () -> dao.store(other));
    }

    @Test
    void nullFieldInAuthorForbidden(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        AuthorDto author = new AuthorDto();
        author.setName(null);
        other.setAuthors(new AuthorDto[] {author});

        assertThrows(DomainException.class, () -> dao.store(other));
    }

    @Test
    void nullAuthorsArrayForbidden(){
        BookDto book = new BookDto();
        book.setTitle(book1.getTitle());
        book.setAuthors(null);

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void emptyAuthorsArrayForbidden(){
        BookDto book = new BookDto();
        book.setTitle(book1.getTitle());
        book.setAuthors(new AuthorDto[] {});

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void authorCanBeUnknownWithEmptyStringFields(){
        BookDto book = new BookDto();
        book.setTitle(book1.getTitle());
        AuthorDto unknownAuthor = new AuthorDto();
        book.setAuthors(new AuthorDto[] {unknownAuthor});

        dao.store(book);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(book), setBookIdToNull(list));
    }

    @Test
    void sameTitleSameSetAuthors(){
        dao.store(book1);

        dao.store(book1);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(book1), setBookIdToNull(list));
    }

    @Test
    void sameTitleSubSetAuthors(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        other.setAuthors(new AuthorDto[] {author1});

        dao.store(book1);
        dao.store(other);

        assertEquals(Set.of(book1, other), setBookIdToNull(dao.getAll()));
    }

    @Test
    void sameTitleSuperSetAuthors(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        other.setAuthors(new AuthorDto[] {author1, author2, author3});

        dao.store(book1);
        dao.store(other);

        assertEquals(Set.of(book1, other), setBookIdToNull(dao.getAll()));
    }

    @Test
    void sameTitleCrossingSetAuthors(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        other.setAuthors(new AuthorDto[] {author1, author3});

        dao.store(book1);
        dao.store(other);

        assertEquals(Set.of(book1, other), setBookIdToNull(dao.getAll()));
    }

    @Test
    void sameTitleNotCrossingSetAuthors(){
        BookDto other = new BookDto();
        other.setTitle(book1.getTitle());
        other.setAuthors(new AuthorDto[] {author3});

        dao.store(book1);
        dao.store(other);

        assertEquals(Set.of(book1, other), setBookIdToNull(dao.getAll()));
    }

    private Set<BookDto> setBookIdToNull(List<BookDto> list){
        return list.stream()
                .peek(bookDto -> bookDto.setId(null))
                .collect(Collectors.toSet());
    }

}
