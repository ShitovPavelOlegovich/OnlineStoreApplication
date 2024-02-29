package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.*;
import com.example.TopExamSpring.repositories.FavoriteRealEstateRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.repositories.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final PeopleRepository peopleRepository;
    private final FavoriteRealEstateRepository favoriteRealEstateRepository;
    public List<RealEstate> getAllRealEstateProducts() {
        log.info("Вывод всех продуктов: ");
        return realEstateRepository.findAll();
    }

    public List<RealEstate> searchTitleTypeOfHousing(String typeOfHousing) {
        log.info("Поиск продукта по марке авто: ");
        return realEstateRepository.findByTypeOfHousingContainingIgnoreCase(typeOfHousing);
    }

//    public List<RealEstate> searchTitleModelAuto(String titleModel) {
//        log.info("Поиск продукта по модели авто: ");
//        return autoRepository.findByModelContainingIgnoreCase(titleModel);
//    }

    public RealEstate getOneRealEstateProduct(Long id) {
        log.info("Поиск продуктов по id:");
        Optional<RealEstate> realEstate = realEstateRepository.findById(id);
        return realEstate.orElse(null);
    }

    @Transactional
    public void createRealEstateProduct(RealEstate realEstate, Principal principal, MultipartFile file1,
                                  MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Создание продукта: ");
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            realEstate.addImageToRealEstateProduct(image1);
        }
        if(file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            realEstate.addImageToRealEstateProduct(image2);
        }
        if(file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            realEstate.addImageToRealEstateProduct(image3);
        }

        realEstate.setOwnerPersonRealEstate(getUserByPrincipal(principal));
//        autoProduct.setCategories(Collections.singleton(Category.PRODUCT_AUTO));
        realEstate.setActiveRealEstate(true);
        RealEstate realEstate1 = realEstateRepository.save(realEstate);
        realEstate1.setPreviewImageId(realEstate1.getImages().get(0).getId());
        realEstateRepository.save(realEstate);
    }

    @Transactional
    public void updateRealEstateProduct(Long id, RealEstate realEstate, Principal principal, MultipartFile file1,
                                  MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Редактирование продукта под id: " + id);
        realEstate.setId(id);
        realEstateRepository.deleteImageById(realEstate.getId());
        Image newImage1;
        Image newImage2;
        Image newImage3;
        if(file1.getSize() != 0) {
            newImage1 = toImageEntity(file1);
            newImage1.setPreviewImage(true);
            realEstate.addImageToRealEstateProduct(newImage1);
        }
        if(file2.getSize() != 0) {
            newImage2 = toImageEntity(file2);
            realEstate.addImageToRealEstateProduct(newImage2);
        }
        if(file3.getSize() != 0) {
            newImage3 = toImageEntity(file3);
            realEstate.addImageToRealEstateProduct(newImage3);
        }
        realEstate.setOwnerPersonRealEstate(getUserByPrincipal(principal));
        realEstate.setDateOfCreated(LocalDateTime.now());
        realEstate.setActiveRealEstate(true);
//        autoProduct.setCategories(Collections.singleton(Category.PRODUCT_AUTO));
        RealEstate realEstate1 = realEstateRepository.save(realEstate);
        realEstate1.setPreviewImageId(realEstate1.getImages().get(0).getId());
        realEstateRepository.save(realEstate1);
    }

    @Transactional
    public void deleteRealEstateProduct(Long id) {
        log.info("Удаление продукта под id: " + id);
        realEstateRepository.deleteById(id);
    }

    public Person getUserByPrincipal(Principal principal) {
        return peopleRepository.findByUsername(principal.getName()).orElse(null);
    }


    public Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setData(file.getBytes());
        return image;
    }


    @Transactional
    public void  banRealEstateProduct(Long id) {
        RealEstate realEstate = realEstateRepository.findById(id).orElse(null);
        if (realEstate != null) {
            realEstate.setActiveRealEstate(false);
            log.info("Заблокировать продукт");
            realEstateRepository.save(realEstate);
        }
    }
    @Transactional
    public void  unBanRealEstateProduct(Long id) {
        RealEstate realEstate = realEstateRepository.findById(id).orElse(null);
        if (realEstate != null) {
            realEstate.setActiveRealEstate(true);
            log.info("Разблокировать продукт");
            realEstateRepository.save(realEstate);
        }
    }

    public List<RealEstate> realEstateProductIsActive(){
        List<RealEstate> realEstates = realEstateRepository.findAll();
        List<RealEstate> inactiveRealEstateProducts = new ArrayList<>();
        for (RealEstate realEstate : realEstates) {
            if (!realEstate.isActiveRealEstate()) {
                inactiveRealEstateProducts.add(realEstate);
            }
        }
        return inactiveRealEstateProducts;

    }

    @Transactional
    public void addToFavorites(Long realEstateProductId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        RealEstate realEstate = realEstateRepository.findById(realEstateProductId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        boolean isProductAlreadyInFavorites = favoriteRealEstateRepository.existsByPersonAndRealEstate(person, realEstate);

        if (isProductAlreadyInFavorites) {
            favoriteRealEstateRepository.deleteByPersonAndRealEstate(person, realEstate);
        }
        FavoriteRealEstateProduct favoriteRealEstateProduct = new FavoriteRealEstateProduct();
        favoriteRealEstateProduct.setPerson(person);
        favoriteRealEstateProduct.setRealEstate(realEstate);

        favoriteRealEstateRepository.save(favoriteRealEstateProduct);
    }

    @Transactional
    public void removeFromFavorites(Long realEstateProductId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        RealEstate realEstate = realEstateRepository.findById(realEstateProductId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        FavoriteRealEstateProduct favoriteRealEstateProduct = favoriteRealEstateRepository.findByPersonAndRealEstate(person, realEstate);
        if (favoriteRealEstateProduct != null) {
            favoriteRealEstateRepository.delete(favoriteRealEstateProduct);
        }
    }

    public List<RealEstate> getFavoriteRealEstateProducts(int idPerson) {
        Optional<Person> person = peopleRepository.findById(idPerson);
        List<FavoriteRealEstateProduct> favoriteRealEstateProducts = favoriteRealEstateRepository.findByPerson(person);
        List<RealEstate> realEstates = new ArrayList<>();
        for (FavoriteRealEstateProduct favoriteRealEstateProduct : favoriteRealEstateProducts) {
            realEstates.add(favoriteRealEstateProduct.getRealEstate());
        }
        return realEstates;
    }
}

