package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.Pet;
import ru.app.web.soa.enums.Error;
import ru.app.web.soa.simpletypecontainers.LongContainer;
import ru.app.web.soa.services.UserPetsService;

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
    public String editPet(@RequestBody Pet editedPet)
    {
        if(userPetsService.tryEditPet(editedPet)) return "Питомец успешно отредактирован";
        return Error.getError(Error.NO_SUCH_PET);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deletePet(@RequestBody LongContainer petId)
    {
        if(userPetsService.tryDeletePet(petId.getField())) return "Питомец успешно удалён";
        return Error.getError(Error.NO_SUCH_PET);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Pet> getAllPets()
    {
        return userPetsService.getAllPets();
    }

    @GetMapping("/get-one")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Object getPetInfo(@RequestBody LongContainer petId)
    {
        var pet = userPetsService.getPet(petId.getField());

        if(pet.isPresent()) return pet;
        else return Error.getError(Error.NO_SUCH_PET);
    }
}
