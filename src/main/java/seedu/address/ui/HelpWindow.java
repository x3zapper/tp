package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103-f13-3.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private double lastKnownX;
    private double lastKnownY;
    private boolean hasStoredPosition = false;

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
        // Capture position when user closes window with X button
        getRoot().setOnCloseRequest(event -> {
            lastKnownX = getRoot().getX();
            lastKnownY = getRoot().getY();
            hasStoredPosition = true;
        });
    }

    /**
     * Sets the help window size and position based on {@code guiSettings}.
     */
    public void setWindowDefaultSize(GuiSettings guiSettings) {
        getRoot().setHeight(guiSettings.getHelpWindowHeight());
        getRoot().setWidth(guiSettings.getHelpWindowWidth());
        if (guiSettings.getHelpWindowCoordinates() != null) {
            lastKnownX = guiSettings.getHelpWindowCoordinates().getX();
            lastKnownY = guiSettings.getHelpWindowCoordinates().getY();
            hasStoredPosition = true;
        }
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *                               <ul>
     *                               <li>
     *                               if this method is called on a thread other than
     *                               the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or
     *                               layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");

        // Restore window if it's minimized
        if (getRoot().isIconified()) {
            getRoot().setIconified(false);
        }

        getRoot().show();

        // Apply stored position after showing using Platform.runLater to ensure it
        // takes effect
        if (hasStoredPosition) {
            logger.fine("Applying stored position: x=" + lastKnownX + ", y=" + lastKnownY);
            Platform.runLater(() -> {
                getRoot().setX(lastKnownX);
                getRoot().setY(lastKnownY);
            });
        } else {
            logger.fine("No stored position, centering on screen");
            getRoot().centerOnScreen();
        }
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        // Save position before hiding
        if (getRoot().isShowing()) {
            lastKnownX = getRoot().getX();
            lastKnownY = getRoot().getY();
            hasStoredPosition = true;
        }
        getRoot().hide();
    }

    /**
     * Gets the last known X coordinate of the help window.
     * If window is currently showing, returns current X. Otherwise returns stored
     * X.
     */
    public double getLastKnownX() {
        return getRoot().isShowing() ? getRoot().getX() : lastKnownX;
    }

    /**
     * Gets the last known Y coordinate of the help window.
     * If window is currently showing, returns current Y. Otherwise returns stored
     * Y.
     */
    public double getLastKnownY() {
        return getRoot().isShowing() ? getRoot().getY() : lastKnownY;
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        // Restore window if it's minimized
        if (getRoot().isIconified()) {
            getRoot().setIconified(false);
        }
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
