package com.rpgsim.skill;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SkillResultTest {
    @Test // Check successful skill result
    void shouldReturnSuccessfulResult(){
        // Arrange
        boolean success = true;
        String message = "Skill executed successfully.";

        // Act
        SkillResult result = new SkillResult(success, message);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals(message, result.getMessage());
    }

    @Test // Check failed skill result
    void shouldReturnFailedResult(){
        boolean success = false;
        String message = "Insufficient mana.";

        SkillResult result = new SkillResult(success, message);

        assertFalse(result.isSuccess());
        assertEquals(message, result.getMessage());
    }

    @Test // Check to reject null message
    void shouldRejectNullMessage(){
        boolean success = true;
        String invalidMessage = null;

        assertThrows(IllegalArgumentException.class, () -> new SkillResult(success, invalidMessage));
    }

    @Test // Check to reject empty message
    void shouldRejectEmptyMessage(){
        boolean success = true;
        String invalidMessage = "";

        assertThrows(IllegalArgumentException.class, () -> new SkillResult(success, invalidMessage));
    }

    @Test // Check to reject blank message
    void shouldRejectBlankMessage(){
        boolean success = true;
        String invalidMessage = "   ";

        assertThrows(IllegalArgumentException.class, () -> new SkillResult(success, invalidMessage));
    }
}
