package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's note in the address book.
 * Guarantees: immutable; is always valid
 */
public class Note {

    public static final String DEFAULT_NOTE = "No current note";
    public final String value;

    /**
     * Constructs a {@code Note}.
     * The given {@code note} must not be null.
     *
     * @param note The note content to store.
     */
    public Note(String note) {
        requireNonNull(note);
        this.value = note;
    }

    @Override
    public String toString() {
        if (this.value.isEmpty()) {
            return DEFAULT_NOTE;
        }
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Note // instanceof handles nulls
                && value.equals(((Note) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
