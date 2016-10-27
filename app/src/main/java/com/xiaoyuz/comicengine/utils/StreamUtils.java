package com.xiaoyuz.comicengine.utils;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Collection of utility functions for working with streams.
 */
public class StreamUtils {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Converts {@link InputStream} <var>is</var> to a UTF string and closes the stream.
     * Credits: mobile/android
     * @param is input stream from which string should be read
     * @return UTF string read from input stream
     * @throws IOException IO exception
     */
    @NonNull
    public static String toUtfString(@NonNull InputStream is) throws IOException {
        try {
            return toString(is, Charset.forName("UTF-8"));
        } catch (IllegalCharsetNameException e) {
            // Do nothing
        } catch (UnsupportedEncodingException e) {
            // Do nothing
        } finally {
            closeQuietly(is);
        }
        return "";
    }

    /**
     * Converts {@link InputStream} <var>is</var> to a string with specified
     * <var>charset</var> and closes the stream.
     * Credits: mobile/android
     * @param is      input stream from which string should be read
     * @param charset charset to be used while reading string
     * @return string read from input stream
     * @throws IOException IO exception
     */
    @NonNull
    public static String toString(@NonNull InputStream is, @NonNull Charset charset)
            throws IOException {
        final StringBuilder result = new StringBuilder();

        Reader reader = null;
        try {
            reader = new InputStreamReader(is, charset);
            final char[] buffer = new char[BUFFER_SIZE];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                result.append(buffer, 0, read);
            }
        } finally {
            closeQuietly(reader);
        }

        return result.toString();
    }

    public static byte[] readByteArray(InputStream in) throws IOException {
        int total = in.available();
        if (total > 0 && in instanceof AssetManager.AssetInputStream) {
            // For assets, the return value of available() is always right,
            // because each asset's length is recorded by AssetManager.
            // See native code of AssetManager.getAssetLength.
            byte[] buffer = new byte[total];
            return buffer;
        } else {
            // in.available() is not foolproof, but often corresponds to the
            // final size of the resource, so the ByteArrayOutputStream doesn't
            // have to resize as often.
            ByteArrayOutputStream out = new ByteArrayOutputStream(total);
            copy(in, out);
            return out.toByteArray();
        }
    }

    public static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = from.read(buffer, 0, buffer.length)) >= 0) {
            to.write(buffer, 0, read);
        }
    }

    /**
     * Close a stream without needing to check if the stream is null or if an
     * exception is thrown.
     * @param stream Stream to close.
     */
    public static void closeQuietly(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignore.
        }
    }
}