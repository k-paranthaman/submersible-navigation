package com.natwest.submersible.navigation.service.parser;

import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for parsing and converting navigation command strings for the probe.
 * <p>
 * Provides static methods to:
 * <ul>
 *   <li>Parse raw command strings (e.g., "LRM") into a list of validated {@link Command} enums for navigation logic.</li>
 *   <li>Convert a list of {@link Command} enums back into a compact string representation for API responses or logging.</li>
 *   <li>Log parsing and conversion steps for traceability and debugging.</li>
 *   <li>Throw a {@link ProbeException} with {@link ErrorCode#INVALID_COMMAND} for invalid or empty input.</li>
 * </ul>
 * Used by controllers and services to ensure command input is validated and consistently transformed between string and enum representations.
 */
@Component
@Slf4j
public class CommandParser {

    /**
     * Parses a string of command characters into a list of {@link Command} enums.
     * <p>
     * Throws a {@link ProbeException} if the input is null or blank.
     * <p>
     * Note: {@link Command#form(char)} always throws a ProbeException for invalid input, so the resulting list will never contain nulls.
     *
     * @param input the raw command string (e.g., "LRM")
     * @return a list of parsed {@link Command} enums
     * @throws ProbeException if input is null, blank, or contains invalid command characters
     */
    public static List<Command> parseCommands(final String input) {

        if (input == null || input.isBlank()) {
            log.warn("Received null or blank command input");
            throw new ProbeException(ErrorCode.INVALID_COMMAND);
        }

        log.debug("Parsing command input: {}", input);

        // Command.form(char) always throws on invalid input, so no nulls will be present in the list.
        List<Command> commands = input.toUpperCase().chars()
                .mapToObj(c -> Command.form((char) c))
                .toList();
        log.debug("Parsed commands: {}", commands);
        return commands;
    }

    /**
     * Converts a list of {@link Command} enums into a compact string representation (e.g., [LEFT, RIGHT] -> "LR").
     * <p>
     * Throws a {@link ProbeException} if the command list is null or empty.
     *
     * @param commands the list of commands to convert
     * @return a string representation of the commands
     * @throws ProbeException if the command list is null or empty
     */
    public static String convertCommands(final List<Command> commands) {
        if (commands == null || commands.isEmpty()) {
            log.warn("Received null or empty command list for conversion");
            throw new ProbeException(ErrorCode.INVALID_COMMAND);
        }

        log.debug("Converting command list to string: {}", commands);

        String commandString = commands.stream()
                .map(Command::name)
                .map(name -> name.charAt(0)) // Get the first character of the enum name
                .map(String::valueOf)
                .reduce("", String::concat);
        log.debug("Converted command string: {}", commandString);
        return commandString;

    }
}
