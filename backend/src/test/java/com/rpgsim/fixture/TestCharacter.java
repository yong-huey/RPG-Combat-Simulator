package com.rpgsim.fixture;

import com.rpgsim.model.Character;

public class TestCharacter extends Character{
    
    private static final int DEFAULT_MAX_HP = 100;
    private static final int DEFAULT_MAX_MANA = 100;
    private static final int DEFAULT_ATTACK = 20;
    private static final int DEFAULT_DEFENSE = 10;
    private static final int DEFAULT_SPEED = 10;

    public TestCharacter(String name){
        this(name, DEFAULT_SPEED);
    }

    public TestCharacter(String name, int speed){
        this(
            name, 
            DEFAULT_MAX_HP, 
            DEFAULT_MAX_MANA,
            DEFAULT_ATTACK,
            DEFAULT_DEFENSE,
            speed
        );
    }

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
