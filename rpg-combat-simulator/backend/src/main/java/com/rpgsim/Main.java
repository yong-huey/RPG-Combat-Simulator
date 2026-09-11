package com.rpgsim;

import com.rpgsim.model.Mage;
import com.rpgsim.model.Warrior;

public class Main {
    public static void main(String[] args) {
        Warrior warrior = new Warrior("Arthur");
        Mage mage = new Mage("Merlin");

        System.out.println(
            warrior.getName()
            + ": "
            + warrior.getCurrentHp()
        );

        warrior.takeDamage(30);

        System.out.println(
            warrior.getName()
            + ": "
            + warrior.getCurrentHp()
        );

        System.out.println(
            mage.getName()
            + ": "
            + mage.getCurrentMana()
        );

        mage.useMana(50);

        System.out.println(
            mage.getName()
            + ": "
            + mage.getCurrentMana()
        );
    }
}
