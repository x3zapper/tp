package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Point;

import org.junit.jupiter.api.Test;

public class GuiSettingsTest {

    private static final double TEST_WINDOW_WIDTH = 800;
    private static final double TEST_WINDOW_HEIGHT = 600;
    private static final int TEST_WINDOW_X = 100;
    private static final int TEST_WINDOW_Y = 100;
    private static final double TEST_HELP_WINDOW_WIDTH = 700;
    private static final double TEST_HELP_WINDOW_HEIGHT = 550;
    private static final int TEST_HELP_WINDOW_X = 200;
    private static final int TEST_HELP_WINDOW_Y = 150;
    private static final double DIFFERENT_WINDOW_WIDTH = 900;
    private static final int DIFFERENT_HELP_WINDOW_X = 300;

    // Default values from GuiSettings
    private static final double DEFAULT_WINDOW_WIDTH = 740;
    private static final double DEFAULT_WINDOW_HEIGHT = 600;
    private static final double DEFAULT_HELP_WINDOW_WIDTH = 600;
    private static final double DEFAULT_HELP_WINDOW_HEIGHT = 500;

    @Test
    public void constructor_default_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings();
        assertNull(guiSettings.getWindowCoordinates());
        assertNull(guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void constructor_withMainWindowParams_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y);
        assertEquals(new Point(TEST_WINDOW_X, TEST_WINDOW_Y), guiSettings.getWindowCoordinates());
        assertNull(guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void constructor_withAllParams_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        assertEquals(new Point(TEST_WINDOW_X, TEST_WINDOW_Y), guiSettings.getWindowCoordinates());
        assertEquals(new Point(TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y), guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void getWindowCoordinates_nonNullCoordinates_returnsDefensiveCopy() {
        GuiSettings guiSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y);
        Point coordinates = guiSettings.getWindowCoordinates();
        coordinates.setLocation(999, 999);
        assertEquals(new Point(TEST_WINDOW_X, TEST_WINDOW_Y), guiSettings.getWindowCoordinates());
    }

    @Test
    public void getHelpWindowCoordinates_nonNullCoordinates_returnsDefensiveCopy() {
        GuiSettings guiSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        Point coordinates = guiSettings.getHelpWindowCoordinates();
        coordinates.setLocation(999, 999);
        assertEquals(new Point(TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y), guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        GuiSettings guiSettings1 = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        GuiSettings guiSettings2 = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                DIFFERENT_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        assertFalse(guiSettings1.equals(guiSettings2));
    }

    @Test
    public void hashCode_differentValues_returnsDifferentHashCode() {
        GuiSettings guiSettings1 = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        GuiSettings guiSettings2 = new GuiSettings(DIFFERENT_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        assertNotEquals(guiSettings1.hashCode(), guiSettings2.hashCode());
    }

    @Test
    public void toStringMethod() {
        GuiSettings guiSettings = new GuiSettings();
        String expected = GuiSettings.class.getCanonicalName() + "{windowWidth=" + guiSettings.getWindowWidth()
                + ", windowHeight=" + guiSettings.getWindowHeight() + ", windowCoordinates="
                + guiSettings.getWindowCoordinates() + ", helpWindowWidth=" + guiSettings.getHelpWindowWidth()
                + ", helpWindowHeight=" + guiSettings.getHelpWindowHeight() + ", helpWindowCoordinates="
                + guiSettings.getHelpWindowCoordinates() + "}";
        assertEquals(expected, guiSettings.toString());
    }

    @Test
    public void getters_allConstructors_returnCorrectValues() {
        // Test default constructor getters
        GuiSettings defaultSettings = new GuiSettings();
        assertEquals(DEFAULT_WINDOW_WIDTH, defaultSettings.getWindowWidth());
        assertEquals(DEFAULT_WINDOW_HEIGHT, defaultSettings.getWindowHeight());

        // Test 4-param constructor getters
        GuiSettings mainWindowSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y);
        assertEquals(TEST_WINDOW_WIDTH, mainWindowSettings.getWindowWidth());
        assertEquals(TEST_WINDOW_HEIGHT, mainWindowSettings.getWindowHeight());
        assertEquals(DEFAULT_HELP_WINDOW_WIDTH, mainWindowSettings.getHelpWindowWidth());
        assertEquals(DEFAULT_HELP_WINDOW_HEIGHT, mainWindowSettings.getHelpWindowHeight());

        // Test 8-param constructor getters
        GuiSettings fullSettings = new GuiSettings(TEST_WINDOW_WIDTH, TEST_WINDOW_HEIGHT,
                TEST_WINDOW_X, TEST_WINDOW_Y, TEST_HELP_WINDOW_WIDTH, TEST_HELP_WINDOW_HEIGHT,
                TEST_HELP_WINDOW_X, TEST_HELP_WINDOW_Y);
        assertEquals(TEST_WINDOW_WIDTH, fullSettings.getWindowWidth());
        assertEquals(TEST_WINDOW_HEIGHT, fullSettings.getWindowHeight());
        assertEquals(TEST_HELP_WINDOW_WIDTH, fullSettings.getHelpWindowWidth());
        assertEquals(TEST_HELP_WINDOW_HEIGHT, fullSettings.getHelpWindowHeight());
    }
}
