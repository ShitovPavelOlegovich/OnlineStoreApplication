package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.AutoProduct;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import com.example.TopExamSpring.model.RealEstate;
import com.example.TopExamSpring.model.enums.Role;
import com.example.TopExamSpring.repositories.AutoRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.repositories.ProductsRepository;
import com.example.TopExamSpring.repositories.RealEstateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PersonService {

    private final PeopleRepository peopleRepository;
    private final ProductsRepository productsRepository;
    private final AutoRepository autoRepository;
    private final RealEstateRepository realEstateRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PeopleRepository peopleRepository, ProductsRepository productsRepository, AutoRepository autoRepository, RealEstateRepository realEstateRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.productsRepository = productsRepository;
        this.autoRepository = autoRepository;
        this.realEstateRepository = realEstateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAllPerson() {
        log.info("Вывод всех пользователей:");
        return peopleRepository.findAll();
    }

    public Person getPersonId(int id) {
        log.info("Вывод пользователей с конкретный id:" + id);
        return peopleRepository.getById(id);
    }

    @Transactional
    public void updatePeron(int id, Person person) {
        log.info("Редактирование пользователя с id:" + id);
        person.setId(id);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setDateOfCreated(LocalDateTime.now());
        person.setRoles(Collections.singleton(Role.ROLE_USER));
        person.setActive(true);
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        log.info("Удаление аккаунта: ");
        peopleRepository.deleteById(id);
    }

    public List<Product> getProductByPerson(int id) {
        log.info("Поиск продуктов: ");
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Person person1 = person.get();
            return productsRepository.findProductByOwnerPerson(person1);
        }
        return null;

    }
    public List<AutoProduct> getAutoProductByPerson(int id) {
        log.info("Поиск продуктов: ");
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Person person1 = person.get();
            return autoRepository.findAutoProductByOwnerPersonAutoProduct(person1);
        }
        return null;
    }

    public List<RealEstate> getRealEstateProductByPerson(int id) {
        log.info("Поиск продуктов: ");
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Person person1 = person.get();
            return realEstateRepository.findRealEstateByOwnerPersonRealEstate(person1);
        }
        return null;
    }



    @Transactional
    public void banPerson(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if(person != null) {
            person.setActive(false);
            log.info("Заблокировать пользователя");
            peopleRepository.save(person);
        }
    }
    @Transactional
    public void unbanPerson(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if(person != null) {
            person.setActive(true);
            log.info("Разблокировать пользователя");
            peopleRepository.save(person);
        }
    }

    @Transactional
    public void appointRoleAdmin(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if(person != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_ADMIN);
            person.setRoles(roles);
            log.info("Назначение ROLE_ADMIN пользователю");
            peopleRepository.save(person);
        }
    }
    @Transactional
    public void appointRoleUser(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if(person != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_USER);
            person.setRoles(roles);
            log.info("Назначение ROLE_USER пользователю");
            peopleRepository.save(person);
        }
    }


    public boolean userIsActive(String username) {
        Optional<Person> person = peopleRepository.findByUsername(username);
        if(person.isPresent()) {
            Person person1 = person.get();
            return !person1.isActive();
        }
        return false;
    }

    public boolean canUpdateProfile(Authentication authentication, int personId) {
        Person loggedInPerson = (Person) authentication.getPrincipal();
        return loggedInPerson.getId() == personId;
    }


}
