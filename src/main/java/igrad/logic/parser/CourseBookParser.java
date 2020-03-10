package igrad.logic.parser;

import static igrad.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static igrad.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static igrad.logic.parser.CliSyntax.FLAG_AUTO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import igrad.logic.commands.*;
import igrad.logic.parser.exceptions.ParseException;
import igrad.services.exceptions.ServiceException;

/**
 * Parses user input.
 */
public class CourseBookParser {

    /**
     * Used for initial separation of command words and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>([a-z]+\\s){2})(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException, IOException, ServiceException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").trim();
        final String argumentsWithFlags = matcher.group("arguments").trim();
        final String arguments = ArgumentTokenizer.removeFlags(argumentsWithFlags);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:

            if (ArgumentTokenizer.isFlagPresent(argumentsWithFlags, FLAG_AUTO.getFlag())) {
                return new AddAutoCommandParser().parse(arguments);
            } else {
                return new AddCommandParser().parse(arguments);
            }

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case CourseAddCommand.COMMAND_WORD:
            return new CourseAddCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
