package com.example.bookapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookapi.book.Book;
import com.example.bookapi.book.BookRequest;
import com.example.bookapi.exception.ResourceNotFoundException;
import com.example.bookapi.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    public Book create(BookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getPublicationYear());

        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequest request) {
        Book book = getBook(id);

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublicationYear(request.getPublicationYear());

        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = getBook(id);
        bookRepository.delete(book);
    }
    
    @Transactional
    public void testRollback() {

        Book book1 = new Book(
                "Rollback Book 1",
                "Test Author",
                2024
        );

        bookRepository.save(book1);

        Book book2 = new Book(
                "Rollback Book 2",
                "Test Author",
                2025
        );

        bookRepository.save(book2);

        // Deliberately creating problem here
        throw new RuntimeException("Something went wrong!");
    }
}
