package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEARCH_MODE;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the
 * argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final int FUZZY_RESULT_LIMIT = 5;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: [" + PREFIX_SEARCH_MODE + "MODE] KEYWORD [MORE_KEYWORDS]...\n"
            + "MODE: 0 (relaxed, default), 1 (strict), 2 (fuzzy - shows top 5 closest matches)\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SEARCH_MODE + "1 alice\n"
            + "Example: " + COMMAND_WORD + " alice " + PREFIX_SEARCH_MODE + "2\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SEARCH_MODE + "2 alica";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (predicate.isFuzzy()) {
            return executeFuzzySearch(model);
        } else {
            return executeNormalSearch(model);
        }
    }

    /**
     * Executes a normal search (relaxed or strict mode).
     *
     * @param model the model to search in
     * @return the command result
     */
    private CommandResult executeNormalSearch(Model model) {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Executes a fuzzy search and returns top 5 closest matches.
     * This method ranks all persons by their minimum Levenshtein distance to the
     * keywords
     * and returns the top 5 closest matches, regardless of distance threshold.
     *
     * @param model the model to search in
     * @return the command result
     */
    private CommandResult executeFuzzySearch(Model model) {
        List<Person> allPersons = model.getAddressBook().getPersonList();
        List<Person> rankedPersons = rankPersonsByDistance(allPersons);

        // Update the filtered list with top 5 matches
        model.updateFilteredPersonList(rankedPersons::contains);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Ranks all persons by their minimum Levenshtein distance to the keywords.
     *
     * @param persons the list of persons to rank
     * @return the top 5 closest matches
     */
    private List<Person> rankPersonsByDistance(List<Person> persons) {
        return persons.stream()
                .sorted(Comparator.comparingInt(predicate::getMinimumDistance))
                .limit(FUZZY_RESULT_LIMIT)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
