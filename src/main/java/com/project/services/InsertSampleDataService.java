package com.project.services;

import com.project.entities.Conversation;
import com.project.entities.Message;
import com.project.entities.Product;
import com.project.entities.User;
import com.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class InsertSampleDataService {
    @Autowired
    private UserService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private ConversationService converService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init() {
        User admin = new User("admin@email.com", "admin", "admin");
        admin.setPassword("admin");
        admin.setRole(rolesService.getRoles()[1]);
        usersService.addUser(admin);

        User user1 = new User("javi@email.com", "Javi", "Garcia");
        user1.setPassword("123456");
        user1.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user1);

        User user2 = new User("ivan@email.com", "Ivan", "Prieto");
        user2.setPassword("123456");
        user2.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user2);

        User user3 = new User("user3@email.com", "User3", "3");
        user3.setPassword("123456");
        user3.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user3);

        User user4 = new User("user4@email.com", "User4", "4");
        user4.setPassword("123456");
        user4.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user4);


        //Usuario 1

        Product gafasSol = new Product("Gafas de sol", "Buenas gafas con proteccion", 14, user1, user2);
        Set<Product> javiProducts = new HashSet<Product>() {
            {
                add(gafasSol);
                add(new Product("Sofa", "Sofa para sentarse", 100, user1));
                add(new Product("Boligrafo", "Para escribir", 1, user1));
            }
        };
       // user1.setProducts(javiProducts);
        javiProducts.forEach(p->productRepository.save(p));
       // usersService.addUser(user1);


        //Usuario 2
        Product corsa= new Product("Opel corsa", "Coche del año 2005", 1400, user2, user1);
        Set<Product> ivanProducts = new HashSet<Product>() {
            {
                add(corsa);
                add(new Product("Cuadro", "Cuadro de Pikaso", 150, user2, user1));
                add(new Product("Mechero", "Para quemar cosas", 1, user2));
            }
        };
        //user2.setProducts(ivanProducts);
        //usersService.addUser(user2);
        ivanProducts.forEach(p->productRepository.save(p));


        //Usuario 3

        Product product3=new Product("Producto 3", "Producto 3", 1, user3, user2);
        Set<Product> user3Products = new HashSet<Product>() {
            {
                add(new Product("Producto 1", "Producto 1", 1400, user3, user4));
                add(new Product("Producto 2", "Producto 2", 150, user3, user4));
                add(product3);
            }
        };
        //user3.setProducts(user3Products);
        //usersService.addUser(user3);
        user3Products.forEach(p->productRepository.save(p));

        //Usuario 4

        Product product5=new Product("Producto 5", "Producto 5", 150, user4, user3);
        Set<Product> user4Products = new HashSet<Product>() {
            {
                add(new Product("Producto 4", "Producto 4", 1400, user4, user3));
                add(product5);
                add(new Product("Producto 6", "Producto 6", 1, user4, user2));
            }
        };
        //user4.setProducts(user4Products);
        //usersService.addUser(user4);
        user4Products.forEach(p->productRepository.save(p));


        //********************************MENSAJES**********************************
        //CONVERSATION 1
        createConversation(user2,user1,gafasSol);
        //CONVERSATION 2
        createConversation(user1, user2, corsa);
        //CONVERSATION 3
        createConversation(user3,user4,product3);
        //CONVERSATION 4
        createConversation(user4,user3,product5);


    }

    private void createConversation(User user1, User user2, Product corsa) {
        productRepository.save(corsa);
        Conversation conversacion2= new Conversation( user2,user1,corsa);
        Set<Message> mensajes2= new HashSet<Message>(){
            {
                add( new Message( "Mensaje1",user1,conversacion2));
                add( new Message( "Mensaje2",user2,conversacion2));
                add( new Message( "Mensaje3",user1,conversacion2));
                add( new Message( "Mensaje4",user2,conversacion2));
            }
        };
        converService.addConversation(conversacion2);
        mensajes2.forEach(m->messageService.addMessage(m));
    }
}