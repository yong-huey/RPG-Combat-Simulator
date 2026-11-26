package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.model.Mage;
import com.rpgsim.enums.TargetType;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealSkillTest {
    @Test // Check correct healing amount
    void shouldRestoreCorrectAmountOfHealth(){
        // Arrange
        Character caster = new Mage("Merlin");

        int damage = caster.getMaxHp() / 2;
        int healAmount = damage / 2;
        int manaCost = caster.getCurrentMana() / 4;

        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();
        int expectedHp = initialHp + healAmount;

        Skill skill = new HealSkill("Test Heal", manaCost, healAmount);

        // Act
        skill.execute(caster, caster);

        // Assert
        assertEquals(expectedHp, caster.getCurrentHp());
    }

    @Test // Check for correct mana consumption
    void shouldConsumeCorrectMana(){
        Character caster = new Mage("Merlin");

        int initialMana = caster.getCurrentMana();
        int manaCost = initialMana / 4;
        int healAmount = caster.getMaxHp() / 4;

        caster.takeDamage(healAmount);

        int expectedMana = initialMana - manaCost;

        Skill skill = new HealSkill("Test Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(expectedMana, caster.getCurrentMana());
    }

    @Test // Check heal dont exceed max health
    void shouldNotHealBeyondMaximumHealth(){
        Character caster = new Mage("Merlin");

        int damage = caster.getMaxHp() / 4;
        int healAmount = damage * 2;
        int manaCost = caster.getCurrentMana() / 4;
        int expectedHp = caster.getMaxHp();

        caster.takeDamage(damage);

        Skill skill = new HealSkill("Overheal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(expectedHp, caster.getCurrentHp());
    }

    @Test // Check heal at full health should not increase health
    void shouldMaintainHealthWhenAlreadyFull(){
        Character caster = new Mage("Merlin");

        int initialHp = caster.getCurrentHp();
        int healAmount = caster.getMaxHp() / 4;
        int manaCost = caster.getCurrentMana() / 4;

        Skill skill = new HealSkill("Test Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(initialHp, caster.getCurrentHp());
    }

    @Test // Check if insufficient mana cannot heal
    void shouldNotHealWhenManaIsInsufficient(){
        Character caster = new Mage("Merlin");

        int additonalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additonalManaRequired;

        int damage = caster.getMaxHp() / 2;
        int healAmount = damage / 2;

        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();

        Skill skill = new HealSkill("Expensive Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(initialHp, caster.getCurrentHp());
    }

    @Test // Check insufficent mana should preserve mana
    void shouldNotConsumeManaWhenManaIsInsufficient(){
        Character caster = new Mage("Merlin");

        int initialMana = caster.getCurrentMana();
        int additonalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additonalManaRequired;

        int healAmount = caster.getMaxHp() / 4;

        caster.takeDamage(healAmount);

        Skill skill = new HealSkill("Expensive Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check correct target type
    void shouldTargetSelf(){
        int manaCost = 15;
        int healAmount = 30;

        Skill skill = new HealSkill("Test Heal", manaCost, healAmount);

        TargetType actualTargetType = skill.getTargetType();

        assertEquals(TargetType.SELF, actualTargetType);
    }

    @Test // Check zero mana heal can be done
    void shouldExecuteZeroCostHealWithoutMana(){
        Character caster = new Mage("Merlin");

        caster.useMana(caster.getCurrentMana());

        int damage = caster.getMaxHp() / 2;
        int healAmount = damage / 2;
        int manaCost = 0;

        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();
        int expectedHp = initialHp + healAmount;

        Skill skill = new HealSkill("Free Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(expectedHp, caster.getCurrentHp());
        assertEquals(0, caster.getCurrentMana());
    }

    @Test // Check to reject negative mana cost
    void shouldRejectNegativeManaCost(){
        String skillName = "Test Heal Skill";
        int invalidManaCost = -10;
        int healAmount = 20;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(skillName, invalidManaCost, healAmount));
    }

    @Test // Check to reject negative healing
    void shouldRejectNegativeHealingAmount(){
        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int invalidHealAmount = -20;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(skillName, manaCost, invalidHealAmount));
    }

    @Test // Check to reject null skill name
    void shouldRejectNullSkillName(){
        String invalidName = null;
        int manaCost = 10;
        int healAmount = 20;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(invalidName, manaCost, healAmount));
    }

    @Test // Check to reject empty skill name
    void shouldRejectEmptySkillName(){
        String invalidName = "";
        int manaCost = 10;
        int healAmount = 20;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(invalidName, manaCost, healAmount));
    }

    @Test // Check to reject blank skill name
    void shouldRejectBlankSkillName(){
        String invalidName = "   ";
        int manaCost = 10;
        int healAmount = 20;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(invalidName, manaCost, healAmount));
    }

    @Test // Check to allow zero mana cost
    void shouldAllowZeroManaCost(){
        String skillName = "Test Heal Skill";
        int manaCost = 0;
        int healAmount = 20;

        Skill skill = assertDoesNotThrow(() -> new HealSkill(skillName, manaCost, healAmount));

        assertEquals(manaCost, skill.getManaCost());
    }

    @Test // Check to allow zero heal
    void shouldAllowZeroHealingAmount(){
        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 0;

        assertDoesNotThrow(() -> new HealSkill(skillName, manaCost, healAmount));
    }
}
