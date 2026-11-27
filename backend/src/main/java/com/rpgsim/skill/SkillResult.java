package com.rpgsim.skill;

public final class SkillResult {
    private final boolean success;
    private final String message;

    public SkillResult(boolean success, String message){
        if(message == null || message.isBlank()){
            throw new IllegalArgumentException("Skill result message cannot be null or blank.");
        }
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }
}
