package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.AutoProduct;
import com.example.TopExamSpring.model.FavoriteAutoProduct;
import com.example.TopExamSpring.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteAutoProductRepository extends JpaRepository<FavoriteAutoProduct, Integer> {
    boolean existsByPersonAndAutoProduct(Person person, AutoProduct autoProduct);
    void deleteByPersonAndAutoProduct(Person person, AutoProduct autoProduct);
    FavoriteAutoProduct findByPersonAndAutoProduct(Person person, AutoProduct autoProduct);
    List<FavoriteAutoProduct> findByPerson(Optional<Person> person);

}
