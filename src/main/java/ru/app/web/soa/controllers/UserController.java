package ru.app.web.soa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import ru.app.web.soa.entities.auxillary.RegistrationResults;
import ru.app.web.soa.entities.auxillary.StringObj;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.entities.auxillary.UserSession;
import ru.app.web.soa.enums.Error;
import ru.app.web.soa.services.UserService;
import ru.app.web.soa.services.UserValidationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final UserValidationService validationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController
    (
        UserService userService,
        UserValidationService validationService,
        AuthenticationManager authenticationManager
    )
    {
        this.userService = userService;
        this.validationService = validationService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/test_autorize")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String roleRestrictionTest()
    {
        return "true";
    }

    @GetMapping("/login")
    public String loginController(@RequestBody User user, HttpServletRequest request)
    {
        var responseString = "";
        var validationResult = validationService.loginIsPossible(user, request.getSession());

        if(validationResult.getPassed())
        {
            authenticateUserAndSetSession(user.getUsername(), user.getPassword(), request);
            UserSession.resetFailedLoginAttempts(request.getSession());
            responseString = "Вы успешно вошли";
        }
        else
        {
            UserSession.addFailedLoginAttempt(request.getSession());
            responseString = validationResult.getError();
        }

        return responseString;
    }

    @GetMapping("/exist")
    public String suchUserExist(@RequestBody StringObj username)
    {
        String message;

        boolean exist = userService.isUserExist(username.getString());
        if(!exist) message = Error.getError(Error.USER_DONT_EXIST);
        else message = "Имя пользователя свободно";

        return message;
    }

    @PostMapping("/create")
    public RegistrationResults createUser(@RequestBody User user, HttpServletRequest request)
    {
        var registrationResults = new RegistrationResults();
        registrationResults.addErrors(validationService.validateNewUser(user));

        var unencryptedPassword = user.getPassword();
        if(registrationResults.getErrors().size() == 0)
        {
            userService.createNewUser(user);
            registrationResults.setSuccessful(true);
            authenticateUserAndSetSession(user.getUsername(), unencryptedPassword, request);
        }

        return registrationResults;
    }

    private void authenticateUserAndSetSession(String username, String unencryptedPass, HttpServletRequest request)
    {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, unencryptedPass);

        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
