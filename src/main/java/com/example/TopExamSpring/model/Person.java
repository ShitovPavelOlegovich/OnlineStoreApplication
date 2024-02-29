package com.example.TopExamSpring.model;

import com.example.TopExamSpring.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле с username не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Поле с именем не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Поле с email не должно быть пустым")
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty(message = "Поле с паролем не должно быть пустым")
    @Column(name = "password", length = 1000)
    private String password;

    @NotEmpty(message = "Поле с номером телефона не должно быть пустым и содержать 11 цифр, начиная +7...")
    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(name = "active")
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_role",
    joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerPerson")
    private List<Product> products;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerPersonAutoProduct")
    private List<AutoProduct> autoProducts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerPersonRealEstate")
    private List<RealEstate> realEstates;

    @OneToMany(mappedBy = "person")
    private List<FavoriteProduct> favoriteProduct = new ArrayList<>();

    @OneToMany(mappedBy = "person")
    private List<FavoriteAutoProduct> favoriteAutoProducts = new ArrayList<>();

    @OneToMany(mappedBy = "person")
    private List<FavoriteRealEstateProduct> favoriteRealEstateProducts = new ArrayList<>();

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }



}
