package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;

public class HealSkill extends AbstractSkill{
    private final int healAmount;

    public HealSkill(String name, int manaCost, int healAmount){
        super(name, manaCost);

        if(healAmount < 0){
            throw new IllegalArgumentException("Healing amount cannot be negative.");
        }

        this.healAmount = healAmount;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SELF;
    }

    @Override 
    public SkillResult execute(Character caster, Character target){
        if(!caster.hasEnoughmana(getManaCost())){
            return new SkillResult(false, caster.getName() + " does not have enough mana!");
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
