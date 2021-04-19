package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.earthquake.DAO.EarthquakeDatabaseAccessor;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeSearchResultActivity extends AppCompatActivity {
    //存储搜索建议，及用户输入的搜索关键字
    MutableLiveData<String> searchQuery;

    //存储所选建议的ID
    private MutableLiveData<String> selectedSearchSuggestionId;

    LiveData<Earthquake> selectedSearchSuggestion;

    
    
    
    private void setSelectedSearchSuggestion(Uri dataString){
        String id = dataString.getPathSegments().get(1);
        selectedSearchSuggestionId.setValue(id);
    }

    
    final Observer<Earthquake> selectedSearchSuggestionObserver = selectedSearchSuggestion -> {
        if (selectedSearchSuggestion != null){
            setSearchQuery(selectedSearchSuggestion.getMDetails());
        }
    };

    private void setSearchQuery(String query){
        searchQuery.setValue(query);
    }



    //存储搜索建议的数组，主要用于recycleView与adapter的绑定
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();
    
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter = new EarthquakeRecyclerViewAdapter(mEarthquakes);
    LiveData<List<Earthquake>> searchResults;


    private final Observer<List<Earthquake>> searchQueryResultObserver
            = updatedEarthquakes -> {
        // Update the UI with the updated search query results.
        mEarthquakes.clear();
        if (updatedEarthquakes != null) {
            mEarthquakes.addAll(updatedEarthquakes);
        }
        mEarthquakeAdapter.notifyDataSetChanged();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.search_result_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mEarthquakeAdapter);
        searchQuery = new MutableLiveData<>();
        searchQuery.setValue(null);
        //绑定搜索，当searchQuery搜索关键数字改变时，通知searchResults改变。
        searchResults = Transformations.switchMap(searchQuery,
                query -> EarthquakeDatabaseAccessor
                        .getInstance(getApplicationContext())
                        .earthquakeDAO()
                        .searchEarthquakes("%" + query + "%"));
       searchResults.observe(EarthquakeSearchResultActivity.this,  searchQueryResultObserver);
       selectedSearchSuggestionId = new MutableLiveData<>();
       selectedSearchSuggestionId.setValue(null);
       selectedSearchSuggestion = Transformations.switchMap(selectedSearchSuggestionId,
               id -> EarthquakeDatabaseAccessor
                       .getInstance(getApplicationContext())
                       .earthquakeDAO()
                       .getEarthquake(id));
       if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
           selectedSearchSuggestion.observe(this, selectedSearchSuggestionObserver);
           setSelectedSearchSuggestion(getIntent().getData());
       }else{
           String query = getIntent().getStringExtra(SearchManager.QUERY);
           setSearchQuery(query);
       }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())){
            setSelectedSearchSuggestion(getIntent().getData());
        }else {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            setSearchQuery(query);
        }
    }
}
