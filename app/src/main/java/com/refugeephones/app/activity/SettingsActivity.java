package com.refugeephones.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.refugeephones.app.Language;
import com.refugeephones.app.LanguagesAdapter;
import com.refugeephones.app.R;
import com.refugeephones.app.ResourceItem;
import com.refugeephones.app.ResourceItemAdapter;
import com.refugeephones.app.fragment.BaseFragment;
import com.refugeephones.app.fragment.NewsFragment;
import com.refugeephones.app.fragment.ResourcesFragment;
import com.refugeephones.app.utils.AppLog;
import com.refugeephones.app.utils.Catalyst;
import com.refugeephones.app.utils.Constants;
import com.refugeephones.app.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main launching activity
 */
public class SettingsActivity extends BaseActivity {
    private final String TAG = "MainActivity";

    private ViewPager viewPager = null;
    private Spinner spinnerLanguages;

    private LanguagesAdapter languagesAdapter = null;
    private List<String> languagesList;
    private List<String> languageCodesList;
    private boolean languageSpinnerLoaded = false;



    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final String[] arrayTabs = getResources().getStringArray(R.array.arrayTabs);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        spinnerLanguages = (Spinner) findViewById(R.id.spinnerLanguages);
        languagesList = new ArrayList<String>();
        languageCodesList = new ArrayList<String>();
        languagesList.add("Loading languages...");
        languageCodesList.add("en");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languagesList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(dataAdapter);
        spinnerLanguages.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        downloadData();
    }

    private void downloadData(){
        new AsyncTask<Void, Void, List<Language>>(){
            private String urlResources = "https://drive.google.com/uc?export=download&id=0Bzb0I-9LyL9BSnpLMWs1WDZwRjA";
            private int CONNECTION_TIMEOUT = 30;   //seconds

            private Dialog diag = null;

            @Override
            protected void onPreExecute() {
                //diag = ProgressDialog.show(getBaseContext(), null, getString(R.string.downloading_data), true, false);
                AppLog.info(TAG, "Downloading languages");
            }

            @Override
            protected List<Language> doInBackground(Void... params) {
                try{
                    final HttpURLConnection con = (HttpURLConnection) new URL(urlResources).openConnection();
                    con.setConnectTimeout((int) (DateUtils.SECOND_IN_MILLIS * CONNECTION_TIMEOUT));
                    con.setReadTimeout((int) (DateUtils.SECOND_IN_MILLIS * CONNECTION_TIMEOUT));
                    final String json = Catalyst.streamToString(con.getInputStream());
                    final JSONArray jar = new JSONObject(json).getJSONArray("languages");
                    final List<Language> list = new Gson().fromJson(jar.toString(), new TypeToken<List<Language>>() {
                    }.getType());

                    //for logs
                    AppLog.info(TAG, String.format("Downloaded %d languages", list.size()));

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
            protected void onPostExecute(List<Language> languages) {
                //diag.dismiss();

                if(languages != null){
                    languagesList = new ArrayList<String>();
                    languageCodesList = new ArrayList<String>();
                    for (Language lang: languages) {
                        languagesList.add(lang.getName());
                        languageCodesList.add(lang.getCode());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, languagesList);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    String selectedLanguage = Prefs.loadLanguagePref();

                    spinnerLanguages.setAdapter(dataAdapter);

                    int index = languageCodesList.indexOf(selectedLanguage);

                    if(index != -1 && index < spinnerLanguages.getCount())
                        spinnerLanguages.setSelection(index);
                    Prefs.saveLanguagePref(selectedLanguage);
                }
                else
                {
                    //TODO toast this
                    //getBaseActivity().toast("Failed to download resource items");
                }
            }
        }.execute((Void) null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        languageSpinnerLoaded = false;
    }

    private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(languageSpinnerLoaded)
                Prefs.saveLanguagePref(languageCodesList.get(position));
            languageSpinnerLoaded = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
