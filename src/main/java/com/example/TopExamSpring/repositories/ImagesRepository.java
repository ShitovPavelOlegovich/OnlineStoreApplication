package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Integer> {
    List<Image> findImagesByProduct(Product product);
}
