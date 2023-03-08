package org.Lup.app.service.person;

import org.Lup.app.dao.book.BookRepository;
import org.Lup.app.dao.person.PersonRepository;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonServiceImpl personService;

    private PersonDto person;

    private BookDto book;

    @BeforeEach
    void init(){
        person = spy(PersonDto.builder()
                .id(1)
                .name("n1")
                .secondName("f1")
                .patronymic("p1")
                .birthDay(LocalDate.of(2003, Month.JANUARY, 20))
                .books(new ArrayList<>())
                .build());
        book = BookDto.builder()
                .id(2)
                .title("The 1")
                .authors(new ArrayList<>())
                .build();
    }

    @Test
    void borrowExistingBookAndPersonIsOk(){
        when(personRepository.findById(1)).thenReturn(Optional.of(person));
        when(bookRepository.findById(2)).thenReturn(Optional.of(book));

        personService.borrowBook(1,2);

        assertTrue(person.getBooks().contains(book));
        verify(personRepository).saveAndFlush(person);
    }

    @Test
    void borrowExistingPersonAndNotBookIsWrong(){
        personService.borrowBook(1,999);

        assertTrue(person.getBooks().isEmpty());
    }

    @Test
    void borrowExistingBookAndNotPersonIsWrong(){
        when(bookRepository.findById(2)).thenReturn(Optional.of(book));

        personService.borrowBook(999, 2);

        assertTrue(person.getBooks().isEmpty());
    }

    @Test
    void borrowNotExistingBookAndPersonIsWrong(){
        personService.borrowBook(999, 999);

        assertTrue(person.getBooks().isEmpty());
    }

    @Test
    void returnBorrowedIsOk(){
        when(personRepository.findById(1)).thenReturn(Optional.of(person));
        person.getBooks().add(book);

        personService.returnBook(1,2);

        assertFalse(person.getBooks().contains(book));
        verify(personRepository).saveAndFlush(person);
    }

    @Test
    void returnNotBorrowedIsWrong(){
        when(personRepository.findById(1)).thenReturn(Optional.of(person));

        personService.returnBook(1,2);



        assertFalse(person.getBooks().contains(book));
        verify(personRepository).saveAndFlush(person);
    }

}
