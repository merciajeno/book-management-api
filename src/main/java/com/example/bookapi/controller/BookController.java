package com.example.bookapi.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.bookapi.book.Book;
import com.example.bookapi.book.BookRequest;
import com.example.bookapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getBooks(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody BookRequest request) {
        return bookService.create(request);
    }

    @PutMapping("/{id}")
    public Book update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
    
    @PostMapping("/test-rollback")
    public String testRollback() {

        bookService.testRollback();

        return "Transaction completed";
    }
}
