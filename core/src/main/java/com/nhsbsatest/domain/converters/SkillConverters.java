package com.nhsbsatest.domain.converters;

import com.nhsbsatest.domain.entity.SkillEntity;
import com.nhsbsatest.model.Skill;

public class SkillConverters {
    public static Skill toDto(SkillEntity from) {
        if (from == null) {
            return null;
        }

        Skill to = new Skill();
        to.setSkillLevel(from.getSkillLevel());
        to.setSkillType(from.getSkillType());

        return to;
    }
}
