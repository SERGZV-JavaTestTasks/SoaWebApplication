package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.simpletypecontainers.StringContainer;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.util.UserSession;
import ru.app.web.soa.util.CustomResponse;
import ru.app.web.soa.services.UserService;
import ru.app.web.soa.services.UserValidationService;
import ru.app.web.soa.util.enums.CustomStatus;
import ru.app.web.soa.util.enums.Message;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final UserValidationService validationService;

    @Autowired
    public UserController(UserService userService, UserValidationService validationService)
    {
        this.userService = userService;
        this.validationService = validationService;
    }

    @GetMapping("/login")
    public CustomResponse<String> loginController(@RequestBody User user, HttpServletRequest request)
    {
        var validationResult = validationService.loginIsPossible(user, request.getSession());

        if (validationResult.getMessage().equals(CustomStatus.OK.getMessage()))
        {
            userService.authenticateUserAndSetSession(user.getUsername(), user.getPassword(), request);
            UserSession.resetFailedLoginAttempts(request.getSession());
            validationResult.setMessageClarification(Message.SUCCESSFUL_LOGIN);
        }
        else
        {
            UserSession.addFailedLoginAttempt(request.getSession());
        }

        return validationResult;
    }

    @GetMapping("/exist")
    public CustomResponse<String> suchUserExist(@RequestBody StringContainer username)
    {
        return userService.suchUserExist(username.getField());
    }

    @PostMapping("/create")
    public CustomResponse<String> createUser(@RequestBody User user, HttpServletRequest request)
    {
        var validationResponse = validationService.validateNewUser(user);

        if (validationResponse.getMessage().equals(CustomStatus.OK.name()))
        {
            var unencryptedPassword = user.getPassword();
            userService.createNewUser(user, unencryptedPassword, request);

            validationResponse.setMessageClarification("The user has been successfully created");
        }

        return validationResponse;
    }
}
