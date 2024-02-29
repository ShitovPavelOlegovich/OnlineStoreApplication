package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.FavoriteProduct;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Integer> {
    FavoriteProduct findByPersonAndProduct(Person person, Product product);

    List<FavoriteProduct> findByPerson(Optional<Person> person);

    boolean existsByPersonAndProduct(Person person, Product product);

    void deleteByPersonAndProduct(Person person, Product product);

}
