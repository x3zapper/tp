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
    private static final String MODE_RELAXED = "0";
    private static final String MODE_STRICT = "1";
    private static final String MODE_FUZZY = "2";
    private static final int MIN_MODE_PREFIX_LENGTH = 3; // "s/" + at least one digit

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
        String[] parts = splitArguments(args);
        validateModePrefixParts(parts);

        String modePrefix = parts[0];
        validateModePrefixLength(modePrefix);

        String mode = extractModeValue(modePrefix);
        return parseMode(mode, parts[1]);
    }

    /**
     * Splits the arguments into mode prefix and remaining arguments.
     *
     * @param args the arguments to split
     * @return an array containing the mode prefix and remaining arguments
     */
    private String[] splitArguments(String args) {
        return args.split("\\s+", 2);
    }

    /**
     * Validates that the split parts contain both mode and keywords.
     *
     * @param parts the split parts to validate
     * @throws ParseException if parts array has insufficient elements
     */
    private void validateModePrefixParts(String[] parts) throws ParseException {
        if (parts.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Validates that the mode prefix has sufficient length.
     *
     * @param modePrefix the mode prefix to validate
     * @throws ParseException if the mode prefix is too short
     */
    private void validateModePrefixLength(String modePrefix) throws ParseException {
        if (modePrefix.length() < MIN_MODE_PREFIX_LENGTH) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Extracts the mode value from the mode prefix.
     *
     * @param modePrefix the mode prefix containing "s/" followed by the mode
     * @return the mode value (e.g., "0", "1", "2")
     */
    private String extractModeValue(String modePrefix) {
        return modePrefix.substring(2);
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
        case MODE_RELAXED:
            return createRelaxedMode(remainingArgs);
        case MODE_STRICT:
            return createStrictMode(remainingArgs);
        case MODE_FUZZY:
            return createFuzzyMode(remainingArgs);
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Creates a relaxed search mode.
     *
     * @param remainingArgs the remaining arguments after extracting the mode
     * @return a SearchMode configured for relaxed matching
     */
    private SearchMode createRelaxedMode(String remainingArgs) {
        return new SearchMode(false, false, remainingArgs);
    }

    /**
     * Creates a strict search mode.
     *
     * @param remainingArgs the remaining arguments after extracting the mode
     * @return a SearchMode configured for strict matching
     */
    private SearchMode createStrictMode(String remainingArgs) {
        return new SearchMode(true, false, remainingArgs);
    }

    /**
     * Creates a fuzzy search mode.
     *
     * @param remainingArgs the remaining arguments after extracting the mode
     * @return a SearchMode configured for fuzzy matching
     */
    private SearchMode createFuzzyMode(String remainingArgs) {
        return new SearchMode(false, true, remainingArgs);
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
        validateNonEmptyPreamble(preamble);

        List<String> keywords = splitIntoKeywords(preamble);
        validateKeywords(keywords);

        return keywords;
    }

    /**
     * Validates that the preamble is not empty.
     *
     * @param preamble the preamble to validate
     * @throws ParseException if the preamble is empty
     */
    private void validateNonEmptyPreamble(String preamble) throws ParseException {
        if (preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Splits the preamble into individual keywords.
     *
     * @param preamble the preamble to split
     * @return a list of keywords
     */
    private List<String> splitIntoKeywords(String preamble) {
        return List.of(preamble.split("\\s+"));
    }

    /**
     * Validates that the keywords list is not empty and contains valid keywords.
     *
     * @param keywords the keywords to validate
     * @throws ParseException if all keywords are blank
     */
    private void validateKeywords(List<String> keywords) throws ParseException {
        if (keywords.isEmpty() || keywords.stream().allMatch(String::isBlank)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
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
