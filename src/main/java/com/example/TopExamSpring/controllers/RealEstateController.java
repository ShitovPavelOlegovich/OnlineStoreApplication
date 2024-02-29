package com.example.TopExamSpring.controllers;


import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.model.RealEstate;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.service.PersonService;
import com.example.TopExamSpring.service.RealEstateService;
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

@Controller
@RequestMapping("/real_estate")
@RequiredArgsConstructor
public class RealEstateController {

    private final RealEstateService realEstateService;
    private final ImagesRepository imagesRepository;
    private final PersonService personService;

    @GetMapping()
    public String findRealEstateProduct(Model model,
                                     @RequestParam(value = "title", required = false, defaultValue = "false") String title) {
        List<RealEstate> realEstates = realEstateService.getAllRealEstateProducts();
        model.addAttribute("realEstateProducts", realEstates);
        model.addAttribute("searchRealEstateProduct", realEstateService.searchTitleTypeOfHousing(title));
        return "real_estate_product/real_estate_product_all";
    }

    @GetMapping("/{id}")
    public String findRealEstateProduct(Model model, @PathVariable("id") Long id) {
        Person person = realEstateService.getOneRealEstateProduct(id).getOwnerPersonRealEstate();
        RealEstate realEstate = realEstateService.getOneRealEstateProduct(id);
        model.addAttribute("realEstateProduct", realEstate);
        model.addAttribute("person", person);
        model.addAttribute("files", realEstate.getImages());
        return "real_estate_product/real_estate_product_one";
    }

    @GetMapping("/people")
    public String getAllPerson(Model model, Principal principal ) {
        model.addAttribute("people", personService.getAllPerson());
        model.addAttribute("person", realEstateService.getUserByPrincipal(principal));
        return "real_estate_product/real_estate_product_one";
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

    @GetMapping("/new/real_estate")
    public String newRealEstateProduct(@ModelAttribute("realEstateProduct") RealEstate realEstate) {
        return "real_estate_product/create_new_real_estate_product";
    }

    @PostMapping("/new/real_estate")
    public String createRealEstateProduct(@ModelAttribute("realEstateProduct") @Valid RealEstate realEstate,
                                    BindingResult bindingResult, Principal principal,
                                    @RequestParam(value = "file1") MultipartFile file1,
                                    @RequestParam(value = "file2") MultipartFile file2,
                                    @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if (bindingResult.hasErrors()) {
            return "real_estate_product/create_new_real_estate_product";
        }

        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        realEstateService.createRealEstateProduct(realEstate, principal, file1, file2, file3);
        return "redirect:/person/" + idPerson;
    }

    @GetMapping("/{id}/edit/real_estate")
    public String editRealEstateProduct(@ModelAttribute("realEstateProduct") RealEstate realEstate,
                                  @PathVariable("id") Long id) {
        realEstateService.getOneRealEstateProduct(id);
        return "real_estate_product/update_real_estate_product";
    }

    @PatchMapping("/{id}/edit/real_estate")
    public String updateRealEstateProduct(@ModelAttribute("realEstateProduct") @Valid RealEstate realEstate, BindingResult bindingResult,
                                    @PathVariable("id") Long id,Principal principal,
                                    @RequestParam(value = "file1") MultipartFile file1,
                                    @RequestParam(value = "file2") MultipartFile file2,
                                    @RequestParam(value = "file3") MultipartFile file3) throws IOException {
        if (bindingResult.hasErrors()) {
            return "real_estate_product/update_real_estate_product";
        }
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        realEstateService.updateRealEstateProduct(id,realEstate, principal, file1, file2, file3);
        return "redirect:/person/" + idPerson;
    }

    @DeleteMapping("/{id}/delete")
    public String deleteRealEstateProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        realEstateService.deleteRealEstateProduct(id);
        return "redirect:/person/" + idPerson;
    }
    @PostMapping("/real_estate/ban/{id}")
    public String banRealEstateProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        realEstateService.banRealEstateProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @PostMapping("/real_estate/unban/{id}")
    public String unBamRealEstateProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        realEstateService.unBanRealEstateProduct(id);
        return "redirect:/person/" + idPerson;
    }

    @PostMapping("/add_favorite_real_estate/{id}")
    public String addFavoriteRealEstateProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        Long idRealEstateProduct = realEstateService.getOneRealEstateProduct(id).getId();
        realEstateService.addToFavorites(idRealEstateProduct, idPerson);
        return "redirect:/home";
    }

    @PostMapping("/remove_favorite_real_estate/{id}")
    public String removeFavoriteRealEstateProduct(@PathVariable("id") Long id, Principal principal) {
        int idPerson = realEstateService.getUserByPrincipal(principal).getId();
        Long idRealEstateProduct = realEstateService.getOneRealEstateProduct(id).getId();
        realEstateService.removeFromFavorites(idRealEstateProduct, idPerson);
        return "redirect:/person/" + idPerson;
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
