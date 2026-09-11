package com.rpgsim.model;

import com.rpgsim.skill.DamageSkill;
import com.rpgsim.skill.HealSkill;

public class Mage extends Character{
    public Mage(String name){
        super(
            name, 
            90, 
            120, 
            12, 
            8, 
            12
        );

        addSkill(
            new DamageSkill(
                "Fireball",
                20,
                25
            )
        );

        addSkill(
            new HealSkill(
                "Heal", 
                15, 
                30
            )
        );
    }
}
