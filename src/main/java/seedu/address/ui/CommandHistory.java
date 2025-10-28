package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

/** Manages command history functionality in {@code CommandBox} */
public class CommandHistory {
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
    }

    /**
     * Adds a command to the history if it is non-empty and not a duplicate of the latest entry.
     *
     * Returns true if added successfully
     */
    public boolean add(String commandText) {
        boolean isAdded = false;

        if (commandText == null || commandText.isEmpty()) {
            return isAdded;
        }

        if (history.isEmpty() || !history.get(history.size() - 1).equals(commandText)) {
            history.add(commandText);
            isAdded = true;

            //Note: optimization can be made to calculate greatest index of history list here
        }

        //Trim history if exceed max size
        if (history.size() > HISTORY_MAX) {
            //Remove oldest one
            history.remove(0);
        }

        reset();
        return isAdded;
    }

    /** Retrieves the previous/older command in history if available */
    public String getPrevious(String currentInput) {
        //Sanity check
        if (history.isEmpty()) {
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
            return history.get(0);
        }

        return history.get(historyIndex);
    }

    /** Retrieves the next/newer command in history, or restores in-progress input if at the end */
    public String getNext(String currentInput) {
        //Sanity check
        if (history.isEmpty()) {
            return currentInput;
        }

        if (historyIndex >= 0 && historyIndex < history.size() - 1) {
            //Check if more history to traverse
            historyIndex += 1;
            return history.get(historyIndex);
        } else if (historyIndex == history.size() - 1) {
            //Check if at the end of command history
            historyIndex = HISTORY_INACTIVE;
            return savedCommand;
        }

        //No more history to traverse
        return currentInput;
    }

    /** Reset command browsing index and saved command */
    public void reset() {
        historyIndex = HISTORY_INACTIVE;
        savedCommand = "";
    }

    /** Checks if command history is empty */
    public boolean isEmpty() {
        return history.isEmpty();
    }
}
