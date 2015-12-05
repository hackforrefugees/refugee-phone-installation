package com.refugeephones.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refugeephones.app.R;

/**
 * Fragment to show apps list
 * Created by Waqas on 005 05-Dec-15.
 */
public class FragApps extends BaseFragment {
    private final String TAG = "FragApps";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
