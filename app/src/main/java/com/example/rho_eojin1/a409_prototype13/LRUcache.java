package com.example.rho_eojin1.a409_prototype13;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by Rho-Eojin1 on 2017. 6. 4..
 */

public class LRUcache {
    private static LRUcache sInstance;
    private LruCache<String, Bitmap> mMemoryCache;

    private LRUcache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //Log.e("Max Memory",String.valueOf(maxMemory));

        final int cacheSize = maxMemory / 2;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static synchronized LRUcache getInstance() {
        if (sInstance == null) {
            sInstance = new LRUcache();
        }
        return sInstance;
    }



    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
            //Log.e("saved","yes!");
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
