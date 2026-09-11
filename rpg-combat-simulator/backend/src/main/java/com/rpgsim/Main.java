package com.rpgsim;

import com.rpgsim.model.Mage;
import com.rpgsim.model.Warrior;
import com.rpgsim.skill.Skill;

public class Main {
    public static void main(String[] args) {
        Warrior warrior = new Warrior("Arthur");
        Mage mage = new Mage("Merlin");

        System.out.println("===Mage Test===");
        
        for(Skill skill : mage.getSkills()){
            System.out.println(
                skill.getName()
                + " | Mana: "
                + skill.getManaCost()
            );
        }

        System.out.println("===Warrior Test===");

        for(Skill skill : warrior.getSkills()){
            System.out.println(
                skill.getName()
                + " | Mana: "
                + skill.getManaCost()
            );
        }

        Skill fireball = mage.getSkillByName("Fireball");
        System.out.println(fireball.execute(mage, warrior));
    }
}
