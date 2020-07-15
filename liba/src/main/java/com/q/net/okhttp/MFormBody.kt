package com.q.net.okhttp

import com.q.net.okhttp.MFormBodyHelper.FORM_ENCODE_SET
import com.q.net.okhttp.MFormBodyHelper.canonicalize
import com.q.net.okhttp.MFormBodyHelper.percentDecode
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.internal.toImmutableList
import okio.Buffer
import okio.BufferedSink
import java.io.IOException
import java.nio.charset.Charset

class MFormBody internal constructor(
    encodedNames: List<String>,
    encodedValues: List<String>
) : RequestBody() {
    private val encodedNames: List<String> = encodedNames.toImmutableList()
    private val encodedValues: List<String> = encodedValues.toImmutableList()

    /** The number of key-value pairs in this form-encoded body. */
    @get:JvmName("size")
    val size: Int
        get() = encodedNames.size

    @JvmName("-deprecated_size")
    @Deprecated(
        message = "moved to val",
        replaceWith = ReplaceWith(expression = "size"),
        level = DeprecationLevel.ERROR
    )
    fun size(): Int = size

    fun encodedName(index: Int) = encodedNames[index]

    fun name(index: Int) = encodedName(index).percentDecode(plusIsSpace = true)

    fun encodedValue(index: Int) = encodedValues[index]

    fun value(index: Int) = encodedValue(index).percentDecode(plusIsSpace = true)

    override fun contentType() = CONTENT_TYPE

    override fun contentLength() = writeOrCountBytes(null, true)

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        writeOrCountBytes(sink, false)
    }

    /**
     * Either writes this request to [sink] or measures its content length. We have one method
     * do double-duty to make sure the counting and content are consistent, particularly when it comes
     * to awkward operations like measuring the encoded length of header strings, or the
     * length-in-digits of an encoded integer.
     */
    private fun writeOrCountBytes(sink: BufferedSink?, countBytes: Boolean): Long {
        var byteCount = 0L
        val buffer: Buffer = if (countBytes) Buffer() else sink!!.buffer

        for (i in 0 until encodedNames.size) {
            if (i > 0) buffer.writeByte('&'.toInt())
            buffer.writeUtf8(encodedNames[i])
            buffer.writeByte('='.toInt())
            buffer.writeUtf8(encodedValues[i])
        }

        if (countBytes) {
            byteCount = buffer.size
            buffer.clear()
        }

        return byteCount
    }

    class Builder @JvmOverloads constructor(private val charset: Charset? = null) {
        private val names = mutableListOf<String>()
        private val values = mutableListOf<String>()

        fun add(name: String, value: String) = apply {
            val tempName = name.canonicalize(
                encodeSet = FORM_ENCODE_SET,
                plusIsSpace = true,
                charset = charset
            )
            if (names.contains(tempName)) {
                names -= tempName
            }
            names += tempName
            val tempValue = value.canonicalize(
                encodeSet = FORM_ENCODE_SET,
                plusIsSpace = true,
                charset = charset
            )
            if (values.contains(tempValue)) {
                values -= tempValue
            }
            values += tempValue
        }

        fun addEncoded(name: String, value: String) = apply {
            val tempName = name.canonicalize(
                encodeSet = FORM_ENCODE_SET,
                alreadyEncoded = true,
                plusIsSpace = true,
                charset = charset
            )
            if (names.contains(tempName)) {
                names -= tempName
            }
            names += tempName
            val tempValue = value.canonicalize(
                encodeSet = FORM_ENCODE_SET,
                alreadyEncoded = true,
                plusIsSpace = true,
                charset = charset
            )
            if (values.contains(tempValue)) {
                values -= tempValue
            }
            values += tempValue
        }

        fun build(): MFormBody = MFormBody(names, values)
    }

    companion object {
        private val CONTENT_TYPE: MediaType = "application/x-www-form-urlencoded".toMediaType()
    }
}



