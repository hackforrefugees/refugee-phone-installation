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

import com.refugeephones.app.utils.AppLog;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by magnus on 05/12/15.
 */

public class ResourceItemAdapter extends BaseAdapter {

    private final String TAG = "com.refugeephones.app.ResourceItemAdapter";

    private Context mContext;
    private ArrayList<ResourceItem> items = new ArrayList<ResourceItem>();

    public ResourceItemAdapter(Context context) {
        this.mContext = context;
    }

    public void updateItems(ArrayList<ResourceItem> items) {
        this.items = items;
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
        return position; //hack
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_resource_row, parent, false);
        }

        LinearLayout rowLayout = (LinearLayout)convertView.findViewById(R.id.resourceRowLayout);
        TextView name = (TextView)convertView.findViewById(R.id.resourceRowName);
        TextView description = (TextView)convertView.findViewById(R.id.resourceRowDescription);
        ImageView icon = (ImageView)convertView.findViewById(R.id.resourceRowIcon);
        Button openBtn = (Button) convertView.findViewById(R.id.btnOpen);

        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout collapseLayout = (LinearLayout) v.findViewById(R.id.collapseLayout);
                ImageView collapseArrow = (ImageView) v.findViewById(R.id.resourceRowCollapseArrow);
                Drawable arrow = null;
                if(collapseLayout.getVisibility() == View.GONE)
                {
                    collapseLayout.setVisibility(View.VISIBLE);

                    arrow  = ContextCompat.getDrawable(v.getContext(), R.drawable.collapsearrow24);

                }
                else
                {
                    collapseLayout.setVisibility(View.GONE);
                    arrow  = ContextCompat.getDrawable(v.getContext(), R.drawable.expandarrow24);
                }
                collapseArrow.setImageDrawable(arrow);
            }
        });

        ResourceItem item = getItem(position);

        openBtn.setOnClickListener(new OpenClickListener(item.getType(), item.getLink()));

        name.setText(item.getName());
        description.setText(item.getDescription());

        if(item.getIcon().equals(""))
        {
            AppLog.debug(TAG, "type=" + item.getType());
            switch(item.getType())
            {
                case APP:
                    icon.setBackgroundResource(R.drawable.touchscreensmartphone24);
                    break;
                case FACEBOOK:
                    icon.setBackgroundResource(R.drawable.facebook24);
                    break;
                case URL:
                    icon.setBackgroundResource(R.drawable.link24);
                    break;
                case YOUTUBE:
                    icon.setBackgroundResource(R.drawable.youtube24);
                    break;
                default:
                    AppLog.warn(TAG, "Did not find an icon for resource type "+item.getType());
            }
        }

        return convertView;
    }

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
