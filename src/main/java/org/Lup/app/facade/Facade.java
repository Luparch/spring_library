package org.Lup.app.facade;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.dto.PersonDto;
import org.Lup.app.exception.DomainException;
import org.Lup.app.mapper.BookMapper;
import org.Lup.app.mapper.PersonMapper;
import org.Lup.app.service.BookService;
import org.Lup.app.service.PersonService;
import org.Lup.app.web.request.AuthorRequest;
import org.Lup.app.web.request.BookRequest;
import org.Lup.app.web.request.PersonRequest;
import org.Lup.app.web.response.BookResponse;
import org.Lup.app.web.response.PersonResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Facade {

    private final BookService bookService;

    private final BookMapper bookMapper;

    private final PersonService personService;

    private final PersonMapper personMapper;

    public Facade(BookService bookService, BookMapper bookMapper,
                  PersonService personService, PersonMapper personMapper){
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.personService = personService;
        this.personMapper = personMapper;
    }

    public Optional<BookResponse> getBookById(Integer bookId) throws DomainException {
        return bookService.getBookById(bookId)
                .map(bookMapper::bookDtoToBookResponse);
    }


    public List<BookResponse> getAllBooks(){
        List<BookDto> list = bookService.getAllBooks();
        return list.stream().map(bookMapper::bookDtoToBookResponse).toList();
    }

    public void deleteBookById(Integer bookId){
        bookService.deleteBookById(bookId);
    }

    public void updateBookById(Integer bookId, BookRequest request) throws DomainException {
        BookDto dto = bookMapper.bookRequestToBookDto(request);
        bookService.updateBookById(bookId, dto);
    }

    public void createBook(BookRequest request) throws DomainException {
        BookDto dto = bookMapper.bookRequestToBookDto(request);
        bookService.createBook(dto);

    }

    public Optional<PersonResponse> getPersonById(Integer personId) throws DomainException {
        return personService.getPersonById(personId)
                .map(personMapper::personDtoToPersonResponse);
    }

    public List<PersonResponse> getAllPersons(){
        List<PersonDto> list = personService.getAllPersons();
        return list.stream().map(personMapper::personDtoToPersonResponse).toList();
    }

    public void deletePersonById(Integer personId){
        personService.deletePersonById(personId);
    }

    public void createPerson(PersonRequest request) throws DomainException {
        PersonDto dto = personMapper.personRequestToPersonDto(request);
        personService.createPerson(dto);
    }

    public List<BookResponse> getBooksByAuthor(AuthorRequest request){
        AuthorDto dto = bookMapper.authorRequestToAuthorDto(request);
        List<BookDto> list = bookService.getBooksByAuthor(dto);
        return list.stream()
                .map(bookMapper::bookDtoToBookResponse)
                .toList();
    }

    public void updatePersonById(Integer personId, PersonRequest request) throws DomainException {
        PersonDto dto = personMapper.personRequestToPersonDto(request);
        personService.updatePersonById(personId, dto);
    }

    public void borrowBook(Integer personId, Integer bookId) throws DomainException {
        personService.borrowBook(personId, bookId);
    }

    public void returnBook(Integer personId, Integer bookId) throws DomainException {
        personService.returnBook(personId, bookId);
    }

    public List<BookDto> getBorrowedBooks(Integer personId) {
        return personService.getBooksBorrowedByPerson(personId);
    }
}
