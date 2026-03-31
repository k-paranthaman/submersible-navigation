package com.natwest.submersible.navigation.service.parser;

import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void testParseCommandsWithValidInput() {
        // Arrange
        String input = "LRFB";

        // Act
        List<Command> commands = CommandParser.parseCommands(input);

        // Assert
        assertNotNull(commands);
        assertEquals(4, commands.size());
        assertEquals(Command.LEFT, commands.get(0));
        assertEquals(Command.RIGHT, commands.get(1));
        assertEquals(Command.FORWARD, commands.get(2));
        assertEquals(Command.BACKWARD, commands.get(3));

    }

    @Test
    void testParseCommandsWithNullInput() {
        // Act & Assert
        ProbeException exception = assertThrows(ProbeException.class, () -> CommandParser.parseCommands(null));
        assertEquals(ErrorCode.INVALID_COMMAND, exception.getErrorCode());
    }

    @Test
    void testParseCommandsWithBlankInput() {
        // Act & Assert
        ProbeException exception = assertThrows(ProbeException.class, () -> CommandParser.parseCommands(" "));
        assertEquals(ErrorCode.INVALID_COMMAND, exception.getErrorCode());
    }

    @Test
    void testParseCommandsWithInvalidInput() {
        // Arrange
        String input = "XYZ";

        // Act & Assert
        ProbeException exception = assertThrows(ProbeException.class, () -> CommandParser.parseCommands(input));
        assertEquals(ErrorCode.INVALID_COMMAND, exception.getErrorCode());
    }

    @Test
    void testConvertCommandsWithValidList() {
        // Arrange
        List<Command> commands = List.of(Command.LEFT, Command.RIGHT, Command.FORWARD, Command.BACKWARD);

        // Act
        String commandString = CommandParser.convertCommands(commands);

        // Assert
        assertNotNull(commandString);
        assertEquals("LRFB", commandString);
    }

    @Test
    void testConvertCommandsWithNullList() {
        // Act & Assert
        ProbeException exception = assertThrows(ProbeException.class, () -> CommandParser.convertCommands(null));
        assertEquals(ErrorCode.INVALID_COMMAND, exception.getErrorCode());
    }

    @Test
    void testConvertCommandsWithEmptyList() {
        // Act & Assert
        ProbeException exception = assertThrows(ProbeException.class, () -> CommandParser.convertCommands(List.of()));
        assertEquals(ErrorCode.INVALID_COMMAND, exception.getErrorCode());
    }
}
