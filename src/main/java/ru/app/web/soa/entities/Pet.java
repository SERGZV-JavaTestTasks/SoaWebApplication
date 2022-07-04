package ru.app.web.soa.entities;

import ru.app.web.soa.enums.PetType;
import ru.app.web.soa.enums.Sex;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "t_pet")
public class Pet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private PetType petType;
    private Long birthDate;
    private Sex sex;
    private String nickname;

    public Long getId() { return id; }
    public PetType getPetType() { return petType; }
    public void setPetType(PetType petType) { this.petType = petType; }
    public Long getBirthDate() { return birthDate; }
    public void setBirthDate(Long birthDate) { this.birthDate = birthDate; }
    public Sex getSex() { return sex; }
    public void setSex(Sex sex) { this.sex = sex; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public void editPet(Pet editedPet)
    {
        petType = editedPet.getPetType();
        birthDate = editedPet.getBirthDate();
        sex = editedPet.getSex();
        nickname = editedPet.nickname;
    }

    public Date getNormalBirthDate()
    {
        Calendar birthCalDate = Calendar.getInstance();
        birthCalDate.setTimeInMillis(birthDate);
        return birthCalDate.getTime();
    }
}
