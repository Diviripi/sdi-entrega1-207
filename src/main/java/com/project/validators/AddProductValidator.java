package com.project.validators;

import com.project.entities.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class AddProductValidator  implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        ValidationUtils.rejectIfEmpty(errors, "title", "Error.empty");
        ValidationUtils.rejectIfEmpty(errors, "description", "Error.empty");
        ValidationUtils.rejectIfEmpty(errors, "price", "Error.empty");

        if(product.getPrice()<0){
            errors.rejectValue("price","Error.addProduct.price.negative");
        }



    }
}
