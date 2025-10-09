package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Finds and lists all persons in address book whose tags contains any of those in the argument.
 * Tag matching is case sensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Filter testing");
    }
}

