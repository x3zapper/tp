package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * Represents the date a person got added to the address book.
 * Guarantees: immutable; is always valid assuming the user does not edit the data file to set the value to null.
 *
 * The property "value" is not supposed to be changed by the user.
 */
public class DateAdded {
    public static final String MESSAGE_CONSTRAINTS = "DateAdded value needs to be a non-null datatype of VALID type "
        + "Instant. That is, the passed in argument for the String parameter constructor must pass "
        + "Instant.parse(argument)";

    public final Instant value;

    /**
     * Constructs a {@code DateAdded}
     * For use when adding a new person.
     */
    public DateAdded() {
        this.value = Instant.now();
    }

    /**
     * Constructs a {@code DateAdded}
     * For use when recreating a person through edit.
     * NOT supposed to be used for adding new persons.
     *
     * @param value A valid timestamp
     */
    public DateAdded(Instant value) {
        requireNonNull(value);
        this.value = value;
    }

    /**
     * Constructs a {@code DateAdded}
     * For use when recreating a person through JSON reads.
     * NOT supposed to be used for adding new persons.
     *
     * @param value A string that is valid when doing Instant.parse(value)
     */
    public DateAdded(String value) {
        requireNonNull(value);
        checkArgument(isValidDateAdded(value), MESSAGE_CONSTRAINTS);
        this.value = Instant.parse(value);
    }

    /**
     * Returns if a given string is a valid Instant after parsing
     * .
     * @param testValue String that would become Instant after parsing
     * @return boolean of if the dateAdded string is valid
     */
    public static boolean isValidDateAdded(String testValue) {
        try {
            Instant result = Instant.parse(testValue);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        assert value != null;
        return value.toString();
    }

    // function is based off the equals function from other original AB3 Person properties
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateAdded)) {
            return false;
        }

        DateAdded otherDateAdded = (DateAdded) other;
        return value.equals(otherDateAdded.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
