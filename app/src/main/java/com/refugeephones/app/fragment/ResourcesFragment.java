package com.refugeephones.app.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refugeephones.app.R;
import com.refugeephones.app.ResourceItem;
import com.refugeephones.app.ResourceItemAdapter;
import com.refugeephones.app.utils.AppLog;
import com.refugeephones.app.utils.Catalyst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Fragment to show apps list
 * Created by Waqas on 005 05-Dec-15.
 */
public class ResourcesFragment extends BaseFragment {
    private final String TAG = "ResourcesFragment";

    private ListView m_resourceListView = null;
    private ResourceItemAdapter resourceItemAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_resourceListView = (ListView)view.findViewById(R.id.resourceListView);
        resourceItemAdapter = new ResourceItemAdapter(getBaseActivity());
        m_resourceListView.setAdapter(resourceItemAdapter);

        m_resourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //resourceItemAdapter.toggleDescription(view);
                AppLog.info(TAG, "clicked: " + position);
            }
        });

        downloadData();
    }

    private void downloadData(){
        new AsyncTask<Void, Void, List<ResourceItem>>(){
            private String urlResources = "https://www.dropbox.com/s/jlofdmj52jhndbl/resources_en.json?raw=1";
            private int CONNECTION_TIMEOUT = 30;   //seconds

            private Dialog diag = null;

            @Override
            protected void onPreExecute() {
                //resourceItemAdapter.clear();
                diag = ProgressDialog.show(getBaseActivity(), null, getString(R.string.downloading_data), true, false);
            }

            @Override
            protected List<ResourceItem> doInBackground(Void... params) {
                try{
                    final HttpURLConnection con = (HttpURLConnection) new URL(urlResources).openConnection();
                    con.setConnectTimeout((int) (DateUtils.SECOND_IN_MILLIS * CONNECTION_TIMEOUT));
                    con.setReadTimeout((int) (DateUtils.SECOND_IN_MILLIS * CONNECTION_TIMEOUT));
                    final String json = Catalyst.streamToString(con.getInputStream());
                    final JSONArray jar = new JSONObject(json).getJSONArray("resources");
                    final List<ResourceItem> list = new Gson().fromJson(jar.toString(), new TypeToken<List<ResourceItem>>() {
                    }.getType());

                    //for logs
                    AppLog.info(TAG, String.format("Downloaded %d resource items", list.size()));

                    return list;
                }
                catch(IOException e){
                    e.printStackTrace();
                } catch(JSONException e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<ResourceItem> resourceItems) {
                diag.dismiss();

                if(resourceItems != null){
                    //lets clear the adapter first
                    resourceItemAdapter.clear();

                    //now lets add all teh items into adapter
                    resourceItemAdapter.addAll(resourceItems);
                }
                else
                    getBaseActivity().toast("Failed to download resource items");
            }
        }.execute((Void) null);
    }


}
