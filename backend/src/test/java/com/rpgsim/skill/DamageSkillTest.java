package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.enums.TargetType;
import com.rpgsim.fixture.TestCharacter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DamageSkillTest {
    @Test // Check correct damage dealt
    void shouldDealCorrectDamage(){
        // Arrange
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int manaCost = 20;
        int baseDamage = target.getDefense() + 10;
        int initialHp = target.getCurrentHp();

        Skill skill = new DamageSkill("Test Skill", manaCost, baseDamage);

        int expectedDamage = baseDamage + caster.getAttack() - target.getDefense();
        int expectedHp = initialHp - expectedDamage;

        // Act
        skill.execute(caster, target);

        // Assert
        assertEquals(expectedHp, target.getCurrentHp());
    }

    @Test // Check correct mana consumption
    void shouldConsumeCorrectMana(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int manaCost = 20;
        int baseDamage = 35;
        int initialMana = caster.getCurrentMana();

        Skill skill = new DamageSkill("Test Skill", manaCost, baseDamage);

        int expectedMana = initialMana - manaCost;

        skill.execute(caster, target);

        assertEquals(expectedMana, caster.getCurrentMana());
    }

    @Test // Check deal minimum damage
    void shouldDealMinimumDamage(){
        int maxHp = 100;
        int maxMana = 100;
        int speed = 10;
        int casterAttack = 10;
        int casterDefense = 5;
        int targetAttack = 15;
        int targetDefense = 20;

        Character caster = new TestCharacter("Caster", maxHp, maxMana, casterAttack, casterDefense, speed);
        Character target = new TestCharacter("Target", maxHp, maxMana, targetAttack, targetDefense, speed);

        int manaCost = 0;
        int baseDamage = 0;
        int minimumDamage = 1;

        int initialHp = target.getCurrentHp();
        int expectedHp = initialHp - minimumDamage;

        Skill skill = new DamageSkill("Test Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(expectedHp, target.getCurrentHp());
    }

    @Test // Check insufficient mana deal no damage
    void shouldNotDealDamageWhenManaIsInsufficient(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int manaCost = caster.getCurrentMana() + 1;
        int baseDamage = 35;
        int initialHp = target.getCurrentHp();

        Skill skill = new DamageSkill("Expensive Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(initialHp, target.getCurrentHp());
    }

    @Test // Check insufficient mana should not consume mana
    void shouldNotConsumeManaWhenManaIsInsufficient(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int initialMana = caster.getCurrentMana();
        int manaCost = initialMana + 1;
        int baseDamage = 35;

        Skill skill = new DamageSkill("Expensive Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check lethal damage
    void shouldDefeatTargetWhenDamageIsLethal(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int manaCost = 0;
        int baseDamage = target.getCurrentHp() + target.getDefense();

        Skill skill = new DamageSkill("Lethal Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(0, target.getCurrentHp());
        assertFalse(target.isAlive());
    }

    @Test // Check correct target type
    void shouldTargetEnemy(){
        int manaCost = 20;
        int baseDamage = 35;

        Skill skill = new DamageSkill("Test Skill", manaCost, baseDamage);

        TargetType actualTargetType = skill.getTargetType();

        assertEquals(TargetType.ENEMY, actualTargetType);
    }

    @Test // Check zero mana skill can be dealt
    void shouldExecuteZeroCostSkillWithoutMana(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        caster.useMana(caster.getCurrentMana());

        int manaCost = 0;
        int baseDamage = 35;
        int initialHp = target.getCurrentHp();

        Skill skill = new DamageSkill("Basic Attack", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(0, caster.getCurrentMana());
        assertTrue(target.getCurrentHp() < initialHp);
    }

    @Test // Check to reject negative mana cost
    void shouldRejectNegativeManaCost(){
        String skillName = "Test Damage Skill";
        int invalidManaCost = -10;
        int baseDamage = 20;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(skillName, invalidManaCost, baseDamage));
    }

    @Test // Check to reject negative base damage
    void shouldRejectNegativeBaseDamage(){
        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int invalidBaseDamage = -20;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(skillName, manaCost, invalidBaseDamage));
    }

    @Test // Check to reject null skill name
    void shouldRejectNullSkillName(){
        String invalidName = null;
        int manaCost = 10;
        int baseDamage = 20;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(invalidName, manaCost, baseDamage));
    }

    @Test // Check to reject empty skill name
    void shouldRejectEmptySkillName(){
        String invalidName = "";
        int manaCost = 10;
        int baseDamage = 20;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(invalidName, manaCost, baseDamage));
    }

    @Test // Check to reject blank skill name
    void shouldRejectBlankSkillName(){
        String invalidName = "   ";
        int manaCost = 10;
        int baseDamage = 20;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(invalidName, manaCost, baseDamage));
    }

    @Test // Check to allow zero mana cost
    void shouldAllowZeroManaCost(){
        String skillName = "Test Damage Skill";
        int manaCost = 0;
        int baseDamage = 20;

        Skill skill = assertDoesNotThrow(() -> new DamageSkill(skillName, manaCost, baseDamage));

        assertEquals(manaCost, skill.getManaCost());
    }

    @Test // Check to allow zero base damage
    void shouldAllowZeroBaseDamage(){
        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 0;

        assertDoesNotThrow(() -> new DamageSkill(skillName, manaCost, baseDamage));
    }

    @Test // Check to prevent integer overflow when calculating damage
    void shouldPreventIntegerOverflowWhenCalculatingDamage(){
        int maxHp = 100;
        int maxMana = 100;
        int speed = 10;
        int casterAttack = 25;
        int casterDefense = 10;
        int targetAttack = 15;
        int targetDefense = 5;

        Character caster = new TestCharacter("Caster", maxHp, maxMana, casterAttack, casterDefense, speed);
        Character target = new TestCharacter("Target", maxHp, maxMana, targetAttack, targetDefense, speed);

        String skillName = "Overflow Damage Skill";
        int manaCost = 0;
        int baseDamage = Integer.MAX_VALUE;

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(0, target.getCurrentHp());
        assertFalse(target.isAlive());
    }

    @Test // Check to report actual damage
    void shouldReportActualDamageDealt(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int initialHp = target.getCurrentHp();

        String skillName = "Overkill Skill";
        int manaCost = 0;
        int baseDamage = initialHp + target.getDefense();

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);

        SkillResult result = skill.execute(caster, target);

        assertTrue(result.isSuccess());
        assertEquals(0, target.getCurrentHp());

        int actualDamage = initialHp - target.getCurrentHp();

        assertTrue(result.getMessage().contains(" " + actualDamage + " damage"));
    }

    @Test // Check successful damage skill result
    void shouldReturnSuccessWhenDamageSkillExecutes(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        String skillName = "Test Damage Skill";
        int manaCost = caster.getCurrentMana() / 2;
        int baseDamage = 20;

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);

        SkillResult result = skill.execute(caster, target);

        assertTrue(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());
    }

    @Test // Check failed damage skill result
    void shouldReturnFailureWhenManaIsInsufficient(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int additionalManaRequired = 1;
        int manaCost = caster.getCurrentMana() + additionalManaRequired;
        int baseDamage = 20;
        String skillName = "Expensive Damage Skill";
        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);
        
        SkillResult result = skill.execute(caster, target);

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());
    }

    @Test // Check defeated caster cannot attack
    void shouldRejectDamageSkillFromDefeatedCaster(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int lethalDamage = caster.getCurrentHp();
        caster.takeDamage(lethalDamage);

        int initialMana = caster.getCurrentMana();
        int initialTargetHp = target.getCurrentHp();
        
        String skillName = "Test Skill";
        int manaCost = initialMana / 2;
        int baseDamage = 20;
        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);

        SkillResult result = skill.execute(caster, target);

        assertFalse(result.isSuccess());
        assertEquals(initialMana, caster.getCurrentMana());
        assertEquals(initialTargetHp, target.getCurrentHp());
    }

    @Test // Check damage skill cannot target defeated character
    void shouldRejectDamageSkillAgainstDefeatedTarget(){
        Character caster = new TestCharacter("Caster");
        Character target = new TestCharacter("Target");

        int lethalDamage = target.getCurrentHp();
        target.takeDamage(lethalDamage);

        int initialMana = caster.getCurrentMana();
        String skillName = "Test Skill";
        int manaCost = initialMana / 2;
        int baseDamage = 20;
        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);

        SkillResult result = skill.execute(caster, target);

        assertFalse(result.isSuccess());
        assertEquals(initialMana, caster.getCurrentMana());
        assertEquals(0, target.getCurrentHp());
    }

    @Test // Check to reject null caster
    void shouldRejectNullCaster(){
        Character caster = null;
        Character target = new TestCharacter("Target");

        int initialHp = target.getCurrentHp();
        int initialMana = target.getCurrentMana();

        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 20;

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);
        
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

        int initialHp = caster.getCurrentHp();
        int initialMana = caster.getCurrentMana();

        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 20;

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage);
        
        SkillResult result = assertDoesNotThrow(() -> skill.execute(caster, target));

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertFalse(result.getMessage().isBlank());

        assertEquals(initialHp, caster.getCurrentHp());
        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check to reject damage skill targeting self
    void shouldRejectEnemyDamageSkillTargetingSelf(){
        Character caster = new TestCharacter("Caster");
        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 20;

        Skill skill = new DamageSkill(skillName, manaCost, baseDamage, TargetType.ENEMY);

        int initialHp = caster.getCurrentHp();
        int initialMana = caster.getCurrentMana();

        SkillResult result = skill.execute(caster, caster);

        assertFalse(result.isSuccess());
        assertEquals(initialHp, caster.getCurrentHp());
        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check configurable target type
    void shouldAllowConfigurableTargetType(){
        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 20;

        for(TargetType targetType : TargetType.values()){
            Skill skill = new DamageSkill(skillName, manaCost, baseDamage, targetType);
            assertEquals(targetType, skill.getTargetType());
        }
    }

    @Test // Check to reject null target type
    void shouldRejectNullTargetType(){
        String skillName = "Test Damage Skill";
        int manaCost = 10;
        int baseDamage = 20;
        TargetType invalidTargetType = null;

        assertThrows(IllegalArgumentException.class, () -> new DamageSkill(skillName, manaCost, baseDamage, invalidTargetType));
    }
}
