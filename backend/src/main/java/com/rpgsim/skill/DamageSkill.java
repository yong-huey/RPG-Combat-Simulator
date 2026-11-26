package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class DamageSkill extends AbstractSkill{

    private final int baseDamage;

    public DamageSkill(String name, int manaCost, int baseDamage){
        super(name, manaCost);

        if(baseDamage < 0){
            throw new IllegalArgumentException("Base damage cannot be negative.");
        }

        this.baseDamage = baseDamage;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.ENEMY;
    }

    @Override
    public SkillResult execute(Character caster, Character target){
        if(!caster.hasEnoughmana(getManaCost())){
            return new SkillResult(false, caster.getName() + " does not have enough mana!");
        }

        caster.useMana(getManaCost());

        int damage = baseDamage + caster.getAttack() - target.getDefense();

        if(damage < 1){
            damage = 1;
        }

        target.takeDamage(damage);

        return new SkillResult(
                true, 
                caster.getName() + " used " + getName() + " and dealt "
                + damage + " damage to " + target.getName()
            );
    }
}
