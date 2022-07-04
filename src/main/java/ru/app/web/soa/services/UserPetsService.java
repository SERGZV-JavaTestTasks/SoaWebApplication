package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.Pet;

import java.util.List;

@Service
public class UserPetsService
{
    private final UserService userService;

    @Autowired
    public UserPetsService(UserService userService) { this.userService = userService; }

    public void addNewPet(Pet pet)
    {
        var user = userService.getCurrentUser();
        user.addPet(pet);
        userService.saveUser(user);
    }

    public void editPet()
    {

    }

    public void deletePet()
    {

    }

    public List<Pet> getAllPets()
    {
        return userService.getCurrentUser().getPets();
    }

    public Pet getPet()
    {
        return new Pet();
    }
}
