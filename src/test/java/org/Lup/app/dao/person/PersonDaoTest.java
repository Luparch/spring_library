package org.Lup.app.dao.person;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {

    private static final String host = "localhost";
    private static final int port = 5432;
    private static final String db = "library_test";
    private static final String user = "postgres";
    private static final String password = "Ytp1288";
    private static PGDataSource ds;
    private static PersonDaoExHandler dao;

    private static PersonDto person;

    @BeforeAll
    static void init(){
        ds = new PGDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(db);
        ds.setUser(user);
        ds.setPassword(password);
        dao = new PersonDaoExHandler(new RawPersonDao(ds));
    }

    @BeforeEach
    void truncateTables() throws SQLException {
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("TRUNCATE books, persons CASCADE");
        st.close();
        conn.close();
    }

    @BeforeEach
    void setTestObjects(){
        person = new PersonDto();
        person.setName("n1");
        person.setSecondName("f1");
        person.setPatronymic("p1");
        Calendar cal = new GregorianCalendar(2003, Calendar.JANUARY, 20);
        person.setBirthDay(cal.getTime().getTime());
    }

    @Test
    void updatingOfAbsentPersonHasNoEffect(){
        person.setName("updated");
        person.setId(1);


        dao.update(person);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(), setPersonIdToNull(list));
    }

    @Test
    void personCanHaveEmptyPatronymic(){
        person.setPatronymic("");

        dao.store(person);

        List<PersonDto> list = dao.getAll();
        assertEquals(Set.of(person), setPersonIdToNull(list));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSourceJsonPath("src\\test\\resources\\person\\personForbiddenTestData.json")
    void storingThisForbidden(PersonDto dto){
        assertThrows(DomainException.class, () -> dao.store(dto));
    }


    @Nested
    class OnePersonAdded{
        @BeforeEach
        void addPerson(){
            dao.store(person);
        }
        @Test
        void storedPersonIsPresent(){
            List<PersonDto> list = dao.getAll();
            assertEquals(Set.of(person), setPersonIdToNull(list));
        }

        @Test
        void getPersonById(){
            Integer id = dao.getAll().get(0).getId();
            PersonDto retrievedPerson = dao.get(id).get();

            retrievedPerson.setId(null);
            assertEquals(person, retrievedPerson);
        }

        @Test
        void deletedPersonIsAbsent(){
            Integer id = dao.getAll().get(0).getId();
            dao.delete(id);

            List<PersonDto> list = dao.getAll();
            assertEquals(Set.of(), setPersonIdToNull(list));
        }

        @Test
        void updatedPersonIsReplaced(){
            Integer id = dao.getAll().get(0).getId();
            person.setName("updated");
            person.setId(id);
            dao.update(person);

            List<PersonDto> list = dao.getAll();
            assertEquals(Set.of(person), new HashSet<>(list));
        }

        @Test
        void personCantBorrowAbsentBook(){
            Integer person_id = dao.getAll().get(0).getId();
            assertThrows(DomainException.class, () -> dao.borrowBook(person_id,1));
        }
    }

    @Nested
    class OneBookAdded{
        private AuthorDto author;
        private BookDto book1;

        @BeforeEach
        void addBook() throws SQLException {
            author = new AuthorDto();
            author.setName("n1");
            author.setSecondName("f1");
            author.setPatronymic("p1");

            book1 = new BookDto();
            book1.setAuthors(new AuthorDto[] {author});
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
            dao.store(person);
            Integer personId = dao.getAll().get(0).getId();
            dao.borrowBook(personId, book1.getId());

            dao.returnBook(personId, book1.getId());

            assertEquals(0, dao.booksBorrowedByPerson(personId).size());
        }

        @Test
        void borrowingTwiceForbidden(){
            dao.store(person);
            Integer personId = dao.getAll().get(0).getId();

            dao.borrowBook(personId, book1.getId());

            assertThrows(DomainException.class, () -> dao.borrowBook(personId, book1.getId()));
        }

        @Test
        void returningNotBorrowedBookForbidden(){
            dao.store(person);
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
