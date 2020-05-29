package com.nhsbsatest.model;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private PlatformIdentifier platformPersonIdentifier;
    private String firstName;
    private String lastName;
    private String email;

    private List<Skill> skillList = new ArrayList<>();

    public PlatformIdentifier getPlatformPersonIdentifier() {
        return platformPersonIdentifier;
    }

    public void setPlatformPersonIdentifier(PlatformIdentifier platformPersonIdentifier) {
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

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }
}
