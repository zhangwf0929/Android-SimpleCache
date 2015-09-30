package cn.zwf.simplecache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CacheManager's api
 * <p/>
 * Created by ZhangWF(zhangwf0929@gmail.com) on 15/6/1.
 */
public class CacheManager implements CacheColumns {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private SQLiteDatabase mDb;

    private static int mMaxRecordSize = 500;
    private static CacheManager sCacheManager = null;

    public static synchronized CacheManager getInstance() {
        return sCacheManager;
    }

    private CacheManager(Context context) {
        CacheDatabaseHelper helper = new CacheDatabaseHelper(context);
        mDb = helper.getWritableDatabase();
        checkCacheSize();
    }

    public static void init(Context context) {
        if (sCacheManager == null) {
            sCacheManager = new CacheManager(context.getApplicationContext());
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

    public synchronized void setValue(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_VALUE, value);
        mDb.replace(TAB_NAME, null, values);
    }

    public synchronized int delValue(String key) {
        return mDb.delete(TAB_NAME, COLUMN_KEY + " ='" + key + "'", null);
    }

    private void checkCacheSize() {
        Cursor cursor = mDb.query(TAB_NAME, new String[]{COLUMN_ID}, null, null, null, null, null);
        if (cursor != null && mMaxRecordSize > 0 && cursor.getCount() > mMaxRecordSize) {
            // 将缓存减半
            cursor.moveToPosition(mMaxRecordSize / 2);
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            mDb.delete(TAB_NAME, COLUMN_KEY + " <'" + id + "'", null);
        }
        closeCursor(cursor);
    }
}
