package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class DamageSkill extends AbstractSkill{

    private final int baseDamage;

    public DamageSkill(String name, int manaCost, int baseDamage){
        this(name, manaCost, baseDamage, TargetType.ENEMY);
    }

    public DamageSkill(String name, int manaCost, int baseDamage, TargetType targetType){
        super(name, manaCost, targetType);

        if(baseDamage < 0){
            throw new IllegalArgumentException("Base damage cannot be negative.");
        }

        this.baseDamage = baseDamage;
    }

    @Override
    public SkillResult execute(Character caster, Character target){
        SkillResult validation = validateExecution(caster, target);
        if(!validation.isSuccess()){
            return validation;
        }

        caster.useMana(getManaCost());

        long calculateDamage = (long) baseDamage + caster.getAttack() - target.getDefense();

        if(calculateDamage < 1){
            calculateDamage = 1;
        }

        if(calculateDamage > Integer.MAX_VALUE){
            calculateDamage = Integer.MAX_VALUE;
        }
        
        int damage = (int) calculateDamage;
        int initialHp = target.getCurrentHp();
        target.takeDamage(damage);
        int actualDamage = initialHp - target.getCurrentHp();

        return new SkillResult(
                true, 
                caster.getName() + " used " + getName() + " and dealt "
                + actualDamage + " damage to " + target.getName()
            );
    }
}
