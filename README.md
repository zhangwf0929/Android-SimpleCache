# Android-SimpleCache
A simple cache by key-value for Android

 * `CacheManager.init(context);` on `onCreate`.
 * `CacheManager.init(context, version);` on `onCreate`,when update,old cache will be drop.
 * `CacheManager.setMaxRecordSize(100);` you can set the cache's size.
 * `CacheManager.getInstance().closeDB();` on `onDestroy`.
 * `CacheManager.getInstance().setValue(key, value);` add or update cache.
 * `CacheManager.getInstance().getValue(key);` return the value by key, return null when key isn't exist.
 * `CacheManager.getInstance().delValue(key);` remove from cache by key, return the rows removed.
 * `CacheManager.getInstance().empty();` clear cache, return the rows removed.

#Download
[ ![Download](https://api.bintray.com/packages/zhangwf0929/maven/Android-SimpleCache/images/download.svg) ](https://bintray.com/zhangwf0929/maven/Android-SimpleCache/_latestVersion)

#Gradle

```
dependencies {
    ...
    compile 'cn.zwf:SimpleCache:1.0.1'
}
```

#Changelog

* **1.0.1**
    * add version control & empty method
* **1.0.0**
    * Initial release
