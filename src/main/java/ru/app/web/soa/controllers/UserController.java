package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.RegistrationResults;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.services.UserService;
import ru.app.web.soa.services.ValidationService;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final ValidationService validationService;

    @Autowired
    public UserController(UserService userService, ValidationService validationService)
    {
        this.userService = userService;
        this.validationService = validationService;
    }

    @PostMapping("/create")
    public RegistrationResults createUser(@RequestBody User user)
    {
        var registrationResults = new RegistrationResults();
        registrationResults.addErrors(validationService.validateNewUser(user));

        var unencryptedPassword = user.getPassword();
        if(registrationResults.getErrors().size() == 0)
        {
            userService.saveUser(user);
            registrationResults.setSuccessful(true);
        }

        return registrationResults;
    }
}
