package org.Lup.app.web;

import org.Lup.app.facade.Facade;
import org.Lup.app.web.constant.WebConstant;
import org.Lup.app.web.request.AuthorRequest;
import org.Lup.app.web.request.BookRequest;
import org.Lup.app.web.response.BookResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/book",
                produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {
    private final Facade facade;

    public BookController(Facade facade){
        this.facade = facade;
    }

    @GetMapping("/{bookId}")
    public Optional<BookResponse> getBook(@PathVariable Integer bookId){
        return facade.getBookById(bookId);
    }

    @GetMapping("/all")
    public List<BookResponse> getAllBooks(){
        return facade.getAllBooks();
    }

    @GetMapping(value = "/byAuthor", params = {"name", "secondName", "patronymic"})
    public List<BookResponse> getBooksByAuthor(@RequestParam("name") String name,
                                               @RequestParam("secondName") String secondName,
                                               @RequestParam("patronymic") String patronymic){
        AuthorRequest request = new AuthorRequest();
        request.setName(name);
        request.setSecondName(secondName);
        request.setPatronymic(patronymic);
        return facade.getBooksByAuthor(request);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Integer bookId){
        facade.deleteBookById(bookId);
    }

    @PutMapping("/{bookId}")
    public void updateBook(@PathVariable Integer bookId, @Validated @RequestBody BookRequest request) {
        facade.updateBookById(bookId, request);
    }

    @PostMapping("/create")
    public void createBook(@Validated @RequestBody BookRequest request) {
        facade.createBook(request);
    }


}
