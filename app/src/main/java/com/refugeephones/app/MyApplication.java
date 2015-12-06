package com.refugeephones.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.refugeephones.app.utils.AppLog;

/**
 * Mother of all... It's the base application
 * Created by Waqas on 005 05-Dec-15.
 */
public class MyApplication extends Application {
    private final String TAG = "MyApplication";

    private ImageLoader imgLoader = null;
    private DisplayImageOptions dio = null;

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.info(TAG, "Application Context created!");

        //create default display image option for UIL
        dio = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();
    }


    /**
     * Get {@link ImageLoader} for handling images in a better way
     * @return {@link ImageLoader} initialized with default {@link ImageLoaderConfiguration}
     */
    public ImageLoader getImageLoader(){
        //init image loader
        if(imgLoader == null || !imgLoader.isInited()){
            imgLoader = ImageLoader.getInstance();
            //imgLoader.init(ImageLoaderConfiguration.createDefault(this));
            imgLoader.init(new ImageLoaderConfiguration.Builder(this)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                            //.memoryCache(new WeakMemoryCache())
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheSize(100 * 1024 * 1024)    //100MB
                    .defaultDisplayImageOptions(dio)
                    .build());
        }

        //disable stupid exceptions
        //L.disableLogging();
        //L.writeDebugLogs(false);

        return imgLoader;
    }


    /**
     * Get a default {@link DisplayImageOptions}
     * @return
     */
    public final DisplayImageOptions getDisplayImageOptions(){
        return dio;
    }

}
