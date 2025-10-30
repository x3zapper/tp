package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CommandHistory}.
 */
public class CommandHistoryTest {

    private CommandHistory history;

    @BeforeEach
    public void setUp() {
        history = new CommandHistory();
    }

    // --- Add tests ---

    @Test
    public void add_emptyCommand_notAdded() {
        //Null commands should never be passed
        assertFalse(history.add(""), "Empty command should not be added");
        assertTrue(history.isEmpty(), "History should remain empty");
    }

    @Test
    public void add_duplicateCommand_notAdded() {
        assertTrue(history.add("help"));
        assertFalse(history.add("help"), "Duplicate command should not be added");
        // Test that previous still works with only one entry
        assertEquals("help", history.getPrevious("x"));
    }

    @Test
    public void add_validCommands_addedSuccessfully() {
        assertTrue(history.add("list"));
        assertTrue(history.add("delete 1"));
        assertFalse(history.isEmpty());
        assertEquals("delete 1", history.getPrevious("x")); // newest should appear first
    }

    @Test
    public void add_exceedsMaxSize_oldestRemoved() {
        for (int i = 0; i < 100; i++) {
            assertTrue(history.add("cmd" + i));
        }
        // Oldest is cmd0, next add should remove it
        history.add("cmd100");

        // Going backward should eventually reach cmd1, not cmd0
        String last = history.getPrevious("temp");
        for (int i = 0; i < 99; i++) {
            last = history.getPrevious(last);
        }
        assertEquals("cmd1", last, "Oldest (cmd0) should have been removed");
    }

    // --- getPrevious tests ---

    @Test
    public void getPrevious_emptyHistory_returnsInput() {
        assertEquals("abc", history.getPrevious("abc"));
    }

    @Test
    public void getPrevious_singleCommand_cyclesCorrectly() {
        history.add("cmd1");

        // First previous gives "cmd1"
        assertEquals("cmd1", history.getPrevious("input"));

        // Further previous calls stay at "cmd1"
        assertEquals("cmd1", history.getPrevious("ignored"));
    }

    @Test
    public void getPrevious_multipleCommands_traversesBackwards() {
        history.add("cmd1");
        history.add("cmd2");
        history.add("cmd3");

        assertEquals("cmd3", history.getPrevious("input"));
        assertEquals("cmd2", history.getPrevious("input"));
        assertEquals("cmd1", history.getPrevious("input"));
        assertEquals("cmd1", history.getPrevious("input"), "Should stay at oldest");
    }

    // --- getNext tests ---

    @Test
    public void getNext_emptyHistory_returnsInput() {
        assertEquals("hello", history.getNext("hello"));
    }

    @Test
    public void getNext_traverseForwards() {
        history.add("cmd1");
        history.add("cmd2");
        history.add("cmd3");

        // Go back twice
        history.getPrevious("x");
        history.getPrevious("x");

        // Move forward once
        assertEquals("cmd3", history.getNext("y"));
    }

    @Test
    public void getNext_returnsSavedCommand_whenAtEnd() {
        history.add("cmd1");
        history.add("cmd2");
        history.add("cmd3");

        // Go all the way back to start
        history.getPrevious("typed");
        history.getPrevious("typed");
        history.getPrevious("typed");

        // Move forward until after the last command
        history.getNext("typed");
        history.getNext("typed");
        String result = history.getNext("typed");

        assertEquals("typed", result, "Should restore saved input after reaching the end");
    }

    // --- Reset tests ---

    @Test
    public void reset_restoresInitialBrowsingState() {
        history.add("cmd1");
        history.add("cmd2");
        history.getPrevious("x");
        history.getPrevious("y");

        history.reset();

        assertEquals("cmd2", history.getPrevious("test"),
                "Should have only reset browsing index");
    }
}
