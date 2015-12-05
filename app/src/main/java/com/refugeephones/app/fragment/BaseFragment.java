package com.refugeephones.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refugeephones.app.MyApplication;
import com.refugeephones.app.activity.BaseActivity;
import com.refugeephones.app.utils.AppLog;

/**
 * Base fragment class to serve all other fragments.
 * All the fragments must extend from this class
 * Created by Waqas on 005 05-Dec-15.
 */
public class BaseFragment extends Fragment {
    private boolean LOG_FRAGMENT_LIFECYCLE = true;
    private String TAG;

    public BaseFragment(){
        TAG = getClass().getSimpleName();
    }

    /**
     * Set the tag value used by base activity in logs
     * @param TAG
     */
    public void setTag(String TAG){
        this.TAG = TAG;
    }

    /**
     * Get reference to the {@link BaseActivity}
     * @return {@link BaseActivity}
     */
    public BaseActivity getBaseActivity(){ return (BaseActivity) getActivity(); }


    /**
     * Get our own custom made application
     * @return {@link MyApplication}
     */
    public MyApplication getMyApplication(){
        return getBaseActivity().getMyApplication();
    }


    /**
     * Get {@link ImageLoader}
     * @return

    public ImageLoader getImageLoader(){ return getBaseActivity().getNibApplication().getImageLoader(); }*/

    @Override
    public void onAttach(Activity activity) {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onAttach **********F");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onCreate **********F");
        super.onCreate(savedInstanceState);

        //AppLog.error(TAG, "onCreate Bundle: " + ((savedInstanceState!=null)? savedInstanceState.toString() : "NULL"));
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onCreateView **********F");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onViewCreated **********F");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onActivityCreated **********F");

        //AppLog.verbose(TAG, "onActivityCreated Bundle: " + ((savedInstanceState!=null)? savedInstanceState.toString() : "NULL"));

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onStart **********F");
        super.onStart();
    }

    @Override
    public void onResume() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onResume **********F");
        super.onResume();
    }

    @Override
    public void onPause() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onPause **********F");
        super.onPause();
    }

    @Override
    public void onStop() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onStop **********F");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onDestroyView **********F");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onDestroy **********F");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        if(LOG_FRAGMENT_LIFECYCLE) AppLog.verbose(TAG, "********** onDetach **********F");
        super.onDetach();
    }



}
