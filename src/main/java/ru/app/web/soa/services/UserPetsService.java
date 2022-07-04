package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.Pet;

import java.util.List;
import java.util.Optional;

@Service
public class UserPetsService
{
    private final UserService userService;

    @Autowired
    public UserPetsService(UserService userService) { this.userService = userService; }

    public void addNewPet(Pet newPet)
    {
        var user = userService.getCurrentUser();
        user.addPet(newPet);
        userService.saveUser(user);
    }

    public boolean tryEditPet(Pet editedPet)
    {
        var user = userService.getCurrentUser();
        var editingResult = user.tryEditPet(editedPet);

        if(editingResult) userService.saveUser(user);
        return editingResult;
    }

    public boolean tryDeletePet(Long petId)
    {
        var user = userService.getCurrentUser();
        var deletingResult = user.tryDeletePetById(petId);

        if(deletingResult) userService.saveUser(user);
        return deletingResult;
    }

    public List<Pet> getAllPets()
    {
        return userService.getCurrentUser().getPets();
    }

    public Optional<Pet> getPet(Long petId)
    {
        var currentUserPets = userService.getCurrentUser().getPets();
        return currentUserPets.stream().filter(pet -> pet.getId().equals(petId)).findFirst();
    }
}
