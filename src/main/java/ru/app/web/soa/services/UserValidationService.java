package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.entities.auxillary.UserSession;
import ru.app.web.soa.entities.auxillary.ValidationResult;
import ru.app.web.soa.enums.Error;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserValidationService
{
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserValidationService
    (
        UserService userService,
        BCryptPasswordEncoder bCryptPasswordEncoder
    )
    {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<String> validateNewUser(User newUser)
    {
        var errors = new ArrayList<String>();

        if(userService.isUserExist(newUser.getUsername())) errors.add(Error.getError(Error.USER_ALREADY_EXIST));

        var valUsernameResult = validateUsername(newUser.getUsername());
        if(!valUsernameResult.getPassed()) errors.add(valUsernameResult.getError());

        var valPasswordResult = validatePassword(newUser.getPassword());
        if(!valPasswordResult.getPassed()) errors.add(valPasswordResult.getError());

        return errors;
    }

    public ValidationResult loginIsPossible(User enteringUser, HttpSession session)
    {
        ValidationResult validationResult = new ValidationResult();

        var attemptsExceeded = UserSession.numLoginAttemptsExceeded(session);
        if(attemptsExceeded)
        {
            validationResult.setError(Error.getError(Error.EXCEEDED_LOGIN_ATTEMPTS));
            return validationResult;
        }

        User user;
        try
        {
            user = (User)userService.loadUserByUsername(enteringUser.getUsername());
        }
        catch (UsernameNotFoundException ex)
        {
            validationResult.setError(Error.getError(Error.USER_DONT_EXIST));
            return validationResult;
        }

        if(!passIsCorrect(enteringUser, user))
        {
            validationResult.setError(Error.getError(Error.INVALID_PASSWORD));
            return validationResult;
        }

        validationResult.setPassed(true);
        return validationResult;
    }

    private boolean passIsCorrect(User checkedUser, User originUser)
    {
        return bCryptPasswordEncoder.matches(checkedUser.getPassword(), originUser.getPassword());
    }

    private ValidationResult validateUsername(String username)
    {
        ValidationResult validationResult = new ValidationResult();

        if(username.length() < 4 || username.length() > 40)
        {
            validationResult.setPassed(false);
            validationResult.setError(Error.getError(Error.INVALID_USERNAME_LENGTH));
        }
        else validationResult.setPassed(true);

        return validationResult;
    }

    private ValidationResult validatePassword(String password)
    {
        ValidationResult validationResult = new ValidationResult();

        if(password.length() < 6)
        {
            validationResult.setPassed(false);
            validationResult.setError(Error.getError(Error.INVALID_PASSWORD_LENGTH));
        }
        else validationResult.setPassed(true);

        return validationResult;
    }
}
