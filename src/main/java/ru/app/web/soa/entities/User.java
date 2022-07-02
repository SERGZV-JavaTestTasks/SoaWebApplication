package ru.app.web.soa.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size
    @Size(min = 4, max = 40, message = "Длинна имени пользователя должна быть между 4 и 40 символами")
    private String username;
    @Size(min = 6, message = "Длинна пароля должна быть не меньше 6 символов")
    private String password;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Pet> userPet;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Pet> getUserPet() { return userPet; }
    public void setUserPet(List<Pet> userPet) { this.userPet = userPet; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return getRoles(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
