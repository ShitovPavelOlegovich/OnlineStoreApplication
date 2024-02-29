package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.FavoriteProduct;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);

    Optional<Person> findByUsername(String username);


}
