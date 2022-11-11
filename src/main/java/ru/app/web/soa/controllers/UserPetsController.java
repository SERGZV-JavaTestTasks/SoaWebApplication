package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.Pet;
import ru.app.web.soa.util.CustomResponse;
import ru.app.web.soa.util.enums.CustomStatus;
import ru.app.web.soa.simpletypecontainers.LongContainer;
import ru.app.web.soa.services.UserPetsService;
import ru.app.web.soa.util.enums.Message;

import java.util.Collections;

@RestController
@RequestMapping("/user-pet")
public class UserPetsController
{
    private final UserPetsService userPetsService;

    @Autowired
    public UserPetsController(UserPetsService userPetsService) { this.userPetsService = userPetsService; }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomResponse<String> addNewPet(@RequestBody Pet newPet)
    {
        userPetsService.addNewPet(newPet);
        return new CustomResponse<>(null, CustomStatus.OK, Message.PET_ADDED);
    }

    @PatchMapping("/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomResponse<String> editPet(@RequestBody Pet editedPet)
    {
        if (userPetsService.tryEditPet(editedPet))
            return new CustomResponse<>(null, CustomStatus.OK, Message.PET_EDITED);

        return new CustomResponse<>(null, CustomStatus.BAD_REQUEST, Message.NO_SUCH_PET);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomResponse<String> deletePet(@RequestBody LongContainer petId)
    {
        if(userPetsService.tryDeletePet(petId.getField()))
            return new CustomResponse<>(null, CustomStatus.OK, Message.PET_DELETED);

        return new CustomResponse<>(null, CustomStatus.BAD_REQUEST, Message.NO_SUCH_PET);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomResponse<Pet> getAllPets()
    {
        return new CustomResponse<>(userPetsService.getAllPets(), CustomStatus.OK);
    }

    @GetMapping("/get-one")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomResponse<Pet> getPetInfo(@RequestBody LongContainer petId)
    {
        var pet = userPetsService.getPet(petId.getField());

        if(pet.isPresent()) return new CustomResponse<>(Collections.singleton(pet.get()), CustomStatus.OK);
        else return new CustomResponse<>(null, CustomStatus.BAD_REQUEST, Message.NO_SUCH_PET);
    }
}
