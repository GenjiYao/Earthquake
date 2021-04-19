package com.example.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeListFragment extends Fragment {
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter = new EarthquakeRecyclerViewAdapter(mEarthquakes);
    private SwipeRefreshLayout mSwipeRefreshView;
    private EarthquakeViewModel earthquakeViewModel;


    public void setmEarthquakes(List<Earthquake> Earthquakes) {
        this.mEarthquakes.clear();
        this.mEarthquakeAdapter.notifyDataSetChanged();
        updateFromPreferences();
        for (Earthquake earthquake : Earthquakes) {
            if (earthquake.getMMagnitude() >= mMinimumMagnitude) {
                if (!this.mEarthquakes.contains(earthquake)) {
                    this.mEarthquakes.add(earthquake);
                    mEarthquakeAdapter.notifyItemInserted(this.mEarthquakes.indexOf(earthquake));
                }
            }
        }
        if (mEarthquakes != null && mEarthquakes.size() > 0){
            for (int i = mEarthquakes.size() - 1; i >= 0; i--) {
                if (mEarthquakes.get(i).getMMagnitude() < mMinimumMagnitude){
                    mEarthquakes.remove(i);
                    mEarthquakeAdapter.notifyItemRemoved(i);
                }
            }
        }
        mSwipeRefreshView.setRefreshing(false);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentRefreshRequested();
    }

    private OnListFragmentInteractionListener mListener;

    public EarthquakeListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earthquake_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mSwipeRefreshView = view.findViewById(R.id.swiperefresh);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mEarthquakeAdapter);
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEarthquakes();
            }
        });
    }

    protected void updateEarthquakes() {
           if (mListener != null) {
               mListener.onListFragmentRefreshRequested();
           }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(List<Earthquake> earthquakes) {
                if (earthquakes != null){
                    setmEarthquakes(earthquakes);
                }
            }
        });

        //注册 OnSharedPreferencesChangeListener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(mPrefListener);

    }
    private  SharedPreferences.OnSharedPreferenceChangeListener mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (PreferencesActivity.PREF_MIN_MAG.equals(key)) {
                List<Earthquake> earthquakes = earthquakeViewModel.getEarthquakes().getValue();
                if (earthquakes != null) {
                    setmEarthquakes(earthquakes);
                }
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private int mMinimumMagnitude = 0;
    private void updateFromPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mMinimumMagnitude = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));            //sharedPreference 读取默认的用户偏好中的最小地震强度。
    }

}
