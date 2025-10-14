package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     */
    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        List<String> tags = Arrays.stream(trimmedArgs.split("\\s+"))
                .collect(Collectors.toList());

        if (tags.size() > 10) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_EXCESSIVE_TAGS));
        }

        for (String tagName : tags) {
            if (!Tag.isValidTagName(tagName)) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_INVALID_TAG));
            }
        }

        return new FilterCommand(new TagContainsKeywordsPredicate(tags));
    }
}

