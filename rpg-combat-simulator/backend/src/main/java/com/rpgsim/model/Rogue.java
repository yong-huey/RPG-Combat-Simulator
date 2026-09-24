package com.rpgsim.model;

import com.rpgsim.skill.DamageSkill;

public class Rogue extends Character {
    public Rogue(String name){
        super(
            name, 
            100, 
            70,
            22,
            9,
            20
        );

        addSkill(
            new DamageSkill(
                "Backstab",
                15,
                35
            )
        );
    }
}
