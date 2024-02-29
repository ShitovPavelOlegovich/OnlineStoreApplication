package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.FavoriteRealEstateProduct;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRealEstateRepository extends JpaRepository<FavoriteRealEstateProduct, Long> {
    void deleteByPersonAndRealEstate(Person person, RealEstate realEstate);

    boolean existsByPersonAndRealEstate(Person person, RealEstate realEstate);
    FavoriteRealEstateProduct findByPersonAndRealEstate(Person person, RealEstate realEstate);

    List<FavoriteRealEstateProduct> findByPerson(Optional<Person> person);

}
