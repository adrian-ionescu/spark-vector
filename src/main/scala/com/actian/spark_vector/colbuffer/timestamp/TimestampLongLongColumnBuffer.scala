/*
 * Copyright 2016 Actian Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.actian.spark_vector.colbuffer.timestamp

import com.actian.spark_vector.colbuffer._
import com.actian.spark_vector.colbuffer.util.TimestampConversion
import com.actian.spark_vector.colbuffer.util.BigIntegerConversion

import java.nio.ByteBuffer
import java.math.BigInteger
import java.sql.Timestamp

private class TimestampLongLongColumnBuffer(maxValueCount: Int, name: String, scale: Int, nullable: Boolean, converter: TimestampConversion.TimestampConverter,
  adjustToUTC: Boolean) extends TimestampColumnBuffer(maxValueCount, LongLongSize, name, scale, nullable, converter, adjustToUTC) {

  override protected def putConverted(converted: BigInteger, buffer: ByteBuffer): Unit = buffer.put(BigIntegerConversion.convertToLongLongByteArray(converted))
}

private[colbuffer] trait TimestampLongLongColumnBufferInstance extends TimestampColumnBufferInstance {

  private[colbuffer] override def getNewInstance(name: String, precision: Int, scale: Int, nullable: Boolean, maxValueCount: Int): ColumnBuffer[_] =
     new TimestampLongLongColumnBuffer(maxValueCount, name, scale, nullable, createConverter(), adjustToUTC)
}