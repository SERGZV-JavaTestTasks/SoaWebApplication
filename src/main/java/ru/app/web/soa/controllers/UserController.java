package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.RegistrationResults;
import ru.app.web.soa.entities.StringObj;
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

//    @GetMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public String roleRestrictionTest()
//    {
//        return "true";
//    }

    @GetMapping("/exist")
    public String suchUserExist(@RequestBody StringObj username)
    {
        String message;
        var validationResult = validationService.isUsernameExist(username.getString());

        if(!validationResult.getPassed()) message = validationResult.getError();
        else message = "Имя пользователя свободно";

        return message;
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
