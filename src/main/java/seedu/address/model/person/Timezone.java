package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
            + "the time offset from UTC in hours. This value cannot be >=24.0 or <=-24.0.";

    public static final double NO_TIMEZONE = -999;
    private static final double MAX_TIMEZONE = 24.0;
    private static final double MIN_TIMEZONE = -24.0;
    private static final double SECONDS_PER_HOUR = 3600;
    private static final int MINUTES_PER_HOUR = 60;

    public final double tzOffset;

    /**
     * Constructs a {@code Timezone}.
     *
     * @param tzOffset A valid double value timezone offset.
     */
    public Timezone(double tzOffset) {
        checkArgument(isValidTz(tzOffset), MESSAGE_CONSTRAINTS);
        this.tzOffset = tzOffset;
    }

    /**
     * Validates a double value if it's a valid timezone value
     *
     * Values accept are:
     * -24 < timezoneValue < 24 or
     * special value -999
     *
     * Other functions calling this must decide if the special value
     * is allowed at their point of ingestion. (e.g. a save file load might accept it but not via the AddCommand)
     * */
    public static boolean isValidTz(double tz) {
        return (tz < MAX_TIMEZONE && tz > MIN_TIMEZONE) || (tz == NO_TIMEZONE);
    }

    /**
     * Returns the local system's UTC offset in hours.
     */
    private static double getLocalOffsetHours() {

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        return offset.getTotalSeconds() / SECONDS_PER_HOUR; //Converting to numerical value as offset is an object
    }

    /**
     * Returns the relative time difference between this timezone and the local system time.
     * Example outputs:
     *  - "Same as local time"
     *  - "3 hours ahead of local time"
     *  - "5 hours behind local time"
     */
    public String getRelativeToLocal() {

        double localOffset = getLocalOffsetHours();
        double diff = tzOffset - localOffset;

        if (diff == 0) {
            return "Same as local time";
        }

        int totalMinutes = (int) Math.round(diff * MINUTES_PER_HOUR);
        int hours = totalMinutes / MINUTES_PER_HOUR;
        int minutes = Math.abs(totalMinutes % MINUTES_PER_HOUR);
        String aheadBehind = diff > 0 ? "ahead" : "behind";

        if (minutes == 0) {
            return String.format("%d hours %s of local time", Math.abs(hours), aheadBehind);
        } else {
            return String.format("%d hours %d minutes %s of local time", Math.abs(hours), minutes, aheadBehind);
        }
    }

    @Override
    public String toString() {
        if (tzOffset == NO_TIMEZONE) {
            return "No Timezone Specified";
        }

        String baseTz = "UTC" + ((tzOffset >= 0) ? "+" : "") + tzOffset;
        String relative = getRelativeToLocal();

        // Only show relative if different from base or NO_TIMEZONE
        if (!relative.equals(baseTz)) {
            return String.format("%s (%s)", baseTz, relative);
        } else {
            return baseTz;
        }
    }

    @Override
    public boolean equals(Object other) {
        //Using java instanceof pattern matching (Java 14)
        return this == other
                || (other instanceof Timezone otherTz
                && tzOffset == otherTz.tzOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tzOffset);
    }
}
