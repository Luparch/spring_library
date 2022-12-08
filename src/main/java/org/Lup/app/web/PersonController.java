package org.Lup.app.web;

import org.Lup.app.facade.Facade;
import org.Lup.app.web.constant.WebConstant;
import org.Lup.app.web.request.PersonRequest;
import org.Lup.app.web.response.PersonResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/person",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    private final Facade facade;

    public PersonController(Facade facade) {
        this.facade = facade;
    }

    @GetMapping("/get/{personId}")
    public PersonResponse getPerson(@PathVariable Integer personId){
        PersonResponse response = facade.getPersonById(personId);
        return response;
    }

    @GetMapping("/get")
    public List<PersonResponse> getAllPersons(){
        List<PersonResponse> list = facade.getAllPersons();
        return list;
    }

    @DeleteMapping("/delete/{personId}")
    public void deletePerson(@PathVariable Integer personId){
        facade.deletePersonById(personId);
    }

    @PutMapping("/update/{personId}")
    public void updatePerson(@PathVariable Integer personId, @RequestBody PersonRequest request){
        facade.updatePersonById(personId, request);
    }

    @PostMapping("/create")
    public void createPerson(@RequestBody PersonRequest request){
        facade.createPerson(request);
    }

    @PostMapping("/{personId}/borrow/{bookId}")
    public void borrowBook(@PathVariable Integer personId, @PathVariable Integer bookId){
        facade.borrowBook(personId, bookId);
    }

    @DeleteMapping("/{personId}/return/{bookId}")
    public void returnBook(@PathVariable Integer personId, @PathVariable Integer bookId){
        facade.returnBook(personId, bookId);
    }

}
