package org.Lup.app.web;

import org.Lup.app.exception.DomainException;
import org.Lup.app.facade.Facade;
import org.Lup.app.web.constant.WebConstant;
import org.Lup.app.web.request.BookRequest;
import org.Lup.app.web.response.BookResponse;
import org.springframework.http.MediaType;
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

    @GetMapping("/get/{bookId}")
    public Optional<BookResponse> getBook(@PathVariable Integer bookId){
        return facade.getBookById(bookId);
    }

    @GetMapping("/get")
    public List<BookResponse> getAllBooks(){
        List<BookResponse> list = facade.getAllBooks();
        return list;
    }

    @GetMapping(value = "/get", params = "author")
    public List<BookResponse> getBooksByAuthor(@RequestParam("author") String authorName){
        List<BookResponse> list = facade.getBooksByAuthor(authorName);
        return list;
    }

    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(@PathVariable Integer bookId){
        facade.deleteBookById(bookId);
    }

    @PutMapping("/update/{bookId}")
    public void updateBook(@PathVariable Integer bookId, @RequestBody BookRequest request) throws DomainException {
        facade.updateBookById(bookId, request);
    }

    @PostMapping("/create")
    public void createBook(@RequestBody BookRequest request) throws DomainException {
        facade.createBook(request);
    }


}
