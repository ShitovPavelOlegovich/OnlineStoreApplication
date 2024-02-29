package com.example.TopExamSpring.controllers;


import com.example.TopExamSpring.model.*;
import com.example.TopExamSpring.repositories.FavoriteProductRepository;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ImagesRepository imagesRepository;
    private final PersonService personService;
    private final PeopleRepository peopleRepository;
    private final AutoProductService autoProductService;
    private final RealEstateService realEstateService;
    private final FavoriteProductRepository favoriteProductRepository;
    private final FavoriteProductService favoriteProductService;


    @GetMapping()
    public String getAllProduct(Model model, @RequestParam(value = "title", required = false, defaultValue = "false") String title) {
        List<Product>  product = productService.getAllProducts();
        model.addAttribute("products", product);
        model.addAttribute("searchProduct", productService.searchTitle(title));
        return "product/info_one_product";
    }

    @GetMapping("/{id}")
    public String getOneProduct(Model model, @PathVariable("id") int id) {
        Person person = productService.getOneProduct(id).getOwnerPerson();
        Product product1 = productService.getOneProduct(id);
        model.addAttribute("product", product1);
        model.addAttribute("person", person);
        model.addAttribute("files", product1.getImages());
        return "product/show";
    }

    @PostMapping("/add_favorite_product/{id}")
    public String addFavoriteProduct(@PathVariable("id") int id, Principal principal) {
        int idPerson = productService.getUserByPrincipal(principal).getId();
        int idProduct = productService.getOneProduct(id).getId();
        productService.addToFavorites(idProduct, idPerson);
        return "redirect:/home";
    }

    @PostMapping("/remove_favorite_product/{id}")
    public String removeFavoriteProduct(@PathVariable("id") int id, Principal principal) {
        int idPerson = productService.getUserByPrincipal(principal).getId();
        int idProduct = productService.getOneProduct(id).getId();
        productService.removeFromFavorites(idProduct, idPerson);
        return "redirect:/person/" + idPerson;
    }

    @GetMapping("/info/favorite_product")
    public String infoFavoriteProduct(Model model, Principal principal) {
        String username = principal.getName();
        Optional<Person> person = peopleRepository.findByUsername(username);
        if (person.isPresent()) {
            Person person1 = person.get();
            List<Product> favoriteProducts = productService.getFavoriteProducts(person1.getId());
            List<AutoProduct> favoriteAutoProducts =  autoProductService.getFavoriteAutoProducts(person1.getId());
            List<RealEstate> favoriteRealProducts = realEstateService.getFavoriteRealEstateProducts(person1.getId());
            model.addAttribute("favoriteProducts", favoriteProducts);
            model.addAttribute("favoriteAutoProducts", favoriteAutoProducts);
            model.addAttribute("favoriteRealProducts", favoriteRealProducts);
        }

        return "person/person_info_favorite_product";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<?> getImageId(@PathVariable("id") String id) {
        if(!id.matches("\\d+")) {
            return ResponseEntity.badRequest().body("Invalid id format");
        }
        int imageId = Integer.parseInt(id);
        Image image = imagesRepository.findById(imageId).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getData())));
    }

}
