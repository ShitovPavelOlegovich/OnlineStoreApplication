package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Проверка наличия входящего логина");
        Optional<Person> person = peopleRepository.findByUsername(username);
        if(person.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
            return new PersonDetails(person.get());

    }


}
