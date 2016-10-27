package com.xiaoyuz.comicengine.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public final class FileUtils {
    private static final String TAG = "FileUtils";
    private static final int BUFFER_SIZE = 4096;

    /**
     * Copies all the bytes from one file to another.
     * If to represents an existing file, that file will be overwritten with the
     * contents of from. If to equals from, then do nothing and return false.
     * If to and from refer to the same file, the behavior is uncertain.
     *
     * @param from the source file
     * @param to the destination file
     * @return true on success
     */
    public static boolean copy(File from, File to) {
        InputStream is = null;
        OutputStream os = null;

        if (from.equals(to)) {
            return false;
        }

        try {
            is = new FileInputStream(from);
            os = new FileOutputStream(to);
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            StreamUtils.closeQuietly(is);
            StreamUtils.closeQuietly(os);
        }
    }

    /**
     * Reads all characters from a file into a String, using the given character
     * set.
     *
     * @param from the file to read from.
     * @param charset the charset used to decode the input stream.
     * @return a string containing all the characters from the file or null on error.
     */
    @Nullable
    public static String read(File from, Charset charset) {
        Reader reader = null;

        try {
            reader = new InputStreamReader(new FileInputStream(from), charset);
            char[] buffer = new char[BUFFER_SIZE];
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.w(TAG, e);
        } finally {
            StreamUtils.closeQuietly(reader);
        }

        return null;
    }

    /**
     * Reads all characters from a file into a String, using the default char set.
     *
     * @param from the file to read from.
     * @return a string containing all the characters from the file or null on error.
     */
    @Nullable
    public static String read(File from) {
        return read(from, Charset.defaultCharset());
    }

    /**
     * Writes a character sequence (such as a string) to a file using the given
     * character set.
     *
     * @param from the character sequence to write.
     * @param to the destination file.
     * @param charset the charset used to encode the output stream.
     * @return true on success
     */
    public static boolean write(CharSequence from, File to, Charset charset) {
        Writer writer = null;

        try {
            writer = new OutputStreamWriter(new FileOutputStream(to), charset).append(from);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            StreamUtils.closeQuietly(writer);
        }
    }

    public static void write(File file, byte[] input) throws IOException {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(input);
        } finally {
            StreamUtils.closeQuietly(stream);
        }
    }

    public static File getUniqueDir(File dest) {
        if (dest.exists() && !dest.isDirectory()) {
            String path = dest.getAbsolutePath();
            for (int i = 1;; i++) {
                dest = new File(path + i);
                if (!dest.exists() || dest.isDirectory()) {
                    break;
                }
            }
        }

        if (!dest.exists()) checkMkdir(dest);
        return dest;
    }

    /**
     * Creates the directory named by the provided {@link File}, assuming its parents exist.
     *
     * @param directory The directory to create.
     * @return true if the directory was created, false on failure or if the directory already
     * existed.
     */
    public static boolean checkMkdir(File directory) {
        return checkMkdirCreated(directory.mkdir(), directory);
    }

    /**
     * Creates the directory named by the provided {@link File}, creating missing parent directories
     * if necessary.
     *
     * @param directory The directory to create.
     * @return true if the directory was created, false on failure or if the directory already
     * existed.
     */
    public static boolean checkMkdirs(File directory) {
        return checkMkdirCreated(directory.mkdirs(), directory);
    }

    private static boolean checkMkdirCreated(boolean created, File directory) {
        String messageFormat = "Could not create directory: %s";
        if (!created) {
            Log.e(TAG, String.format(messageFormat, directory.getPath()));
        }

        return created;
    }

    /**
     * Recursively delete files and directories.
     *
     * @return true if successful
     */
    public static boolean deleteRecursive(File target) {
        if (!target.exists()) {
            // Already gone, that's certainly a success.
            return true;
        }

        return deleteRecursiveInternal(target);
    }

    private static boolean deleteRecursiveInternal(File target) {
        if (target.isDirectory()) {
            File[] list = target.listFiles();

            if (list != null) {
                for (File file : list) {
                    if (!deleteRecursiveInternal(file)) {
                        return false;
                    }
                }
            }
        }

        return target.delete();
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}
