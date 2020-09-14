package com.example.testapp.app;

import android.app.Application;
import android.content.Context;

import com.example.testapp.R;
import com.example.testapp.rest.ApiFactory;
import com.google.gson.Gson;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * Created by User on 11/08/2020
 * Class that store all App related variables
 */
public class App extends Application {


    private ApiFactory apiFactory;
    private static App instance;
    private Gson gson;
    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRest();
        initGson();
        initCalligraphy();
    }

    /**
     * Initialise calligraphy
     */
    private void initCalligraphy() {
        //Custom Font
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(getString(R.string.text_font_regular))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    /**
     * Initialise Gson Library
     */
    private void initGson() {
        gson = new Gson();
    }

    /**
     * Initialise api factory
     */
    public void initRest() {
        this.apiFactory = new ApiFactory();
    }

    /**
     * @return Context : Application context
     */
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }


    /**
     * @return Gson instance
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * @return ApiFactory instance
     */
    public ApiFactory getApiFactory() {
        return apiFactory;
    }

}
