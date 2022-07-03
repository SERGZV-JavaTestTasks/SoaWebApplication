package ru.app.web.soa.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;

@Service
public class UserService implements UserDetailsService
{
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveUser(User user)
    {
        user.setRoles(Collections.singleton(new Role(2L, RoleType.USER.get())));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");

        return user;
    }
}
