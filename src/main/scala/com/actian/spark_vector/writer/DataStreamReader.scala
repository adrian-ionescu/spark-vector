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
package com.actian.spark_vector.writer

import scala.annotation.tailrec
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import com.actian.spark_vector.vector.ErrorCodes
import com.actian.spark_vector.vector.VectorException
import org.apache.spark.Logging

object DataStreamReader extends Logging {
  // scalastyle:off magic.number
  /**
   * Read a Vector `code` from `in` and return `false` if a different
   * code was read from the `ByteBuffer`
   */
  @tailrec
  def readCode(in: ByteBuffer, code: Array[Int]): Boolean = {
    if (in.getInt() != code(0)) {
      false
    }
    code.length == 1 || readCode(in, code.tail)
  }

  /** Read a variable length message's length from `in` */
  def readLength(in: ByteBuffer): Long = {
    in.get() match {
      case x if (x & 0xFF) < 255 => x & 0xFF
      case _ => 255 + readLength(in)
    }
  }

  /** Read a `ByteArray` from `in` */
  def readByteArray(in: ByteBuffer): Array[Byte] = {
    val len = readLength(in)
    logTrace(s"preparing to read an array of size $len")
    val ret = Array.ofDim[Byte](len.toInt)
    in.get(ret, 0, len.toInt)
    ret
  }

  /** Read an ASCII string from `in` */
  def readString(in: ByteBuffer): String = {
    new String(readByteArray(in), "ASCII")
  }

  /** Read a byte buffer of length `len from `socket` */
  def readByteBuffer(len: Int)(implicit socket: SocketChannel): ByteBuffer = {
    val buffer = ByteBuffer.allocate(len)
    var i = 0
    while (i < len) {
      val j = socket.read(buffer)
      if (j <= 0) throw new VectorException(ErrorCodes.CommunicationError,
        s"Connection to Vector end point has been closed or amount of data communicated does not match the message length")
      i += j
    }
    buffer.flip()
    buffer
  }

  /** Read a byte buffer and its length integer from `socket` */
  def readByteBufferWithLength(implicit socket: SocketChannel): ByteBuffer = {
    val len = readByteBuffer(4).getInt()
    logTrace(s"peading a byte buffer with length: Will be reading ${len - 4} bytes")
    readByteBuffer(len - 4)
  }

  /** Read data from `socket`, store it in a `ByteBuffer` and execute the `code` that reads from it */
  def readWithByteBuffer[T](code: ByteBuffer => T)(implicit socket: SocketChannel): T = {
    val buffer = readByteBufferWithLength
    code(buffer)
  }
  // scalastyle:on magic.number
}