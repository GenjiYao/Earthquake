package com.example.earthquake.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.earthquake.DAO.EarthquakeDAO;
import com.example.earthquake.DAO.EarthquakeDatabaseAccessor;
import com.example.earthquake.Earthquake;

import java.net.URI;

public class EarthquakeSearchProvider extends ContentProvider {
    private static final int SEARCH_SUGGESTIONS = 1;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.earthquake.provider.EarthquakeSearchProvider", SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.example.earthquake.provider.EarthquakeSearchProvider", SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.example.earthquake.provider.EarthquakeSearchProvider",SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.example.earthquake.provider.EarthquakeSearchProvider", SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH_SUGGESTIONS);
    }
    @Override
    public boolean onCreate() {
        EarthquakeDatabaseAccessor.getInstance(getContext().getApplicationContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case SEARCH_SUGGESTIONS:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == SEARCH_SUGGESTIONS){
            String searchQuery = "%" + uri.getLastPathSegment() + "%";

            EarthquakeDAO earthquakeDAO =EarthquakeDatabaseAccessor.getInstance(getContext().getApplicationContext()).earthquakeDAO();
            Cursor c = earthquakeDAO.generateSearchSuggestions(searchQuery);
            return c;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
