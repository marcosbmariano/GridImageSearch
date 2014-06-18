package marcos.gridimagesearch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by marcos on 6/16/14.
 */
public class ImageResult implements Serializable{

    private String fullUrl;
    private String thumbUrl;

    public ImageResult(JSONObject json){
        try{
            fullUrl = json.getString("url");
            thumbUrl = json.getString("tbUrl");
        } catch (JSONException e){
            fullUrl = null;
            thumbUrl = null;
        }
    }


    public String getFullUrl() {
        return fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    @Override
    public String toString() {
        return fullUrl + thumbUrl;
    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray array) {
        ArrayList<ImageResult> resultList = new ArrayList<ImageResult>();

        for (int i = 0; i < array.length(); i++) {
            try{
                ImageResult obj = new ImageResult(array.getJSONObject(i));

                //resultList.add(new ImageResult(array.getJSONObject(i)));
                resultList.add(obj);


            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return resultList;
    }



}
