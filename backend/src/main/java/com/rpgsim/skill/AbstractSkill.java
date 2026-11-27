package com.rpgsim.skill;

import com.rpgsim.model.Character;

public abstract class AbstractSkill implements Skill{
    private final String name;
    private final int manaCost;

    protected AbstractSkill(String name, int manaCost){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Skill name cannot be null or blank.");
        }
        if (manaCost < 0){
            throw new IllegalArgumentException("Mana cost cannot be negative.");
        }

        this.name = name;
        this.manaCost = manaCost;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override 
    public int getManaCost(){
        return manaCost;
    }

    protected SkillResult validateExecution(Character caster, Character target){
        if(!caster.isAlive()){
            return new SkillResult(false, caster.getName() + " is defeated and cannot use skills.");
        }
        if(!target.isAlive()){
            return new SkillResult(false, target.getName() + " is already defeated.");
        }
        if(!caster.hasEnoughmana(getManaCost())){
            return new SkillResult(false, caster.getName() + " has insufficient mana.");
        }
        return new SkillResult(true, "Skill execution validated successfully.");
    }
}
