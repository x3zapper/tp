package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class HelpWindowTest {

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
}
