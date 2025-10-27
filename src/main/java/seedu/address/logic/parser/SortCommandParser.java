package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_TYPE;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;



/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_SORT_TYPE,
                PREFIX_SORT_ORDER
        );

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_SORT_TYPE,
                PREFIX_SORT_ORDER
        );

        if (argMultimap.getValue(PREFIX_SORT_ORDER).isEmpty() || argMultimap.getValue(PREFIX_SORT_TYPE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String sortType = argMultimap.getValue(PREFIX_SORT_TYPE).get();
        String sortOrder = argMultimap.getValue(PREFIX_SORT_ORDER).get();

        if (sortType.equalsIgnoreCase(SortCommand.DATE_ADDED_SORT_TYPE_ARGUMENT)) {
            if (sortOrder.equalsIgnoreCase(SortCommand.ASCENDING_SORT_ORDER_ARGUMENT)) {
                return new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING);
            } else if (sortOrder.equalsIgnoreCase(SortCommand.DESCENDING_SORT_ORDER_ARGUMENT)) {
                return new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_DESCENDING);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
            }
        } else if (sortType.equalsIgnoreCase(SortCommand.NAME_SORT_TYPE_ARGUMENT)) {
            if (sortOrder.equalsIgnoreCase(SortCommand.ASCENDING_SORT_ORDER_ARGUMENT)) {
                return new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING);
            } else if (sortOrder.equalsIgnoreCase(SortCommand.DESCENDING_SORT_ORDER_ARGUMENT)) {
                return new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_DESCENDING);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
            }
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

    }

}
