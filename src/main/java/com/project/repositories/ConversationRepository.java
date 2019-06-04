package com.project.repositories;

import com.project.entities.Conversation;
import com.project.entities.Product;
import com.project.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    Page<Conversation> findAll(Pageable pageable);

    @Query("SELECT c from Conversation c where c.user1 = ?1 or c.user2 = ?1")
    Page<Conversation> findAllByUser(Pageable pageable, User user);

    Optional<Conversation> findById(Long id);

    @Query("SELECT c from Conversation c where (c.user1 = ?1 or c.user2 = ?1) and c.product = ?2")
    Conversation findAllByUserAndProduct(User user, Product product);

    @Transactional
    @Modifying
    @Query("DELETE from Conversation where (user1.id= ?1 or user2.id =?1)")
    void deleteByUserId(Long id);

    @Query("select c from Conversation c where c.user1.id=?1 or c.user2.id =?1")
    List<Conversation> findAllByUserId(Long id);
}
