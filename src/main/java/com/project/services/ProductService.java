package com.project.services;

import com.project.entities.Product;
import com.project.entities.User;
import com.project.repositories.ProductRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public void addProduct(Product product) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        user.addProduct(product);
        product.setUser(user);
        product.setDate(new Date());
        productRepository.save(product);

    }

    public Set<Product> getProductsForUser(User user) {
        Set<Product> products = new HashSet<Product>();
        products = productRepository.findAllByUser(user);

        return products;
    }

    public Set<Product> getAllProducts() {
        Set<Product> products = new HashSet<Product>();
        productRepository.findAll().forEach(p -> products.add(p));
        return products;
    }

    public void deleteProduct(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(id).get();
        if (product.getUser().equals(user)) {
            productRepository.delete(product);
        }

    }

    public Page<Product> searchProductsByTitle(Pageable pageable, String searchText) {

        Page<Product> products = new PageImpl<Product>(new LinkedList<Product>());
        searchText = "%" + searchText + "%";

        products = productRepository.searchByDescriptionAndName(pageable, searchText);

        return products;
    }

    public Page<Product> getAllProductsPageable(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products;
    }

    public void buyProduct(Long id, User user) {
        Product product = productRepository.findById(id).get();
        product.setSold(true);
        product.setBuyer(user);
        productRepository.save(product);
    }

    public Product getById(long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getHighlightedProductsForUser(Pageable pageable, User user) {
        return productRepository.findAllHighlightedByUser(pageable, user);
    }

    public void highlightProduct(Product product) {
        product.setHighlighted(true);
        productRepository.save(product);
    }

    public Page<Product> searchProductsByTitleNoUser(Pageable pageable, String searchText, User user) {
        return productRepository.findAllByTextAndNoUser(pageable, searchText, user.getId());
    }

    public Page<Product> getAllProductsPageableNoUser(Pageable pageable, User user) {
        return productRepository.findAllNoUser(pageable, user.getId());
    }

    public Page<Product> getBoughtProducts(Pageable pageable, User user) {
        return productRepository.findAllBoughtByUser(pageable, user);
    }
}
