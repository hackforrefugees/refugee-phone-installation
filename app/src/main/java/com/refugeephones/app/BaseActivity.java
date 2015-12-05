package com.refugeephones.app;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.refugeephones.app.utils.AppLog;
import com.refugeephones.app.utils.Catalyst;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base activity to serve all the child activities.
 * All activites must extend from this wrapper class
 * Created by Waqas on 005 05-Dec-15.
 */
public class BaseActivity extends AppCompatActivity {
    private boolean LOG_ACTIVITY_LIFECYCLE = true;
    public String TAG;
    private final int TICTOC_MSG_ID = 110192;	//just a random id
    private final Map<HashMap<TicTocListener, TicTocListener.RefreshInterval>, Handler> mapTicTocQue = new HashMap<HashMap<TicTocListener, TicTocListener.RefreshInterval>, Handler>();


    /**
     * Private constructor
     */
    public BaseActivity(){ TAG = getClass().getSimpleName(); }


    /**
     * Set the Tag for this activity at root level
     * @param TAG
     */
    public void setTag(String TAG){ this.TAG = TAG; }



    /**
     * Get our own custom made application
     * @return {@link MyApplication}
     */
    public MyApplication getMyApplication(){
        return (MyApplication) getApplication();
    }


    /**
     * Display a short {@link Toast}
     * @param resId string's resource id
     */
    public void toast(int resId){ Toast.makeText(this, resId, Toast.LENGTH_SHORT).show(); }


    /**
     * Display a short {@link Toast}
     * @param text text to be shown in toast
     */
    public void toast(String text){ Toast.makeText(this, text, Toast.LENGTH_SHORT).show(); }


    /**
     * Display a long {@link Toast}
     * @param resId string's resource id
     */
    public void toastLong(int resId){ Toast.makeText(this, resId, Toast.LENGTH_LONG).show(); }


    /**
     * Display a long {@link Toast}
     * @param text text to be shown in toast
     */
    public void toastLong(String text){ Toast.makeText(this, text, Toast.LENGTH_LONG).show(); }


    /**
     * Sets a 32x32 icon in the actionbar
     * @param resDrawable
     */
    public void setIcon(int resDrawable){
        if(getSupportActionBar() != null){
            Drawable dr = getResources().getDrawable(resDrawable);
            //dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

            //resizing to 24dp
            int imgSize = Catalyst.convertDpToPixel(this, 24);
            Bitmap resized = Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), imgSize, imgSize, false);
            //getSupportActionBar().setIcon(new BitmapDrawable(getResources(), resized));
            //AppLog.error(TAG, "width: " + Catalyst.convertPixelToDp(this, resized.getWidth()) + ", height: " + Catalyst.convertPixelToDp(this, resized.getHeight()));

            //adding 8dp border and creating all together as 32dp
            int borderSize = Catalyst.convertDpToPixel(this, 4);
            Bitmap bmpWithBorder = Bitmap.createBitmap(resized.getWidth() + borderSize * 2, resized.getHeight() + borderSize * 2, resized.getConfig());
            Canvas canvas = new Canvas(bmpWithBorder);
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawBitmap(resized, borderSize, borderSize, null);

            getSupportActionBar().setIcon(new BitmapDrawable(getResources(), bmpWithBorder));
            //AppLog.error(TAG, "width: " + Catalyst.convertPixelToDp(this, bmpWithBorder.getWidth()) + ", height: " + Catalyst.convertPixelToDp(this, bmpWithBorder.getHeight()));
        }
        else
            AppLog.warn(TAG, "ActionBar not found, hence setting the icon failed!");
    }


    /**
     * Set the title in {@link ActionBarActivity}
     */
    @Override
    public void setTitle(CharSequence title) {
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        else
            super.setTitle(title);
    }


    /**
     * Set the title in {@link ActionBarActivity}
     */
    @Override
    public void setTitle(int titleId) {
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(titleId);
        else
            super.setTitle(titleId);
    }


    /**
     * Allows to kill the activity with a toast and log-warning message
     * @param msg message to be shown
     */
    public void abortActivity(String msg){
        //Catalyst.PopToast(this, msg);
        toast(msg);
        AppLog.warn(TAG, msg);
        finish();
    }


    /**
     * Get all the views which matches the given Tag recursively
     * @param root parent view. for e.g. Layouts
     * @param tag tag to look for
     * @return List of views
     */
    public List<View> findViewsWithTagRecursively(ViewGroup root, Object tag){
        List<View> allViews = new ArrayList<>();

        final int childCount = root.getChildCount();
        for(int i=0; i<childCount; i++){
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup){
                allViews.addAll(findViewsWithTagRecursively((ViewGroup) childView, tag));
            }
            else{
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }



    /**
     * Registers a refreshing/recalling {@link TicTocListener} for this activity on the <font color="red"><b>main/UI thread</b></font>.
     * If such a listener is already registered, then the method <u>will not</u> initiate a new one untill an old one is unregistered.<br />
     * As a good practice, you must unregister your listener in <b>onStop()</b> of your Activity's life-cycle by calling <b>unregisterTicTocListener</b>.<br />
     * Otherwise, the {@link BaseActivity} will automatically unregister it in its <b>onDestroy()</b> call.
     * @param listener {@link TicTocListener} object
     * @param interval {@link com.refugeephones.app.BaseActivity.TicTocListener.RefreshInterval} object
     */
    public void registerTicTocListener(TicTocListener listener, TicTocListener.RefreshInterval interval){
        //verify of the tictoc listener is already added in the que
        for(HashMap<TicTocListener, TicTocListener.RefreshInterval> tmp : mapTicTocQue.keySet()){
            TicTocListener tictoc = tmp.keySet().iterator().next();
            if(listener == tictoc){
                AppLog.warn(TAG, "TicToc listener already registered in que");
                return;
            }
        }

        //add tictoc to map with its handler
        final HashMap<TicTocListener, TicTocListener.RefreshInterval> map = new HashMap<TicTocListener, TicTocListener.RefreshInterval>();//{{ put(listener, interval); }};
        map.put(listener, interval);

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Handler tmpHandler = null;
                HashMap<TicTocListener, TicTocListener.RefreshInterval> tmp = null;

                //iterate throw container to find handler
                for(HashMap<TicTocListener, TicTocListener.RefreshInterval> t : mapTicTocQue.keySet())
                    if(t == msg.obj){
                        tmp = t;
                        tmpHandler = mapTicTocQue.get(tmp);
                    }

                if(tmpHandler != null && tmp != null){
                    TicTocListener tmpListener = tmp.keySet().iterator().next();
                    TicTocListener.RefreshInterval tmpInterval = tmp.get(tmpListener);

                    tmpListener.onRefresh();
                    tmpHandler.sendMessageDelayed(Message.obtain(msg), tmpInterval.getDuration());
                    //msg.recycle();

                    return true;
                }
                else
                    return false;
            }
        });

        //add to que
        mapTicTocQue.put(map, handler);

        //lets finally kick-in
        AppLog.debug(TAG, "TicToc Listener Registered!");
        Message msg = Message.obtain();
        msg.what = TICTOC_MSG_ID;
        msg.obj = map;
        handler.sendMessage(msg);
        //msg.recycle();
    }


    /**
     * Calls to unregister a {@link TicTocListener} from this activity.<br />
     * As a good practice, you must unregister your listener in <b>onStop()</b> of your Activity's life-cycle.<br />
     * Otherwise, the {@link BaseActivity} will automatically unregister it in its <b>onDestroy()</b> call.
     * @param listener {@link TicTocListener} to unregister
     */
    public void unregisterTicTocListener(TicTocListener listener){
        boolean removed = false;
        HashMap<TicTocListener, TicTocListener.RefreshInterval> toBeRemoved = null;

        for(HashMap<TicTocListener, TicTocListener.RefreshInterval> tmp : mapTicTocQue.keySet()){
            final TicTocListener tictoc = tmp.keySet().iterator().next();
            //final RefreshInterval refresh = tmp.get(tictoc);
            final Handler handler = mapTicTocQue.get(tmp);

            if(tictoc == listener){
                handler.removeMessages(TICTOC_MSG_ID);
                removed = true;
                toBeRemoved = tmp;
            }
        }

        if(removed){
            //remove tictoc entry for map
            mapTicTocQue.remove(toBeRemoved);

            AppLog.debug(TAG, "TicToc Listener Unregistered!");
        }
        else
            AppLog.warn(TAG, "TicToc Listener does not exist in the que");
    }


    /**
     * Runs a given {@link Runnable} on the <font color="red"><b>non-UI/separate thread</b></font>.<br />
     * It is usefull when a lengthy or time-taking operation (for example: database or webcalls) is required to be performed without blocking the UI thread.<br />
     * <u>Do not use it for Views' manipulation</u>, because that must be conducted on UI-thread or use its {@link CallbackFromNonUiThread} implementation which runs on UI-thread.
     * @param action the action to run on the non-UI thread
     * @param callback {@link CallbackFromNonUiThread} to get notified once the operation is executed completely, or <code>null</code> if not necessary. It always runs on UI-thread in order to help views' manipulation.
     * @return reuturns the {@link Thread} named <b>"threadRunOnNonUiThread"</b> that will be used for executing the given action.
     * Try not to do anything fancy with it, else you may break the successfull execution
     */
    public Thread runOnNonUiThread(final Runnable action, final CallbackFromNonUiThread callback){
        final Handler h = new Handler();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(callback != null){	//with callback
                    final Date dt = new Date();
                    action.run();	//run the given runnable

                    //calls callback on UI thread
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(new Date().getTime() - dt.getTime());
                        }
                    });
                }
                else	//without callback
                    action.run();
            }
        }, "threadRunOnNonUiThread");

        t.start();	//start the non-ui thread

        return t;
    }




    ///////////////////////////////////ACTIVITY LIFE-CYCLE Stuff////////////////////////////////////////////




    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onCreate ++++++++++");
        super.onCreate(savedInstanceState);

        //set actionbar Up behaviour
        /*if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }*/


        //Log orientation type
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                AppLog.verbose(TAG, "screen-orientation = LANDSCAPE");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                AppLog.verbose(TAG, "screen-orientation = PORTRAIT");
                break;
            case Configuration.ORIENTATION_SQUARE:
                AppLog.verbose(TAG, "screen-orientation = SQUARE");
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                AppLog.verbose(TAG, "screen-orientation = UNDEFINED");
                break;
            default:
                AppLog.verbose(TAG, "screen-orientation = UNKNOWN TO ME :(");
                break;
        }
    }

    @Override
    protected void onRestart() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onRestart ++++++++++");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onStart ++++++++++");
        super.onStart();
    }

    @Override
    protected void onResume() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onResume ++++++++++");
        super.onResume();

//		//set the screen state to ON as the activity is in foreground.
//		//This is also helpful to identify the initial screen state in case the application context is retsarted
//		getPhoniroApplication().setScreenState(ScreenState.ON);
    }

    @Override
    protected void onPause() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onPause ++++++++++");
        super.onPause();

        //unregister local broadcast manager from internal communication
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(internalBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onStop ++++++++++");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(LOG_ACTIVITY_LIFECYCLE) AppLog.debug(TAG, "++++++++++ onDestroy ++++++++++");
        super.onDestroy();

        //unregister tictoc listener if user forgot to do it before finishing the workshift
        {
            final List<TicTocListener> lst = new ArrayList<TicTocListener>();

            //populate all
            for(HashMap<TicTocListener, TicTocListener.RefreshInterval> tmp : mapTicTocQue.keySet()){
                final TicTocListener tictoc = tmp.keySet().iterator().next();
                //final RefreshInterval refresh = tmp.get(tictoc);
                //final Handler handler = mapTicToc.get(tmp);
                lst.add(tictoc);
            }

            //unregister all
            if(!lst.isEmpty()){
                AppLog.warn(TAG, String.format("Did you forget to unregister (Total: %d) TICTOC listeners by calling 'unregisterTicTocListener(listener)'? "
                        + "Anyhow, it has been unregistered automatically.", lst.size()));

                for(TicTocListener t : lst)
                    unregisterTicTocListener(t);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.mnuAbout:
                //Intent intent = new Intent(this, About.class);
                //startActivity(intent);

                final View v = View.inflate(this, R.layout.diag_about, null);
                //set linkify for 'other apps'
                ((TextView)v.findViewById(android.R.id.text2)).setMovementMethod(LinkMovementMethod.getInstance());

                new AlertDialog.Builder(this)
                        .setTitle(item.getTitle())
                        .setView(v)
                        .setNegativeButton(android.R.string.ok, null)
                        .show();
                break;*/

            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }





















    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Listener that can be used to resonate its dedicated method every second (or custom time)
     * @author waa
     *
     */
    public interface TicTocListener {

        /**
         * Defines refresh interval for {@link TicTocListener}
         * @author waa
         *
         */
        public static enum RefreshInterval{
            /**
             * One hour (i.e. 3600000 milliseconds)
             */
            EVERY_HOUR(3600000),
            /**
             * One minute (i.e. 60000 milliseconds)
             */
            EVERY_MINUTE(60000),
            /**
             * Five second (i.e. 5000 milliseconds)
             */
            EVERY_FIVE_SECONDS(5000),
            /**
             * Two second (i.e. 2000 milliseconds)
             */
            EVERY_TWO_SECONDS(2000),
            /**
             * One second (i.e. 1000 milliseconds)
             */
            EVERY_SECOND(1000),
            /**
             * It is equivalent to 500 milliseconds
             */
            EVERY_HALF_SECOND(500),
            /**
             * It is equivalent to 100 milliseconds
             */
            EVERY_10TH_OF_SECOND(100);

            private final long duration;

            private RefreshInterval(long l){ duration = l; }
            /**
             * Get the duration of corresponding value in milliseconds
             * @return milliseconds
             */
            public long getDuration(){ return duration; }
        }

        /**
         * Called on refresh interval basis
         */
        public abstract void onRefresh();

    }


    /**
     * Call back used to identify the completion of running a {@link Runnable} on non-UI thread
     * @author waa
     *
     */
    public interface CallbackFromNonUiThread {

        /**
         * Called when the operation is performed.
         * @param timeTook Time (in milliseconds) took to perform the execution
         */
        public abstract void onDone(long timeTook);

    }

}
