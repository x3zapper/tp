package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    public static final String DATE_ADDED_SORT_TYPE_ARGUMENT = "dateadded";
    public static final String NAME_SORT_TYPE_ARGUMENT = "name";
    public static final String REVERSE_SORT_TYPE_ARGUMENT = "reverse";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons by their names "
            + "by the the first character of their name."
            + "Parameters: 'name' or 'dateadded'\n"
            + "Example: " + COMMAND_WORD + "name";

    private final String sortType;

    /**
     * @param sortType sort type, can either be by name or by the date added
     */
    public SortCommand(String sortType) {
        requireNonNull(sortType);
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (this.sortType.equals(DATE_ADDED_SORT_TYPE_ARGUMENT)) {
            model.sortFilteredPersonListByDateAdded();
        } else if (this.sortType.equals(NAME_SORT_TYPE_ARGUMENT)) {
            model.sortFilteredPersonListByName();
        } else {
            model.reverseSortOrder();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
