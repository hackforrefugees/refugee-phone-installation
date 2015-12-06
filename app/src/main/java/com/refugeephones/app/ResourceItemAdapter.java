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

public class ResourceItemAdapter extends BaseAdapter {

    private final String TAG = "com.refugeephones.app.ResourceItemAdapter";

    private BaseActivity activity;
    private ArrayList<ResourceItem> items = new ArrayList<ResourceItem>(2);

    public ResourceItemAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends ResourceItem> collection){
        items.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ResourceItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_resource_row, parent, false);
        }

        LinearLayout rowLayout = (LinearLayout)convertView.findViewById(R.id.resourceRowLayout);
        TextView name = (TextView)convertView.findViewById(R.id.resourceRowName);
        TextView description = (TextView)convertView.findViewById(R.id.resourceRowDescription);
        ImageView icon = (ImageView)convertView.findViewById(R.id.resourceRowIcon);
        Button openBtn = (Button) convertView.findViewById(R.id.btnOpen);

        /*{
            TypedValue outValue = new TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            convertView.setBackgroundResource(outValue.resourceId);
        }*/

        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout collapseLayout = (LinearLayout) v.findViewById(R.id.collapseLayout);
                ImageView collapseArrow = (ImageView) v.findViewById(R.id.resourceRowCollapseArrow);
                Drawable arrow = null;
                if(collapseLayout.getVisibility() == View.GONE){
                    Catalyst.expandView(collapseLayout);
                    //collapseLayout.setVisibility(View.VISIBLE);
                    arrow  = ContextCompat.getDrawable(v.getContext(), R.drawable.collapsearrow24);
                }
                else{
                    //collapseLayout.setVisibility(View.GONE);
                    Catalyst.collapseView(collapseLayout);
                    arrow  = ContextCompat.getDrawable(v.getContext(), R.drawable.expandarrow24);
                }
                collapseArrow.setImageDrawable(arrow);
            }
        });

        final ResourceItem item = getItem(position);

        openBtn.setOnClickListener(new OpenClickListener(item.getType(), item.getLink()));
        name.setText(item.getName());
        description.setText(item.getDescription());
        activity.getMyApplication().getImageLoader().displayImage(item.getIcon(), icon);

        return convertView;
    }

    /*public void toggleDescription(View v){
        LinearLayout collapseLayout = (LinearLayout) v.findViewById(R.id.collapseLayout);
        ImageView collapseArrow = (ImageView) v.findViewById(R.id.resourceRowCollapseArrow);

        if(collapseLayout.getVisibility() == View.GONE){
            Catalyst.expandView(collapseLayout);
            //collapseLayout.setVisibility(View.VISIBLE);
            collapseArrow.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.collapsearrow24));
        }
        else{
            //collapseLayout.setVisibility(View.GONE);
            Catalyst.collapseView(collapseLayout);
            collapseArrow.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.expandarrow24));
        }
    }*/

    private class OpenClickListener implements View.OnClickListener
    {
        private ResourceItem.ResourceType mType;
        private String mUrl;

        public OpenClickListener(ResourceItem.ResourceType type, String url)
        {
            this.mType = type;
            this.mUrl = url;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch(mType)
            {
                case APP:
                    intent = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("market://details?id="+mUrl));
                    break;
                case FACEBOOK:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                    break;
                case URL:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                    break;
                case YOUTUBE:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                    break;
                default:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                    AppLog.warn(TAG, "Do not know how to open the link for resource type "+mType);
            }
            try{
                v.getContext().startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                AppLog.error(TAG, "ActivityNotFoundException when trying to open url "+mUrl+" for type "+mType);
            }

        }
    }
}
