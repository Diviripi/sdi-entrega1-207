package com.project.validators;

import com.project.entities.User;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class SignUpFormValidator implements Validator {


    @Autowired
    private UserService usersService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
      //  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");


       /* if (usersService.getUserByDni(user.getDni()) != null) {
            errors.rejectValue("dni", "Error.signup.dni.duplicate");
        }*/

       if(usersService.getUserByEmail(user.getEmail())!=null){
           errors.rejectValue("email","Error.signup.email.repeated");
       }
       if(user.getEmail().length()<6 || user.getEmail().length()>24){
           errors.rejectValue("email","Error.signup.email.length");
       }
        if (user.getName().length() < 5 || user.getName().length() > 24) {
            errors.rejectValue("name", "Error.signup.name.length");
        }
        if (user.getLastName().length() < 5 || user.getLastName().length() > 24) {
            errors.rejectValue("lastName", "Error.signup.lastName.length");
        }
        if (user.getPassword().length() < 5 || user.getPassword().length() > 24) {
            errors.rejectValue("password", "Error.signup.password.length");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Error.signup.passwordConfirm.coincidence");
        }
    }
}
