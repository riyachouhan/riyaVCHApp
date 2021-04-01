package com.vch.utiles;

import android.content.SharedPreferences;
import androidx.multidex.MultiDexApplication;


import io.fabric.sdk.android.Fabric;

/**
 * Created by pintu22 on 13/11/17.
 */

public class App extends MultiDexApplication {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences RegPref;
    public static SharedPreferences.Editor RegEditor;
    private static App instance;
    private static DataHolder dataHolder;

    public static App getInstance() {
        return instance;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        dataHolder = new DataHolder();
        pref = this.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
        editor = pref.edit();

        RegPref = this.getSharedPreferences(Constant.REG_NAME, MODE_PRIVATE);
        RegEditor = RegPref.edit();

    }

    public static DataHolder getDataHolder() {
        return dataHolder;
    }
}
