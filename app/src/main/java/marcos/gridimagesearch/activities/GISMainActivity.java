package marcos.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;


import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import marcos.gridimagesearch.marcos.gridimagesearch.helpers.EndlessScrollListener;
import marcos.gridimagesearch.models.ImageResult;
import marcos.gridimagesearch.marcos.gridimagesearch.adapters.ImageResultArrayAdapter;
import marcos.gridimagesearch.R;


public class GISMainActivity extends ActionBarActivity  {
    private GridView mGVResults;
    private ArrayList<ImageResult> mImageResults;
    private ImageResultArrayAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String mSize, mType, mColor, mSite, mQuery;
    private AsyncHttpClient mClient;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gismain);

        mImageResults = new ArrayList<ImageResult>();
        mClient = new AsyncHttpClient();

        setupViews();

        setupmGVResults();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    public void setupmGVResults(){
        mAdapter = new ImageResultArrayAdapter(this, mImageResults);
        mGVResults.setAdapter(mAdapter);
        mGVResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplicationContext(),ImageDisplayActivity.class);
                ImageResult imageResult = mImageResults.get(position);
                intent.putExtra("result",imageResult);
                startActivity(intent);

            }
        });

        mGVResults.setOnScrollListener( new EndlessScrollListener(16){
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

    }

    public void customLoadMoreDataFromApi(int page){
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        httpClientGetResult(page * 8);
    }

    public void getSettings(){
        mSize = mSharedPreferences.getString("lt_size", "");
        mSite = mSharedPreferences.getString("et_site","");
        mColor = mSharedPreferences.getString("lt_color","");
        mType = mSharedPreferences.getString("lt_type", "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSettings(); //get settings for default search filter
    }

    public void setupViews(){
        mGVResults = (GridView)findViewById(R.id.gvResults);
    }


    //this method is called when the user enter a new query
    //in the search action bar
    public void onImageSearch(){
        int pageStart = 0;

        mImageResults.clear();//clear list
        mAdapter.notifyDataSetChanged();

        httpClientGetResult(pageStart);
    }

    private void httpClientGetResult(int pageStart){

        if (hasInternetConnection()) {

            mClient.get("https://ajax.googleapis.com/ajax/services/search/images?" +
                    "rsz=" + 8 +
                    "&start=" + pageStart +
                    "&imgcolor=" + mColor + // color filter
                    "&imgsz=" + mSize + //image size filter
                    "&imgtype=" + mType + //image type
                    "&as_sitesearch=" + mSite + //site where is to be searched
                    "&v=1.0&q=" + Uri.encode(mQuery), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    JSONArray imageJsonResultsArray = null;
                    try {
                        imageJsonResultsArray =
                                response.getJSONObject("responseData").getJSONArray("results");

                        mAdapter.addAll(ImageResult.fromJsonArray(imageJsonResultsArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Toast.makeText(getBaseContext(),"No internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    //check if internet connection is available
    public boolean hasInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       //Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.gismain,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mQuery = s;
                onImageSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_search:
                return true;
        }
        return false;
    }


} //eof class
