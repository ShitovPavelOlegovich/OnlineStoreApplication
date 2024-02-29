package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.*;
import com.example.TopExamSpring.model.enums.Category;
import com.example.TopExamSpring.repositories.AutoRepository;
import com.example.TopExamSpring.repositories.FavoriteAutoProductRepository;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AutoProductService {

    private final AutoRepository autoRepository;
    private final ImagesRepository imagesRepository;
    private final PeopleRepository peopleRepository;
    private final FavoriteAutoProductRepository favoriteAutoProductRepository;

    public List<AutoProduct> getAllAutoProducts() {
        log.info("Вывод всех продуктов: ");
        return autoRepository.findAll();
    }

    public List<AutoProduct> searchTitleMarkAuto(String titleMark) {
        log.info("Поиск продукта по марке авто: ");
        return autoRepository.findByMarkContainingIgnoreCase(titleMark);
    }

    public List<AutoProduct> searchTitleModelAuto(String titleModel) {
        log.info("Поиск продукта по модели авто: ");
        return autoRepository.findByModelContainingIgnoreCase(titleModel);
    }

    public AutoProduct getOneAutoProduct(Long id) {
        log.info("Поиск продуктов по id:");
        Optional<AutoProduct> autoProduct = autoRepository.findById(id);
        return autoProduct.orElse(null);
    }

    @Transactional
    public void createAutoProduct(AutoProduct autoProduct, Principal principal, MultipartFile file1,
                              MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Создание продукта: ");
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            autoProduct.addImageToProduct(image1);
        }
        if(file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            autoProduct.addImageToProduct(image2);
        }
        if(file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            autoProduct.addImageToProduct(image3);
        }

        autoProduct.setOwnerPersonAutoProduct(getUserByPrincipal(principal));
//        autoProduct.setCategories(Collections.singleton(Category.PRODUCT_AUTO));
        autoProduct.setActiveAutoProduct(true);
        AutoProduct autoProduct1 = autoRepository.save(autoProduct);
        autoProduct1.setPreviewImageId(autoProduct1.getImages().get(0).getId());
        autoRepository.save(autoProduct);
    }

    @Transactional
    public void updateAutoProduct(Long id, AutoProduct autoProduct, Principal principal, MultipartFile file1,
                              MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Редактирование продукта под id: " + id);
        autoProduct.setId(id);
        autoRepository.deleteImageById(autoProduct.getId());
        Image newImage1;
        Image newImage2;
        Image newImage3;
        if(file1.getSize() != 0) {
            newImage1 = toImageEntity(file1);
            newImage1.setPreviewImage(true);
            autoProduct.addImageToProduct(newImage1);
        }
        if(file2.getSize() != 0) {
            newImage2 = toImageEntity(file2);
            autoProduct.addImageToProduct(newImage2);
        }
        if(file3.getSize() != 0) {
            newImage3 = toImageEntity(file3);
            autoProduct.addImageToProduct(newImage3);
        }
        autoProduct.setOwnerPersonAutoProduct(getUserByPrincipal(principal));
        autoProduct.setDateOfCreated(LocalDateTime.now());
        autoProduct.setActiveAutoProduct(true);
//        autoProduct.setCategories(Collections.singleton(Category.PRODUCT_AUTO));
        AutoProduct autoProduct1 = autoRepository.save(autoProduct);
        autoProduct1.setPreviewImageId(autoProduct1.getImages().get(0).getId());
        autoRepository.save(autoProduct1);
    }

    @Transactional
    public void deleteAutoProduct(Long id) {
        log.info("Удаление продукта под id: " + id);
        autoRepository.deleteById(id);
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
    public void  banAutoProduct(Long id) {
        AutoProduct autoProduct = autoRepository.findById(id).orElse(null);
        if (autoProduct != null) {
            autoProduct.setActiveAutoProduct(false);
            log.info("Заблокировать продукт");
            autoRepository.save(autoProduct);
        }
    }
    @Transactional
    public void  unBanAutoProduct(Long id) {
        AutoProduct autoProduct = autoRepository.findById(id).orElse(null);
        if (autoProduct != null) {
            autoProduct.setActiveAutoProduct(true);
            log.info("Разблокировать продукт");
            autoRepository.save(autoProduct);
        }
    }

    public List<AutoProduct> autoProductIsActive(){
        List<AutoProduct> autoProducts = autoRepository.findAll();
        List<AutoProduct> inactiveAutoProducts = new ArrayList<>();
        for (AutoProduct autoProduct : autoProducts) {
            if (!autoProduct.isActiveAutoProduct()) {
                inactiveAutoProducts.add(autoProduct);
            }
        }
        return inactiveAutoProducts;

    }

    @Transactional
    public void addToFavorites(Long autoProductId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        AutoProduct autoProduct = autoRepository.findById(autoProductId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        boolean isProductAlreadyInFavorites = favoriteAutoProductRepository.existsByPersonAndAutoProduct(person, autoProduct);

        if (isProductAlreadyInFavorites) {
            favoriteAutoProductRepository.deleteByPersonAndAutoProduct(person, autoProduct);
        }
        FavoriteAutoProduct favoriteAutoProduct = new FavoriteAutoProduct();
        favoriteAutoProduct.setPerson(person);
        favoriteAutoProduct.setAutoProduct(autoProduct);

        favoriteAutoProductRepository.save(favoriteAutoProduct);
    }

    @Transactional
    public void removeFromFavorites(Long autoProductId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        AutoProduct autoProduct = autoRepository.findById(autoProductId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        FavoriteAutoProduct favoriteAutoProduct = favoriteAutoProductRepository.findByPersonAndAutoProduct(person, autoProduct);
        if (favoriteAutoProduct != null) {
            favoriteAutoProductRepository.delete(favoriteAutoProduct);
        }
    }

    public List<AutoProduct> getFavoriteAutoProducts(int idPerson) {
        Optional<Person> person = peopleRepository.findById(idPerson);
        List<FavoriteAutoProduct> favoriteAutoProducts = favoriteAutoProductRepository.findByPerson(person);
        List<AutoProduct> autoProducts = new ArrayList<>();
        for (FavoriteAutoProduct favoriteAutoProduct : favoriteAutoProducts) {
            autoProducts.add(favoriteAutoProduct.getAutoProduct());
        }
        return autoProducts;
    }
}
