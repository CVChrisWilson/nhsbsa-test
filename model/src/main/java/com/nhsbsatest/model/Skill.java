package com.nhsbsatest.model;

import java.util.Objects;

public class Skill {
    private String skillType;
    private SkillLevel skillLevel;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(skillType, skill.skillType) &&
                skillLevel == skill.skillLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillType, skillLevel);
    }
}
