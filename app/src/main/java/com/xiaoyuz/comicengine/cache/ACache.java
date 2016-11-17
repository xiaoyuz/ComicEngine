package com.xiaoyuz.comicengine.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xiaoyuz.comicengine.utils.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ACache {

    private static final String DEFAULT_CACHE_NAME = "ACache";

    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE;
    private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
    private ACacheManager mCacheManager;

    public static ACache get(Context ctx) {
        return get(ctx, DEFAULT_CACHE_NAME);
    }

    public static ACache get(Context ctx, String cacheName) {
        File f = new File(ctx.getCacheDir(), cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static ACache get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    public static ACache get(Context ctx, long max_zise, int max_count) {
        File f = new File(ctx.getCacheDir(), "ACache");
        return get(f, max_zise, max_count);
    }

    public static ACache get(File cacheDir, long max_zise, int max_count) {
        ACache cache = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (cache == null) {
            cache = new ACache(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), cache);
        }
        return cache;
    }

    private static String myPid() {
        return "_" + android.os.Process.myPid();
    }

    private ACache(File cacheDir, long max_size, int max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + cacheDir.getAbsolutePath());
        }
        mCacheManager = new ACacheManager(cacheDir, max_size, max_count);
    }

    public void put(String key, String value) {
        File file = mCacheManager.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    public void put(String key, String value, int saveTime) {
        put(key, CacheUtils.newStringWithDateInfo(saveTime, value));
    }

    public String getAsString(String key) {
        File file = mCacheManager.get(key);
        if (!file.exists())
            return null;
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            if (!CacheUtils.isDue(readString)) {
                return CacheUtils.clearDateInfo(readString);
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
    }

    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    public JSONObject getAsJSONObject(String key) {
        String JSONString = getAsString(key);
        try {
            JSONObject obj = new JSONObject(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, byte[] value) {
        File file = mCacheManager.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    public void put(String key, byte[] value, int saveTime) {
        put(key, CacheUtils.newByteArrayWithDateInfo(saveTime, value));
    }

    public byte[] getAsBinary(String key) {
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = mCacheManager.get(key);
            if (!file.exists())
                return null;
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) RAFile.length()];
            RAFile.read(byteArray);
            if (!CacheUtils.isDue(byteArray)) {
                return CacheUtils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
    }

    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject = ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public void put(String key, Bitmap value) {
        put(key, BitmapUtils.Bitmap2Bytes(value));
    }

    public void put(String key, Bitmap value, int saveTime) {
        put(key, BitmapUtils.Bitmap2Bytes(value), saveTime);
    }

    public Bitmap getAsBitmap(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return BitmapUtils.Bytes2Bimap(getAsBinary(key));
    }

    public void put(String key, Drawable value) {
        put(key, BitmapUtils.drawable2Bitmap(value));
    }

    public void put(String key, Drawable value, int saveTime) {
        put(key, BitmapUtils.drawable2Bitmap(value), saveTime);
    }

    public Drawable getAsDrawable(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return BitmapUtils.bitmap2Drawable(BitmapUtils.Bytes2Bimap(getAsBinary(key)));
    }

    public void putAsGson(String key, Object object) {
        Gson gson = new Gson();
        put(key, gson.toJson(object));
    }

    public void putAsGson(String key, Object object, int saveTime) {
        Gson gson = new Gson();
        put(key, gson.toJson(object), saveTime);
    }

    public Object getFromGson(String key, Type type) {
        String gsonStr = getAsString(key);
        if (TextUtils.isEmpty(gsonStr)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(gsonStr, type);
    }

    public File file(String key) {
        File f = mCacheManager.newFile(key);
        if (f.exists())
            return f;
        return null;
    }

    public boolean remove(String key) {
        return mCacheManager.remove(key);
    }

    public void clear() {
        mCacheManager.clear();
    }

}
