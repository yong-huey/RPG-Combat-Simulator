package com.rpgsim.model;

import com.rpgsim.skill.DamageSkill;

public class Warrior extends Character {
    public Warrior(String name){
        super(
            name, 
            150, 
            40, 
            25, 
            18, 
            12
        );

        addSkill(
            new DamageSkill(
                "Heavy Strike", 
                10,
                25
            )
        );
    }
}