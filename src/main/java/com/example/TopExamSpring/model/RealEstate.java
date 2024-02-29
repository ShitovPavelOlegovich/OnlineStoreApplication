package com.example.TopExamSpring.model;

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

@Entity
@Table(name = "real_estate_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 0, message = "Количество комнат не должно быть меньше 0")
    @Column(name = "count_of_rooms")
    private int countOfRooms;

    @Min(value = 0, message = "Цена не должна быть меньше 0")
    @Column(name = "price")
    private int price;

    @Min(value = 0, message = "Общая площадь не должна быть меньше 0")
    @Column(name = "total_area")
    private int totalArea;

    @Min(value = 0, message = "Этажей не должно быть меньше 0")
    @Column(name = "floor")
    private int floor;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "type_of_housing", length = 100)
    private String typeOfHousing;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "sellers", length = 100)
    private String sellers;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "repair", length = 100)
    private String repair;


    @Min(value = 0, message = "Площадь кухни не должна быть меньше 0")
    @Column(name = "kitchen_of_area")
    private double kitchenOfArea;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "balcony_or_loggia", length = 100)
    private String balconyOrLoggia;

    @Min(value = 0, message = "Высота потолков не должна быть меньше 0")
    @Column(name = "height_of_ceiling")
    private int heightOfCeiling;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "bathroom", length = 100)
    private String bathroom;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "window_house", length = 100)
    private String windowHouse;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "year_the_built", length = 100)
    private String yearTheBuilt;

    @Min(value = 0, message = "Высота потолков не должна быть меньше 0")
    @Column(name = "floor_in_the_house")
    private int floorInTheHouse;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "type_house", length = 100)
    private String typeHouse;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "elevator", length = 100)
    private String elevator;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "parking", length = 100)
    private String parking;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "description", length = 100)
    private String description;

    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 100 символов")
    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @Column(name = "active_real_estate")
    private boolean activeRealEstate;

    private int previewImageId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person ownerPersonRealEstate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "realEstate")
    private List<Image> images = new ArrayList<>();

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
    public void addImageToRealEstateProduct(Image image) {
        image.setRealEstate(this);
        images.add(image);
    }
}
