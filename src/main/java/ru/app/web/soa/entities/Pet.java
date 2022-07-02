package ru.app.web.soa.entities;

import ru.app.web.soa.enums.PetType;
import ru.app.web.soa.enums.Sex;

import javax.persistence.*;

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
}
