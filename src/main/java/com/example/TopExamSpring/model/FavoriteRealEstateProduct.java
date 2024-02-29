package com.example.TopExamSpring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "favorite_real_estate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRealEstateProduct {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;
}
