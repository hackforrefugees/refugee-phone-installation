package com.refugeephones.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("deprecation")
@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class Catalyst {
	private static final String TAG = "Catalyst";
	
    
    /**
	 * Pops a short toast for the given message
	 * @param context context with in which the message should be shown.
	 * If you are calling this on a <u><b>non-UI thread</b></u> then you must need to pass your <u>activity's reference</u> in order for it to work.
	 * @param textId resource id of string
	 */
    @Deprecated
	public static void PopToast(Context context, int textId){
		PopToast(context, context.getResources().getString(textId));
	}
	
	/**
	 * Pops a short toast for the given message
	 * @param context context with in which the message should be shown.
	 * If you are calling this on a <u><b>non-UI thread</b></u> then you must need to pass your <u>activity's reference</u> in order for it to work.
	 * @param message message to be shown in Toast
	 */
	@Deprecated
	public static void PopToast(Context context, final String message){
		if(context instanceof Activity){
			try{
				final Activity act = (Activity) context;
			
				act.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
					}
				});
			}
			catch (Exception e) {
				AppLog.error(TAG, "Error in popToast due to -> " + e.toString());
			}
		}
		else
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Pops a long toast for the given message
	 * @param context context with in which the message should be shown.
	 * If you are calling this on a <u><b>non-UI thread</b></u> then you must need to pass your <u>activity's reference</u> in order for it to work.
	 * @param textId resource id of string
	 */
	@Deprecated
	public static void PopToastLong(Context context, int textId){
		PopToastLong(context, context.getResources().getString(textId));
	}
	
	/**
	 * Pops a long toast for the given message
	 * @param context context with in which the message should be shown.
	 * If you are calling this on a <u><b>non-UI thread</b></u> then you must need to pass your <u>activity's reference</u> in order for it to work.
	 * @param message message to be shown in Toast
	 */
	@Deprecated
	public static void PopToastLong(Context context, final String message){
		if(context instanceof Activity){
			try{
				final Activity act = (Activity) context;
			
				act.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(act, message, Toast.LENGTH_LONG).show();
					}
				});
			}
			catch (Exception e) {
				AppLog.error(TAG, "Error in popToast due to -> " + e.toString());
			}
		}
		else
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	

    

	
	
	
	
	/**
	 * Call this method to delete any cache created by app
	 * @param app context for your application
	 */
	public static void clearApplicationData(Application app) {
		AppLog.info(TAG, "Clearing app cache");
		//code from: http://www.hrupin.com/2011/11/how-to-clear-user-data-in-your-android-application-programmatically
		File cache = app.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				/*if (!s.equals("lib"))*/ {
					File f = new File(appDir, s);
					if(deleteDir(f))
						AppLog.debug(TAG, String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
				}
			}
		}
		
		//manually clearing preference data (it must include all the preference names here)		
		
		//erasing settings
		//app.getPhoniroSettings().new Editor().deleteAllSettings();
		//erasing default preferences
		//PreferenceManager.getDefaultSharedPreferences(app).edit().clear().apply();
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	
	
	/**
	 * Deletes a database file from app's <b>databases</b> folder
	 * @param context application's context
	 * @param dbFileName database's file name 
	 */
	public static void deleteDatabaseFile(Context context, String dbFileName){
		File dbFile = context.getDatabasePath(dbFileName);
		
		if(dbFile.exists()){
			AppLog.debug(TAG, String.format("Deleting database file (%s)", dbFile.getPath()));
			
			if(dbFile.delete())
				AppLog.debug(TAG, String.format("Database file (%s) deleted successfully!", dbFile.getPath()));
			else
				AppLog.warn(TAG, String.format("Could not delete database file (%s)", dbFile.getPath()));
		}
		else
			AppLog.warn(TAG, String.format("Database file (%s) does not exist", dbFile.getPath()));
	}
	
	
	
	/**
	 * Set the default Locale for app
	 * @param context context on which the locale will be implemented
	 * @param locale new locale for example, <b>sv</b> for Swedish or <b>en</b> for English
	 */
	public static void setDefaultLocale(Context context,String locale) {
        Locale locJa = new Locale(locale.trim());
        Locale.setDefault(locJa);

        Configuration config = new Configuration();
        config.locale = locJa;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());//null as 2nd argument

        locJa = null;
        config = null;
    }
	
	
	
	/**
	 * Checks if the screen is or above 9 inches
	 * @param activity activity screen
	 * @return True if its 9 or above, else false
	 */
	/*public static boolean isTablet(Activity activity)
	{
	    Display display = activity.getWindowManager().getDefaultDisplay();
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    display.getMetrics(displayMetrics);

	    int width = displayMetrics.widthPixels / displayMetrics.densityDpi;
	    int height = displayMetrics.heightPixels / displayMetrics.densityDpi;

	    double screenDiagonal = Math.sqrt( width * width + height * height );
	    return (screenDiagonal >= 9.0 );
	}*/
	
	
	
	/**
	 * Checks if the screen size is equal or above given length
	 * @param activity activity screen
	 * @param screen_size diagonal size of screen, for example 7.0 inches
	 * @return True if its equal or above, else false
	 */
	public static boolean checkScreenSize(Activity activity, double screen_size)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels / displayMetrics.densityDpi;
        int height = displayMetrics.heightPixels / displayMetrics.densityDpi;

        double screenDiagonal = Math.sqrt(width * width + height * height);
        
        AppLog.debug(TAG, "Calculated screen-size: " + screenDiagonal + " inches");
        
        return (screenDiagonal >= screen_size );
    }

	
	
	
	
	/**
	 * Converts Date object into string format as for e.g. <b>April 25th, 2012</b>
	 * @param date date object
	 * @return string format of provided date object
	 */
	public static String getCustomDateString(Date date){
		SimpleDateFormat tmp = new SimpleDateFormat("MMMM d");
		
	    String str = tmp.format(date);
	    str = str.substring(0, 1).toUpperCase() + str.substring(1);

	    if(date.getDate()>10 && date.getDate()<14)
	    	str = str + "th, ";
	    else{
		    if(str.endsWith("1")) str = str + "st, ";
		    else if(str.endsWith("2")) str = str + "nd, ";
		    else if(str.endsWith("3")) str = str + "rd, ";
		    else str = str + "th, ";
	    }

	    tmp = new SimpleDateFormat("yyyy");
	    str = str + tmp.format(date);

	    return str;
	}
		
	
	/**
     * Get {@link Bundle} of meta-data defined in AndroidManifest.xml
     * @param context {@link Context} of application
     * @return key/value pairs found in {@link Bundle}, or <code>null</code> upon error or not found
     */
    public static Bundle getMetaData(Context context){
    	Bundle bndl = null;
    	try{
    		ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
    		bndl = ai.metaData;
    	}
    	catch (Exception e) {
    		AppLog.error(TAG, "Error in retrieving meta bundle due to -> " + e.toString());
    		e.printStackTrace();
		}
    	return bndl;
    }


    /**
     * Checks if the data network is available or not 
     * @param context application's {@link Context}
     * @return <code>true</code> if available, else <code>false</code>
     */
    public static boolean isDataNetworkAvailable(Context context){
    	try{
	    	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    	return activeNetworkInfo != null;
    	}
    	catch (Exception e) { return false; }
    }
    
    
    
    
    /**
     * Perform SHA-256 hash on the given string. It returns a hashed string as Base64 string 
     * @param str String to be hashed in SHA-256
     * @return Base64 string if hashed successfully, else NULL
     */
    public static String getHashSHA256(String str){
    	String hash = null;
    	
    	try{
	    	MessageDigest digest = null;
	    	
	    	try { digest = MessageDigest.getInstance("SHA-256"); }
	    	catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return hash;
			}
	    	
	    	digest.reset();
	    	hash = Base64.encodeToString(digest.digest(str.getBytes()), Base64.DEFAULT).trim();
	    	digest = null;
    	}
    	catch (Exception e) {
    		AppLog.error(TAG, "Error in getHashSHA256() due to -> " + e.toString());
		}
    	
    	return hash;
    }
    
    
    /**
     * Perform MD-5 hash on the given string. It returns a hashed string as Base64 string
     * @param str String to be hashed in MD5
     * @return Base64 string if hashed successfully, else NULL
     */
    public static String getHashMD5(String str){
    	String hash = null;
    	
    	try{
	    	MessageDigest digest = null;
	    	
	    	try { digest = MessageDigest.getInstance("MD5"); }
	    	catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return hash;
			}
	    	
	    	digest.reset();
	    	hash = Base64.encodeToString(digest.digest(str.getBytes()), Base64.DEFAULT).trim();
	    	digest = null;
    	}
    	catch (Exception e) {
    		AppLog.error(TAG, "Error in getHashMD5() due to -> " + e.toString());
		}
    	
    	return hash;
    }
    
    
    
    /**
     * Converts InputStream to String and closes the stream afterwards
     * @param is Stream which needs to be converted to string
     * @return String value out form stream or NULL if stream is null or invalid. Finally the stream is closed too. 
     */
    public static String streamToString(InputStream is) {
        try {
        	StringBuilder sb = new StringBuilder();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is), 65728);	//as per 64K size of logs
        	String line = null;
        	
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            //close stream
            is.close();
            
            return sb.toString();
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
        
        return null;
    }
    
    
    
    /**
     * Check wheather the EditText is null or empty (excludes white-spaces)
     * @param textBox reference to EditText object
     * @return True if null or empty, else False
     */
    public static boolean isNullOrEmpty(EditText textBox){
    	return isNullOrEmpty(textBox.getText().toString());
    }
    
    
    /**
     * Check wheather the string is null or empty (excludes white-spaces)
     * @param text string to be verified
     * @return True if null or empty, else False
     */
    public static boolean isNullOrEmpty(String text){
    	return (text == null)? true : (text.trim().length() <= 0);
    }
    
    
    
    
    /*public static void createShortcut(Context context){
    	final Intent shortcutIntent = new Intent();
    	shortcutIntent.setComponent(new ComponentName(context, Login.class));
    	//shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	//shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	
    	final Intent putShortCutIntent = new Intent();
    	putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    	putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
    	putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.phoniro_icon));
    	putShortCutIntent.putExtra("duplicate", false);  // Just create once
    	putShortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(putShortCutIntent);
        
        AppLog.info(TAG, "Application's shortcut created");
        Catalyst.PopToast(context, "Shortcut created!");
    }*/
    
    
    
    
    /**
     * Converts DensityPixels to Pixels
     * @param context used to identify scale
     * @param dp density pixels
     * @return dp converted into Pixels
     */
    public static int convertDpToPixel(Context context, float dp){
    	Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
        
    	//final float scale = context.getResources().getDisplayMetrics().density;
    	//return (int) (dp * scale + 0.5f);
    }
    
    
    /**
     * Converts Pixels to DensityPixels
     * @param context used to identify scale
     * @param px pixels
     * @return converted density pixels
     */
    public static float convertPixelToDp(Context context, int px){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	    
    	//final float scale = context.getResources().getDisplayMetrics().density;
    	//return px / scale;
	}
    

    
    
    
    /**
     * Get the last best known {@link Location} of your device
     * @param context
     * @return {@link Location} object or <code>null</code> if cannot resolve
     */
    public static final Location getBestKnownLocation(Context context){
    	Location loc = null;

	    try{
		    final LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		    List<String> providers = locManager.getProviders(true);
    	
    	/* some old approach
    	//read in reverse to get the last good provider and if its not null then break out
    	for(int i=providers.size()-1; i>=0; i--){
    		Location tmp = locManager.getLastKnownLocation(providers.get(i));
    		
    		if(tmp != null){
    			loc = tmp;
    			break;
    		}
    	}
    	*/

		    {//new approach by waqas based on the comparison of accuracyvalue
			    List<Location> tmp = new ArrayList<Location>();

			    //gather all available Last Known locations from best providers
			    for(String s : providers){
				    Location l = locManager.getLastKnownLocation(s);

				    if(l != null)
					    tmp.add(l);
			    }

			    //lets pick the best location based on accuracy value
			    if(!tmp.isEmpty()){
				    loc = tmp.get(0);    //default
				    double d = loc.getAccuracy();    //start seed

				    for(Location l : tmp){
					    if(l.getAccuracy() <= d)
						    loc = l;
				    }
			    }
		    }
	    }
	    catch(Exception ex){
		    ex.printStackTrace();
	    }
    	
    	return loc;
    }
    
    
    
    /**
     * Compress a string into <b>GZip</b> {@link Base64} string
     * @param string string to be compressed
     * @return compressed base64 string
     * @throws IOException
     */
    public static String compress(String string) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(string.getBytes());
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return Base64.encodeToString(compressed, Base64.DEFAULT).trim();
    }
        
    
    
    /**
     * Decompress a {@link Base64} <b>GZip</b> string into its original form
     * @param strCompressed string to be decompressed
     * @return original form string
     * @throws IOException
     */
    public static String decompress(String strCompressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(Base64.decode(strCompressed.trim(), Base64.DEFAULT));
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }
    
    
    
    /**
     * Update the application with the provided apk file
     * @param activity reference to update process launching host
     * @param apkFile apk file for update
     */
    public static void updateApk(Activity activity, File apkFile){
		AppLog.debug(TAG, "Starting installation of: " + apkFile);

    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	Uri uri = Uri.fromFile(apkFile);
		//AppLog.debug(TAG, "URI: " + uri.toString());
    	//intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
    	intent.setDataAndType(uri,"application/vnd.android.package-archive");
    	activity.startActivity(intent);
    }
    
    
    
    /**
     * Calling this method verifies if the call is made from <b>Main/UI thread</b> or not.
     * @return <code>true</code> if the call is from main/ui thread, else <code>false</code>
     */
    public static boolean isMainThread(){
    	return Looper.myLooper() == Looper.getMainLooper();
    }
    
    
    
    /**
     * Convert a layout into {@link Bitmap}
     * @param context
     * @param layoutID
     * @return {@link Bitmap} object of layout
     */
    public static Bitmap createClusterBitmap(Context context, int layoutID) {
        View cluster = LayoutInflater.from(context).inflate(layoutID, null);

        cluster.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        cluster.layout(0, 0, cluster.getMeasuredWidth(),cluster.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(cluster.getMeasuredWidth(), cluster.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(clusterBitmap);
        cluster.draw(canvas);
        
        return clusterBitmap;
    }
    
    
    /**
	 * Get SQLite's date format <b>("yyyy-MM-dd HH:mm:ss")</b> for parsing Date objects. Its TimeZone is set to <b>GMT</b> by default.
	 * @return {@link DateFormat} object
	 */
	public static DateFormat getSQLiteDateTimeFormat(){
		//dateFormatISO8601.setTimeZone(TimeZone.getTimeZone("GMT"));
		return getSQLiteDateTimeFormat(true);
	}
	
	
	/**
	 * Get SQLite's date format <b>("yyyy-MM-dd HH:mm:ss")</b> for parsing Date objects. 
	 * @param isGMT True=GMT, False=Device's Locale
	 * @return {@link DateFormat} object
	 */
	public static DateFormat getSQLiteDateTimeFormat(Boolean isGMT){
		return getSQLiteDateTimeFormat(isGMT, false);
	}
	
	/**
	 * Get SQLite's date format <b>("yyyy-MM-dd HH:mm:ss.SSS")</b> for parsing Date objects. However the millisecond part <b>(.SSS)</b> is optional
	 * @param isGMT True=GMT, False=Device's Locale
	 * @param appendMilliseconds True if wants to append (3 digits) milliseconds after time, else False
	 * @return {@link DateFormat} object
	 */
	public static DateFormat getSQLiteDateTimeFormat(boolean isGMT, boolean appendMilliseconds){
		SimpleDateFormat format = null;
		
		if(appendMilliseconds) format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		else format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(isGMT)
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		return format;
	}
	
	
	/**
	 * Converts Layout into a Bitmap object
	 * @param context application context - used for access LayoutInflator
	 * @param layoutResId resource id of Layout which needs to be converted into Bitmap
	 * @return Bitmap object of Layout
	 */
	public static Bitmap getBitmapOfLayout(Context context, int layoutResId){
		Bitmap bitmap = null;
		
		//first, View preparation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflatedView = inflater.inflate(layoutResId, null);

		/*LayoutParams lp = inflatedView.getLayoutParams();
		if(lp != null){
			inflatedView.layout(0, 0, lp.width, lp.height);
			bitmap = Bitmap.createBitmap(lp.width, lp.height, Bitmap.Config.ARGB_8888);                
		    Canvas c = new Canvas(bitmap);
		    inflatedView.draw(c);
		}
		else{*/
			//second, set the width and height of inflated view
			inflatedView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			inflatedView.layout(0, 0, inflatedView.getMeasuredWidth(), inflatedView.getMeasuredHeight());
	
			//third, finally conversion
			bitmap = Bitmap.createBitmap(inflatedView.getMeasuredWidth(),
					inflatedView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			inflatedView.draw(canvas);
		//}
		
		return bitmap;
	}
	
	
	
	/**
	 * Allows a view to be collapsed and Gone with a smooth animation
	 * @param v view to be collapsed and gone
	 */
	public static void collapseView(final View v) {
	    final int initialHeight = v.getMeasuredHeight();
	    
	    final Animation a = new Animation(){
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration(500);//(int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}
	
	
	/**
	 * Allows a collapsed and Gone view to be expanded and Visible
	 * @param v view to be expanded and Visible
	 */
	public static void expandView(final View v) {
	    v.measure(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    final int targtetHeight = v.getMeasuredHeight();
	    
	    v.getLayoutParams().height = 0;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation(){
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? LayoutParams.WRAP_CONTENT
	                    : (int)(targtetHeight * interpolatedTime);
	            v.requestLayout();
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration(500);//(int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}
	
	
	/**
	 * Delete a folder together with all of its files and subfolders 
	 * @param fileOrDirectory
	 */
	public static void deleteRecursive(File fileOrDirectory) {
	    if(fileOrDirectory.isDirectory())
	        for(File child : fileOrDirectory.listFiles())
	        	deleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	
	/**
	 * Get the battery life in percentage
	 * @param context
	 * @return Battery life in percent or -1 if cannot evaluate
	 */
	public static int getBatteryLife(Context context){
		Intent batteryIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int rawlevel = batteryIntent.getIntExtra("level", -1);
		double scale = batteryIntent.getIntExtra("scale", -1);
		double level = -1;
		if (rawlevel >= 0 && scale > 0) {
			level = rawlevel / scale;
			
			level *= 100;
			//AppLog.error(TAG, "battery level: " + level);
		}
		
		return (int) level;
	}
	
	

	
	
	
	/**
	 * Sets a hyperlink style to the textview
	 * @param tv
	 */
	public static void makeTextViewHyperlink(TextView tv) {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(tv.getText());
		ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ssb, TextView.BufferType.SPANNABLE);
	}
	
	
	
	
	
}
