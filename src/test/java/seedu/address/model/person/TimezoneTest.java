package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class TimezoneTest {

    private static final double SECONDS_PER_HOUR = 3600;

    private static double getLocalOffset() {
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        return offset.getTotalSeconds() / SECONDS_PER_HOUR;
    }

    private String computeExpectedRelative(double tzOffset, double referenceOffset) {
        double diff = tzOffset - referenceOffset;

        if (diff == 0) {
            return "Same as system time";
        }

        int totalMinutes = (int) Math.round(diff * 60);
        int hours = totalMinutes / 60;
        int minutes = Math.abs(totalMinutes % 60);
        String aheadBehind = diff > 0 ? "ahead of" : "behind";

        if (minutes == 0) {
            return String.format("%d hour(s) %s system time", Math.abs(hours), aheadBehind);
        } else {
            return String.format("%d hour(s) %d minute(s) %s system time", Math.abs(hours), minutes, aheadBehind);
        }
    }

    @Test
    public void constructor_invalidTz_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Timezone(25.0)); // > 24
        assertThrows(IllegalArgumentException.class, () -> new Timezone(-25.0)); // < -24
    }

    @Test
    public void isValidTz() {
        assertTrue(Timezone.isValidTz(0.0));
        assertTrue(Timezone.isValidTz(5.5));
        assertTrue(Timezone.isValidTz(-7.75));
        assertTrue(Timezone.isValidTz(Timezone.NO_TIMEZONE));

        assertFalse(Timezone.isValidTz(25.0));
        assertFalse(Timezone.isValidTz(-25.0));
    }

    @Test
    public void equals() {
        Timezone tz1 = new Timezone(5.5);
        Timezone tz2 = new Timezone(5.5);
        Timezone tz3 = new Timezone(8.0);

        assertTrue(tz1.equals(tz2));
        assertTrue(tz1.equals(tz1));
        assertFalse(tz1.equals(tz3));
        assertFalse(tz1.equals(null));
        assertFalse(tz1.equals("UTC+5.5"));
    }

    @Test
    public void toStringAndRelative() {
        double localOffset = getLocalOffset();

        Timezone tz = new Timezone(0.0);
        String expectedRelative = computeExpectedRelative(tz.tzOffset, localOffset);
        String expectedString = String.format("UTC+0.0 (%s)", expectedRelative);
        assertEquals(expectedRelative, tz.getRelativeToLocal());
        assertEquals(expectedString, tz.toString());

        Timezone tz2 = new Timezone(8.0);
        expectedRelative = computeExpectedRelative(tz2.tzOffset, localOffset);
        expectedString = String.format("UTC+8.0 (%s)", expectedRelative);
        assertEquals(expectedRelative, tz2.getRelativeToLocal());
        assertEquals(expectedString, tz2.toString());

        Timezone tz3 = new Timezone(-13.5);
        expectedRelative = computeExpectedRelative(tz3.tzOffset, localOffset);
        expectedString = String.format("UTC-13.5 (%s)", expectedRelative);
        assertEquals(expectedRelative, tz3.getRelativeToLocal());
        assertEquals(expectedString, tz3.toString());
    }
}
