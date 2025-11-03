package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("second"));
        NameContainsKeywordsPredicate strictPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("first"), true, false);
        NameContainsKeywordsPredicate fuzzyPredicate = new NameContainsKeywordsPredicate(
                Collections.singletonList("first"), false, true);

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // different strictness -> returns false
        FindCommand strictFindCommand = new FindCommand(strictPredicate);
        assertFalse(findFirstCommand.equals(strictFindCommand));

        // different fuzzy mode -> returns false
        FindCommand fuzzyFindCommand = new FindCommand(fuzzyPredicate);
        assertFalse(findFirstCommand.equals(fuzzyFindCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0) + " (Search mode: relaxed)";
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getSortedPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3) + " (Search mode: relaxed)";
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getSortedPersonList());
    }

    @Test
    public void execute_mixedCaseKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + " (Search mode: relaxed)";
        NameContainsKeywordsPredicate predicate = preparePredicate("fIoNa");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(FIONA), model.getSortedPersonList());
    }

    @Test
    public void execute_partialKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2) + " (Search mode: relaxed)";
        NameContainsKeywordsPredicate predicate = preparePredicate("Mei");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getSortedPersonList());
    }

    @Test
    public void execute_strictMode_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0) + " (Search mode: strict)";
        NameContainsKeywordsPredicate predicate = preparePredicate("Mei", true, false);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getSortedPersonList());
    }

    @Test
    public void execute_fuzzyMode_personsFound() {
        // "Alica" is within distance 1 of "Alice", fuzzy mode returns top 5 closest
        // matches
        NameContainsKeywordsPredicate predicate = preparePredicate("Alica", false, true);
        FindCommand command = new FindCommand(predicate);
        command.execute(model);
        // Fuzzy mode should return top 5 closest matches
        // The number should be min(5, total persons in address book)
        int expectedCount = Math.min(5, model.getAddressBook().getPersonList().size());
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedCount)
                + " (Search mode: fuzzy)";
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());
        // Should have at most 5 persons in the filtered list
        assertTrue(model.getSortedPersonList().size() <= 5);
    }

    @Test
    public void execute_fuzzyMode_returnsTopFiveMatches() {
        // Fuzzy mode should always return top 5 closest matches (or all if less than
        // 5persons)
        NameContainsKeywordsPredicate predicate = preparePredicate("xyz", false, true);
        FindCommand command = new FindCommand(predicate);
        command.execute(model);
        int expectedCount = Math.min(5, model.getAddressBook().getPersonList().size());
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedCount)
                + " (Search mode: fuzzy)";
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());
        // Should return top 5 (or all available) even if far from the keyword
        assertEquals(expectedCount, model.getSortedPersonList().size());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    private NameContainsKeywordsPredicate preparePredicate(String userInput, boolean isStrict, boolean isFuzzy) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")), isStrict, isFuzzy);
    }
}
