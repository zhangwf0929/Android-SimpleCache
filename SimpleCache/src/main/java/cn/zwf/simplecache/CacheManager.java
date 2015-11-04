package cn.zwf.simplecache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CacheManager's api
 * Created by ZhangWF(zhangwf0929@gmail.com) on 15/6/1.
 */
public class CacheManager implements CacheColumns {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private SQLiteDatabase mDb;

    private static int mMaxRecordSize = 500;
    private static CacheManager sCacheManager = null;
    private static final int DEF_VERSION = 1;

    public static synchronized CacheManager getInstance() {
        return sCacheManager;
    }

    private CacheManager(Context context, int version) {
        CacheDatabaseHelper helper = new CacheDatabaseHelper(context, version);
        mDb = helper.getWritableDatabase();
        checkCacheSize();
    }

    public static void init(Context context) {
        init(context, DEF_VERSION);
    }

    public static void init(Context context, int version) {
        if (sCacheManager == null) {
            sCacheManager = new CacheManager(context.getApplicationContext(), version);
        }
    }

    public static void setMaxRecordSize(int size) {
        mMaxRecordSize = size;
    }

    public synchronized void closeDB() {
        if (mDb != null && mDb.isOpen() && mOpenCounter.decrementAndGet() == 0) {
            mDb.close();
        }
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public String getValue(String key) {
        String result = null;
        Cursor cursor = mDb.query(TAB_NAME, null, COLUMN_KEY + " ='" + key + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE));
        }
        closeCursor(cursor);
        return result;
    }

    public <T> T getObject(String key, Class<T> cls) {
        String value = getValue(key);
        if (!TextUtils.isEmpty(value)) {
            return new Gson().fromJson(value, cls);
        }
        return null;
    }

    public <T> List<T> getList(String key, Type type) {
        String value = getValue(key);
        if (!TextUtils.isEmpty(value)) {
            return new Gson().fromJson(value, type);
        }
        return null;
    }

    public synchronized long setValue(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, value);
        return mDb.replace(TAB_NAME, null, values);
    }

    public synchronized long setObject(String key, Object object) {
        return setValue(key, new Gson().toJson(object));
    }

    public synchronized int delValue(String key) {
        return mDb.delete(TAB_NAME, COLUMN_KEY + " ='" + key + "'", null);
    }

    public synchronized int empty() {
        return mDb.delete(TAB_NAME, "1=1", null);
    }

    private void checkCacheSize() {
        Cursor cursor = mDb.query(TAB_NAME, new String[]{COLUMN_ID}, null, null, null, null, null);
        if (cursor != null && mMaxRecordSize > 0 && cursor.getCount() > mMaxRecordSize) {
            // 将缓存减半
            cursor.moveToPosition(mMaxRecordSize / 2);
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            mDb.delete(TAB_NAME, COLUMN_ID + " <'" + id + "'", null);
        }
        closeCursor(cursor);
    }
}
