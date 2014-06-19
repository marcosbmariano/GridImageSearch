package marcos.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import marcos.gridimagesearch.models.ImageResult;
import marcos.gridimagesearch.marcos.gridimagesearch.adapters.ImageResultArrayAdapter;
import marcos.gridimagesearch.R;


public class GISMainActivity extends ActionBarActivity  {
    private EditText mQueryText;
    private GridView mGVResults;
    private Button mSearchButton;
    private ArrayList<ImageResult> mImageResults = new ArrayList<ImageResult>();
    private ImageResultArrayAdapter adapter;
    private SharedPreferences mSharedPreferences;
    private String mSize, mType, mColor, mSite;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gismain);

        setupViews();

        setupmGVResults();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void setupmGVResults(){
        adapter = new ImageResultArrayAdapter(this, mImageResults);
        mGVResults.setAdapter(adapter);
        mGVResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplicationContext(),ImageDisplayActivity.class);
                ImageResult imageResult = mImageResults.get(position);
                intent.putExtra("result",imageResult);
                startActivity(intent);

            }
        });

    }

    public void getSettings(){
        mSize = mSharedPreferences.getString("lt_size", "");
        mSite = mSharedPreferences.getString("et_site","");
        mColor = mSharedPreferences.getString("lt_color","");
        mType = mSharedPreferences.getString("lt_type", "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences.edit().putString("query", mQueryText.getText().toString()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSettings();

        mQueryText.setText(mSharedPreferences.getString("query",""));



//
    }

    public void setupViews(){
        mQueryText = (EditText)findViewById(R.id.etQuery);
        mGVResults = (GridView)findViewById(R.id.gvResults);
        mSearchButton = (Button)findViewById(R.id.btnSearch);
    }


    //imgsz=small|medium|large|xlarge



    public void onImageSearch(View v){
        String query = mQueryText.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();

        String natanString ="https://ajax.googleapis.com/ajax/services/search/images?rsz=&&" + "start="
                + 0 + "&v=1.0&q=";
        client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start0" +
                "&imgcolor=" + mColor  +
                "&imgsz=" + mSize +
                "&imgtype=" + mType +
                "&v=1.0&q="+ Uri.encode(query), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResultsArray = null;

                try{
                    imageJsonResultsArray =
                            response.getJSONObject("responseData").getJSONArray("results");

                    mImageResults.clear();//clear list
                    adapter.addAll(ImageResult.fromJsonArray(imageJsonResultsArray));
                    adapter.addAll(ImageResult.fromJsonArray(imageJsonResultsArray));
                    adapter.addAll(ImageResult.fromJsonArray(imageJsonResultsArray));


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gismain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
