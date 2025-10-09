package seedu.address.model.person;

import java.util.Objects;

/**
 * Represents a Person's timezone in the address book.
 *
 * Stores a floating point offset in HOURS from UTC
 * Guarantees: immutable; is always valid
 *
 * Refer to https://en.wikipedia.org/wiki/List_of_UTC_offsets
 * for a list of allowable UTC offsets. Generally, not +/-24.0 hrs from UTC
 */
public class Timezone {
    public static final String MESSAGE_CONSTRAINTS = "Timezone value is a floating point number that represents\n"
            + "the time offset from UTC in hours. This value cannot be >24.0 or <-24.0.";

    public static final double NO_TIMEZONE = -999;
    private static final double MAX_TIMEZONE = 24.0;
    private static final double MIN_TIMEZONE = -24.0;

    public final double tzOffset;

    public Timezone(double tzOffset) {
        this.tzOffset = tzOffset;
    }

    public static boolean isValidTz(double tz) {
        return (tz < MAX_TIMEZONE && tz > MIN_TIMEZONE) || (tz == NO_TIMEZONE);
    }

    public String getTzOffset(Timezone other) {
        return "NOT IMPLEMENTED";
    }

    @Override
    public String toString() {
        if (tzOffset == NO_TIMEZONE) {
            return "No Timezone Specified";
        }

        return ((tzOffset > 0) ? "+" : "-") + String.format("%.2f", tzOffset);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof Timezone
                && tzOffset == ((Timezone) other).tzOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tzOffset);
    }
}
