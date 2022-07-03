package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.entities.ValidationResult;
import ru.app.web.soa.enums.Error;
import ru.app.web.soa.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserValidationService
{
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private final UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    String errorText = "Ошибка ";


    @Autowired
    public UserValidationService
    (
        UserRepository userRepository,
        UserService userService,
        BCryptPasswordEncoder bCryptPasswordEncoder
    )
    {
        this.userRepository = userRepository;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<String> validateNewUser(User newUser)
    {
        var errors = new ArrayList<String>();

        if(isUserExist(newUser.getUsername())) errors.add(Error.getError(Error.USERALREADYEXIST));

        var valUsernameResult = validateUsername(newUser.getUsername());
        if(!valUsernameResult.getPassed()) errors.add(valUsernameResult.getError());

        var valPasswordResult = validatePassword(newUser.getPassword());
        if(!valPasswordResult.getPassed()) errors.add(valPasswordResult.getError());

        return errors;
    }

    public ValidationResult loginIsPossible(User enteringUser)
    {
        ValidationResult validationResult = new ValidationResult();

        User user;
        try
        {
            user = (User)userService.loadUserByUsername(enteringUser.getUsername());
        }
        catch (UsernameNotFoundException ex)
        {
            validationResult.setPassed(false);
            validationResult.setError(Error.getError(Error.USERDONTEXIST));

            return validationResult;
        }

        if(!passIsCorrect(enteringUser, user))
        {
            validationResult.setPassed(false);
            validationResult.setError(Error.getError(Error.INVALIDPASSWORD));

            return validationResult;
        }

        validationResult.setPassed(true);
        return validationResult;
    }

    public boolean isUserExist(String username)
    {
        ValidationResult validationResult = new ValidationResult();
        String SQL = "SELECT * FROM t_user WHERE username = ?";

        Query query = em.createNativeQuery(SQL, User.class);
        query.setParameter(1, username);

        @SuppressWarnings("unchecked")
        List<User> users = query.getResultList();

        return users.size() > 0;
    }

    private boolean passIsCorrect(User checkedUser, User originUser)
    {
        var encryptedCUPass = bCryptPasswordEncoder.encode(checkedUser.getPassword());
        return encryptedCUPass.equals(originUser.getPassword());
    }

    private ValidationResult validateUsername(String username)
    {
        ValidationResult validationResult = new ValidationResult();

        if(username.length() < 4 || username.length() > 40)
        {
            validationResult.setPassed(false);
            validationResult.setError(Error.getError(Error.INVALIDUSERNAMELENGTH));
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
            validationResult.setError(Error.getError(Error.INVALIDPASSWORDLENGTH));
        }
        else validationResult.setPassed(true);

        return validationResult;
    }
}
