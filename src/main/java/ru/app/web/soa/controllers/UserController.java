package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.RegistrationResults;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/create")
    public RegistrationResults createUser(@RequestBody User user)
    {
        var registrationResults = new RegistrationResults();

        var unencryptedPassword = user.getPassword();
        userService.saveUser(user);



        return null;
    }


}
