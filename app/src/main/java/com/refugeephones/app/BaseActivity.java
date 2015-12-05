package com.refugeephones.app;

import android.support.v7.app.AppCompatActivity;

/**
 * Base activity to serve all the child activities.
 * All activites must extend from this wrapper class
 * Created by Waqas on 005 05-Dec-15.
 */
public class BaseActivity extends AppCompatActivity {
    private String TAG = "BaseActivity";


    /**
     * Get our own custom made application
     * @return {@link MyApplication}
     */
    public MyApplication getMyApplication(){
        return (MyApplication) getApplication();
    }


}
