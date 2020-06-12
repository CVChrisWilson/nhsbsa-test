package com.nhsbsatest.domain.converters;

import com.nhsbsatest.domain.entity.PersonEntity;
import com.nhsbsatest.model.Person;
import com.nhsbsatest.model.PlatformIdentifier;
import com.nhsbsatest.model.Skill;
import org.springframework.util.CollectionUtils;

public class PersonConverters {
    public static Person toDto(PersonEntity from) {
        if (from == null) {
            return null;
        }

        Person to = new Person();
        to.setEmail(from.getEmail());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setPlatformPersonIdentifier(PlatformIdentifier.valueOf(from.getPlatformPersonIdentifier()));
        if (!CollectionUtils.isEmpty(from.getSkills())) {
            from.getSkills().stream()
                    .map(SkillConverters::toDto)
                    .forEach(to.getSkillList()::add);
        }

        return to;
    }
}
