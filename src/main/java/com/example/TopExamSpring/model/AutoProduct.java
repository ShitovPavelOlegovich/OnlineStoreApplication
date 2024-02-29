package com.example.TopExamSpring.model;

import com.example.TopExamSpring.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auto_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoProduct {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "type_of_car", length = 100)
    private String typeOfCar;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "fabricator", length = 100)
    private String fabricator;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "mark", length = 100)
    private String mark;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "model", length = 100)
    private String model;


    @Min(value = 0, message = "Цена должна быть меньше 0")
    @Column(name = "price")
    private int price;


    @Column(name = "year_of_issue")
    private String yearOfIssue;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "mileage", length = 100)
    private String mileage;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "transmission", length = 100)
    private String transmission;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "drive_unit", length = 100)
    private String driveUnit;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "engine_type", length = 100)
    private String engineType;


    @Min(value = 0, message = "Поле не должно быть меньше 0")
    @Column(name = "power")
    private double power;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "fuel_consumption", length = 100)
    private String fuelConsumption;


    @Min(value = 0, message = "Владельцев не должно быть меньше 0")
    @Column(name = "owners_of_pts")
    private int ownersOfPTS;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "state", length = 100)
    private String state;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "type_body", length = 100)
    private String typeBody;


    @Min(value = 0, message = "Количество мест не должно быть меньше 0")
    @Column(name = "count_of_seats")
    private int countOfSeats;


    @Min(value = 0, message = "Обьем багажника не должен быть меньше 0")
    @Column(name = "trunk_volume")
    private int trunkVolume;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "steering_wheel", length = 100)
    private String steeringWheel;


    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 300, message = "Описание авто должно быть от 2 до 300 символов")
    @Column(name = "description_auto", length = 300)
    private String descriptionAuto;

    @NotEmpty(message = "Поле с названием не должно быть пустым")
    @Column(name = "city")
    private String city;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person ownerPersonAutoProduct;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @ElementCollection(targetClass = Category.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "categories_product",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "autoProduct")
    private List<Image> images = new ArrayList<>();

    @Column(name = "active_auto_product")
    private boolean activeAutoProduct;

    private int previewImageId;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
    public void addImageToProduct(Image image) {
        image.setAutoProduct(this);
        images.add(image);
    }
}
