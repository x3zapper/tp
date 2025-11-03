package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_TYPE;

import java.time.Instant;
import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all persons in the address book to the user.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    /** {@code Comparator} that compares persons' name in ascending order */
    public static final Comparator<Person> COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING =
        Comparator.<Person, String>comparing(person -> person.getName().toString().toLowerCase(),
                Comparator.naturalOrder())
                .thenComparing(person -> Instant.parse(person.getDateAdded().toString()));

    /** {@code Comparator} that compares persons' name in ascending order */
    public static final Comparator<Person> COMPARATOR_SORT_PERSONS_BY_NAME_DESCENDING =
        COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING.reversed();

    /** {@code Comparator} that compares the persons' date added ascending */
    public static final Comparator<Person> COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING =
        Comparator.<Person, Instant>comparing(person -> Instant.parse(person.getDateAdded().toString()))
                .thenComparing(person -> person.getName().toString().toLowerCase(), Comparator.naturalOrder());

    /** {@code Comparator} that compares the persons' date added ascending */
    public static final Comparator<Person> COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_DESCENDING =
        COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING.reversed();


    public static final String DATE_ADDED_SORT_TYPE_ARGUMENT = "dateadded";
    public static final String NAME_SORT_TYPE_ARGUMENT = "name";
    public static final String ASCENDING_SORT_ORDER_ARGUMENT = "asc";
    public static final String DESCENDING_SORT_ORDER_ARGUMENT = "dsc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes sorting of shown contacts lexicographically by name or by their date added to the address book"
            + "\n There are ONLY 'dateadded' & 'name' for sort type and "
            + "'asc' & 'dsc' for ascending & descending sort orders respectively.\n"
            + "Note: Sort order will stick throughout the session.\n"
            + "Parameters: " + PREFIX_SORT_TYPE + "SORT_TYPE "
            + PREFIX_SORT_ORDER + "SORT_ORDER" + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SORT_TYPE + NAME_SORT_TYPE_ARGUMENT + " "
            + PREFIX_SORT_ORDER + DESCENDING_SORT_ORDER_ARGUMENT + "\n"
            + "Default: " + COMMAND_WORD + " " + PREFIX_SORT_TYPE + DATE_ADDED_SORT_TYPE_ARGUMENT + " "
            + PREFIX_SORT_ORDER + ASCENDING_SORT_ORDER_ARGUMENT;

    private final Comparator<Person> comparator;

    /**
     * @param comparator Comparator to update the SortedList to
     */
    public SortCommand(Comparator<Person> comparator) {
        requireNonNull(comparator);
        this.comparator = comparator;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateSortComparator(this.comparator);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return comparator.equals(otherSortCommand.comparator);
    }
}
