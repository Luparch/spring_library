package org.Lup.app.dao.book;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookDaoTest {
    @Autowired
    private BookRepository dao;
    private AuthorDto author1, author2, author3;
    private BookDto book, copyOfBook;

    @BeforeEach
    void setTestObjects(){
        author1 = AuthorDto.builder()
                .name("n1")
                .secondName("f1")
                .patronymic("p1")
                .build();

        author2 = AuthorDto.builder()
                .name("n2")
                .secondName("f2")
                .patronymic("p2")
                .build();

        author3 = AuthorDto.builder()
                .name("n3")
                .secondName("f3")
                .patronymic("p3")
                .build();

        book = BookDto.builder()
                .title("The 1")
                .authors(List.of(author1,author2))
                .build();

        copyOfBook = BookDto.builder()
                .title(book.getTitle())
                .authors(List.copyOf(book.getAuthors()))
                .build();
    }

    @BeforeEach
    void truncateTables() {
        dao.deleteAll();
    }

    @Test
    void findByAuthorTest(){
        dao.saveAndFlush(book);

        List<BookDto> list = dao.getByAuthor(author1);
        assertEquals(Set.of(book), setBookIdToNull(list));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSourceJsonPath("src\\test\\resources\\book\\bookAllowedTestData.json")
    void storingThisAllowed(BookDto dto){
        dao.saveAndFlush(dto);

        List<BookDto> list = dao.findAll();
        assertEquals(Set.of(dto), setBookIdToNull(list));
    }

    @Test
    void authorsListIsLikeSetOfAuthors(){
        book.setAuthors(List.of(author1,author1));
        dao.saveAndFlush(book);

        book.setAuthors((List.of(author1)));
        List<BookDto> list = dao.findAll();
        assertEquals(Set.of(book), setBookIdToNull(list));
    }

    @Nested
    class OneBookAdded{
        @BeforeEach
        void addBook(){
            dao.saveAndFlush(book);
        }
        @Test
        void storedBookIsPresent(){
            List<BookDto> list = dao.findAll();
            assertEquals(Set.of(book), setBookIdToNull(list));
        }

        @Test
        void getBookById(){
            Integer id = dao.findAll().get(0).getId();
            BookDto retrievedBook = dao.findById(id).get();
            retrievedBook.setId(null);

            assertEquals(book, retrievedBook);
        }

        @Test
        void deletedBookIsAbsent(){
            Integer id = dao.findAll().get(0).getId();
            dao.deleteById(id);

            List<BookDto> list = dao.findAll();
            assertEquals(Set.of(), setBookIdToNull(list));
        }

        @Test
        void updatedBookIsReplaced(){
            book.setTitle("updated");
            book.setAuthors(List.of(author1, author3));
            Integer id = dao.findAll().get(0).getId();
            book.setId(id);
            dao.flush();

            List<BookDto> list = dao.findAll();
            assertEquals(Set.of(book), new HashSet<>(list));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @ArgumentsSourceJsonPath("src\\test\\resources\\book\\storingSecondBookAllowedTestData.json")
        void storingThisSecondBookAllowed(BookDto dto){
            dao.saveAndFlush(dto);

            List<BookDto> list = dao.findAll();
            assertEquals(Set.of(book, dto), setBookIdToNull(list));
        }

    }

    private Set<BookDto> setBookIdToNull(List<BookDto> list){
        return list.stream()
                .peek(bookDto -> bookDto.setId(null))
                .collect(Collectors.toSet());
    }

}
