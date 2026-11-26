package com.rpgsim.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    @Test // Check starting health
    void shouldStartWithFullHealth(){
        Character warrior = new Warrior("Authur");

        assertEquals(warrior.getMaxHp(), warrior.getCurrentHp());
    }

    @Test // Check damage taken
    void shouldReduceHealthWhenTakingDamage(){
        // Arrange
        Character warrior = new Warrior("Authur");
        int damage = 30;
        int expectedHp = warrior.getCurrentHp() - damage;

        // Act
        warrior.takeDamage(damage);

        // Assert
        assertEquals(expectedHp, warrior.getCurrentHp());
    }

    @Test // Check no negative health
    void shouldNotHaveNegativeHealth(){
        Character warrior = new Warrior("Authur");
        int lethalDamage = warrior.getCurrentHp() + 1;

        warrior.takeDamage(lethalDamage);

        assertEquals(0, warrior.getCurrentHp());
    }

    @Test // Check defeated
    void shouldBeDefeatedWhenHealthReachesZero(){
        Character warrior = new Warrior("Authur");
        int lethalDamage = warrior.getCurrentHp() + 1;

        warrior.takeDamage(lethalDamage);

        assertFalse(warrior.isAlive());
    }

    @Test // Check healing
    void shouldRestoreHealthWhenHealed(){
        Character warrior = new Warrior("Arthur");
        int damage = 50;
        int healing = 30;
        int expectedHp = warrior.getCurrentHp() - damage + healing;

        warrior.takeDamage(damage);
        warrior.heal(healing);

        assertEquals(expectedHp, warrior.getCurrentHp());
    }

    @Test // Check heal does not exceed max health
    void shouldNotExceedMaximumHealth(){
        Character warrior = new Warrior("Authur");
        int damage = 20;
        int healing = 100;

        warrior.takeDamage(damage);
        warrior.heal(healing);

        assertEquals(warrior.getMaxHp(), warrior.getCurrentHp());
    }

    @Test // Check for mana reduction when used
    void shouldReduceManaWhenUsed(){
        Character mage = new Mage("Merlin");
        int manaCost = 20;
        int expectedMana = mage.getCurrentMana() - manaCost;

        mage.useMana(manaCost);

        assertEquals(expectedMana, mage.getCurrentMana());
    }

    @Test // Check insufficient mana
    void shouldThrowExceptionWhenManaIsInsufficient(){
        Character mage = new Mage("Merlin");
        int initialMana = mage.getCurrentMana();
        int manaCost = initialMana + 1;

        assertThrows(IllegalArgumentException.class, () -> mage.useMana(manaCost));

        assertEquals(initialMana, mage.getCurrentMana());
    }

    @Test // Check to reject negative damage
    void shouldRejectNegativeDamage(){
        Character warrior = new Warrior("Arthur");

        int initialHp = warrior.getCurrentHp();
        int invalidDamage = -30;

        assertThrows(IllegalArgumentException.class, () -> warrior.takeDamage(invalidDamage));

        assertEquals(initialHp, warrior.getCurrentHp());
    }

    @Test // Check to reject negative heal
    void shouldRejectNegativeHealing(){
        Character mage = new Mage("Merlin");

        int initialHp = mage.getCurrentHp();
        int invalidHeal = -30;

        assertThrows(IllegalArgumentException.class, () -> mage.heal(invalidHeal));

        assertEquals(initialHp, mage.getCurrentHp());
    }

    @Test // Check to reject negative mana consumption
    void shouldRejectNegativeManaConsumption(){
        Character mage = new Mage("Merlin");

        int initialMana = mage.getCurrentMana();
        int invalidMana = -20;

        assertThrows(IllegalArgumentException.class, () -> mage.useMana(invalidMana));

        assertEquals(initialMana, mage.getCurrentMana());
    }

    @Test // Check to reject negative mana requirement
    void shouldRejectNegativeManaRequirement(){
        Character mage = new Mage("Merlin");

        int invalidManaCost = -20;

        assertThrows(IllegalArgumentException.class, () -> mage.hasEnoughmana(invalidManaCost));
    }

    @Test // Check when zero damage dealt, maintain health
    void shouldMaintainHealthWhenDamageIsZero(){
        Character warrior = new Warrior("Arthur");

        int initialHp = warrior.getCurrentHp();

        warrior.takeDamage(0);

        assertEquals(initialHp, warrior.getCurrentHp());
    }

    @Test // Check when zero heal done, maintain health
    void shouldMaintainHealthWhenHealIsZero(){
        Character mage = new Mage("Merlin");

        int damage = mage.getCurrentHp() / 2;
        mage.takeDamage(damage);

        int initialHp = mage.getCurrentHp();

        mage.heal(0);

        assertEquals(initialHp, mage.getCurrentHp());
    }

    @Test // Check when zero mana consume, mana maintain
    void shouldMaintainManaWhenCostIsZero(){
        Character mage = new Mage("Merlin");

        int initialMana = mage.getCurrentMana();

        mage.useMana(0);

        assertEquals(initialMana, mage.getCurrentMana());
    }

    @Test // Check character still can use zero mana skills
    void shouldHaveEnoughManaForZeroCost(){
        Character mage = new Mage("Merlin");
        
        mage.useMana(mage.getCurrentMana());

        boolean hasEnoughMana = mage.hasEnoughmana(0);

        assertTrue(hasEnoughMana);
    }

    @Test // Check for integer overflow for healing
    void shouldPreventIntegerOverflowWhenHealing(){
        Character mage = new Mage("Merlin");

        int damage = mage.getMaxHp() / 2;
        mage.takeDamage(damage);

        int healingAmount = Integer.MAX_VALUE;
        int expectedHp = mage.getMaxHp();

        mage.heal(healingAmount);

        assertEquals(expectedHp, mage.getCurrentHp());
    }
}
