package cn.zwf.simplecache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper
 * Created by ZhangWF(zhangwf0929@gmail.com) on 15/6/1.
 */
public class CacheDatabaseHelper extends SQLiteOpenHelper implements CacheColumns {

    private CacheTable table = new CacheTable();

    private static final String NAME = "simple_cache.db";

    public CacheDatabaseHelper(Context context, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(table.createSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TAB_NAME);
        onCreate(sqLiteDatabase);
    }
}
