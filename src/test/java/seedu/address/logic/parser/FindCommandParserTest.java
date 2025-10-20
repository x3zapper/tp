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
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
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
        assertParseSuccess(parser, "/s 0 Alice Bob", expectedFindCommand);

        // strict mode flag
        FindCommand strictFindCommand = new FindCommand(
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), true));
        assertParseSuccess(parser, "/s 1 Alice Bob", strictFindCommand);
        assertParseSuccess(parser, "Alice /s 1 Bob", strictFindCommand);
    }

    @Test
    public void parse_invalidMode_throwsParseException() {
        assertParseFailure(parser, "/s 2 Alice", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "/s Alice", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyModeFlag_throwsParseException() {
        assertParseFailure(parser, "/s 1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

}
