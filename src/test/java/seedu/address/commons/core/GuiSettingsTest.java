package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Point;

import org.junit.jupiter.api.Test;

public class GuiSettingsTest {

    @Test
    public void constructor_default_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings();
        assertNull(guiSettings.getWindowCoordinates());
        assertNull(guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void constructor_withMainWindowParams_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings(800, 600, 100, 100);
        assertEquals(new Point(100, 100), guiSettings.getWindowCoordinates());
        assertNull(guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void constructor_withAllParams_initializesCorrectly() {
        GuiSettings guiSettings = new GuiSettings(800, 600, 100, 100, 700, 550, 200, 150);
        assertEquals(new Point(100, 100), guiSettings.getWindowCoordinates());
        assertEquals(new Point(200, 150), guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void getWindowCoordinates_nonNullCoordinates_returnsDefensiveCopy() {
        GuiSettings guiSettings = new GuiSettings(800, 600, 100, 100);
        Point coordinates = guiSettings.getWindowCoordinates();
        coordinates.setLocation(999, 999);
        assertEquals(new Point(100, 100), guiSettings.getWindowCoordinates());
    }

    @Test
    public void getHelpWindowCoordinates_nonNullCoordinates_returnsDefensiveCopy() {
        GuiSettings guiSettings = new GuiSettings(800, 600, 100, 100, 700, 550, 200, 150);
        Point coordinates = guiSettings.getHelpWindowCoordinates();
        coordinates.setLocation(999, 999);
        assertEquals(new Point(200, 150), guiSettings.getHelpWindowCoordinates());
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        GuiSettings guiSettings1 = new GuiSettings(800, 600, 100, 100, 700, 550, 200, 150);
        GuiSettings guiSettings2 = new GuiSettings(800, 600, 100, 100, 700, 550, 300, 150);
        assertFalse(guiSettings1.equals(guiSettings2));
    }

    @Test
    public void hashCode_differentValues_returnsDifferentHashCode() {
        GuiSettings guiSettings1 = new GuiSettings(800, 600, 100, 100, 700, 550, 200, 150);
        GuiSettings guiSettings2 = new GuiSettings(900, 600, 100, 100, 700, 550, 200, 150);
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
}
