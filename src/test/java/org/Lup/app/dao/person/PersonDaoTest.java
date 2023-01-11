package org.Lup.app.dao.person;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonDaoTest {

    private final String host = "localhost";
    private final int port = 5432;
    private final String db = "library_test";
    private final String user = "postgres";
    private final String password = "Ytp1288";
    private PGDataSource ds;
    private PersonDao dao;

    private PersonDto person1;

    @BeforeAll
    void init(){
        ds = new PGDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(db);
        ds.setUser(user);
        ds.setPassword(password);
        dao = new PersonDao(new RawPersonDao(ds));

        person1 = new PersonDto();
        person1.setName("n1");
        person1.setSecondName("f1");
        person1.setPatronymic("p1");
        Calendar cal = new GregorianCalendar(2003, Calendar.JANUARY, 20);
        person1.setBirthDay(cal.getTime().getTime());
    }

    @BeforeEach
    @AfterAll
    void truncateTables() throws SQLException {
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("TRUNCATE books, persons CASCADE");
        st.close();
        conn.close();
    }

    @Test
    void storedPersonIsPresent(){
        dao.store(person1);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(person1), setPersonIdToNull(list));
    }

    @Test
    void deletedPersonIsAbsent(){
        dao.store(person1);

        Integer id = dao.getAll().get(0).getId();
        dao.delete(id);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(), setPersonIdToNull(list));
    }

    @Test
    void updatedPersonIsReplaced(){
        dao.store(person1);
        Integer id = dao.getAll().get(0).getId();
        PersonDto person = new PersonDto();
        person.setName(person1.getName() + " updated");
        person.setSecondName(person1.getSecondName());
        person.setPatronymic(person1.getPatronymic());
        person.setBirthDay(person1.getBirthDay());
        person.setId(id);

        dao.update(person);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(person), new HashSet<>(list));
    }

    @Test
    void getPersonById(){
        dao.store(person1);

        Integer id = dao.getAll().get(0).getId();
        PersonDto person = dao.get(id).get();

        person.setId(null);
        assertEquals(person1, person);
    }

    @Test
    void updatingOfAbsentPersonHasNoEffect(){
        PersonDto person = new PersonDto();
        person.setName(person1.getName() + " updated");
        person.setSecondName(person1.getSecondName());
        person.setPatronymic(person1.getPatronymic());
        person.setBirthDay(person1.getBirthDay());
        person.setId(1);

        dao.update(person);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(), setPersonIdToNull(list));
    }

    @Test
    void nullNameForbidden(){
        person1.setName(null);

        assertThrows(DomainException.class, () -> dao.store(person1));

        person1.setName("n1");
    }

    @Test
    void nullSecondNameForbidden(){
        person1.setSecondName(null);

        assertThrows(DomainException.class, () -> dao.store(person1));

        person1.setSecondName("f1");
    }

    @Test
    void nullPatronymicForbidden(){
        person1.setPatronymic(null);

        assertThrows(DomainException.class, () -> dao.store(person1));

        person1.setPatronymic("p1");
    }

    @Test
    void nullBirthDateForbidden(){
        PersonDto person = new PersonDto();
        person.setName(person1.getName());
        person.setSecondName(person1.getSecondName());
        person.setPatronymic(person1.getPatronymic());
        person.setBirthDay(null);

        assertThrows(DomainException.class, () -> dao.store(person));
    }

    @Test
    void personCantBorrowAbsentBook(){
        dao.store(person1);

        Integer person_id = dao.getAll().get(0).getId();
        assertThrows(DomainException.class, () -> dao.borrowBook(person_id,1));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class BookPresent{
        private AuthorDto author1;
        private BookDto book1;

        @BeforeEach
        void addBook() throws SQLException {
            author1 = new AuthorDto();
            author1.setName("n1");
            author1.setSecondName("f1");
            author1.setPatronymic("p1");

            book1 = new BookDto();
            book1.setAuthors(new AuthorDto[] {author1});
            book1.setTitle("The 1");

            Connection conn = ds.getConnection();
            PreparedStatement pst = conn.prepareStatement("CALL insert_book_with_authors(?, ?)");
            pst.setString(1, book1.getTitle());
            pst.setArray(2, conn.createArrayOf("author", book1.getAuthors()));
            pst.executeUpdate();
            pst.close();

            pst = conn.prepareStatement("SELECT * FROM select_all_books()");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                book1.setId(rs.getInt("book_id"));
            }
            pst.close();
            conn.close();
        }

        @Test
        void absentPersonCantBorrowBook(){
            assertThrows(DomainException.class, () -> dao.borrowBook(1, book1.getId()));
        }

        @Test
        void absentPersonCantReturnBook(){
            assertThrows(DomainException.class, () -> dao.returnBook(1, book1.getId()));
        }

        @Test
        void personReturnBorrowedBook(){
            dao.store(person1);
            Integer personId = dao.getAll().get(0).getId();
            dao.borrowBook(personId, book1.getId());

            dao.returnBook(personId, book1.getId());

            assertEquals(0, dao.booksBorrowedByPerson(personId).size());
        }

        @Test
        void borrowingTwiceForbidden(){
            dao.store(person1);
            Integer personId = dao.getAll().get(0).getId();

            dao.borrowBook(personId, book1.getId());

            assertThrows(DomainException.class, () -> dao.borrowBook(personId, book1.getId()));
        }

        @Test
        void returningNotBorrowedBookForbidden() throws SQLException {
            dao.store(person1);
            Integer personId = dao.getAll().get(0).getId();

            assertThrows(DomainException.class, () -> dao.returnBook(personId, book1.getId()));
        }

    }

    private Set<PersonDto> setPersonIdToNull(List<PersonDto> list){
        return list.stream()
                .peek(personDto -> personDto.setId(null))
                .collect(Collectors.toSet());
    }
}
