package com.project.services;

import com.project.entities.Conversation;
import com.project.entities.Product;
import com.project.entities.User;
import com.project.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ConversationService {

    @Autowired
    ConversationRepository conversationRepository;

    @PostConstruct
    public void inti() {
    }

    public void addConversation(Conversation conv) {
        conversationRepository.save(conv);
    }

    public void deleteConversation(Conversation conv) {
        conversationRepository.delete(conv);
    }

    public Conversation getConversation(Long id) {
        return conversationRepository.findById(id).get();
    }

    public Page<Conversation> getConversationByUser(Pageable pageable, User user) {
        return conversationRepository.findAllByUser(pageable, user);
    }

    public Conversation getConversationByUserAndProduct(User user, Product product) {
        return conversationRepository.findAllByUserAndProduct(user, product);
    }

    public void remove(Conversation conversation) {
        conversationRepository.delete(conversation);
    }
}
