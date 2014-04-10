package com.example.pass.callnao;

/**
 * Created by HiddenVinnyP on 09.04.2014.
 */
import android.app.Application;


public class GlobalValues {
    Nao nao = new Nao() ;
    private static GlobalValues singleton;

    public static GlobalValues getInstance() {
        return singleton;
        }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        singleton = this;
//    }
}
