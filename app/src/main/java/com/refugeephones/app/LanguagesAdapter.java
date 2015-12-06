package com.refugeephones.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.refugeephones.app.activity.BaseActivity;
import com.refugeephones.app.utils.AppLog;
import com.refugeephones.app.utils.Catalyst;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by magnus on 05/12/15.
 */

public class LanguagesAdapter extends BaseAdapter {

    private final String TAG = "com.refugeephones.app.LanguagesAdapter";

    private BaseActivity activity;
    private ArrayList<Language> languages = new ArrayList<Language>(2);

    public LanguagesAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public void clear(){
        languages.clear();
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Language> collection){
        languages.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Language getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }

}
