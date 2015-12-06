package com.refugeephones.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Niels on 06-Dec-15.
 */
public class Prefs {

    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor editor;

    public Prefs(Context context)
    {
        sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
    }

    public static void saveLanguagePref(String lang)
    {
        editor.putString(Constants.PREFERENCE_LANGUAGE, lang);
        editor.commit();
    }

    public static String loadLanguagePref()
    {
        return  sharedPrefs.getString(Constants.PREFERENCE_LANGUAGE, "en");
    }

    public static void saveLanguageUrlPref(String url)
    {
        AppLog.debug("", "Saving url to " + url);
        editor.putString(Constants.PREFERENCE_LANGUAGE_URL, url);
        editor.commit();
    }

    public static String loadLanguageUrlPref()
    {
        return  sharedPrefs.getString(Constants.PREFERENCE_LANGUAGE_URL, Constants.JSON_DEFAULT_URL_RESOURCES);
    }
}
