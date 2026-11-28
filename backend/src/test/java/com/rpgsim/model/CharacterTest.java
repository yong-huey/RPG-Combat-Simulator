package com.rpgsim.model;

import java.util.List;

import com.rpgsim.skill.Skill;
import com.rpgsim.skill.DamageSkill;
import com.rpgsim.fixture.TestCharacter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    @Test // Check starting health
    void shouldStartWithFullHealth(){
        Character character = new TestCharacter("Test Character");

        assertEquals(character.getMaxHp(), character.getCurrentHp());
    }

    @Test // Check damage taken
    void shouldReduceHealthWhenTakingDamage(){
        // Arrange
        Character character = new TestCharacter("Test Character");
        int initialHp = character.getCurrentHp();
        int damage = 30;
        int expectedHp = initialHp - damage;

        // Act
        character.takeDamage(damage);

        // Assert
        assertEquals(expectedHp, character.getCurrentHp());
    }

    @Test // Check no negative health
    void shouldNotHaveNegativeHealth(){
        Character character = new TestCharacter("Test Character");
        int lethalDamage = character.getCurrentHp() + 1;

        character.takeDamage(lethalDamage);

        assertEquals(0, character.getCurrentHp());
    }

    @Test // Check defeated
    void shouldBeDefeatedWhenHealthReachesZero(){
        Character character = new TestCharacter("Test Character");
        int lethalDamage = character.getCurrentHp() + 1;

        character.takeDamage(lethalDamage);

        assertFalse(character.isAlive());
    }

    @Test // Check healing
    void shouldRestoreHealthWhenHealed(){
        Character character = new TestCharacter("Test Character");
        int damage = 50;
        int healing = 30;
        int expectedHp = character.getCurrentHp() - damage + healing;

        character.takeDamage(damage);
        character.heal(healing);

        assertEquals(expectedHp, character.getCurrentHp());
    }

    @Test // Check heal does not exceed max health
    void shouldNotExceedMaximumHealth(){
        Character character = new TestCharacter("Test Character");
        int damage = 20;
        int healing = 100;

        character.takeDamage(damage);
        character.heal(healing);

        assertEquals(character.getMaxHp(), character.getCurrentHp());
    }

    @Test // Check for mana reduction when used
    void shouldReduceManaWhenUsed(){
        Character character = new TestCharacter("Test Character");
        int manaCost = 20;
        int expectedMana = character.getCurrentMana() - manaCost;

        character.useMana(manaCost);

        assertEquals(expectedMana, character.getCurrentMana());
    }

    @Test // Check insufficient mana
    void shouldThrowExceptionWhenManaIsInsufficient(){
        Character character = new TestCharacter("Test Character");
        int initialMana = character.getCurrentMana();
        int manaCost = initialMana + 1;

        assertThrows(IllegalArgumentException.class, () -> character.useMana(manaCost));

        assertEquals(initialMana, character.getCurrentMana());
    }

    @Test // Check to reject negative damage
    void shouldRejectNegativeDamage(){
        Character character = new TestCharacter("Test Character");

        int initialHp = character.getCurrentHp();
        int invalidDamage = -30;

        assertThrows(IllegalArgumentException.class, () -> character.takeDamage(invalidDamage));

        assertEquals(initialHp, character.getCurrentHp());
    }

    @Test // Check to reject negative heal
    void shouldRejectNegativeHealing(){
        Character character = new TestCharacter("Test Character");

        int initialHp = character.getCurrentHp();
        int invalidHeal = -30;

        assertThrows(IllegalArgumentException.class, () -> character.heal(invalidHeal));

        assertEquals(initialHp, character.getCurrentHp());
    }

    @Test // Check to reject negative mana consumption
    void shouldRejectNegativeManaConsumption(){
        Character character = new TestCharacter("Test Character");

        int initialMana = character.getCurrentMana();
        int invalidMana = -20;

        assertThrows(IllegalArgumentException.class, () -> character.useMana(invalidMana));

        assertEquals(initialMana, character.getCurrentMana());
    }

    @Test // Check to reject negative mana requirement
    void shouldRejectNegativeManaRequirement(){
        Character character = new TestCharacter("Test Character");

        int invalidManaCost = -20;

        assertThrows(IllegalArgumentException.class, () -> character.hasEnoughMana(invalidManaCost));
    }

    @Test // Check when zero damage dealt, maintain health
    void shouldMaintainHealthWhenDamageIsZero(){
        Character character = new TestCharacter("Test Character");

        int initialHp = character.getCurrentHp();

        character.takeDamage(0);

        assertEquals(initialHp, character.getCurrentHp());
    }

    @Test // Check when zero heal done, maintain health
    void shouldMaintainHealthWhenHealIsZero(){
        Character character = new TestCharacter("Test Character");

        int damage = character.getCurrentHp() / 2;
        character.takeDamage(damage);

        int initialHp = character.getCurrentHp();

        character.heal(0);

        assertEquals(initialHp, character.getCurrentHp());
    }

    @Test // Check when zero mana consume, mana maintain
    void shouldMaintainManaWhenCostIsZero(){
        Character character = new TestCharacter("Test Character");

        int initialMana = character.getCurrentMana();

        character.useMana(0);

        assertEquals(initialMana, character.getCurrentMana());
    }

    @Test // Check character still can use zero mana skills
    void shouldHaveEnoughManaForZeroCost(){
        Character character = new TestCharacter("Test Character");
        
        character.useMana(character.getCurrentMana());

        boolean hasEnoughMana = character.hasEnoughMana(0);

        assertTrue(hasEnoughMana);
    }

    @Test // Check for integer overflow for healing
    void shouldPreventIntegerOverflowWhenHealing(){
        Character character = new TestCharacter("Test Character");

        int damage = character.getMaxHp() / 2;
        character.takeDamage(damage);

        int healingAmount = Integer.MAX_VALUE;
        int expectedHp = character.getMaxHp();

        character.heal(healingAmount);

        assertEquals(expectedHp, character.getCurrentHp());
    }

    @Test // Check to not allow adding skills using getters
    void shouldPreventExternalSkillAddition(){
        Character mage = new Mage("Merlin");

        int initialSkillCount = mage.getSkills().size();

        int manaCost = 0;
        int baseDamage = 20;

        Skill newSkill = new DamageSkill("External Skill", manaCost, baseDamage);

        List<Skill> skills = mage.getSkills();

        assertThrows(UnsupportedOperationException.class, () -> skills.add(newSkill));

        assertEquals(initialSkillCount, mage.getSkills().size());
    }

    @Test // Check to not allow remove skills using getters
    void shouldPreventExternalSkillRemoval(){
        Character mage = new Mage("Merlin");

        List<Skill> skills = mage.getSkills();

        int initialSkillCount = skills.size();

        Skill existingSkill = skills.get(0);

        assertThrows(UnsupportedOperationException.class, () -> skills.remove(existingSkill));

        assertEquals(initialSkillCount, mage.getSkills().size());
    }

    @Test // Check to not allow clearing skills through getters
    void shouldPreventExternalSkillClearing(){
        Character mage = new Mage("Merlin");

        List<Skill> skills = mage.getSkills();

        int initialSkillCount = skills.size();

        assertThrows(UnsupportedOperationException.class, () -> skills.clear());

        assertEquals(initialSkillCount, mage.getSkills().size());
    }

    @Test // Check to allow adding skills through normal method
    void shouldAllowSkillAdditionThroughAddSkill(){
        Character mage = new Mage("Merlin");

        int initialSkillCount = mage.getSkills().size();
        int additionalSkillCount = 1;

        int manaCost = 0;
        int baseDamage = 20;

        Skill newSkill = new DamageSkill("New Skill", manaCost, baseDamage);

        int expectedSkillCount = initialSkillCount + additionalSkillCount;

        mage.addSkill(newSkill);

        assertEquals(expectedSkillCount, mage.getSkills().size());
        assertTrue(mage.getSkills().contains(newSkill));
    }

    @Test // Check previous retrieved remains
    void shouldReturnIndependentSkillSnapshot(){
        Character mage = new Mage("Merlin");

        List<Skill> originalSkills = mage.getSkills();

        int originalSkillCount = originalSkills.size();

        int manaCost = 0;
        int baseDamage = 20;

        Skill newSkill = new DamageSkill("New Skill", manaCost, baseDamage);

        mage.addSkill(newSkill);

        assertEquals(originalSkillCount, originalSkills.size());

        assertFalse(originalSkills.contains(newSkill));
        assertTrue(mage.getSkills().contains(newSkill));
    }

    @Test // Check revive character
    void shouldNotReviveDefeatedCharacterThroughHealing(){
        Character character = new TestCharacter("Test Character");
        
        int lethalDamage = character.getCurrentHp();
        character.takeDamage(lethalDamage);

        int healAmount = character.getMaxHp();
        
        character.heal(healAmount);

        assertEquals(0, character.getCurrentHp());
        assertFalse(character.isAlive());
    }
}
