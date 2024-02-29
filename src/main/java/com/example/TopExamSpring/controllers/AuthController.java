package com.example.TopExamSpring.controllers;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.service.PersonService;
import com.example.TopExamSpring.service.RegistrationService;
import com.example.TopExamSpring.util.PersonEmailValidator;
import com.example.TopExamSpring.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PersonValidator personValidator;
    private final PersonEmailValidator personEmailValidator;
    private final RegistrationService registrationService;
    private final PersonService personService;


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "username", defaultValue = "false") String username) {
        if(!personService.userIsActive(username)) {
            return "auth/login";
        }
        return "redirect:/auth/ban";
    }

    @GetMapping("/ban")
    public String banPage() {
        return "ban_person/ban";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person")Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registrationPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person,bindingResult);
        personEmailValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.registration(person);
        return "redirect:/auth/login";
    }
}
