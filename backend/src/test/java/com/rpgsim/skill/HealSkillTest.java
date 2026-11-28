package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;
import com.rpgsim.fixture.TestCharacter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealSkillTest {
    @Test // Check correct healing amount
    void shouldRestoreCorrectAmountOfHealth(){
        // Arrange
        Character caster = new TestCharacter("Caster");

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
        Character caster = new TestCharacter("Caster");

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
        Character caster = new TestCharacter("Caster");

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
        Character caster = new TestCharacter("Caster");

        int initialHp = caster.getCurrentHp();
        int healAmount = caster.getMaxHp() / 4;
        int manaCost = caster.getCurrentMana() / 4;

        Skill skill = new HealSkill("Test Heal", manaCost, healAmount);

        skill.execute(caster, caster);

        assertEquals(initialHp, caster.getCurrentHp());
    }

    @Test // Check if insufficient mana cannot heal
    void shouldNotHealWhenManaIsInsufficient(){
        Character caster = new TestCharacter("Caster");

        int additionalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additionalManaRequired;

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
        Character caster = new TestCharacter("Caster");

        int initialMana = caster.getCurrentMana();
        int additionalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additionalManaRequired;

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
        Character caster = new TestCharacter("Caster");

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

    @Test // Check to report actual heal
    void shouldReportActualHealingWhenOverhealing(){
        Character caster = new TestCharacter("Caster");

        int damage = caster.getCurrentHp() / 2;
        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();

        String skillName = "Overheal Skill";
        int manaCost = 0;
        int healAmount = caster.getMaxHp();

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertTrue(result.isSuccess());
        assertEquals(caster.getMaxHp(), caster.getCurrentHp());

        int actualHealing = caster.getCurrentHp() - initialHp;

        assertTrue(result.getMessage().contains(" " + actualHealing + " HP"));
    }

    @Test // Check to report no healing when full health
    void shouldReportZeroHealingWhenAtFullHealth(){
        Character caster = new TestCharacter("Caster");
        
        int initialHp = caster.getCurrentHp();
        int manaCost = caster.getCurrentMana();
        int healAmount = caster.getMaxHp();

        String skillName = "Test Heal Skill";

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertTrue(result.isSuccess());
        assertEquals(initialHp, caster.getCurrentHp());
        assertEquals(0, caster.getCurrentMana());

        int expectedHealing = 0;

        assertTrue(result.getMessage().contains(" " + expectedHealing + " HP"));
    }

    @Test // Check to allow zero healing
    void shouldMaintainHealthWhenExecutingZeroHealingSkill(){
        Character caster = new TestCharacter("Caster");

        int damage = caster.getCurrentHp() / 2;
        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();
        int initialMana = caster.getCurrentMana();

        String skillName = "Test Heal Skill";
        int manaCost = 0;
        int healAmount = 0;
        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertTrue(result.isSuccess());
        assertEquals(initialHp, caster.getCurrentHp());
        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check successful healing skill result
    void shouldReturnSuccessWhenHealSkillExecuted(){
        Character caster = new TestCharacter("Caster");
        int damage = caster.getCurrentHp() / 2;
        caster.takeDamage(damage);

        String skillName = "Test Heal Skill";
        int manaCost = caster.getCurrentMana() / 2;
        int healAmount = damage;

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertTrue(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());
    }

    @Test // Check failed healing skill result
    void shouldReturnFailureWhenManaIsInsufficient(){
        Character caster = new TestCharacter("Caster");
        int additionalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additionalManaRequired;
        int healAmount = 20;
        String skillName = "Expensive Heal Skill";

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());
    }

    @Test // Check defeated caster cannot heal
    void shouldRejectHealingSkillFromDefeatedCaster(){
        Character caster = new TestCharacter("Caster");

        int lethalDamage = caster.getCurrentHp();
        caster.takeDamage(lethalDamage);

        int initialMana = caster.getCurrentMana();

        String skillName = "Test Heal Skill";
        int manaCost = initialMana / 2;
        int healAmount = caster.getMaxHp();
        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = skill.execute(caster, caster);

        assertFalse(result.isSuccess());
        assertEquals(initialMana, caster.getCurrentMana());
        assertEquals(0, caster.getCurrentHp());
        assertFalse(caster.isAlive());
    }

    @Test // Check to reject null caster
    void shouldRejectNullCaster(){
        Character caster = null;
        Character target = new TestCharacter("Target");
        int damage = 30;
        target.takeDamage(damage);

        int initialHp = target.getCurrentHp();
        int initialMana = target.getCurrentMana();

        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 20;

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = assertDoesNotThrow(() -> skill.execute(caster, target));

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());

        assertEquals(initialHp, target.getCurrentHp());
        assertEquals(initialMana, target.getCurrentMana());
    }

    @Test // Check to reject null target
    void shouldRejectNullTarget(){
        Character caster = new TestCharacter("Caster");
        Character target = null;
        int damage = 30;
        caster.takeDamage(damage);

        int initialHp = caster.getCurrentHp();
        int initialMana = caster.getCurrentMana();

        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 20;

        Skill skill = new HealSkill(skillName, manaCost, healAmount);

        SkillResult result = assertDoesNotThrow(() -> skill.execute(caster, target));

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());

        assertEquals(initialHp, caster.getCurrentHp());
        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check to reject heal skill from targeting another character
    void shouldRejectSelfHealSkillTargetingAnotherCharacter(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 20;
        int damage = 30;

        Skill skill = new HealSkill(skillName, manaCost, healAmount, TargetType.SELF);

        caster.takeDamage(damage);
        target.takeDamage(damage);

        int initialCasterHp = caster.getCurrentHp();
        int initialCasterMana = caster.getCurrentMana();
        int initialTargetHp = target.getCurrentHp();
        int initialTargetMana = target.getCurrentMana();

        SkillResult result = skill.execute(caster, target);

        assertFalse(result.isSuccess());

        assertEquals(initialCasterHp, caster.getCurrentHp());
        assertEquals(initialCasterMana, caster.getCurrentMana());

        assertEquals(initialTargetHp, target.getCurrentHp());
        assertEquals(initialTargetMana, target.getCurrentMana());
    }

    @Test // Check configurable target types
    void shouldAllowConfigurableTargetTypes(){
        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 20;

        for(TargetType targetType : TargetType.values()){
            Skill skill = new HealSkill(skillName, manaCost, healAmount, targetType);

            // Assert
            assertEquals(targetType, skill.getTargetType());
        }
    }

    @Test // Check to reject null target type
    void shouldRejectNullTargetType(){
        String skillName = "Test Heal Skill";
        int manaCost = 10;
        int healAmount = 20;
        TargetType invalidTargetType = null;

        assertThrows(IllegalArgumentException.class, () -> new HealSkill(skillName, manaCost, healAmount, invalidTargetType));
    }
}
