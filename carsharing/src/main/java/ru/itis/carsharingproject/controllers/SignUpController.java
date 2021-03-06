package ru.itis.carsharingproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.carsharingproject.dto.UserForm;
import ru.itis.carsharingproject.models.User;
import ru.itis.carsharingproject.security.details.UserDetailsImpl;
import ru.itis.carsharingproject.services.SignUpService;
import ru.itis.carsharingproject.services.UsersService;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private UsersService usersService;

    @GetMapping("/sign_up")
    public String getSignUpPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registration";
    }

    @GetMapping("/confirm")
    public String getConfirmPage() {
        return "confirm_page";
    }

    @GetMapping("/activate")
    public String activateAccount() {
//        User user = usersService.findUserById(userDetails.getUser().getId()).orElseThrow(IllegalAccessError::new);
//        usersService.editUserStatus(user);
        return "redirect:/sign_in";
    }

    @PostMapping("/sign_up")
    public String signUp(@Valid UserForm userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().anyMatch(error -> {
                if (Objects.requireNonNull(error.getCodes())[0].equals("userForm.ValidNames")) {
                    model.addAttribute("namesErrorMessage", error.getDefaultMessage());
                }
                if (Objects.requireNonNull(error.getCodes())[0].equals("userForm.ValidMatchPassword")) {
                    model.addAttribute("passwordMatchErrorMessage", error.getDefaultMessage());
                }
                return true;
            });
            model.addAttribute("userForm", userForm);
            return "registration";
        }
        signUpService.signUp(userForm);
        return "redirect:/confirm";

    }

}
