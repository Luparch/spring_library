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

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BookDaoTest {

    private static final String host = "localhost";
    private static final int port = 5432;
    private static final String db = "library_test";
    private static final String user = "postgres";
    private static final String password = "Ytp1288";
    private static PGDataSource ds;
    private static BookDao dao;


    private Map<String, Class<?>> map;
    private AuthorDto author1, author2, author3;
    private BookDto book, copyOfBook;

    @BeforeAll
    static void init() throws SQLException {
        ds = new PGDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(db);
        ds.setUser(user);
        ds.setPassword(password);
        dao = new BookDao(new RawBookDao(ds));
    }

    @BeforeEach
    void setTestObjects(){
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

        book = new BookDto();
        book.setTitle("The 1");
        book.setAuthors(new AuthorDto[] {author1,author2});

        copyOfBook = new BookDto();
        copyOfBook.setTitle(book.getTitle());
        copyOfBook.setAuthors(book.getAuthors().clone());
    }
    @BeforeEach
    void truncateTables() throws SQLException {
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("TRUNCATE books CASCADE");
        st.close();
        conn.close();
    }

    @Test
    void updatingOfAbsentBookForbidden(){
        book.setId(1);

        assertThrows(DomainException.class, () -> dao.update(book));
    }

    @Test
    void nullTitleForbidden(){
        book.setTitle(null);

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void nullAuthorForbidden(){
        book.setAuthors(new AuthorDto[] {null});

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void nullFieldInAuthorForbidden(){
        author1.setName(null);
        book.setAuthors(new AuthorDto[] {author1});

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void nullAuthorsArrayForbidden(){
        book.setAuthors(null);

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void emptyAuthorsArrayForbidden(){
        book.setAuthors(new AuthorDto[] {});

        assertThrows(DomainException.class, () -> dao.store(book));
    }

    @Test
    void authorCanBeUnknownWithEmptyStringFields(){
        author1.setName("");
        author1.setSecondName("");
        author1.setPatronymic("");
        book.setAuthors(new AuthorDto[] {author1});

        dao.store(book);

        List<BookDto> list = dao.getAll();
        assertEquals(Set.of(book), setBookIdToNull(list));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class OneBookAdded{
        @BeforeEach
        void addBook(){
            dao.store(book);
        }
        @Test
        void storedBookIsPresent(){
            List<BookDto> list = dao.getAll();
            assertEquals(Set.of(book), setBookIdToNull(list));
        }

        @Test
        void deletedBookIsAbsent(){
            Integer id = dao.getAll().get(0).getId();
            dao.delete(id);

            List<BookDto> list = dao.getAll();
            assertEquals(Set.of(), setBookIdToNull(list));
        }

        @Test
        void updatedBookIsReplaced(){
            book.setTitle("updated");
            book.setAuthors(new AuthorDto[]{author1, author3});
            Integer id = dao.getAll().get(0).getId();
            book.setId(id);
            dao.update(book);

            List<BookDto> list = dao.getAll();
            assertEquals(Set.of(book), new HashSet<>(list));
        }

        @Test
        void getBookById(){
            Integer id = dao.getAll().get(0).getId();
            BookDto retrievedBook = dao.get(id).get();
            retrievedBook.setId(null);

            assertEquals(book, retrievedBook);
        }


        @Test
        void sameTitleSameSetAuthors(){
            dao.store(copyOfBook);

            List<BookDto> list = dao.getAll();
            assertEquals(Set.of(book), setBookIdToNull(list));
        }

        @Test
        void sameTitleSubSetAuthors(){
            copyOfBook.setAuthors(new AuthorDto[] {author1});
            dao.store(copyOfBook);

            assertEquals(Set.of(book, copyOfBook), setBookIdToNull(dao.getAll()));
        }

        @Test
        void sameTitleSuperSetAuthors(){
            copyOfBook.setAuthors(new AuthorDto[] {author1, author2, author3});
            dao.store(copyOfBook);

            assertEquals(Set.of(book, copyOfBook), setBookIdToNull(dao.getAll()));
        }

        @Test
        void sameTitleCrossingSetAuthors(){
            copyOfBook.setAuthors(new AuthorDto[] {author1, author3});
            dao.store(copyOfBook);

            assertEquals(Set.of(book, copyOfBook), setBookIdToNull(dao.getAll()));
        }

        @Test
        void sameTitleNotCrossingSetAuthors(){
            copyOfBook.setAuthors(new AuthorDto[] {author3});
            dao.store(copyOfBook);

            assertEquals(Set.of(book, copyOfBook), setBookIdToNull(dao.getAll()));
        }
    }

    private Set<BookDto> setBookIdToNull(List<BookDto> list){
        return list.stream()
                .peek(bookDto -> bookDto.setId(null))
                .collect(Collectors.toSet());
    }

}
