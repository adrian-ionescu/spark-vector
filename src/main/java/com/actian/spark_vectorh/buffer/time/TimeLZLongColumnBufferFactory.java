package com.actian.spark_vectorh.buffer.time;

import static com.actian.spark_vectorh.buffer.time.TimeLZColumnFactoryCommons.isSupportedColumnType;

import com.actian.spark_vectorh.buffer.time.TimeConversion.TimeConverter;
import com.actian.spark_vectorh.buffer.time.TimeLZColumnFactoryCommons.TimeLZConverter;

public final class TimeLZLongColumnBufferFactory extends TimeLongColumnBufferFactory {
    private static final int MIN_TIME_LONG_LZ_SCALE = 5;
    private static final int MAX_TIME_LONG_LZ_SCALE = 9;

    @Override
    public boolean adjustToUTC() {
        return false;
    }

    @Override
    public boolean supportsColumnType(String type, int precision, int scale, boolean nullable) {
        return isSupportedColumnType(type, scale, MIN_TIME_LONG_LZ_SCALE, MAX_TIME_LONG_LZ_SCALE);
    }

    @Override
    protected TimeConverter createConverter() {
        return new TimeLZConverter();
    }
}
