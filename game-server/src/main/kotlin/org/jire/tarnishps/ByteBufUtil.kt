package org.jire.tarnishps

import io.netty.buffer.ByteBuf
import io.netty.util.ByteProcessor
import java.nio.charset.Charset

/**
 * @author Jire
 */
object ByteBufUtil {

    private const val STRING_DELIMITER_317 = 10.toByte()

    private val stringByteProcessor317 = ByteProcessor.IndexOfProcessor(STRING_DELIMITER_317)

    @JvmStatic
    fun ByteBuf.readStringArray(): ByteArray {
        val start = readerIndex()

        val end = forEachByte(stringByteProcessor317)
        require(end != -1) {
            "Unterminated string"
        }

        val length = end - start
        val bytes = ByteArray(length)
        readBytes(bytes)

        return bytes
    }

    @JvmStatic
    @JvmOverloads
    fun ByteBuf.readString(charset: Charset = Charsets.UTF_8): String {
        val start = readerIndex()

        val end = forEachByte(stringByteProcessor317)
        require(end != -1) {
            "Unterminated string"
        }

        val length = end - start
        val string = toString(start, length, charset)
            .intern()

        readerIndex(end + 1)

        return string
    }

}