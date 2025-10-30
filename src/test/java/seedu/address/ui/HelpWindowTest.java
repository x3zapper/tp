package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

public class HelpWindowTest {

    private static final double TEST_HELP_WIDTH = 700;
    private static final double TEST_HELP_HEIGHT = 550;
    private static final int TEST_HELP_X = 200;
    private static final int TEST_HELP_Y = 150;
    private static final double TEST_MAIN_WIDTH = 740;
    private static final double TEST_MAIN_HEIGHT = 600;
    private static final int TEST_MAIN_X = 100;
    private static final int TEST_MAIN_Y = 100;
    private static final int ALTERNATIVE_MAIN_X = 50;
    private static final int ALTERNATIVE_MAIN_Y = 60;
    private static final int ALTERNATIVE_HELP_X = 250;
    private static final int ALTERNATIVE_HELP_Y = 180;
    private static final int MODIFIED_COORDINATE = 999;
    private static final double DEFAULT_HELP_WIDTH = 600;
    private static final double DEFAULT_HELP_HEIGHT = 500;
    private static final double DELTA = 0.01;

    @Test
    public void helpMessage_isCorrect() {
        assertEquals("Refer to the user guide: https://ay2526s1-cs2103-f13-3.github.io/tp/UserGuide.html",
                HelpWindow.HELP_MESSAGE);
    }

    @Test
    public void userGuideUrl_isCorrect() {
        assertEquals("https://ay2526s1-cs2103-f13-3.github.io/tp/UserGuide.html",
                HelpWindow.USERGUIDE_URL);
    }

    @Test
    public void helpMessage_containsUserGuideUrl() {
        assertNotNull(HelpWindow.HELP_MESSAGE);
        assertNotNull(HelpWindow.USERGUIDE_URL);
        // Verify HELP_MESSAGE contains USERGUIDE_URL
        assertEquals("Refer to the user guide: " + HelpWindow.USERGUIDE_URL, HelpWindow.HELP_MESSAGE);
    }

    @Test
    public void guiSettings_withCoordinates_createsValidSettings() {
        // Test that GuiSettings correctly stores help window coordinates
        GuiSettings guiSettings = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);

        Point coordinates = guiSettings.getHelpWindowCoordinates();
        assertNotNull(coordinates);
        assertEquals(TEST_HELP_X, coordinates.getX(), DELTA);
        assertEquals(TEST_HELP_Y, coordinates.getY(), DELTA);
        assertEquals(TEST_HELP_WIDTH, guiSettings.getHelpWindowWidth(), DELTA);
        assertEquals(TEST_HELP_HEIGHT, guiSettings.getHelpWindowHeight(), DELTA);
    }

    @Test
    public void guiSettings_withoutCoordinates_hasNullCoordinates() {
        // Test default GuiSettings has null coordinates
        GuiSettings guiSettings = new GuiSettings();

        assertEquals(null, guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void guiSettings_mainWindowOnly_hasNullHelpCoordinates() {
        // Test 4-param constructor has null help window coordinates
        GuiSettings guiSettings = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y);

        assertEquals(null, guiSettings.getHelpWindowCoordinates());
        assertEquals(DEFAULT_HELP_WIDTH, guiSettings.getHelpWindowWidth(), DELTA); // Default
        assertEquals(DEFAULT_HELP_HEIGHT, guiSettings.getHelpWindowHeight(), DELTA); // Default
    }

    @Test
    public void guiSettings_defensiveCopy_coordinatesNotShared() {
        // Test that getHelpWindowCoordinates returns a defensive copy
        GuiSettings guiSettings = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);

        Point coordinates1 = guiSettings.getHelpWindowCoordinates();
        Point coordinates2 = guiSettings.getHelpWindowCoordinates();

        assertNotNull(coordinates1);
        assertNotNull(coordinates2);
        // Should be different objects (defensive copy)
        assertTrue(coordinates1 != coordinates2);
        // But have same values
        assertEquals(coordinates1.getX(), coordinates2.getX(), DELTA);
        assertEquals(coordinates1.getY(), coordinates2.getY(), DELTA);
    }

    @Test
    public void guiSettings_modifyReturnedCoordinates_doesNotAffectOriginal() {
        // Test that modifying returned coordinates doesn't affect stored values
        GuiSettings guiSettings = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);

        Point coordinates = guiSettings.getHelpWindowCoordinates();
        coordinates.setLocation(MODIFIED_COORDINATE, MODIFIED_COORDINATE);

        Point freshCoordinates = guiSettings.getHelpWindowCoordinates();
        assertEquals(TEST_HELP_X, freshCoordinates.getX(), DELTA);
        assertEquals(TEST_HELP_Y, freshCoordinates.getY(), DELTA);
    }

    @Test
    public void guiSettings_multipleCoordinatePairs_maintainsIndependence() {
        // Test that main window and help window coordinates are independent
        GuiSettings guiSettings = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT,
                ALTERNATIVE_MAIN_X, ALTERNATIVE_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);

        Point mainCoordinates = guiSettings.getWindowCoordinates();
        Point helpCoordinates = guiSettings.getHelpWindowCoordinates();

        assertNotNull(mainCoordinates);
        assertNotNull(helpCoordinates);

        assertEquals(ALTERNATIVE_MAIN_X, mainCoordinates.getX(), DELTA);
        assertEquals(ALTERNATIVE_MAIN_Y, mainCoordinates.getY(), DELTA);
        assertEquals(TEST_HELP_X, helpCoordinates.getX(), DELTA);
        assertEquals(TEST_HELP_Y, helpCoordinates.getY(), DELTA);
    }

    @Test
    public void guiSettings_equalityWithHelpCoordinates_worksCorrectly() {
        // Test equality comparison with help window coordinates
        GuiSettings settings1 = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);
        GuiSettings settings2 = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);

        assertEquals(settings1, settings2);
        assertEquals(settings1.hashCode(), settings2.hashCode());
    }

    @Test
    public void guiSettings_differentHelpCoordinates_notEqual() {
        // Test that different help coordinates make objects unequal
        GuiSettings settings1 = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, TEST_HELP_X, TEST_HELP_Y);
        GuiSettings settings2 = new GuiSettings(TEST_MAIN_WIDTH, TEST_MAIN_HEIGHT, TEST_MAIN_X, TEST_MAIN_Y,
                TEST_HELP_WIDTH, TEST_HELP_HEIGHT, ALTERNATIVE_HELP_X, ALTERNATIVE_HELP_Y);

        assertTrue(!settings1.equals(settings2));
    }
}
