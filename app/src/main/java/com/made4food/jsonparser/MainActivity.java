package com.made4food.jsonparser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<Google> googleList ;
    Button SearchButton ;
    EditText editText;
    GoogleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleList  = new ArrayList<>();

        editText = (EditText) findViewById(R.id.editText);

        ListView listview = (ListView)findViewById(R.id.list);
        adapter = new GoogleAdapter(getApplicationContext(), R.layout.row, googleList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView v =(TextView) view.findViewById(R.id.res);

                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(v.getText().toString()));
                startActivity(i);
            }
        });

        SearchButton = (Button) findViewById(R.id.search);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+editText.getText().toString();
                new JSONAsyncTask().execute(url);

            }
        });
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {

            InputStream inputStream ;
            HttpURLConnection urlConnection ;
            Boolean result ;
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    parseResult(response);
                    result = true; // Successful
                }else{
                    result = false; //"Failed to fetch data!";
                    Log.v("JSON", "failed");
                }
            } catch (Exception e) {
                result = false ;
               Log.d("", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!"

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(!result)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();


            //adapter.clear();

            //adapter.addAll(googleList);
            adapter.notifyDataSetChanged();
        }


    }

    private void parseResult(String result) {
        try{

            googleList.clear();

            JSONObject response = new JSONObject(result);

            JSONObject res = response.getJSONObject("responseData").getJSONArray("results").getJSONObject(0);
            JSONObject res1 = response.getJSONObject("responseData").getJSONArray("results").getJSONObject(1);
            JSONObject res2 = response.getJSONObject("responseData").getJSONArray("results").getJSONObject(2);
            JSONObject res3 = response.getJSONObject("responseData").getJSONArray("results").getJSONObject(3);




            Google google = new Google();
            google.setResult( res.getString("unescapedUrl"));
            google.setTitle(res.getString("titleNoFormatting"));
            googleList.add(google);


            Google google1 = new Google();
            google1.setResult( res1.getString("unescapedUrl"));
            google1.setTitle(res1.getString("titleNoFormatting"));
            googleList.add(google1);

            Google google2 = new Google();
            google2.setResult(res2.getString("unescapedUrl"));
            google2.setTitle(res2.getString("titleNoFormatting"));
            googleList.add(google2);

            Google google3 = new Google();
            google3.setResult(res3.getString("unescapedUrl"));
            google3.setTitle(res3.getString("titleNoFormatting"));
            googleList.add(google3);



        }catch (JSONException e){
            Log.v("URL", e.toString());
        }
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

}
