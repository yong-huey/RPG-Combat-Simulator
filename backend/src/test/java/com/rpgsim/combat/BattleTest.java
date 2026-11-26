package com.rpgsim.combat;

import com.rpgsim.model.Character;
import com.rpgsim.model.Warrior;
import com.rpgsim.model.Mage;
import com.rpgsim.model.Archer;
import com.rpgsim.skill.Skill;
import com.rpgsim.skill.DamageSkill;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BattleTest {
    private static class TestCharacter extends Character{
        private static final int TEST_MAX_HP = 100;
        private static final int TEST_MAX_MANA = 100;
        private static final int TEST_ATTACK = 20;
        private static final int TEST_DEFENSE = 10;
        TestCharacter(String name, int speed){
            super(name, TEST_MAX_HP, TEST_MAX_MANA, TEST_ATTACK, TEST_DEFENSE, speed);
        }
    }

    @Test // Check if start and no winner
    void shouldStartWithActiveBattleAndNoWinner(){
        // Arrange
        int playerOneSpeed = 10;
        int playerTwoSpeed = 20;

        Character playerOne = new TestCharacter("Player One", playerOneSpeed);
        Character playerTwo = new TestCharacter("Player Two", playerTwoSpeed);

        // Act
        Battle battle = new Battle(playerOne, playerTwo);

        // Assert
        assertFalse(battle.isBattleOver());
        assertNull(battle.getWinner());
    }
}
