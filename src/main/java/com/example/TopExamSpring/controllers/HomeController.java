package com.example.TopExamSpring.controllers;

import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import com.example.TopExamSpring.repositories.FavoriteProductRepository;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final ProductService productService;
    private final PersonService personService;
    private final ImagesRepository imagesRepository;
    private final AutoProductService autoProductService;
    private final FavoriteProductRepository favoriteProductRepository;
    private final FavoriteProductService favoriteProductService;

    private final RealEstateService realEstateService;

    @Autowired
    public HomeController(ProductService productService, PersonService personService, ImagesRepository imagesRepository, AutoProductService autoProductService, FavoriteProductRepository favoriteProductRepository, FavoriteProductService favoriteProductService, RealEstateService realEstateService) {
        this.productService = productService;
        this.personService = personService;
        this.imagesRepository = imagesRepository;
        this.autoProductService = autoProductService;
        this.favoriteProductRepository = favoriteProductRepository;
        this.favoriteProductService = favoriteProductService;
        this.realEstateService = realEstateService;
    }

    @GetMapping()
    public String getInfoAllProduct(Model model, @RequestParam(value = "title", required = false, defaultValue = "false") String title) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("autoProducts", autoProductService.getAllAutoProducts());
        model.addAttribute("searchProduct", productService.searchTitle(title));
        model.addAttribute("realEstateProducts", realEstateService.getAllRealEstateProducts());
        return "home_page/home";
    }

    @GetMapping("/{id}")
    private String getInfoOneProduct(Model model, @PathVariable("id") int id) {
        Person person = productService.getOneProduct(id).getOwnerPerson();
        Product findOneProduct = productService.getOneProduct(id);
        model.addAttribute("product", findOneProduct);
        model.addAttribute("person", person);
        model.addAttribute("files", findOneProduct.getImages());
        return "product/show";
    }

    @GetMapping("/people")
    public String getAllPerson(Model model, Principal principal ) {
        model.addAttribute("people", personService.getAllPerson());
        model.addAttribute("person", productService.getUserByPrincipal(principal));
        return "home_page/home";
    }


    @GetMapping("/person/{id}")
    public String personPage(Model model, @PathVariable("id") int id) {
        Person person = personService.getPersonId(id);
        model.addAttribute("person", person);
        model.addAttribute("people", personService.getAllPerson());
        model.addAttribute("products", person.getProducts());
        model.addAttribute("autoProducts", person.getAutoProducts());
        model.addAttribute("realEstateProducts", person.getRealEstates());
        return "person/person_product";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<?> getImageId(@PathVariable("id") String id, Model model) {
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

//    @GetMapping("/images/{id}")
//    public ResponseEntity<byte[]> getImageId(@PathVariable("id") String id, Model model) {
//        try {
//            int imageId = Integer.parseInt(id);
//            Image image = imagesRepository.findById(imageId).orElse(null);
//
//            if(image != null) {
//                byte[] imageData = image.getData();
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.valueOf(image.getContentType()));
//                return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (NumberFormatException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }






}
