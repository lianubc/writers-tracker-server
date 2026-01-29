package com.writerstracker.server.repository;

import com.writerstracker.server.model.Book;
import com.writerstracker.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // This magic method finds all books belonging to a specific user
    List<Book> findAllByUser(User user);
}
