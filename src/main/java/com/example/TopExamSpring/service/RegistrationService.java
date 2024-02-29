package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.enums.Role;
import com.example.TopExamSpring.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registration(Person person) {
        log.info("Регистрация пользователя");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActive(true);
        person.setRoles(Collections.singleton(Role.ROLE_USER));
        peopleRepository.save(person);
    }
}
