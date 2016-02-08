package com.actian.spark_vectorh.buffer.time;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public final class TimeConversion {

    public final static int MILLISECONDS_IN_MINUTE = 60 * 1000;
    public static final int MILLISECONDS_IN_DAY = 24 * 60 * MILLISECONDS_IN_MINUTE;
    public static final long NANOSECONDS_IN_DAY = (long) MILLISECONDS_IN_DAY * 1000000;
    public static final int MILLISECONDS_SCALE = 3;
    public static final int NANOSECONDS_SCALE = 9;
    public static final int NANOS_IN_MILLI = 1000000;
    public static final int[] powersOfTen = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };

    private TimeConversion() {
        throw new IllegalStateException();
    }

    public static long timeInNanos(Timestamp source) {
        return (source.getTime() / 1000) * powersOfTen[NANOSECONDS_SCALE] + source.getNanos();
    }

    public static long normalizedTime(Timestamp source) {
        return normalizedTime(timeInNanos(source));
    }

    public static long normalizedTime(long nanos) {
        long remainder = nanos % NANOSECONDS_IN_DAY;
        return remainder >= 0 ? remainder : remainder + NANOSECONDS_IN_DAY;
    }

    public static long scaledTime(Timestamp source, int scale) {
        /* scale should be <= 9 */
        return scaledTime(timeInNanos(source), scale);
    }

    public static long scaledTime(long nanos, int scale) {
        int adjustment = NANOSECONDS_SCALE - scale;
        return nanos / powersOfTen[adjustment];
    }

    public static void convertLocalDateToUTC(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        date.setTime(date.getTime() + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
    }

    public static void convertLocalTimeStampToUTC(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        int nanos = time.getNanos();
        time.setTime(time.getTime() + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
        time.setNanos(nanos);
    }

    public static abstract class TimeConverter {
        public long convert(Timestamp source, int scale) {
            return convert(timeInNanos(source), scale);
        }

        public abstract long convert(long nanos, int scale);
    }
}
