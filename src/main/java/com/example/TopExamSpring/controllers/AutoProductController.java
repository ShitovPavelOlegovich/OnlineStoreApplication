package com.example.TopExamSpring.controllers;

import com.example.TopExamSpring.model.AutoProduct;
import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.Product;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.repositories.PeopleRepository;
import com.example.TopExamSpring.service.AutoProductService;
import com.example.TopExamSpring.service.PersonService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auto_product")
@RequiredArgsConstructor
public class AutoProductController {

    private final AutoProductService autoProductService;
    private final PeopleRepository peopleRepository;
    private final ImagesRepository imagesRepository;
    private final PersonService personService;


    @GetMapping()
    public String findAllAutoProduct(Model model,
                                     @RequestParam(value = "title", required = false, defaultValue = "false") String title,
                                     @RequestParam(value = "titleModel", required = false, defaultValue = "false") String titleModel) {
        List<AutoProduct> autoProducts = autoProductService.getAllAutoProducts();
        model.addAttribute("autoProducts", autoProducts);
        model.addAttribute("searchAutoProduct", autoProductService.searchTitleMarkAuto(title));
        model.addAttribute("searchAutoProductFoModel", autoProductService.searchTitleModelAuto(titleModel));
        return "auto_product/auto_product_all";
    }

    @GetMapping("/{id}")
    public String findOneAutoProduct(Model model, @PathVariable("id") Long id) {
        Person person = autoProductService.getOneAutoProduct(id).getOwnerPersonAutoProduct();
        AutoProduct autoProduct = autoProductService.getOneAutoProduct(id);
        model.addAttribute("autoProduct", autoProduct);
        model.addAttribute("person", person);
        model.addAttribute("files", autoProduct.getImages());
        return "auto_product/auto_product_one";
    }

    @GetMapping("/people")
    public String getAllPerson(Model model, Principal principal ) {
        model.addAttribute("people", personService.getAllPerson());
        model.addAttribute("person", autoProductService.getUserByPrincipal(principal));
        return "auto_product/auto_product_one";
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

    @GetMapping("/new/auto_product")
    public String newAutoProduct(@ModelAttribute("autoProduct") AutoProduct autoProduct) {
        return "auto_product/create_new_auto_product";
    }

    @PostMapping("/new/auto_product")
    public String createAutoProduct(@ModelAttribute("autoProduct") @Valid AutoProduct autoProduct,
                                    BindingResult bindingResult, Principal principal,
                                    @RequestParam(value = "file1") MultipartFile file1,
                                    @RequestParam(value = "file2") MultipartFile file2,
                                    @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if (bindingResult.hasErrors()) {
            return "auto_product/create_new_auto_product";
        }

        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        autoProductService.createAutoProduct(autoProduct, principal, file1, file2, file3);
        return "redirect:/person/" + idPerson;
    }

    @GetMapping("/{id}/edit/auto_product")
    public String editAutoProduct(@ModelAttribute("autoProduct") AutoProduct autoProduct,
                                  @PathVariable("id") Long id) {
        autoProductService.getOneAutoProduct(id);
        return "auto_product/update_auto_product";
    }

    @PatchMapping("/{id}/edit/auto_product")
    public String updateAutoProduct(@ModelAttribute("autoProduct") @Valid AutoProduct autoProduct, BindingResult bindingResult,
                                    @PathVariable("id") Long id,Principal principal,
                                    @RequestParam(value = "file1") MultipartFile file1,
                                    @RequestParam(value = "file2") MultipartFile file2,
                                    @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if (bindingResult.hasErrors()) {
            return "auto_product/update_auto_product";
        }
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        autoProductService.updateAutoProduct(id,autoProduct, principal, file1, file2, file3);
        return "redirect:/person/" + idPerson;
    }

    @DeleteMapping("/{id}")
    public String deleteAutoProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        autoProductService.deleteAutoProduct(id);
        return "redirect:/person/" + idPerson;
    }
    @PostMapping("/auto/product/ban/{id}")
    public String bamAutoProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        autoProductService.banAutoProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @PostMapping("/auto/product/unban/{id}")
    public String unBamProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        autoProductService.unBanAutoProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @PostMapping("/add_favorite_auto_product/{id}")
    public String addFavoriteProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        Long idAutoProduct = autoProductService.getOneAutoProduct(id).getId();
        autoProductService.addToFavorites(idAutoProduct, idPerson);
        return "redirect:/home";
    }

    @PostMapping("/remove_favorite_auto_product/{id}")
    public String removeFavoriteProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = autoProductService.getUserByPrincipal(principal).getId();
        Long idAutoProduct = autoProductService.getOneAutoProduct(id).getId();
        autoProductService.removeFromFavorites(idAutoProduct, idPerson);
        return "redirect:/person/" + idPerson;
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
