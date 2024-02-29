package com.example.TopExamSpring.controllers;


import com.example.TopExamSpring.model.*;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.service.AutoProductService;
import com.example.TopExamSpring.service.PersonService;
import com.example.TopExamSpring.service.ProductService;
import com.example.TopExamSpring.service.RealEstateService;
import com.example.TopExamSpring.util.PersonEmailValidator;
import com.example.TopExamSpring.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final PersonValidator personValidator;
    private final ProductService productService;
    private final ImagesRepository imagesRepository;
    private final AutoProductService autoProductService;
    private final PeopleRepository peopleRepository;
    private final PersonEmailValidator personEmailValidator;
    private final RealEstateService realEstateService;


    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator, ProductService productService, ImagesRepository imagesRepository, AutoProductService autoProductService, PeopleRepository peopleRepository, PersonEmailValidator personEmailValidator, RealEstateService realEstateService) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.productService = productService;
        this.imagesRepository = imagesRepository;
        this.autoProductService = autoProductService;
        this.peopleRepository = peopleRepository;
        this.personEmailValidator = personEmailValidator;
        this.realEstateService = realEstateService;
    }

    @GetMapping()
    public String pagePerson(Model model, Principal principal){
        Person person = productService.getUserByPrincipal(principal);
        model.addAttribute("person", person);
        model.addAttribute("products", personService.getProductByPerson(person.getId()));
        model.addAttribute("autoProducts", personService.getAutoProductByPerson(person.getId()));
        model.addAttribute("realEstateProducts", personService.getRealEstateProductByPerson(person.getId()));
        return "person/person_page";
    }

    @GetMapping("/person/product")
    public String pagePersonProduct(Model model, Principal principal){
        Person person = productService.getUserByPrincipal(principal);
        model.addAttribute("person", person);
        model.addAttribute("products", personService.getProductByPerson(person.getId()));
        return "person/person_product";
    }


    @GetMapping("/{id}")
    public String getOnePerson(Model model, @PathVariable("id") int id, Principal principal) {
        Person person = personService.getPersonId(id);
//        Product findOneProduct = productService.getOneProduct(id);
        model.addAttribute("person", person);
        model.addAttribute("products", personService.getProductByPerson(id));
        model.addAttribute("product", productService.getOneProduct(id));
        model.addAttribute("autoProducts", personService.getAutoProductByPerson(person.getId()));
        model.addAttribute("realEstateProducts", personService.getRealEstateProductByPerson(person.getId()));
        if (!person.getUsername().equals(principal.getName())) {
            return "person/person_product";
        }
        return "person/person_page";
    }

    @GetMapping("/{id}/product")
    private String getInfoProduct(Model model, @PathVariable("id") int id) {
        Product product =  productService.getOneProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("files", product.getImages());
        return "person/show_product";
    }

    @GetMapping("/{id}/product/show")
    private String getInfoProductPerson(Model model, @PathVariable("id") int id) {
        Product product =  productService.getOneProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("person", personService.getPersonId(id));
        model.addAttribute("files", product.getImages());
        model.addAttribute("autoProducts");
        return "person/person_product_show";
    }

    @GetMapping("/{id}/auto_product")
    private String getInfoAutoProduct(Model model, @PathVariable("id") Long id) {
       AutoProduct autoProduct = autoProductService.getOneAutoProduct(id);
        model.addAttribute("autoProduct", autoProduct);
        model.addAttribute("files", autoProduct.getImages());
        return "person/person_auto_product_show_principal";
    }

    @GetMapping("/{id}/auto_product/show")
    private String getInfoAutoProductPerson(Model model, @PathVariable("id") Long id) {
        AutoProduct autoProduct = autoProductService.getOneAutoProduct(id);
        model.addAttribute("autoProduct", autoProduct);
        model.addAttribute("person", personService.getPersonId(Math.toIntExact(id)));
        model.addAttribute("files", autoProduct.getImages());
        return "person/person_auto_product_show";
    }

    @GetMapping("/{id}/real_estate")
    private String getInfoRealEstateProduct(Model model, @PathVariable("id") Long id) {
        RealEstate realEstate = realEstateService.getOneRealEstateProduct(id);
        model.addAttribute("realEstate", realEstate);
        model.addAttribute("files", realEstate.getImages());
        return "person/person_real_estate_product_show_principal";
    }

    @GetMapping("/{id}/real_estate/show")
    private String getInfoRealEstateProductPerson(Model model, @PathVariable("id") Long id) {
        RealEstate realEstate = realEstateService.getOneRealEstateProduct(id);
        model.addAttribute("realEstate", realEstate);
        model.addAttribute("person", personService.getPersonId(Math.toIntExact(id)));
        model.addAttribute("files", realEstate.getImages());
        return "person/person_real_estate_product_show";
    }


    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.getPersonId(id));
        return "person/edit_profile";
    }


    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        personEmailValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "person/edit_profile";
        }
        personService.updatePeron(id, person);

        return "redirect:/person/{id}";
    }

    @GetMapping("/new")
    public String newProduct(@ModelAttribute("product") Product product) {
        return "person/new_product";
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                                Principal principal, @RequestParam(value = "file1") MultipartFile file1,
                                @RequestParam(value = "file2") MultipartFile file2,
                                @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if(bindingResult.hasErrors()) {
            return "person/new_product";
        }

        int idPerson = productService.getUserByPrincipal(principal).getId();
        productService.createProduct(product,principal,file1,file2,file3);
        return "redirect:/person/" + idPerson;
    }

    @GetMapping("/{id}/edit_product")
    public String editProduct(@ModelAttribute("product") Product product, @PathVariable("id") int id) {
        productService.getOneProduct(id);
        return "person/edit_product";
    }

    @PatchMapping("/{id}/edit_product")
    public String updateProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                                @PathVariable("id") int id, Principal principal,
                                @RequestParam(value = "file1") MultipartFile file1,
                                @RequestParam(value = "file2") MultipartFile file2,
                                @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if (bindingResult.hasErrors()) {
            return "person/edit_product";
        }
        int idPerson = productService.getUserByPrincipal(principal).getId();
        productService.updateProduct(id, product, principal,file1,file2,file3);

        return "redirect:/person/" + idPerson;//исправить ошибку
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int id, Principal principal) {
        int idPerson = productService.getUserByPrincipal(principal).getId();
        productService.deleteProduct(id);
        return "redirect:/person/" + idPerson;//исправить ошибку
    }

    @PostMapping("/product/ban/{id}")
    public String bamProduct(@PathVariable("id") int id, Principal principal) {
        int idPerson = productService.getUserByPrincipal(principal).getId();
        productService.banProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @PostMapping("/product/unban/{id}")
    public String unBamProduct(@PathVariable("id") int id, Principal principal) {
        int idPerson = productService.getUserByPrincipal(principal).getId();
        productService.unBanProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @GetMapping("/info/product/ban")
    public String infoProductBan(Model model, Principal principal) {
        String username = principal.getName();
        Optional<Person> person = peopleRepository.findByUsername(username);
        if(person.isPresent()) {
            Person person1 = person.get();
            model.addAttribute("productsBan", productService.productIsActive(person1.getId()));
            model.addAttribute("autoProductsBan", autoProductService.autoProductIsActive());
            model.addAttribute("realEstateBan", realEstateService.realEstateProductIsActive());
        }

        return "person/info_product_ban";
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

}
