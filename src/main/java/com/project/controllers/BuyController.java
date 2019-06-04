package com.project.controllers;

import com.project.entities.Product;
import com.project.entities.User;
import com.project.services.ProductService;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.LinkedList;

@Controller
public class BuyController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;


    @RequestMapping("/buy/list")
    public String getList(Model model, Pageable pageable, Principal principal,
                          @RequestParam(value = "", required = false) String searchText) {
        Page<Product> products = new PageImpl<Product>(new LinkedList<Product>());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        if (searchText != null && !searchText.isEmpty()) {
            products = productService.searchProductsByTitleNoUser(pageable, searchText, user);
        } else {
            products = productService.getAllProductsPageableNoUser(pageable, user);
        }

        model.addAttribute("user", user);

        model.addAttribute("productList", products.getContent());
        model.addAttribute("page", products);
        return "/buy/list";
    }

    @RequestMapping("/bought/list")
    public String getBoughtList(Model model, Pageable pageable, Principal principal,
                                @RequestParam(value = "", required = false) String searchText) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        Page<Product> products = productService.getBoughtProducts(pageable, user);

        model.addAttribute("user", user);
        model.addAttribute("productList", products.getContent());
        model.addAttribute("page", products);
        return "/bought/list";
    }

    @RequestMapping("/buy/buyProduct/{id}")
    public String buyProduct(@PathVariable Long id) {
        //TODO make this like it should, it should decrease he counter and be only to buy if the user counter
        //have enough money
        Product product = productService.getById(id);
        double price = product.getPrice();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        double userMoney = user.getMoney();

        if (userMoney >= price) {
            productService.buyProduct(product.getId(),user);
            userService.updateMoney(user, price);
            return "redirect:/buy/list";
        } else {
            return "redirect:/buy/product/notEnoughMoney/{id}";
        }
    }

    @RequestMapping("buy/product/notEnoughMoney/{id}")
    public String showError(Model model, @PathVariable Long id) {
        Product product = productService.getById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        return "buy/error";

    }
}
