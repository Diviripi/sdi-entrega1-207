package com.project.controllers;

import com.project.entities.Product;
import com.project.entities.User;
import com.project.services.ProductService;
import com.project.services.UserService;
import com.project.validators.AddProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Set;

@Controller
public class ProductController {
    @Autowired
    AddProductValidator addProductValidator;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/sales/addProduct", method = RequestMethod.GET)
    public String addProduct(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("product", new Product());
        return "/sales/addProduct";
    }


    @RequestMapping(value = "/sales/addProduct", method = RequestMethod.POST)
    public String addProduct(@Validated Product product, BindingResult result, Model model) {
        addProductValidator.validate(product, result);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        if (user.getMoney() < 20 && product.isHighlighted()) {
            return "redirect:/sales/addProduct/highlightError";
        }
        if (result.hasErrors()) {
            return "/sales/addProduct";
        }
        if (product.isHighlighted()) {
            userService.updateMoney(user, 20);
        }
        productService.addProduct(product);

        return "redirect:/sales/list";
    }

    @RequestMapping("/sales/addProduct/highlightError")
    public String showHighlightErrorAddProduct(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "sales/highlightError";
    }

    @RequestMapping(value = "/sales/list", method = RequestMethod.GET)
    public String seeProducts(Model model, Principal principal) {
        String email = principal.getName(); // email para la autentificacion

        User user = userService.getUserByEmail(email);


        Set<Product> products = productService.getProductsForUser(user);

        model.addAttribute("user", user);
        model.addAttribute("productList", products);


        return "/sales/list";
    }

    @RequestMapping("/sales/delete/{id}")
    public String deleteMark(@PathVariable Long id) {
        productService.deleteProduct(id);

        return "redirect:/sales/list";
    }

    @RequestMapping(value = "/sales/highlighted", method = RequestMethod.GET)
    public String seeHighlightedProducts(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName(); // email para la autentificacion
        User user = userService.getUserByEmail(email);
        Page<Product> products = productService.getHighlightedProductsForUser(pageable, user);
        model.addAttribute("user", user);
        model.addAttribute("productList", products.getContent());
        model.addAttribute("page", products);
        return "/sales/highlighted";
    }

    @RequestMapping(value = "/sales/highlight/{id}", method = RequestMethod.GET)
    public String highlightProduct(Model model, Pageable pageable, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        Product product = productService.getById(id);
        if (user.getMoney() < 20) {
            return "redirect:/sales/addProduct/highlightError";
        }
        userService.updateMoney(user, 20);
        productService.highlightProduct(product);
        return "redirect:/sales/highlighted";
    }
}
