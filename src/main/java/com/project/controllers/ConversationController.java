package com.project.controllers;

import com.project.entities.Conversation;
import com.project.entities.Message;
import com.project.entities.Product;
import com.project.entities.User;
import com.project.services.ConversationService;
import com.project.services.MessageService;
import com.project.services.ProductService;
import com.project.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ConversationController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ConversationService conversationService;

    @Autowired
    MessageService messageService;

    Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @RequestMapping(value = "/product/newMessage/{id}", method = RequestMethod.GET)
    public String sendMessage(Model model, @PathVariable Long id) {
        logger.info("See message");
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        return "/product/newMessage";
    }

    @RequestMapping(value = "/product/newMessage/{id}", method = RequestMethod.POST)
    public String createMessage(Model model, @PathVariable Long id, @RequestParam("message") String message) {
        logger.info("Sending new message");
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((Authentication) auth).getName();
        User user = userService.getUserByEmail(email);
        Conversation conv = new Conversation(product.getUser(), user, product);
        Message mes = new Message(message, user, conv);
        conversationService.addConversation(conv);
        messageService.addMessage(mes);
        List<Message> messages = messageService.getMessageByConversation(conv);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("messageList", messages);
        return "redirect:/product/conversation/{id}";
    }

    @RequestMapping(value = "/product/sendMessage/{id}", method = RequestMethod.POST)
    public String sendMessage(Model model, @PathVariable Long id, @RequestParam("message") String message) {
        logger.info("Sending new message");
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((Authentication) auth).getName();
        User user = userService.getUserByEmail(email);
        Conversation conv = conversationService.getConversationByUserAndProduct(user, product);
        Message message1 = new Message(message, user, conv);
        messageService.addMessage(message1);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("messageList", messageService.getMessageByConversation(conv));
        return "/product/conversation";
    }

    @RequestMapping(value = "/product/conversation/{id}", method = RequestMethod.GET)
    public String listConversation(Model model, @PathVariable Long id) {
        logger.info("See conversation");
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((Authentication) auth).getName();
        User user = userService.getUserByEmail(email);
        Conversation conv = conversationService.getConversationByUserAndProduct(user, product);
        if (conv != null) {
            model.addAttribute("user", user);
            model.addAttribute("product", product);
            model.addAttribute("messageList", messageService.getMessageByConversation(conv));
            return "/product/conversation";
        } else {
            if (product.getUser().getEmail().equals(email)) {
                return "redirect:/conversations/list";
            } else {
                return "redirect:/product/newMessage/{id}";
            }
        }
    }

    @RequestMapping("conversations/list")
    public String listConversation(Model model, Pageable pageable) {
        logger.info("See conversations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((Authentication) auth).getName();
        User user = userService.getUserByEmail(email);
        Page<Conversation> conversations = conversationService.getConversationByUser(pageable, user);
        model.addAttribute("user", user);
        model.addAttribute("conversationList", conversations.getContent());
        model.addAttribute("page", conversations);
        return "conversation/list";
    }

    @RequestMapping("product/conversation/delete/{id}")
    public String deleteConversation(Model model, Pageable pageable, @PathVariable Long id) {
        logger.info("Delete conversation");
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((Authentication) auth).getName();
        User user = userService.getUserByEmail(email);
        Conversation conversation = conversationService.getConversationByUserAndProduct(user, product);
        List<Message> messages = messageService.getMessageByConversation(conversation);
        for (Message message : messages)
            messageService.remove(message);
        conversationService.remove(conversation);
        Page<Conversation> conversations = conversationService.getConversationByUser(pageable, user);
        model.addAttribute("user", user);
        model.addAttribute("conversationList", conversations.getContent());
        model.addAttribute("page", conversations);
        return "conversation/list";
    }

}
