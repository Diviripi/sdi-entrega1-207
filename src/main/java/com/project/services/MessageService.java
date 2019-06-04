package com.project.services;

import com.project.entities.Conversation;
import com.project.entities.Message;
import com.project.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @PostConstruct
    public void init() {
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessageByConversation(Conversation conv) {
        return messageRepository.findAllByConversation(conv);
    }

    public void remove(Message message) {
        messageRepository.delete(message);
    }

}
