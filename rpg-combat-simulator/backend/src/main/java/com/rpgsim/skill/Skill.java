package com.rpgsim.skill;

import com.rpgsim.model.Character;

public interface Skill {
    String getName();
    int getManaCost();
    String execute(Character caster, Character target);
}
