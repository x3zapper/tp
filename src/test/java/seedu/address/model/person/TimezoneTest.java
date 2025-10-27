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
        Timezone tz = new Timezone(0.0);
        assertEquals("UTC+0.0 (8 hours behind of local time)", tz.toString());
        assertEquals("8 hours behind of local time", tz.getRelativeToLocal());

        Timezone tz2 = new Timezone(8.0);
        assertEquals("UTC+8.0 (Same as local time)", tz2.toString());
        assertEquals("Same as local time", tz2.getRelativeToLocal());

        Timezone tz3 = new Timezone(13.5);
        assertEquals("UTC+13.5 (5 hours 30 minutes ahead of local time)", tz3.toString());
        assertEquals("5 hours 30 minutes ahead of local time", tz3.getRelativeToLocal());
    }
}
