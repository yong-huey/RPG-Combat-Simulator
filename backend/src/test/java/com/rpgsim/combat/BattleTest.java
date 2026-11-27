package com.rpgsim.combat;

import java.util.List;

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

    @Test // Check faster character starts first
    void shouldStartWithFasterCharacter(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character slower = new TestCharacter("Slower", slowerSpeed);
        Character faster = new TestCharacter("Faster", fasterSpeed);

        Battle battle = new Battle(slower, faster);

        assertSame(faster, battle.getCurrentTurn());
    }

    @Test // Check switch turn on successful attack
    void shouldSwitchTurnAfterSuccessfulAttack(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character attacker = new TestCharacter("Attacker", fasterSpeed);
        Character defender = new TestCharacter("Defenser", slowerSpeed);

        Battle battle = new Battle(attacker, defender);

        String skillName = "Basic Attack";

        battle.useSkill(skillName, defender);

        assertSame(defender, battle.getCurrentTurn());
    }

    @Test // Check if opponent is correct
    void shouldReturnOpponentOfCurrentCharacter(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character attacker = new TestCharacter("Attacker", fasterSpeed);
        Character defender = new TestCharacter("Defender", slowerSpeed);

        Battle battle = new Battle(attacker, defender);

        Character opponent = battle.getOpponent();

        assertSame(defender, opponent);
    }

    @Test // Check invalid skill should not switch turn
    void shouldNotSwitchTurnWhenSkillDoesNotExist(){
        Character mage = new Mage("Merlin");
        Character warrior = new Warrior("Arthur");

        Battle battle = new Battle(mage, warrior);

        Character initialTurn = battle.getCurrentTurn();
        Character opponent = battle.getOpponent();

        String invalidSkillName = "Nonexistent Skill";

        battle.useSkill(invalidSkillName, opponent);

        assertSame(initialTurn, battle.getCurrentTurn());
    }

    @Test // Check no self attack
    void shouldRejectEnemySkillTargetingSelf(){
        Character mage = new Mage("Merlin");
        Character warrior = new Warrior("Arthur");

        Battle battle = new Battle(mage, warrior);

        Character caster = battle.getCurrentTurn();

        int initialHp = caster.getCurrentHp();
        String skillName = "Basic Attack";

        battle.useSkill(skillName, caster);

        assertEquals(initialHp, caster.getCurrentHp());
        assertSame(caster, battle.getCurrentTurn());
    }

    @Test // Check no healing of enemy
    void shouldRejectSelfSkillTargetingEnemy(){
        Character mage = new Mage("Merlin");
        
        int slowerSpeed = mage.getSpeed() - 1;

        Character opponent = new TestCharacter("Opponent", slowerSpeed);

        int damage = opponent.getMaxHp() / 2;
        opponent.takeDamage(damage);

        int initialHp = opponent.getCurrentHp();
        int initialMana = mage.getCurrentMana();

        Battle battle = new Battle(mage, opponent);

        String skill = "Heal";

        battle.useSkill(skill, opponent);

        assertEquals(initialHp, opponent.getCurrentHp());
        assertEquals(initialMana, mage.getCurrentMana());
        assertSame(mage, battle.getCurrentTurn());
    }

    @Test // Check no damage to external characters
    void shouldRejectTargetOutsideBattle(){
        Character mage = new Mage("Merlin");
        Character warrior = new Warrior("Arthur");
        Character outsider = new Archer("Robin");

        Battle battle = new Battle(mage, warrior);

        Character initialTurn = battle.getCurrentTurn();

        int initialHp = outsider.getCurrentHp();
        String skillName = "Basic Attack";

        battle.useSkill(skillName, outsider);

        assertEquals(initialHp, outsider.getCurrentHp());
        assertSame(initialTurn, battle.getCurrentTurn());
    }

    @Test // Check battle end when one dies
    void shouldEndBattleWhenCharacterIsDefeated(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character attacker = new TestCharacter("Attacker", fasterSpeed);
        Character defender = new TestCharacter("Defender", slowerSpeed);

        int manaCost = 0;
        int baseDamage = defender.getMaxHp() + defender.getDefense();

        Skill lethalSkill = new DamageSkill("Lethal Skill", manaCost, baseDamage);

        attacker.addSkill(lethalSkill);

        Battle battle = new Battle(attacker, defender);

        battle.useSkill(lethalSkill.getName(), defender);

        assertFalse(defender.isAlive());
        assertTrue(battle.isBattleOver());
    }

    @Test // Check correct winner
    void shouldReturnSurvivingCharacterAsWinner(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character attacker = new TestCharacter("Attacker", fasterSpeed);
        Character defender = new TestCharacter("Defender", slowerSpeed);

        int manaCost = 0;
        int baseDamage = defender.getMaxHp() + defender.getDefense();

        Skill lethalSkill = new DamageSkill("Lethal Skill", manaCost, baseDamage);

        attacker.addSkill(lethalSkill);

        Battle battle = new Battle(attacker, defender);

        battle.useSkill(lethalSkill.getName(), defender);

        assertSame(attacker, battle.getWinner());
    }

    @Test // Check battle log does record
    void shouldRecordSuccessfulActionInBattleLog(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        int initialLogSize = battle.getBattleLog().size();

        Character opponent = battle.getOpponent();
        String skillName = "Basic Attack";

        battle.useSkill(skillName, opponent);

        assertTrue(battle.getBattleLog().size() > initialLogSize);
    }

    @Test // Check not allowed to use skill post battle
    void shouldNotAllowAttackAfterBattleEnds(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;
        
        Character attacker = new TestCharacter("Attacker", fasterSpeed);
        Character defender = new TestCharacter("Defender", slowerSpeed);

        int manaCost = 0;
        int baseDamage = defender.getCurrentHp() + defender.getDefense();

        Skill lethalSkill = new DamageSkill("Lethal Skill", manaCost, baseDamage);

        attacker.addSkill(lethalSkill);

        Battle battle = new Battle(attacker, defender);

        battle.useSkill(lethalSkill.getName(), defender);

        int attackerHp = attacker.getCurrentHp();
        int defenderHp = defender.getCurrentHp();

        String skillName = "Basic Attack";

        battle.useSkill(skillName, defender);
        battle.useSkill(skillName, attacker);

        assertEquals(attackerHp, attacker.getCurrentHp());
        assertEquals(defenderHp, defender.getCurrentHp());
        assertTrue(battle.isBattleOver());
        assertSame(attacker, battle.getWinner());
    }

    @Test // Check if insufficient mana, casting should not switch turns
    void shouldNotSwitchTurnWhenManaIsInsufficient(){
        int slowerSpeed = 10;
        int fasterSpeed = 20;

        Character caster = new TestCharacter("Caster", fasterSpeed);
        Character target = new TestCharacter("Target", slowerSpeed);

        int additionalMana = 1;
        int manaCost = caster.getCurrentMana() + additionalMana;

        int baseDamage = 35;

        Skill expensiveSkill = new DamageSkill("Expensive Skill", manaCost, baseDamage);

        caster.addSkill(expensiveSkill);

        Battle battle = new Battle(caster, target);

        int initialMana = caster.getCurrentMana();
        int initialHp = target.getCurrentHp();

        battle.useSkill(expensiveSkill.getName(), target);

        assertEquals(initialMana, caster.getCurrentMana());
        assertEquals(initialHp, target.getCurrentHp());
        assertSame(caster, battle.getCurrentTurn());
    }

    @Test // Check to not allow external logging
    void shouldPreventExternalBattleLogAddition(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        List<String> battleLog = battle.getBattleLog();

        int initialLogSize = battleLog.size();

        String fakeEntry = "Fake battle log";

        assertThrows(UnsupportedOperationException.class, () -> battleLog.add(fakeEntry));

        assertEquals(initialLogSize, battle.getBattleLog().size());
    }

    @Test // Check to not allow external clearing of log
    void shouldPreventExternalBattleLogClearing(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        String skillName = "Basic Attack";

        battle.useSkill(skillName, battle.getOpponent());

        List<String> battleLog = battle.getBattleLog();

        int initialLogSize = battleLog.size();

        assertFalse(battleLog.isEmpty());

        assertThrows(UnsupportedOperationException.class, () -> battleLog.clear());

        assertEquals(initialLogSize, battle.getBattleLog().size());
    }

    @Test // Check previous retrieved remains
    void shouldReturnIndependentBattleLogSnapshot(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        List<String> originalLog = battle.getBattleLog();
        int originalLogSize = originalLog.size();

        String skillName = "Basic Attack";

        battle.useSkill(skillName, battle.getOpponent());

        assertEquals(originalLogSize, originalLog.size());
        assertTrue(battle.getBattleLog().size() > originalLogSize);
    }

    @Test // Check same speed player 1 to start first
    void shouldAllowPlayerOneToStartWhenSpeedAreEqual(){
        int sharedSpeed = 10;
        Character player1 = new TestCharacter("Player 1", sharedSpeed);
        Character player2 = new TestCharacter("Player 2", sharedSpeed);

        Battle battle = new Battle(player1, player2);

        assertSame(player1, battle.getCurrentTurn());
    }

    @Test // Check null player 1
    void shouldRejectNullPlayerOne(){
        Character player1 = null;
        Character player2 = new Mage("Merlin");

        assertThrows(IllegalArgumentException.class, () -> new Battle(player1, player2));
    }

    @Test // Check null player 2
    void shouldRejectNullPlayerTwo(){
        Character player1 = new Mage("Merlin");
        Character player2 = null;

        assertThrows(IllegalArgumentException.class, () -> new Battle(player1, player2));
    }

    @Test // Check no fight against self
    void shouldRejectSameCharacterAsBothPlayer(){
        Character warrior = new Warrior("Arthur");

        assertThrows(IllegalArgumentException.class, () -> new Battle(warrior, warrior));
    }

    @Test // Check allow same class
    void shouldAllowDifferentCharactersOfSameClass(){
        Character player1 = new Warrior("Arthur");
        Character player2 = new Warrior("Lancelot");

        Battle battle = assertDoesNotThrow(() -> new Battle(player1, player2));

        assertFalse(battle.isBattleOver());
        assertNotSame(player1, player2);
    }

    @Test // Check to reject player 1 die before battle
    void shouldRejectDefeatedPlayerOne(){
        Character player1 = new Warrior("Arthur");
        Character player2 = new Mage("Merlin");

        int lethalDamage = player1.getCurrentHp();

        player1.takeDamage(lethalDamage);

        assertThrows(IllegalArgumentException.class, () -> new Battle(player1, player2));
    } 

    @Test // Check to reject player 2 die before battle
    void shouldRejectDefeatedPlayerTwo(){
        Character player1 = new Warrior("Arthur");
        Character player2 = new Mage("Merlin");

        int lethalDamage = player2.getCurrentHp();

        player2.takeDamage(lethalDamage);

        assertThrows(IllegalArgumentException.class, () -> new Battle(player1, player2));
    } 

    @Test // Check to reject null target
    void shouldRejectNullSkillTarget(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        Character currentPlayer = battle.getCurrentTurn();
        Character opponent = battle.getOpponent();

        int initialMana = currentPlayer.getCurrentMana();
        int initialHp = currentPlayer.getCurrentHp();
        int opponentHp = opponent.getCurrentHp();
        int initialLogSize = battle.getBattleLog().size();

        String skillName = "Basic Attack";
        Character invalidTarget = null;

        assertDoesNotThrow(() -> battle.useSkill(skillName, invalidTarget));

        assertSame(currentPlayer, battle.getCurrentTurn());
        assertEquals(initialMana, currentPlayer.getCurrentMana());
        assertEquals(initialHp, currentPlayer.getCurrentHp());
        assertEquals(opponentHp, opponent.getCurrentHp());

        assertTrue(battle.getBattleLog().size() > initialLogSize);
    }

    @Test // Check to reject null skill name
    void shouldRejectNullSkillName(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        Character currentPlayer = battle.getCurrentTurn();
        Character opponent = battle.getOpponent();

        int initialMana = currentPlayer.getCurrentMana();
        int initialHp = currentPlayer.getCurrentHp();
        int opponentHp = opponent.getCurrentHp();
        int initialLogSize = battle.getBattleLog().size();

        String invalidSkillName = null;

        assertDoesNotThrow(() -> battle.useSkill(invalidSkillName, opponent));

        assertSame(currentPlayer, battle.getCurrentTurn());
        assertEquals(initialMana, currentPlayer.getCurrentMana());
        assertEquals(initialHp, currentPlayer.getCurrentHp());
        assertEquals(opponentHp, opponent.getCurrentHp());

        assertTrue(battle.getBattleLog().size() > initialLogSize);
    }

    @Test // Check to reject blank skill name
    void shouldRejectBlankSkillName(){
        Character warrior = new Warrior("Arthur");
        Character mage = new Mage("Merlin");

        Battle battle = new Battle(warrior, mage);

        Character currentPlayer = battle.getCurrentTurn();
        Character opponent = battle.getOpponent();

        int initialMana = currentPlayer.getCurrentMana();
        int initialHp = currentPlayer.getCurrentHp();
        int opponentHp = opponent.getCurrentHp();
        int initialLogSize = battle.getBattleLog().size();

        String invalidSkillName = "   ";

        assertDoesNotThrow(() -> battle.useSkill(invalidSkillName, opponent));

        assertSame(currentPlayer, battle.getCurrentTurn());
        assertEquals(initialMana, currentPlayer.getCurrentMana());
        assertEquals(initialHp, currentPlayer.getCurrentHp());
        assertEquals(opponentHp, opponent.getCurrentHp());

        assertTrue(battle.getBattleLog().size() > initialLogSize);
    }
}
