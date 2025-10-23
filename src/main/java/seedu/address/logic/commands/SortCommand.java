package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class SortCommand extends Command {
    // TODO Add sort as a param e.g. s/ (will need to edit clisyntax.java)
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Sort command not implemented yet!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons by their names "
            + "by the the first character of their name."
            + "Parameters: 'name' or 'dateadded'\n"
            + "Example: " + COMMAND_WORD + "name";

    private final String sortType;

    public SortCommand(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (this.sortType.equals("dateadded")) {
            model.sortFilteredPersonListByDateAdded();
        } else {
            model.sortFilteredPersonListByName();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
