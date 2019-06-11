package com.project.controllers;


import com.project.entities.User;
import com.project.services.RolesService;
import com.project.services.SecurityService;
import com.project.services.UserService;
import com.project.validators.SignUpFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UserController {
    @Autowired
    SignUpFormValidator signUpFormValidator;

    @Autowired
    UserService userService;

    @Autowired
    RolesService rolesService;

    @Autowired
    SecurityService securityService;

    Logger logger= LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        logger.info("Sign up");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, BindingResult result, Model model) {
        logger.info("Signing up");
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            logger.info("Error Sign up");
            return "signup";
        }

        user.setRole(rolesService.getRoles()[0]);
        userService.addUser(user);
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
        logger.info("Logged");

        return "redirect:/buy/list";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {

        logger.info("access logging");
        return "login";
    }

    @RequestMapping("/users/list")
    public String getListado(Model model) {
        logger.info("See users");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("usersList", userService.getUsers());
        return "users/list";
    }

    @RequestMapping(value = "/users/deleteMultiple", method = RequestMethod.POST)
    public String getListado(HttpServletRequest request, Model model) {
        logger.info("Delete users");
        String[] ids = request.getParameterValues("id");
        if (ids != null) {
            userService.deleteUsers(ids);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "redirect:/users/list";
    }


}
