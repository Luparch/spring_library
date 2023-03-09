package org.Lup.app.web;

import org.Lup.app.dto.BookDto;
import org.Lup.app.facade.Facade;
import org.Lup.app.web.constant.WebConstant;
import org.Lup.app.web.request.PersonRequest;
import org.Lup.app.web.response.PersonResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/person",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    private final Facade facade;

    public PersonController(Facade facade) {
        this.facade = facade;
    }

    @GetMapping("/get/{personId}")
    public Optional<PersonResponse> getPerson(@PathVariable Integer personId){
        return facade.getPersonById(personId);
    }

    @GetMapping("/get")
    public List<PersonResponse> getAllPersons(){
        return facade.getAllPersons();
    }

    @DeleteMapping("/delete/{personId}")
    public void deletePerson(@PathVariable Integer personId){
        facade.deletePersonById(personId);
    }

    @PutMapping(value = "/update/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updatePerson(@PathVariable Integer personId, @Validated @RequestBody PersonRequest request){
        facade.updatePersonById(personId, request);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createPerson(@RequestBody @Validated PersonRequest request){
        facade.createPerson(request);
    }

    @PostMapping("/{personId}/borrow/{bookId}")
    public void borrowBook(@PathVariable Integer personId, @PathVariable Integer bookId){
        facade.borrowBook(personId, bookId);
    }

    @DeleteMapping("/{personId}/borrow/{bookId}")
    public void returnBook(@PathVariable Integer personId, @PathVariable Integer bookId){
        facade.returnBook(personId, bookId);
    }

    @GetMapping("/{personId}/books")
    public List<BookDto> getBorrowedBooks(@PathVariable Integer personId){
        return facade.getBorrowedBooks(personId);
    }

}
