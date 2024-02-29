package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.AutoProduct;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {
    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findProductByOwnerPerson(Person owner);

    List<Product> findProductByOwnerPerson(Optional<Person> person);

    void deleteImageById(int id);
}
