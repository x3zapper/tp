package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    private final CommandHistory commandHistory;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.commandHistory = new CommandHistory();
        initEventListeners();
    }

    /**
     * Initialises all event listeners
     */
    private void initEventListeners() {
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        // Handle key presses for command history navigation
        commandTextField.setOnKeyPressed(event -> {
            if (!commandTextField.isFocused()) {
                return;
            }

            if (event.getCode() == KeyCode.UP) {
                //Get older command
                handleHistoryUp();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                //Get newer command
                handleHistoryDown();
                event.consume();
            }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    //This function is the entrypoint when a user submits a command string (before any parsing)
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.isEmpty()) {
            return;
        }

        //Store command history
        commandHistory.add(commandText);

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Navigate to previous command in history.
     */
    private void handleHistoryUp() {
        String commandText = commandTextField.getText();

        //Set TextField to past command recorded
        commandTextField.setText(commandHistory.getPrevious(commandText));
        //Move cursor to the end of the command
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Navigate to next command in history.
     */
    private void handleHistoryDown() {
        String commandText = commandTextField.getText();

        //Set TextField to past command recorded
        commandTextField.setText(commandHistory.getNext(commandText));
        //Move cursor to the end of the command
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
