package com.project.moneymanager.validator;


import com.project.moneymanager.models.User;
import com.project.moneymanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.project.moneymanager.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;
    private final UserRepository userRepository;

    public UserValidator (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean supports (Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate (Object target, Errors errors) {
        User user = (User) target;
        List <User> allusers = (List<User>) userRepository.findAll();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.user.username");
        }
//        if (userService.findByUsername(user.getUsername()) != null) {
//            errors.rejectValue("username", "Duplicate.user.username");
//        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (user.getEmail().length() < 8 || user.getEmail().length() > 50) {
            errors.rejectValue("email", "Size.user.email");
        }
        if (userService.findByUsername(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.user.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.user.password");
        }

        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
           errors.rejectValue("passwordConfirmation", "Diff.user.passwordConfirmation");
            errors.rejectValue("passwordConfirmation", "passwordConfirmation");
        }
        for (int i = 0; i < allusers.size(); i++) {
            String email = allusers.get(i).getEmail();
            if (email.equals(user.getEmail())) {
                errors.rejectValue("email", "EmailIsAlreadyThere");
            }

        }

    }
}
