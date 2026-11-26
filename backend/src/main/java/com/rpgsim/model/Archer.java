package com.rpgsim.model;

import com.rpgsim.skill.DamageSkill;

public class Archer extends Character{
    public Archer(String name) {
        super(
                name,
                110,
                60,
                20,
                10,
                16
        );

        addSkill(
                new DamageSkill(
                        "Power Shot",
                        15,
                        30
                )
        );
    }
}
