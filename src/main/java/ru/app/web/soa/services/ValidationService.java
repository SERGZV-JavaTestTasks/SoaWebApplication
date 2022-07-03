package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.RegistrationResults;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService
{
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;

    @Autowired
    public ValidationService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<String> validateNewUser(User user)
    {
        var error = "Error ";
        var errors = new ArrayList<String>();

        var userExist = isUsernameExist(user.getUsername());
        if(userExist) errors.add(error + HttpStatus.CONFLICT + " Такое имя пользователя уже существует");

        return errors;
    }

    private boolean isUsernameExist(String username)
    {
        String SQL = "SELECT * FROM t_user WHERE username = ?";

        Query query = em.createNativeQuery(SQL, User.class);
        query.setParameter(1, username);

        List<User> users = query.getResultList();

        for(User dbUser : users)
            if(dbUser.getUsername().equals(username)) return true;

        return false;
    }
}
