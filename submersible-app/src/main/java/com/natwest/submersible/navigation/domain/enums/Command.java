package com.natwest.submersible.navigation.domain.enums;

import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;

/**
 * The {@code Command} enum represents the set of valid movement commands for the submersible navigation system.
 * <p>
 * Each command is associated with a unique character code:
 * <ul>
 *   <li>{@link #FORWARD} ('F') - Move the submersible forward</li>
 *   <li>{@link #BACKWARD} ('B') - Move the submersible backward</li>
 *   <li>{@link #LEFT} ('L') - Turn the submersible left</li>
 *   <li>{@link #RIGHT} ('R') - Turn the submersible right</li>
 * </ul>
 * <p>
 * Usage:
 * <ul>
 *   <li>Use {@link #form(char)} to convert a character input to its corresponding {@code Command}.</li>
 *   <li>If an invalid character is provided, a {@link com.natwest.submersible.navigation.exception.ProbeException} is thrown with {@link com.natwest.submersible.navigation.exception.ErrorCode#INVALID_COMMAND}.</li>
 * </ul>
 *
 * @author NatWest
 * @since 1.0
 */
public enum Command {

    FORWARD ('F'), BACKWARD('B'), LEFT('L'), RIGHT('R');

    private final char code;

    Command(char code){
        this.code = code;
    }

    public static Command form(final char input){
        for (Command command : Command.values()) {
            if(command.code == input){
                return command;
            }
        }
        throw new ProbeException(ErrorCode.INVALID_COMMAND, "Invalid command code: " + input);
    }
}
