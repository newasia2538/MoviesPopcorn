package com.example.maii.moviespopcorn;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by MAII on 11/7/2559.
 */
public class SimpleRealmApp extends Application {

    private static SimpleRealmApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(config);
    }
    public static SimpleRealmApp getInstance() {
        return instance;
    }
}
