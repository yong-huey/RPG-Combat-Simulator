package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class HealSkill implements Skill{
    
    private final String name;
    private final int manaCost;
    private final int healAmount;

    public HealSkill(String name, int manaCost, int healAmount){
        this.name = name;
        this.manaCost = manaCost;
        this.healAmount = healAmount;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public int getManaCost(){
        return manaCost;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SELF;
    }

    @Override 
    public String execute(Character caster, Character target){
        if(!caster.hasEnoughmana(manaCost)){
            return caster.getName() + " does not have enough mana.";
        }

        caster.useMana(manaCost);

        target.heal(healAmount);

        return caster.getName()
                + " used "
                + name
                + " on "
                + target.getName()
                + " for "
                + healAmount
                + " HP.";
    }
}
