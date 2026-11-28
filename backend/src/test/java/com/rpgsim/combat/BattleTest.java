package com.rpgsim.combat;

import java.util.List;

import com.rpgsim.model.Character;
import com.rpgsim.model.Warrior;
import com.rpgsim.model.Mage;
import com.rpgsim.skill.Skill;
import com.rpgsim.skill.DamageSkill;
import com.rpgsim.fixture.TestCharacter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BattleTest {
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
        Character defender = new TestCharacter("Defender", slowerSpeed);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

        Character initialTurn = battle.getCurrentTurn();
        Character opponent = battle.getOpponent();

        String invalidSkillName = "Nonexistent Skill";

        battle.useSkill(invalidSkillName, opponent);

        assertSame(initialTurn, battle.getCurrentTurn());
    }

    @Test // Check no self attack
    void shouldRejectEnemySkillTargetingSelf(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

        Character caster = battle.getCurrentTurn();

        int initialHp = caster.getCurrentHp();
        String skillName = "Basic Attack";

        battle.useSkill(skillName, caster);

        assertEquals(initialHp, caster.getCurrentHp());
        assertSame(caster, battle.getCurrentTurn());
    }

    @Test // Check no healing of enemy
    void shouldRejectSelfSkillTargetingEnemy(){
        Character caster = new Mage("Merlin");
        
        int slowerSpeed = caster.getSpeed() - 1;

        Character opponent = new TestCharacter("Opponent", slowerSpeed);

        int damage = opponent.getMaxHp() / 2;
        opponent.takeDamage(damage);

        int initialHp = opponent.getCurrentHp();
        int initialMana = caster.getCurrentMana();

        Battle battle = new Battle(caster, opponent);

        String skill = "Heal";

        battle.useSkill(skill, opponent);

        assertEquals(initialHp, opponent.getCurrentHp());
        assertEquals(initialMana, caster.getCurrentMana());
        assertSame(caster, battle.getCurrentTurn());
    }

    @Test // Check no damage to external characters
    void shouldRejectTargetOutsideBattle(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");
        Character outsider = new TestCharacter("Outsider");

        Battle battle = new Battle(playerOne, playerTwo);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

        List<String> battleLog = battle.getBattleLog();

        int initialLogSize = battleLog.size();

        String fakeEntry = "Fake battle log";

        assertThrows(UnsupportedOperationException.class, () -> battleLog.add(fakeEntry));

        assertEquals(initialLogSize, battle.getBattleLog().size());
    }

    @Test // Check to not allow external clearing of log
    void shouldPreventExternalBattleLogClearing(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

        List<String> originalLog = battle.getBattleLog();
        int originalLogSize = originalLog.size();

        String skillName = "Basic Attack";

        battle.useSkill(skillName, battle.getOpponent());

        assertEquals(originalLogSize, originalLog.size());
        assertTrue(battle.getBattleLog().size() > originalLogSize);
    }

    @Test // Check same speed player 1 to start first
    void shouldAllowPlayerOneToStartWhenSpeedsAreEqual(){
        int sharedSpeed = 10;
        Character playerOne = new TestCharacter("Player 1", sharedSpeed);
        Character playerTwo = new TestCharacter("Player 2", sharedSpeed);

        Battle battle = new Battle(playerOne, playerTwo);

        assertSame(playerOne, battle.getCurrentTurn());
    }

    @Test // Check null player 1
    void shouldRejectNullPlayerOne(){
        Character playerOne = null;
        Character playerTwo = new TestCharacter("Test Character");

        assertThrows(IllegalArgumentException.class, () -> new Battle(playerOne, playerTwo));
    }

    @Test // Check null player 2
    void shouldRejectNullPlayerTwo(){
        Character playerOne = new TestCharacter("Test Character");
        Character playerTwo = null;

        assertThrows(IllegalArgumentException.class, () -> new Battle(playerOne, playerTwo));
    }

    @Test // Check no fight against self
    void shouldRejectSameCharacterAsBothPlayers(){
        Character player = new TestCharacter("Test Character");

        assertThrows(IllegalArgumentException.class, () -> new Battle(player, player));
    }

    @Test // Check allow same class
    void shouldAllowDifferentCharactersOfSameClass(){
        Character playerOne = new Warrior("Arthur");
        Character playerTwo = new Warrior("Lancelot");

        Battle battle = assertDoesNotThrow(() -> new Battle(playerOne, playerTwo));

        assertFalse(battle.isBattleOver());
        assertNotSame(playerOne, playerTwo);
    }

    @Test // Check to reject player 1 die before battle
    void shouldRejectDefeatedPlayerOne(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        int lethalDamage = playerOne.getCurrentHp();

        playerOne.takeDamage(lethalDamage);

        assertThrows(IllegalArgumentException.class, () -> new Battle(playerOne, playerTwo));
    } 

    @Test // Check to reject player 2 die before battle
    void shouldRejectDefeatedPlayerTwo(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        int lethalDamage = playerTwo.getCurrentHp();

        playerTwo.takeDamage(lethalDamage);

        assertThrows(IllegalArgumentException.class, () -> new Battle(playerOne, playerTwo));
    } 

    @Test // Check to reject null target
    void shouldRejectNullSkillTarget(){
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

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
        Character playerOne = new TestCharacter("Player 1");
        Character playerTwo = new TestCharacter("Player 2");

        Battle battle = new Battle(playerOne, playerTwo);

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
