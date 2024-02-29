package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.FavoriteProduct;
import com.example.TopExamSpring.repositories.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;

    public List<FavoriteProduct> getAllFavoriteProduct() {
        return favoriteProductRepository.findAll();
    }

    public FavoriteProduct getOneFavoriteProduct(int id) {
//        Optional<FavoriteProduct> favoriteProduct = favoriteProductRepository.findById(id);
//        if (favoriteProduct.isPresent()) {
//            FavoriteProduct favoriteProduct1 = favoriteProduct.get();
//            return favoriteProduct1;
//        }
//        return new FavoriteProduct();
        Optional<FavoriteProduct> favoriteProduct = favoriteProductRepository.findById(id);
        return favoriteProduct.orElse(null);
    }


}
