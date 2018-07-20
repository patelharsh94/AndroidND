package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ReviewTrailerApiCallTask extends AsyncTask<String, Integer, HashMap<String, String>> {

    private static String TAG = "API_CALL_TASK";
    public ReviewTrailerResultGetter resultGetter = null;
    private Context root;
    String type;


    ReviewTrailerApiCallTask(Context root) {
        this.root = root;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> dataMap) {
        super.onPostExecute(dataMap);

        if(this.type.equals(root.getResources().getString(R.string.type_review))) {
            resultGetter.getReviewData(dataMap);
        } else if (this.type.equals(root.getResources().getString(R.string.type_trailer))) {
            resultGetter.getTrailerData(dataMap);
        }

    }

    private HashMap<String, String> parseJsonFromData(String apiData) {

        HashMap<String, String> finalDataMap = new HashMap<>();

        try {

            JSONObject dataJSON = new JSONObject(apiData);

            JSONArray dataList = dataJSON.getJSONArray("results");
            JSONObject currObject;

            Log.i("TAG", "IN review api call: " + dataList);
            Log.i("TAG", "TYPE FOUND: " + this.type);
            Log.i("TAG", "TYPE FOUND: " + root.getResources().getString(R.string.type_review));

            for (int i = 0; i < dataList.length(); i++) {
                currObject = dataList.getJSONObject(i);
                if(this.type.equals(root.getResources().getString(R.string.type_review))) {
                    finalDataMap.put(currObject.getString("id"), currObject.getString("content"));
                } else if (this.type.equals(root.getResources().getString(R.string.type_trailer))) {
                    finalDataMap.put(currObject.getString("key"), currObject.getString("name"));
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return finalDataMap;
    }


    @Override
    protected HashMap<String, String> doInBackground(String... strings) {
        HashMap<String, String > dataMap = new HashMap<>();

        try {

            URL url = new URL(strings[0]);
            this.type = strings[1];

            String data;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            Log.i(TAG, "GOT DATA: " + br.toString());

            while((data = br.readLine()) != null) {
                dataMap = parseJsonFromData(data);
            }

            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return dataMap;
    }
}
