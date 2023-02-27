package org.Lup.app.dao.person;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class PersonDaoTest {
    @Autowired
    private PersonRepository dao;

    private PersonDto person;

    @BeforeEach
    void truncateTables() {
        dao.deleteAll();
    }

    @BeforeEach
    void setTestObjects(){
        PersonDto person = PersonDto.builder()
                .name("n1")
                .secondName("f1")
                .patronymic("p1")
                .birthDay(LocalDate.of(2003, Month.JANUARY, 20))
                .build();
    }




    private Set<PersonDto> setPersonIdToNull(List<PersonDto> list){
        return list.stream()
                .peek(personDto -> personDto.setId(null))
                .collect(Collectors.toSet());
    }
}
