package exercise.controller;

import java.util.List;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;

    // BEGIN
    @GetMapping(path = "")
    public ResponseEntity<List<BookDTO>> index() {
        List<BookDTO> books = bookService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(books.size()))
                .body(books);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookDTO> show (@PathVariable("id") Long id) {
        BookDTO book = bookService.findById(id);
        return ResponseEntity.ok()
                .body(book);
    }

    @PostMapping(path = "")
    public ResponseEntity<BookDTO> create (@Valid @RequestBody BookCreateDTO bookData) {
        BookDTO book = bookService.create(bookData);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update (@PathVariable("id") Long id, @Valid @RequestBody BookUpdateDTO bookData) {
        BookDTO book = bookService.update(bookData,id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BookDTO> delete (@PathVariable("id") Long id) {
        bookService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    // END
}
