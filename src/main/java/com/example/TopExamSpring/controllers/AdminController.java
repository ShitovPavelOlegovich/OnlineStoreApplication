package com.example.TopExamSpring.controllers;

import com.example.TopExamSpring.model.Person;
import com.example.TopExamSpring.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonService personService;

    @Autowired
    public AdminController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String getAllPerson(Model model) {
        model.addAttribute("people", personService.getAllPerson());
        return "admin/admin_page";
    }

    @GetMapping("/{id}/person")
    public String getOnePerson(Model model, @PathVariable("id") int id) {
        Person person = personService.getPersonId(id);
        model.addAttribute("person", person);
        model.addAttribute("products", person.getProducts());
        model.addAttribute("autoProducts", person.getAutoProducts());
        model.addAttribute("realEstateProducts", person.getRealEstates());
        return "person/person_product";
    }

    @PostMapping("/person/ban/{id}")
    public String banPerson(@PathVariable("id") int id) {
        personService.banPerson(id);
        return "redirect:/admin";
    }

    @PostMapping("/person/unban/{id}")
    public String unbanPerson(@PathVariable("id") int id) {
        personService.unbanPerson(id);
        return "redirect:/admin";
    }

    @PostMapping("/person/role/admin/{id}")
    public String adminRole(@PathVariable("id") int id) {
        personService.appointRoleAdmin(id);
        return "redirect:/admin";
    }

    @PostMapping("/person/role/user/{id}")
    public String userRole(@PathVariable("id") int id) {
        personService.appointRoleUser(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete/person")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/admin";
    }
}
