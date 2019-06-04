package com.project.repositories;

import com.project.entities.Product;
import com.project.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface

ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.user = ?1 ORDER BY p.id ASC ")
    Set<Product> findAllByUser(User user);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(?1) )")
    Page<Product> searchByDescriptionAndName(Pageable pageable, String searchText);

    @Query("SELECT p FROM Product p WHERE p.buyer = ?1 ORDER BY p.id ASC ")
    Page<Product> findAllBoughtByUser(Pageable pageable, User user);

    Page<Product> findAll(Pageable pageable);

    Product findById(long id);

    @Query("SELECT p FROM Product p WHERE p.user = ?1 and p.highlighted=true")
    Page<Product> findAllHighlightedByUser(Pageable pageable, User user);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(?1)) and p.user.id <> ?2")
    Page<Product> findAllByTextAndNoUser(Pageable pageable, String searchText, Long id);

    @Query("SELECT p FROM Product p WHERE p.user.id <>?1")
    Page<Product> findAllNoUser(Pageable pageable, Long id);
}