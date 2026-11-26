package com.rpgsim;

import com.rpgsim.combat.Battle;
import com.rpgsim.model.Mage;
import com.rpgsim.model.Warrior;
import com.rpgsim.skill.Skill;

public class Main {

    public static void main(String[] args) {

        Mage mage = new Mage("Merlin");
        Warrior warrior = new Warrior("Arthur");

        System.out.println("===Merlin Skills===");
        for (Skill skill : mage.getSkills()) {
                System.out.println(
                        skill.getName()
                                + " | Mana: "
                                + skill.getManaCost()
                );
        }

        System.out.println();

        System.out.println("=== Arthur's Skills ===");

        for (Skill skill : warrior.getSkills()) {
                System.out.println(
                        skill.getName()
                                + " | Mana: "
                                + skill.getManaCost()
                );
        }
    }
}