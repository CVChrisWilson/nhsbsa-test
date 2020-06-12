package com.nhsbsatest.domain.entity;

import com.nhsbsatest.model.SkillLevel;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "skill")
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skillType;

    private SkillLevel skillLevel;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillEntity skillEntity = (SkillEntity) o;
        return Objects.equals(id, skillEntity.id) &&
                Objects.equals(skillType, skillEntity.skillType) &&
                skillLevel == skillEntity.skillLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skillType, skillLevel);
    }

    public SkillEntity() {}

    public SkillEntity(String skillType, SkillLevel skillLevel) {
        this.skillType = skillType;
        this.skillLevel = skillLevel;
    }
}
