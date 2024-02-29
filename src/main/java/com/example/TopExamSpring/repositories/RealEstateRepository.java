package com.example.TopExamSpring.repositories;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    List<RealEstate> findByTypeOfHousingContainingIgnoreCase(String typeOfHousing);

    List<RealEstate> findRealEstateByOwnerPersonRealEstate(Person owner);
    void deleteImageById(Long id);


}
