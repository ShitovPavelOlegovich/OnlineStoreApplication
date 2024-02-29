package com.example.TopExamSpring.util;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class PersonEmailValidator implements Validator {

    private final PeopleRepository peopleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (peopleRepository.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Пользователь с данным email уже существует!");
        }

    }
}
