package com.rpgsim.combat;

import com.rpgsim.model.Character;
import com.rpgsim.skill.Skill;
import com.rpgsim.enums.TargetType;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final Character player1;
    private final Character player2;

    private Character currentTurn;

    private final List<String> battleLog = new ArrayList<>();

    public Battle(Character player1, Character player2){
        this.player1 = player1;
        this.player2 = player2;

        if(player1.getSpeed() >= player2.getSpeed()){
            currentTurn = player1;
        } else {
            currentTurn = player2;
        }

        battleLog.add("Battle started between "
            + player1.getName()
            + " and "
            + player2.getName()
            + "."
        );

        battleLog.add(currentTurn.getName() + " goes first.");
    }

    public void useSkill(String skillName, Character target){
        if(isBattleOver()){
            battleLog.add("Battle is already over.");
            return;
        }

        Skill skill = currentTurn.getSkillByName(skillName);

        if(skill == null){
            battleLog.add(
                currentTurn.getName()
                + " does not know "
                + skillName
                + "."
            );
            return;
        }

        if(!isValidTarget(skill, target)){
            battleLog.add(
                "Invalid target for "
                + skill.getName()
                + "."
            );
            return;
        }

        String result = skill.execute(currentTurn, target);

        battleLog.add(result);

        if(!target.isAlive()){
            battleLog.add(target.getName() + " has been defeated.");
            battleLog.add(currentTurn.getName() + " wins the battle.");
            return;
        }

        switchTurn();
    }

    public boolean isBattleOver(){
        return !player1.isAlive() || ! player2.isAlive();
    }

    private boolean isValidTarget(
        Skill skill,
        Character target
    ){
        TargetType targetType = skill.getTargetType();
        if(targetType == TargetType.SELF){
            return target == currentTurn;
        }
        if(targetType == TargetType.ENEMY){
            return target == getOpponent();
        }
        return target == player1 || target == player2;
    }

    public Character getOpponent(){
        if(currentTurn == player1){
            return player2;
        }
        return player1;
    }

    public Character getWinner(){
        if(!isBattleOver()){
            return null;
        }

        if(player1.isAlive()){
            return player1;
        }

        return player2;
    }

    private void switchTurn(){
        if(currentTurn == player1){
            currentTurn = player2;
        } else {
            currentTurn = player1;
        }
    }

    public Character getCurrentTurn(){
        return currentTurn;
    }

    public Character getPlayer1(){
        return player1;
    }

    public Character getPlayer2(){
        return player2;
    }

    public List<String> getBattleLog(){
        return battleLog;
    }
}
