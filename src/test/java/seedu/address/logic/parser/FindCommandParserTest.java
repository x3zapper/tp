package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);

        // explicit relaxed mode flag
        assertParseSuccess(parser, "Alice Bob s/0", expectedFindCommand);

        // strict mode flag at end
        FindCommand strictFindCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), true, false));
        assertParseSuccess(parser, "Alice Bob s/1", strictFindCommand);

        // strict mode flag at beginning
        assertParseSuccess(parser, "s/1 Alice Bob", strictFindCommand);

        // fuzzy mode flag at end
        FindCommand fuzzyFindCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), false, true));
        assertParseSuccess(parser, "Alice Bob s/2", fuzzyFindCommand);

        // fuzzy mode flag at beginning
        assertParseSuccess(parser, "s/2 Alice Bob", fuzzyFindCommand);
    }

    @Test
    public void parse_invalidMode_throwsParseException() {
        assertParseFailure(parser, "Alice s/3", FindCommand.MESSAGE_INVALID_SEARCH_MODE);
        assertParseFailure(parser, "Alice s/abc", FindCommand.MESSAGE_INVALID_SEARCH_MODE);
        assertParseFailure(parser, "s/3 Alice", FindCommand.MESSAGE_INVALID_SEARCH_MODE);
        assertParseFailure(parser, "s/abc Alice", FindCommand.MESSAGE_INVALID_SEARCH_MODE);
    }

    @Test
    public void parse_onlyModeFlag_throwsParseException() {
        // Only mode flag with space before it - should fail as there are no keywords
        assertParseFailure(parser, " s/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
        // Only mode flag at beginning - should fail as there are no keywords
        assertParseFailure(parser, "s/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

}
