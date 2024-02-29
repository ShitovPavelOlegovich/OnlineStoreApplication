package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.*;
import com.example.TopExamSpring.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductsRepository productsRepository;
    private final AutoRepository autoRepository;
    private final ImagesRepository imagesRepository;
    private final PeopleRepository peopleRepository;
    private final FavoriteProductRepository favoriteProductRepository;

    public List<Product> getAllProducts() {
        log.info("Вывод всех продуктов: ");
        return productsRepository.findAll();
    }

    public List<Product> searchTitle(String title) {
        log.info("Поиск продукта по названию: ");
        return productsRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Image> getImageByProduct(int id) {
        log.info("Поиск фото: ");
        Optional<Product> product = productsRepository.findById(id);
        if(product.isPresent()) {
            Product product1 = product.get();
            return imagesRepository.findImagesByProduct(product1);
        }
        return null;
    }

    public Product getOneProduct(int id) {
        log.info("Поиск продуктов по id:");
        Optional<Product> book = productsRepository.findById(id);
        return book.orElse(null);
    }


    @Transactional
    public void createProduct(Product product, Principal principal, MultipartFile file1,
                              MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Создание продукта: " + product);
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if(file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if(file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }

        product.setOwnerPerson(getUserByPrincipal(principal));
        product.setActiveProduct(true);
//        product.setCategories(Collections.singleton(Category.PRODUCT_ELECTRONIC));
        Product product1 = productsRepository.save(product);
        product1.setPreviewImageId(product1.getImages().get(0).getId());
        productsRepository.save(product);
    }

    @Transactional
    public void updateProduct(int id, Product product, Principal principal, MultipartFile file1,
                              MultipartFile file2, MultipartFile file3) throws IOException {
        log.info("Редактирование продукта под id: " + id);
        product.setId(id);
        productsRepository.deleteImageById(product.getId());
        Image newImage1;
        Image newImage2;
        Image newImage3;
        if(file1.getSize() != 0) {
            newImage1 = toImageEntity(file1);
            newImage1.setPreviewImage(true);
            product.addImageToProduct(newImage1);
        }
        if(file2.getSize() != 0) {
            newImage2 = toImageEntity(file2);
            product.addImageToProduct(newImage2);
        }
        if(file3.getSize() != 0) {
            newImage3 = toImageEntity(file3);
            product.addImageToProduct(newImage3);
        }
        product.setOwnerPerson(getUserByPrincipal(principal));
        product.setDateOfCreated(LocalDateTime.now());
//        product.setCategories(Collections.singleton(Category.PRODUCT_ELECTRONIC));
        product.setActiveProduct(true);
        Product product1 = productsRepository.save(product);
        product1.setPreviewImageId(product1.getImages().get(0).getId());
        productsRepository.save(product1);
    }

    @Transactional
    public void deleteProduct(int id) {
        log.info("Удаление продукта под id: " + id);
        productsRepository.deleteById(id);
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
    public void  banProduct(int id) {
        Product product = productsRepository.findById(id).orElse(null);
        if (product != null) {
            product.setActiveProduct(false);
            log.info("Заблокировать продукт");
            productsRepository.save(product);
        }
    }
    @Transactional
    public void  unBanProduct(int id) {
        Product product = productsRepository.findById(id).orElse(null);
        if (product != null) {
            product.setActiveProduct(true);
            log.info("Разблокировать продукт");
            productsRepository.save(product);
        }
    }

    public List<Product> productIsActive(int idPerson){
        Optional<Person> person = peopleRepository.findById(idPerson);
        List<Product> products = productsRepository.findProductByOwnerPerson(person);
        List<Product> inactiveProducts = new ArrayList<>();
        for (Product product : products) {
            if (!product.isActiveProduct()) {
                inactiveProducts.add(product);
            }
        }
        return inactiveProducts;

    }

    @Transactional
    public void addToFavorites(int productId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        Product product = productsRepository.findById(productId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        boolean isProductAlreadyInFavorites = favoriteProductRepository.existsByPersonAndProduct(person, product);

        if (isProductAlreadyInFavorites) {
            favoriteProductRepository.deleteByPersonAndProduct(person, product);
        }
        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setPerson(person);
        favoriteProduct.setProduct(product);

        favoriteProductRepository.save(favoriteProduct);
    }

    @Transactional
    public void removeFromFavorites(int productId, int personId) {
        Person person = peopleRepository.findById(personId).orElseThrow(()
                -> new RuntimeException("Person not found!"));

        Product product = productsRepository.findById(productId).orElseThrow(()
                -> new RuntimeException("Product not found!"));

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByPersonAndProduct(person, product);
        if (favoriteProduct != null) {
            favoriteProductRepository.delete(favoriteProduct);
        }
    }

    public List<Product> getFavoriteProducts(int idPerson) {
        Optional<Person> person = peopleRepository.findById(idPerson);
        List<FavoriteProduct> favoriteProducts = favoriteProductRepository.findByPerson(person);
        List<Product> products = new ArrayList<>();
        for (FavoriteProduct favoriteProduct : favoriteProducts) {
            products.add(favoriteProduct.getProduct());
        }
        return products;
    }



}
