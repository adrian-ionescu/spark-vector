package com.actian.spark_vectorh.buffer.timestamp;

import static com.actian.spark_vectorh.buffer.timestamp.TimestampLZColumnFactoryCommons.isSupportedColumnType;

import com.actian.spark_vectorh.buffer.timestamp.TimestampConversion.TimestampConverter;
import com.actian.spark_vectorh.buffer.timestamp.TimestampLZColumnFactoryCommons.TimestampLZConverter;

public final class TimestampLZLongColumnBufferFactory extends TimestampLongColumnBufferFactory {
    private static final int MIN_TIMESTAMP_LZ_LONG_SCALE = 0;
    private static final int MAX_TIMESTAMP_LZ_LONG_SCALE = 7;

    @Override
    public boolean adjustToUTC() {
        return false;
    }

    @Override
    public boolean supportsColumnType(String type, int precision, int scale, boolean nullable) {
        return isSupportedColumnType(type, scale, MIN_TIMESTAMP_LZ_LONG_SCALE, MAX_TIMESTAMP_LZ_LONG_SCALE);
    }

    @Override
    protected TimestampConverter createConverter() {
        return new TimestampLZConverter();
    }
}
