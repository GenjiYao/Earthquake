package com.example.earthquake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EarthquakeMainActivity extends AppCompatActivity implements EarthquakeListFragment.OnListFragmentInteractionListener{

    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAMENT";
    private EarthquakeViewModel earthquakeViewModel;
    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    EarthquakeListFragment mlist_item_earthquake;


    @Override
    public void onListFragmentRefreshRequested() {
        updateEarthquakes();
    }

    private void updateEarthquakes() {
        if (earthquakeViewModel != null){
            earthquakeViewModel.loadEarthquakes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), EarthquakeSearchResultActivity.class));

        SearchView searchView = (SearchView)menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);


        //menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_settings);
        return true;
    }

    private static final int SHOW_PREFERENCES = 1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.settings_menu_item:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivityForResult(intent, SHOW_PREFERENCES);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();         //获取fragment管理
        if (savedInstanceState == null){                           //android 在意外结束activity时会自动保存状态，
                                                                  // 若以完整生命周期结束，即以finish方法则不会自动保存
            FragmentTransaction ft = fm.beginTransaction();
            mlist_item_earthquake = new EarthquakeListFragment();
            ft.add(R.id.main_activity_frame,mlist_item_earthquake,TAG_LIST_FRAGMENT);
            ft.commitNow();
        }else {
            mlist_item_earthquake = (EarthquakeListFragment) fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }
//        Date now = Calendar.getInstance().getTime();
//        ArrayList<Earthquake> dummyQuakes = new ArrayList<>(0);
//        dummyQuakes.add(new Earthquake("0", now, "San Jose", null, 7.3, null));
//        dummyQuakes.add(new Earthquake("1", now, "LA", null, 6.5, null));
//        mlist_item_earthquake.setmEarthquakes(dummyQuakes);
          earthquakeViewModel = ViewModelProviders.of(this).get(EarthquakeViewModel.class);
    }
}
