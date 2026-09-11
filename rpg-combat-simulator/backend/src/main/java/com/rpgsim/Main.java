package com.rpgsim;

import com.rpgsim.model.Mage;
import com.rpgsim.model.Warrior;

public class Main {
    public static void main(String[] args) {
        Warrior warrior = new Warrior("Arthur");
        Mage mage = new Mage("Merlin");

        System.out.println("\n===Starting Stats===");
        System.out.println(
            warrior.getName()
            + "HP: "
            + warrior.getCurrentHp()
            + "/"
            + warrior.getMaxHp()
        );

        System.out.println(
            mage.getName()
            + "Mana: "
            + mage.getCurrentMana()
            + "/"
            + mage.getMaxMana()
        );

        System.out.println("\n===Damage Test===");
        warrior.takeDamage(30);
        System.out.println(
            warrior.getName()
            + "HP: "
            + warrior.getCurrentHp()
            + "/"
            + warrior.getMaxHp()
        );

        System.out.println("\n===Mana Test===");
        mage.useMana(30);
        System.out.println(
            mage.getName()
            + "Mana: "
            + mage.getCurrentMana()
            + "/"
            + mage.getMaxMana()
        );
        
        System.out.println("\n===Alive Test===");
        warrior.takeDamage(500);
        System.out.println(
            warrior.getName()
            + "HP: "
            + warrior.getCurrentHp()
            + "/"
            + warrior.getMaxHp()
        );
        System.out.println("Alive: " + warrior.isAlive());
    }
}
