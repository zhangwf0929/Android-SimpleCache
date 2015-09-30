package cn.zwf.simplecache;

/**
 * Create DBTable's script
 * <p/>
 * Created by ZhangWF(zhangwf0929@gmail.com) on 15/6/1.
 */
public class CacheTable implements CacheColumns {
    public String createSQL() {
        return "CREATE TABLE "
                + TAB_NAME + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_KEY + " TEXT UNIQUE, "
                + COLUMN_VALUE + " TEXT"
                + " );"
                ;
    }
}
