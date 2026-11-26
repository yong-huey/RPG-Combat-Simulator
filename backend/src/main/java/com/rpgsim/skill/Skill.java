package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public interface Skill {
    String getName();
    int getManaCost();
    TargetType getTargetType();
    SkillResult execute(Character caster, Character target);
}
