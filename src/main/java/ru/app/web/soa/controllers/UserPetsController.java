package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.Pet;
import ru.app.web.soa.services.UserPetsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-pet")
public class UserPetsController
{
    private final UserPetsService userPetsService;

    @Autowired
    public UserPetsController(UserPetsService userPetsService) { this.userPetsService = userPetsService; }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addNewPet(@RequestBody Pet newPet)
    {
        userPetsService.addNewPet(newPet);
        return "Питомец успешно добавлен";
    }

    @PatchMapping("/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editPet()
    {
        return "";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deletePet()
    {
        return "";
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Pet> getAllPets()
    {
        return userPetsService.getAllPets();
    }

//    @GetMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public Pet getPetInfo(@RequestBody StringObj PetId)
//    {
//        return new Pet();
//    }
}
