package com.project.controllers;


import com.project.entities.User;
import com.project.services.RolesService;
import com.project.services.SecurityService;
import com.project.services.UserService;
import com.project.validators.SignUpFormValidator;
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

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, BindingResult result, Model model) {
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        }

        user.setRole(rolesService.getRoles()[0]);
        userService.addUser(user);
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());

        return "redirect:/buy/list";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/users/list")
    public String getListado(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("usersList", userService.getUsers());
        return "users/list";
    }

    @RequestMapping(value = "/users/deleteMultiple", method = RequestMethod.POST)
    public String getListado(HttpServletRequest request, Model model) {
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
