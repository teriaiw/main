package igrad.logic.commands.requirement;

import static java.util.Objects.requireNonNull;

import java.util.List;

import igrad.logic.commands.CommandResult;
import igrad.logic.commands.exceptions.CommandException;
import igrad.model.Model;
import igrad.model.requirement.Title;
import igrad.model.requirement.Requirement;

/**
 * Deletes an existing requirement from the course book.
 */
public class RequirementDeleteCommand extends RequirementCommand {

    public static final String COMMAND_WORD = REQUIREMENT_COMMAND_WORD + "delete";

    public static final String MESSAGE_DETAILS = COMMAND_WORD + ": Deletes the requirement specified.\n";

    public static final String MESSAGE_USAGE = "Example: " + COMMAND_WORD + " Unrestricted Electives\n";

    public static final String MESSAGE_HELP = MESSAGE_DETAILS + MESSAGE_USAGE;

    public static final String MESSAGE_REQUIREMENT_DELETE_SUCCESS = "Deleted Requirement: %1$s";

    private final Title name;

    public RequirementDeleteCommand(Title name) {
        requireNonNull(name);

        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Requirement> requirements = model.getRequirementList();

        Requirement requirementToDelete;

        // check if requirement exists in course book
        if (!requirements.stream().anyMatch(requirement -> requirement.getTitle().equals(name))) {
            throw new CommandException(MESSAGE_REQUIREMENT_NON_EXISTENT);
        } else {
            requirementToDelete = requirements.stream()
                .filter(requirement -> requirement.getTitle().equals(name))
                .findFirst().get();
        }

        model.deleteRequirement(requirementToDelete);

        return new CommandResult(
            String.format(MESSAGE_REQUIREMENT_DELETE_SUCCESS, requirementToDelete));
    }

}
