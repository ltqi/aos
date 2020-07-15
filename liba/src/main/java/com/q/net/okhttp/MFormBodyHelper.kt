package com.q.net.okhttp

import okhttp3.internal.parseHexDigit
import okio.Buffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object MFormBodyHelper {
    internal const val FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~"
    internal val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    internal fun String.percentDecode(
        pos: Int = 0,
        limit: Int = length,
        plusIsSpace: Boolean = false
    ): String {
        for (i in pos until limit) {
            val c = this[i]
            if (c == '%' || c == '+' && plusIsSpace) {
                // Slow path: the character at i requires decoding!
                val out = Buffer()
                out.writeUtf8(this, pos, i)
                out.writePercentDecoded(this, pos = i, limit = limit, plusIsSpace = plusIsSpace)
                return out.readUtf8()
            }
        }

        // Fast path: no characters in [pos..limit) required decoding.
        return substring(pos, limit)
    }

    internal fun Buffer.writePercentDecoded(
        encoded: String,
        pos: Int,
        limit: Int,
        plusIsSpace: Boolean
    ) {
        var codePoint: Int
        var i = pos
        while (i < limit) {
            codePoint = encoded.codePointAt(i)
            if (codePoint == '%'.toInt() && i + 2 < limit) {
                val d1 = encoded[i + 1].parseHexDigit()
                val d2 = encoded[i + 2].parseHexDigit()
                if (d1 != -1 && d2 != -1) {
                    writeByte((d1 shl 4) + d2)
                    i += 2
                    i += Character.charCount(codePoint)
                    continue
                }
            } else if (codePoint == '+'.toInt() && plusIsSpace) {
                writeByte(' '.toInt())
                i++
                continue
            }
            writeUtf8CodePoint(codePoint)
            i += Character.charCount(codePoint)
        }
    }

    internal fun String.canonicalize(
        pos: Int = 0,
        limit: Int = length,
        encodeSet: String,
        alreadyEncoded: Boolean = false,
        strict: Boolean = false,
        plusIsSpace: Boolean = false,
        unicodeAllowed: Boolean = false,
        charset: Charset? = null
    ): String {
        var codePoint: Int
        var i = pos
        while (i < limit) {
            codePoint = codePointAt(i)
            if (codePoint < 0x20 ||
                codePoint == 0x7f ||
                codePoint >= 0x80 && !unicodeAllowed ||
                codePoint.toChar() in encodeSet ||
                codePoint == '%'.toInt() &&
                (!alreadyEncoded || strict && !isPercentEncoded(i, limit)) ||
                codePoint == '+'.toInt() && plusIsSpace
            ) {
                // Slow path: the character at i requires encoding!
                val out = Buffer()
                out.writeUtf8(this, pos, i)
                out.writeCanonicalized(
                    input = this,
                    pos = i,
                    limit = limit,
                    encodeSet = encodeSet,
                    alreadyEncoded = alreadyEncoded,
                    strict = strict,
                    plusIsSpace = plusIsSpace,
                    unicodeAllowed = unicodeAllowed,
                    charset = charset
                )
                return out.readUtf8()
            }
            i += Character.charCount(codePoint)
        }

        // Fast path: no characters in [pos..limit) required encoding.
        return substring(pos, limit)
    }

    internal fun String.isPercentEncoded(pos: Int, limit: Int): Boolean {
        return pos + 2 < limit &&
                this[pos] == '%' &&
                this[pos + 1].parseHexDigit() != -1 &&
                this[pos + 2].parseHexDigit() != -1
    }

    internal fun Buffer.writeCanonicalized(
        input: String,
        pos: Int,
        limit: Int,
        encodeSet: String,
        alreadyEncoded: Boolean,
        strict: Boolean,
        plusIsSpace: Boolean,
        unicodeAllowed: Boolean,
        charset: Charset?
    ) {
        var encodedCharBuffer: Buffer? = null // Lazily allocated.
        var codePoint: Int
        var i = pos
        while (i < limit) {
            codePoint = input.codePointAt(i)
            if (alreadyEncoded && (codePoint == '\t'.toInt() || codePoint == '\n'.toInt() ||
                        codePoint == '\u000c'.toInt() || codePoint == '\r'.toInt())) {
                // Skip this character.
            } else if (codePoint == '+'.toInt() && plusIsSpace) {
                // Encode '+' as '%2B' since we permit ' ' to be encoded as either '+' or '%20'.
                writeUtf8(if (alreadyEncoded) "+" else "%2B")
            } else if (codePoint < 0x20 ||
                codePoint == 0x7f ||
                codePoint >= 0x80 && !unicodeAllowed ||
                codePoint.toChar() in encodeSet ||
                codePoint == '%'.toInt() &&
                (!alreadyEncoded || strict && !input.isPercentEncoded(i, limit))) {
                // Percent encode this character.
                if (encodedCharBuffer == null) {
                    encodedCharBuffer = Buffer()
                }

                if (charset == null || charset == StandardCharsets.UTF_8) {
                    encodedCharBuffer.writeUtf8CodePoint(codePoint)
                } else {
                    encodedCharBuffer.writeString(input, i, i + Character.charCount(codePoint), charset)
                }

                while (!encodedCharBuffer.exhausted()) {
                    val b = encodedCharBuffer.readByte().toInt() and 0xff
                    writeByte('%'.toInt())
                    writeByte(HEX_DIGITS[b shr 4 and 0xf].toInt())
                    writeByte(HEX_DIGITS[b and 0xf].toInt())
                }
            } else {
                // This character doesn't need encoding. Just copy it over.
                writeUtf8CodePoint(codePoint)
            }
            i += Character.charCount(codePoint)
        }
    }

}
