package com.example.dictionary;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DictionaryRequest extends AsyncTask<String  , Integer , String> {

    Context context;
    TextView showDef;

    DictionaryRequest(Context context , TextView tV)
    {
        this.context = context;
        showDef = tV;
    }

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "6e1effff";  // add the Id from the API credentials
            final String app_key = "95479d610826930d67000ec39c8de2f4";   // add the key from the API credentials
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String def;
            try {
                JSONObject js = new JSONObject(result);
                JSONArray results = js.getJSONArray("results");

                JSONObject lEntries = results.getJSONObject(0);
                JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

                JSONObject entries = laArray.getJSONObject(0);
                JSONArray e = entries.getJSONArray("entries");

                JSONObject jsonObject = e.getJSONObject(0);
                JSONArray sensesArray = jsonObject.getJSONArray("senses");

                JSONObject de = sensesArray.getJSONObject(0);
                JSONArray d = de.getJSONArray("definitions");


                def = d.getString(0);
                showDef.setText(def);


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            Log.v("Result of Dictionary" , "onPostExecute" +result);
        }
    }


