package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/** Manages command history functionality in {@code CommandBox} */
public class CommandHistory {
    private static final Logger logger = LogsCenter.getLogger(CommandHistory.class);

    private static final int HISTORY_MAX = 100;
    private static final int HISTORY_INACTIVE = -1;

    private final List<String> history;
    private int historyIndex;
    private String savedCommand;

    /** Creates a {@code CommandHistory} object */
    public CommandHistory() {
        this.history = new ArrayList<String>();
        this.historyIndex = HISTORY_INACTIVE;
        this.savedCommand = "";

        logger.fine("Initialized CommandHistory");
    }

    /**
     * Adds a command to the history if it is non-empty and not a duplicate of the latest entry.
     *
     * Returns true if added successfully
     */
    public boolean add(String commandText) {
        assert commandText != null;

        boolean isAdded = false;

        if (commandText == null || commandText.isEmpty()) {
            logger.fine("Ignoring empty command input");
            return isAdded;
        }

        if (history.isEmpty() || !history.get(history.size() - 1).equals(commandText)) {
            history.add(commandText);
            isAdded = true;

            //Note: optimization can be made to calculate greatest index of history list here

            logger.fine("Added command: " + commandText);
        } else {
            logger.fine("Skipped duplicate command: " + commandText);
        }

        //Trim history if exceed max size
        if (history.size() > HISTORY_MAX) {
            //Remove oldest one
            String removed = history.remove(0);

            logger.fine("History size exceeded, removing oldest command: " + removed);
        }

        reset();
        return isAdded;
    }

    /** Retrieves the previous/older command in history if available */
    public String getPrevious(String currentInput) {
        assert currentInput != null;
        //Sanity check
        if (currentInput == null) {
            return "";
        } else if (history.isEmpty()) {
            logger.fine("History empty. Returning current input.");
            return currentInput;
        }

        if (historyIndex > 0) {
            //Check if there is more history to traverse
            historyIndex -= 1;
        } else if (historyIndex == HISTORY_INACTIVE) {
            //Check if just started browsing
            historyIndex = history.size() - 1;
            savedCommand = currentInput;
        } else {
            //No more history to traverse
            logger.fine("Already at oldest command in history.");
            return history.get(0);
        }

        String result = history.get(historyIndex);
        logger.fine("Navigated up, index: " + historyIndex + ", command: " + result);
        return result;
    }

    /** Retrieves the next/newer command in history, or restores in-progress input if at the end */
    public String getNext(String currentInput) {
        assert currentInput != null;
        //Sanity check
        if (currentInput == null) {
            return "";
        } else if (history.isEmpty()) {
            logger.fine("History empty. Returning current input.");
            return currentInput;
        }

        if (historyIndex >= 0 && historyIndex < history.size() - 1) {
            //Check if more history to traverse
            historyIndex += 1;
            String result = history.get(historyIndex);
            logger.fine("Navigated down, index: " + historyIndex + ", command: " + result);
            return result;
        } else if (historyIndex == history.size() - 1) {
            //Check if at the end of command history
            historyIndex = HISTORY_INACTIVE;
            logger.fine("Reached end of history, returning saved in-progress command: " + savedCommand);
            return savedCommand;
        }

        //No more history to traverse
        logger.fine("No newer command to navigate to.");
        return currentInput;
    }

    /** Reset command browsing index and saved command */
    public void reset() {
        historyIndex = HISTORY_INACTIVE;
        savedCommand = "";
        logger.fine("History index and saved in-progress command reset");
    }

    /** Checks if command history is empty */
    public boolean isEmpty() {
        return history.isEmpty();
    }
}
