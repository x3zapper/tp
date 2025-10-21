package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEARCH_MODE;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        validateNonEmptyArgs(trimmedArgs);

        SearchMode searchMode = extractSearchMode(trimmedArgs);
        String argsToProcess = searchMode.getRemainingArgs();
        List<String> keywords = extractKeywords(argsToProcess);

        return new FindCommand(new NameContainsKeywordsPredicate(
                keywords, searchMode.isStrict(), searchMode.isFuzzy()));
    }

    /**
     * Validates that the arguments are not empty.
     *
     * @param args the arguments to validate
     * @throws ParseException if the arguments are empty
     */
    private void validateNonEmptyArgs(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Extracts the search mode from the arguments.
     *
     * @param args the arguments to extract the search mode from
     * @return the search mode and remaining arguments
     * @throws ParseException if the search mode is invalid
     */
    private SearchMode extractSearchMode(String args) throws ParseException {
        if (args.startsWith("s/")) {
            return extractSearchModeFromPrefix(args);
        } else {
            return extractSearchModeFromSuffix(args);
        }
    }

    /**
     * Extracts the search mode when the prefix is at the beginning.
     *
     * @param args the arguments to extract the search mode from
     * @return the search mode and remaining arguments
     * @throws ParseException if the search mode is invalid
     */
    private SearchMode extractSearchModeFromPrefix(String args) throws ParseException {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String modePrefix = parts[0];
        if (modePrefix.length() <= 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String mode = modePrefix.substring(2);
        return parseMode(mode, parts[1]);
    }

    /**
     * Extracts the search mode when the prefix is at the end.
     *
     * @param args the arguments to extract the search mode from
     * @return the search mode and remaining arguments
     * @throws ParseException if the search mode is invalid
     */
    private SearchMode extractSearchModeFromSuffix(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SEARCH_MODE);

        if (argMultimap.getValue(PREFIX_SEARCH_MODE).isPresent()) {
            String mode = argMultimap.getValue(PREFIX_SEARCH_MODE).get().trim();
            return parseMode(mode, argMultimap.getPreamble());
        }

        return new SearchMode(false, false, args);
    }

    /**
     * Parses the mode string and returns the corresponding search mode.
     *
     * @param mode          the mode string to parse
     * @param remainingArgs the remaining arguments after extracting the mode
     * @return the search mode
     * @throws ParseException if the mode is invalid
     */
    private SearchMode parseMode(String mode, String remainingArgs) throws ParseException {
        switch (mode) {
        case "0":
            return new SearchMode(false, false, remainingArgs);
        case "1":
            return new SearchMode(true, false, remainingArgs);
        case "2":
            return new SearchMode(false, true, remainingArgs);
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Extracts keywords from the processed arguments.
     *
     * @param argsToProcess the arguments to extract keywords from
     * @return the list of keywords
     * @throws ParseException if no keywords are found
     */
    private List<String> extractKeywords(String argsToProcess) throws ParseException {
        String preamble = argsToProcess.trim();

        if (preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> keywords = List.of(preamble.split("\\s+"));

        if (keywords.isEmpty() || keywords.stream().allMatch(String::isBlank)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return keywords;
    }

    /**
     * Represents the search mode and remaining arguments.
     */
    private static class SearchMode {
        private final boolean isStrict;
        private final boolean isFuzzy;
        private final String remainingArgs;

        SearchMode(boolean isStrict, boolean isFuzzy, String remainingArgs) {
            this.isStrict = isStrict;
            this.isFuzzy = isFuzzy;
            this.remainingArgs = remainingArgs;
        }

        public boolean isStrict() {
            return isStrict;
        }

        public boolean isFuzzy() {
            return isFuzzy;
        }

        public String getRemainingArgs() {
            return remainingArgs;
        }
    }
}
