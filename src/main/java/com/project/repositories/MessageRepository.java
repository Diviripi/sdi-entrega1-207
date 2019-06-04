package com.project.repositories;

import com.project.entities.Conversation;
import com.project.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Page<Message> findAll(Pageable pageable);

    @Query("Select m from Message m where ?1 = m.conversation")
    List<Message> findAllByConversation(Conversation conv);

    @Transactional
    @Modifying
    @Query("delete from Message where user_id=?1")
    void deleteByUserId(Long id);

    @Transactional
    @Modifying
    @Query("delete from Message where conversation_id=?1")
    void deleteByConverId(long id);
}
