package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Note(null));
    }

    @Test
    public void constructor_validNote_success() {
        String text = "This is a note";
        Note note = new Note(text);
        assertEquals(text, note.value);
    }

    @Test
    public void equals() {
        Note note1 = new Note("Note A");
        Note note2 = new Note("Note A");
        Note note3 = new Note("Note B");

        assertTrue(note1.equals(note2));
        assertTrue(note1.equals(note1));
        assertFalse(note1.equals(note3));
        assertFalse(note1.equals(null));
        assertFalse(note1.equals("Not a Note"));
    }

    @Test
    public void toString_returnsValue() {
        String text = "Some note";
        Note note = new Note(text);
        assertEquals(text, note.toString());
    }

    @Test
    public void hashCode_consistentWithEquals() {
        Note note1 = new Note("Note X");
        Note note2 = new Note("Note X");
        Note note3 = new Note("Note Y");

        assertTrue(note1.hashCode() == note2.hashCode());
        assertFalse(note1.hashCode() == note3.hashCode());
    }

    @Test
    public void constructor_escapeCharacters_success() {
        String noteText = "Line1\nLine2\tTabbed\\Backslash";
        Note note = new Note(noteText);
        assertEquals(noteText, note.value);
    }

    @Test
    public void constructor_commandLikeStrings_success() {
        String noteText = "delete 1";
        Note note = new Note(noteText);
        assertEquals(noteText, note.value);
    }

    @Test
    public void equals_escapeCharacters() {
        Note note1 = new Note("Hello\nWorld");
        Note note2 = new Note("Hello\nWorld");
        Note note3 = new Note("Hello\tWorld");
        assertTrue(note1.equals(note2));
        assertFalse(note1.equals(note3));
    }
}
