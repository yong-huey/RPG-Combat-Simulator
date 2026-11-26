package com.rpgsim.skill;

import com.rpgsim.model.Character;
import com.rpgsim.model.Warrior;
import com.rpgsim.model.Mage;
import com.rpgsim.enums.TargetType;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DamageSkillTest {
    @Test // Check correct damage dealt
    void shouldDealCorrectDamage(){
        // Arrange
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

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
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

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
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

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
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

        int manaCost = caster.getCurrentMana() + 1;
        int baseDamage = 35;
        int initialHp = target.getCurrentHp();

        Skill skill = new DamageSkill("Expensive Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(initialHp, target.getCurrentHp());
    }

    @Test // Check insufficient mana should not consume mana
    void shouldNotConsumeManaWhenManaIsInsufficient(){
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

        int initialMana = caster.getCurrentMana();
        int manaCost = initialMana + 1;
        int baseDamage = 35;

        Skill skill = new DamageSkill("Expensive Skill", manaCost, baseDamage);

        skill.execute(caster, target);

        assertEquals(initialMana, caster.getCurrentMana());
    }

    @Test // Check lethal damage
    void shouldDefeatTargetWhenDamageIsLethal(){
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

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
        Character caster = new Mage("Merlin");
        Character target = new Warrior("Arthur");

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
}
