package com.nhsbsatest.domain.entity;

import com.nhsbsatest.model.PlatformIdentifier;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String platformPersonIdentifier;

    private String firstName;

    private String lastName;

    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformPersonIdentifier() {
        return platformPersonIdentifier;
    }

    public void setPlatformPersonIdentifier(String platformPersonIdentifier) {
        this.platformPersonIdentifier = platformPersonIdentifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public PersonEntity() {}

    public PersonEntity(com.nhsbsatest.model.Person from) {
        this.platformPersonIdentifier = from.getPlatformPersonIdentifier().toString();
        this.firstName = from.getFirstName();
        this.lastName = from.getLastName();
        this.email = from.getEmail();
        from.getSkillList().stream()
                .map(skill -> new Skill(skill.getSkillType(), skill.getSkillLevel()))
                .forEach(this.skills::add);
    }

    public PersonEntity(com.nhsbsatest.model.NewPerson from) {
        this.platformPersonIdentifier = PlatformIdentifier.create().toString();
        this.firstName = from.getFirstName();
        this.lastName = from.getLastName();
        this.email = from.getEmail();
        from.getSkillList().stream()
                .map(skill -> new Skill(skill.getSkillType(), skill.getSkillLevel()))
                .forEach(this.skills::add);
    }
}
