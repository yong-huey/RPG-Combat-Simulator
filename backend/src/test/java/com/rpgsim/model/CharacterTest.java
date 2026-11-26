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
        Character warrior = new Warrior("Authur");

        warrior.takeDamage(30);

        assertEquals(120, warrior.getCurrentHp());
    }

    @Test // Check no negative health
    void shouldNotHaveNegativeHealth(){
        Character warrior = new Warrior("Authur");

        warrior.takeDamage(500);

        assertEquals(0, warrior.getCurrentHp());
    }

    @Test // Check defeated
    void shouldBeDefeatedWhenHealthReachesZero(){
        Character warrior = new Warrior("Authur");

        warrior.takeDamage(500);

        assertFalse(warrior.isAlive());
    }

    @Test // Check healing
    void shouldRestoreHealthWhenHealed(){
        Character warrior = new Warrior("Arthur");

        warrior.takeDamage(50);
        warrior.heal(30);

        assertEquals(130, warrior.getCurrentHp());
    }

    @Test // Check heal does not exceed max health
    void shouldNotExceedMaximumHealth(){
        Character warrior = new Warrior("Authur");

        warrior.takeDamage(20);
        warrior.heal(100);

        assertEquals(warrior.getMaxHp(), warrior.getCurrentHp());
    }

    @Test // Check for mana reduction when used
    void shouldReduceManaWhenUsed(){
        Character mage = new Mage("Merlin");

        mage.useMana(20);

        assertEquals(100, mage.getCurrentMana());
    }

    @Test // Check insufficient mana
    void shouldThrowExceptionWhenManaIsInsufficient(){
        Character mage = new Mage("Merlin");

        assertThrows(IllegalArgumentException.class, () -> mage.useMana(200));

        assertEquals(120, mage.getCurrentMana());
    }
}
