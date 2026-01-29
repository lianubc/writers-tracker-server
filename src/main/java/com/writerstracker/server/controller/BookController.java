package com.writerstracker.server.controller;

import com.writerstracker.server.model.Book;
import com.writerstracker.server.model.User;
import com.writerstracker.server.repository.BookRepository;
import com.writerstracker.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Import added
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Book> getMyBooks(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return bookRepository.findAllByUser(user);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).get();

        // Security: Force the book to belong to the logged-in user
        book.setUser(user);

        return bookRepository.save(book);
    } // delete

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. Get the current logged-in user
        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email).get();

        // 2. Find the book by ID
        Book book = bookRepository.findById(id).orElse(null);

        // 3. Handle "Book not found"
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        // 4. CRITICAL SECURITY CHECK: Does this book belong to the logged-in user?
        // This satisfies the worksheet requirement: "User B cannot delete User A's data"
        if (!book.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this book");
        }

        // 5. Safe to delete
        bookRepository.delete(book);
        return ResponseEntity.ok("Book deleted successfully");
    }
}