package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class HealSkill extends AbstractSkill{
    private final int healAmount;

    public HealSkill(String name, int manaCost, int healAmount){
        this(name, manaCost, healAmount, TargetType.SELF);
    }

    public HealSkill(String name, int manaCost, int healAmount, TargetType targetType){
        super(name, manaCost, targetType);

        if(healAmount < 0){
            throw new IllegalArgumentException("Healing amount cannot be negative.");
        }

        this.healAmount = healAmount;
    }

    @Override 
    public SkillResult execute(Character caster, Character target){
        SkillResult validation = validateExecution(caster, target);
        if(!validation.isSuccess()){
            return validation;
        }

        caster.useMana(getManaCost());
        int initialHp = target.getCurrentHp();
        target.heal(healAmount);
        int actualHealing = target.getCurrentHp() - initialHp;

        return new SkillResult(
                true, caster.getName() + " used " + getName() + " and healed "
                + target.getName() + " for " + actualHealing + " HP."
            );
    }
}
