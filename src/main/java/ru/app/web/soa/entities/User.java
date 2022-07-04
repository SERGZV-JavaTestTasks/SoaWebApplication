package ru.app.web.soa.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pet> pets;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> userPet) { this.pets = userPet; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addPet(Pet pet) { pets.add(pet); }
    public boolean tryDeletePetById(Long petId) { return pets.removeIf(pet -> pet.getId().equals(petId)); }

    public boolean tryEditPet(Pet editedPet)
    {
        for (var pet : pets)
        {
            if(pet.getId().equals(editedPet.getId()))
            {
                pet.editPet(editedPet);
                return true;
            }
        }

        return false;
    }


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
