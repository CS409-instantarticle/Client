package com.example.rho_eojin1.a409_prototype13;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Rho-Eojin1 on 2017. 6. 3..
 */

public class LimitHTTP {
    private static LimitHTTP sInstance;
    private HttpClient prev = null;

    public static synchronized LimitHTTP getInstance() {
        if (sInstance == null) {
            sInstance = new LimitHTTP();
        }
        return sInstance;
    }

    public void addHttp(HttpClient newHttp){
        if(prev == null){
            prev = newHttp;
            prev.execute();
        }else{
            prev.cancel(true);
            prev = newHttp;
            prev.execute();
        }
    }
}
