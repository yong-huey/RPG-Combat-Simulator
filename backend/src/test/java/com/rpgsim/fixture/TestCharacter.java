package com.rpgsim.fixture;

import com.rpgsim.model.Character;

public class TestCharacter extends Character{
    public TestCharacter(
        String name,
        int maxHp,
        int maxMana,
        int attack,
        int defense,
        int speed
    ){
        super(name, maxHp, maxMana, attack, defense, speed);
    }
}
