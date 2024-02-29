package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.AutoProduct;
import com.example.TopExamSpring.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<AutoProduct, Long> {

    List<AutoProduct> findByMarkContainingIgnoreCase(String titleMark);

    List<AutoProduct> findByModelContainingIgnoreCase(String titleModel);

    List<AutoProduct> findAutoProductByOwnerPersonAutoProduct(Person owner);

    void deleteImageById(Long id);
}
