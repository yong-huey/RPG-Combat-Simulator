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
}
