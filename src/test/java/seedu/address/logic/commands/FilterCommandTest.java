package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_WITH_TAGS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {cd m

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        List<String> firstTagList = Collections.singletonList("friends");
        List<String> secondTagList = Collections.singletonList("colleagues");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstTagList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondTagList);

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_noMatchingTag_noPersonFound() {
        List<String> tags = Collections.singletonList("nonexistent");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_WITH_TAGS, 0, String.join(", ", tags));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() {
        List<String> tags = Collections.singletonList("friends");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(
                MESSAGE_PERSONS_LISTED_WITH_TAGS,
                expectedModel.getFilteredPersonList().size(),
                String.join(", ", tags)
        );

        // Assuming ALICE and BENSON both have the "friends" tag in TypicalPersons
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleTags_singlePersonFound() {
        List<String> tags = Arrays.asList("friends", "owesMoney");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(
                MESSAGE_PERSONS_LISTED_WITH_TAGS,
                expectedModel.getFilteredPersonList().size(),
                String.join(", ", tags)
        );

        // Assuming only BENSON has both tags
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        List<String> tags = Collections.singletonList("friends");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FilterCommand filterCommand = new FilterCommand(predicate);

        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
