package com.refugeephones.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.refugeephones.app.fragment.BaseFragment;
import com.refugeephones.app.fragment.NewsFragment;
import com.refugeephones.app.R;
import com.refugeephones.app.fragment.ResourcesFragment;
import com.refugeephones.app.utils.AppLog;

/**
 * Main launching activity
 */
public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";

    private ViewPager viewPager = null;


    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] arrayTabs = getResources().getStringArray(R.array.arrayTabs);
        final ActionBar actionBar = getSupportActionBar();

        //init views
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //set actionbar to show tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        final ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add tabs to actionbar, specifying the tab's text and TabListener
        for (int i = 0; i < arrayTabs.length; i++)
            actionBar.addTab(actionBar.newTab().setText(arrayTabs[i]).setTabListener(tabListener));

        //set fragments pager
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                BaseFragment frag = null;

                switch (position) {
                    case 0: {
                        frag = new ResourcesFragment();
                    }
                        break;
                    case 1: {
                        frag = new NewsFragment();
                    }
                        break;
                    default:
                        AppLog.debug(TAG, "Unknown page position.");
                        break;

                }

                return frag;
            }

            @Override
            public int getCount() {
                return arrayTabs.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return arrayTabs[position];
            }
        });
        //add pager change listener to update actionbar tabs
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(Menu.NONE, 1, Menu.NONE, R.string.settings);
        mi.setIcon(android.R.drawable.ic_menu_preferences);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = true;

        switch(item.getItemId()){
            case 1:
                toast("No available");
                break;
            default:
                retVal = super.onOptionsItemSelected(item);
        }

        return retVal;
    }



}
