package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.TagContainsKeywordsPredicate;



/**
 * Finds and lists all persons in address book whose tags contains any of those in the argument.
 * Tag matching is case sensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose tags contain "
            + "all of the specified tags (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " client VIP";

    public static final String MESSAGE_INVALID_TAG = "Error: Invalid tag '%s'. "
            + "Tags may only contain alphanumeric characters.";

    private final TagContainsKeywordsPredicate predicate;

    public FilterCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);


        model.updateFilteredPersonList(predicate);

        int size = model.getFilteredPersonList().size();
        String tagsString = String.join(", ", predicate.getTags());

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_WITH_TAGS, size, tagsString));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

