package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.app.web.soa.entities.Role;
import ru.app.web.soa.entities.User;
import ru.app.web.soa.enums.RoleType;
import ru.app.web.soa.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService
{
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createNewUser(User user)
    {
        user.setRoles(Collections.singleton(new Role(2L, RoleType.USER.get())));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveUser(User user) { userRepository.save(user); }

    public boolean isUserExist(String username)
    {
        String SQL = "SELECT * FROM t_user WHERE username = ?";

        Query query = em.createNativeQuery(SQL, User.class);
        query.setParameter(1, username);

        @SuppressWarnings("unchecked")
        List<User> users = query.getResultList();

        return users.size() > 0;
    }

    public User getCurrentUser()
    {
        var userName = getCurrentUsername();
        return (User)loadUserByUsername(userName);
    }

    public String getCurrentUsername()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");

        return user;
    }
}
