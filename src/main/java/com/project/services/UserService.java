package com.project.services;

import com.project.entities.Conversation;
import com.project.entities.User;
import com.project.repositories.ConversationRepository;
import com.project.repositories.MessageRepository;
import com.project.repositories.ProductRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private ConversationRepository converRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MessageRepository messageRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).get();
    }


    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setMoney(100);
        usersRepository.save(user);
    }

    public void deleteUser(Long id) {
        //TODO Delete first everything related to it
        //mensajes del usuario

        messageRepository.deleteByUserId(id);
        //mensajes en sus conversaciones
        List<Conversation> conversDelUsuario=converRepository.findAllByUserId(id);
        conversDelUsuario.forEach(c->{
            messageRepository.deleteByConverId(c.getId());
        });

        //Convers con productos de otros
        //Convers con sus productos

        converRepository.deleteByUserId(id);


        //Sus productos
        //Con el borrado en cascada deberia borrase
        //El usuario
        usersRepository.deleteById(id);

    }

    public void deleteUsers(String[] ids) {
        for (String id : ids) {
            this.deleteUser(Long.parseLong(id));
        }
    }

    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public void updateMoney(User user, double price) {
        user.setMoney(user.getMoney() - price);
        usersRepository.save(user);
    }
}
