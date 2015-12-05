package com.refugeephones.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.refugeephones.app.BaseFragment;

/**
 * Created by Waqas on 005 05-Dec-15.
 */
public class Temp extends BaseFragment {
    private final String TAG = "Temp";

    private TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        textView = new TextView(container.getContext());

        return textView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView.setText(getArguments().getString("val"));
    }

}
