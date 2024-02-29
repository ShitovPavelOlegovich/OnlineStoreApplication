package com.example.TopExamSpring.model;

import com.example.TopExamSpring.model.enums.Category;
import com.example.TopExamSpring.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty(message = "Поле с названием не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    @Column(name = "title", length = 100)
    private String title;

    @NotEmpty(message = "Поле с описанием продукта не должно быть пустым")
    @Size(min = 5 , max = 300, message = "Описание продукта должно содержать от 5 до 300 символов")
    @Column(name = "description", length = 300)
    private String description;


    @Min(value = 0, message = "Цена должна быть больше 0")
    @Column(name = "price")
    private int price;

    @NotEmpty(message = "Поле с названием не должно быть пустым")
    @Column(name = "city")
    private String city;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person ownerPerson;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
    private List<Image> images = new ArrayList<>();


    @ElementCollection(targetClass = Category.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "categories_product",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;

    @Column(name = "active_product")
    private boolean activeProduct;


    private int previewImageId;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
