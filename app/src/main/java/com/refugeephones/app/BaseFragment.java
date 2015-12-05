package com.refugeephones.app;

import android.support.v4.app.Fragment;

/**
 * Base fragment class to serve all other fragments.
 * All the fragments must extend from this class
 * Created by Waqas on 005 05-Dec-15.
 */
public class BaseFragment extends Fragment {
    private final String TAG = "BaseFragment";


    /**
     * Get our own custom made base activity
     * @return {@link MyApplication}
     */
    public BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }


    /**
     * Get our own custom made application
     * @return {@link MyApplication}
     */
    public MyApplication getMyApplication(){
        return getBaseActivity().getMyApplication();
    }


}
