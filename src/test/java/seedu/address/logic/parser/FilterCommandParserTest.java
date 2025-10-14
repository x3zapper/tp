package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedCommand =
                new FilterCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "owesMoney")));
        assertParseSuccess(parser, "friends owesMoney", expectedCommand);

        // multiple whitespaces between tags
        assertParseSuccess(parser, " \n friends \n \t owesMoney  \t", expectedCommand);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        // invalid character in tag
        assertParseFailure(parser, "friend$",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_INVALID_TAG));
    }

    @Test
    public void parse_moreThanTenTags_throwsParseException() {
        String input = "tag1 tag2 tag3 tag4 tag5 tag6 tag7 tag8 tag9 tag10 tag11";
        assertThrows(ParseException.class, () -> new FilterCommandParser().parse(input));
    }
}
