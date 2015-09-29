package cn.zwf.lib.cache;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import cn.zwf.simplecache.CacheManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        Log.d("zwf", "begin ApplicationTest");
        CacheManager.init(getApplication());
        CacheManager.getInstance().setValue("1", "a");
        String content = CacheManager.getInstance().getValue("1");
        Log.d("zwf", "get cache " + content);
        CacheManager.getInstance().closeDB();
        Log.d("zwf", "end ApplicationTest");
    }
}