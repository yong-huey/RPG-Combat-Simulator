package com.rpgsim;

import com.rpgsim.combat.Battle;
import com.rpgsim.model.Mage;
import com.rpgsim.model.Warrior;

public class Main {

    public static void main(String[] args) {

        Mage mage = new Mage("Merlin");
        Warrior warrior = new Warrior("Arthur");

        Battle battle = new Battle(
                mage,
                warrior
        );

        System.out.println(
                "First turn: "
                        + battle.getCurrentTurn().getName()
        );

        battle.useSkill(
                "Fireball",
                warrior
        );

        battle.useSkill(
                "Heavy Strike",
                mage
        );

        battle.useSkill(
                "Fireball",
                warrior
        );

        battle.useSkill(
                "Heavy Strike",
                mage
        );

        System.out.println("\n=== Battle Log ===");

        for (String entry : battle.getBattleLog()) {
            System.out.println(entry);
        }

        System.out.println("\n=== Current Stats ===");

        System.out.println(
                mage.getName()
                        + " HP: "
                        + mage.getCurrentHp()
                        + "/"
                        + mage.getMaxHp()
        );

        System.out.println(
                warrior.getName()
                        + " HP: "
                        + warrior.getCurrentHp()
                        + "/"
                        + warrior.getMaxHp()
        );
    }
}