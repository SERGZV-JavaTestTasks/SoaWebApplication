package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.util.CustomResponse;
import ru.app.web.soa.util.UserSession;

import ru.app.web.soa.util.enums.CustomStatus;
import ru.app.web.soa.util.enums.Message;

import javax.servlet.http.HttpSession;

import static ru.app.web.soa.util.enums.CustomStatus.CONFLICT;

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
    
    public CustomResponse<String> validateNewUser(User newUser)
    {
        if(userService.isUserExist(newUser.getUsername()))
            return new CustomResponse<>(null, CustomStatus.CONFLICT, Message.USER_ALREADY_EXIST);

        var valUsernameResult = validateUsername(newUser.getUsername());
        if (!valUsernameResult.getMessage().equals(CustomStatus.OK.name())) return valUsernameResult;

        var valPasswordResult = validatePassword(newUser.getPassword());
        if (!valPasswordResult.getMessage().equals(CustomStatus.OK.name())) return valPasswordResult;

        return new CustomResponse<>(null, CustomStatus.OK);
    }

    public CustomResponse<String> loginIsPossible(User enteringUser, HttpSession session)
    {
        var attemptsExceeded = UserSession.numLoginAttemptsExceeded(session);
        if(attemptsExceeded) return new CustomResponse<>(null, CONFLICT, Message.EXCEEDED_LOGIN_ATTEMPTS);

        User user;
        try
        {
            user = (User)userService.loadUserByUsername(enteringUser.getUsername());
        }
        catch (UsernameNotFoundException ex)
        {
            return new CustomResponse<>(null, CONFLICT, Message.USER_DONT_EXIST);
        }

        if(!passIsCorrect(enteringUser, user))
        {
            return new CustomResponse<>(null, CONFLICT, Message.INVALID_PASSWORD);
        }

        return new CustomResponse<>(null, CustomStatus.OK);
    }

    private boolean passIsCorrect(User checkedUser, User originUser)
    {
        return bCryptPasswordEncoder.matches(checkedUser.getPassword(), originUser.getPassword());
    }

    private CustomResponse<String> validateUsername(String username)
    {
        if(username.length() < 4 || username.length() > 40)
        {
            return new CustomResponse<>(null, CustomStatus.BAD_REQUEST, Message.INVALID_USERNAME_LENGTH);
        }

        return new CustomResponse<>(null, CustomStatus.OK);
    }

    private CustomResponse<String> validatePassword(String password)
    {
        if(password.length() < 6)
        {
            return new CustomResponse<>(null, CustomStatus.BAD_REQUEST, Message.INVALID_PASSWORD_LENGTH);

        }
        return new CustomResponse<>(null, CustomStatus.OK);
    }
}
