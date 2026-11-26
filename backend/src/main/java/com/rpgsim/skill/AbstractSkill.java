package com.rpgsim.skill;

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
}
