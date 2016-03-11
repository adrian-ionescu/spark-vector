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
package com.actian.spark_vector.colbuffer.time

import com.actian.spark_vector.colbuffer._
import com.actian.spark_vector.colbuffer.util.TimeConversion

import java.nio.ByteBuffer
import java.sql.Timestamp

private class TimeIntColumnBuffer(maxValueCount: Int, name: String, scale: Int, nullable: Boolean, converter: TimeConversion.TimeConverter, adjustToUTC: Boolean) extends
  TimeColumnBuffer(maxValueCount, IntSize, name, scale, nullable, converter, adjustToUTC) {

  override protected def putConverted(converted: Long, buffer: ByteBuffer): Unit = buffer.putInt(converted.toInt)
}

private[colbuffer] trait TimeIntColumnBufferInstance extends TimeColumnBufferInstance {

  private[colbuffer] override def getNewInstance(name: String, precision: Int, scale: Int, nullable: Boolean, maxValueCount: Int): ColumnBuffer[_] =
     new TimeIntColumnBuffer(maxValueCount, name, scale, nullable, createConverter(), adjustToUTC)
}