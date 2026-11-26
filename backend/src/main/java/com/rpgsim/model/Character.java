package com.rpgsim.model;

import com.rpgsim.skill.Skill;
import com.rpgsim.skill.DamageSkill;

import java.util.ArrayList;
import java.util.List;

public abstract class Character{
    
    private String name;

    private int maxHp;
    private int currentHp;

    private int maxMana;
    private int currentMana;

    private int attack;
    private int defense;
    private int speed;

    private final List<Skill> skills = new ArrayList<>();

    public Character(
        String name,
        int maxHp,
        int maxMana,
        int attack,
        int defense,
        int speed
    ){
        this.name = name;
        
        this.maxHp = maxHp;
        this.currentHp = maxHp;

        this.maxMana = maxMana;
        this.currentMana = maxMana;

        this.attack = attack;
        this.defense = defense;
        this.speed = speed;

        addSkill(new DamageSkill(
            "Basic Attack",
            0,
            5
        ));

    }

    private void validateNonNegative(int amount, String fieldName){
        if (amount < 0){
            throw new IllegalArgumentException(
                fieldName + " cannot be negative."
            );
        }
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public List<Skill> getSkills(){
        return skills;
    }

    public Skill getSkillByName(String skillName){
        for(Skill skill : skills){
            if(skill.getName().equalsIgnoreCase(skillName)){
                return skill;
            }
        }
        return null;
    }

    public void takeDamage(int damage){
        validateNonNegative(damage, "Damage");
        currentHp -= damage;
        if(currentHp < 0){
            currentHp = 0;
        }
    }

    public void heal(int amount){
        validateNonNegative(amount, "Healing");
        currentHp += Math.min(amount, maxHp - currentHp);
    }

    public boolean isAlive(){
        return currentHp > 0;
    }

    public boolean hasEnoughmana(int manaCost){
        validateNonNegative(manaCost, "Mana cost");
        return currentMana >= manaCost;
    }

    public void useMana(int amount){
        if(!hasEnoughmana(amount)){
            throw new IllegalArgumentException("Not Enough Mana");
        }

        currentMana -= amount;
    }

    public String getName(){
        return name;
    }

    public int getCurrentHp(){
        return currentHp;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public int getCurrentMana(){
        return currentMana;
    }

    public int getMaxMana(){
        return maxMana;
    }

    public int getAttack(){
        return attack;
    }

    public int getDefense(){
        return defense;
    }

    public int getSpeed(){
        return speed;
    }


}