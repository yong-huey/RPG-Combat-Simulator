package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class DamageSkill implements Skill{

    private final String name;
    private final int manaCost;
    private final int baseDamage;

    public DamageSkill(String name, int manaCost, int baseDamage){
        this.name = name;
        this.manaCost = manaCost;
        this.baseDamage = baseDamage;
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
        return TargetType.ENEMY;
    }

    @Override
    public String execute(Character caster, Character target){
        if(!caster.hasEnoughmana(manaCost)){
            return caster.getName() + " does not have enough mana.";
        }

        caster.useMana(manaCost);

        int damage = baseDamage + caster.getAttack() - target.getDefense();

        if(damage < 1){
            damage = 1;
        }

        target.takeDamage(damage);

        return caster.getName()
                + " used "
                + name
                + " on "
                + target.getName()
                + " for "
                + damage
                + " damage.";
    }
}
