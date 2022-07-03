package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.entities.ValidationResult;
import ru.app.web.soa.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ValidationService
{
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;

    String errorText = "Error ";

    @Autowired
    public ValidationService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<String> validateNewUser(User user)
    {
        var errors = new ArrayList<String>();

        var valUserExist = isUsernameExist(user.getUsername());
        if(!valUserExist.getPassed()) errors.add(valUserExist.getError());

        var valUsernameResult = validateUsername(user.getUsername());
        if(!valUsernameResult.getPassed()) errors.add(valUsernameResult.getError());

        var valPasswordResult = validatePassword(user.getPassword());
        if(!valPasswordResult.getPassed()) errors.add(valPasswordResult.getError());

        return errors;
    }

    public ValidationResult isUsernameExist(String username)
    {
        ValidationResult validationResult = new ValidationResult();
        String SQL = "SELECT * FROM t_user WHERE username = ?";

        Query query = em.createNativeQuery(SQL, User.class);
        query.setParameter(1, username);

        @SuppressWarnings("unchecked")
        List<User> users = query.getResultList();

        if(users.size() > 0)
        {
            validationResult.setPassed(false);
            validationResult.setError(errorText + HttpStatus.CONFLICT + " Такое имя пользователя уже существует");

            return validationResult;
        }

        validationResult.setPassed(true);
        return validationResult;
    }

    private ValidationResult validateUsername(String username)
    {
        ValidationResult validationResult = new ValidationResult();

        if(username.length() < 4 || username.length() > 40)
        {
            validationResult.setPassed(false);
            validationResult.setError(errorText + HttpStatus.BAD_REQUEST + " Длинна имени пользователя должна быть между 4 и 40 символами");
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
            validationResult.setError(errorText + HttpStatus.BAD_REQUEST + " Длинна пароля должна быть не меньше 6 символов");
        }
        else validationResult.setPassed(true);

        return validationResult;
    }
}
