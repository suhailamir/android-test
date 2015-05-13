package com.example.suhail.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class MyActivity extends Activity {
    private static final String TAG = "PostsActivity";

    //    TestAndroid test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
//        final EditText editText = (EditText) findViewById(R.id.edit_message);
        Button btnSubmit = (Button) findViewById(R.id.btnShowMessage);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetServerData().execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class GetServerData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            final TestAndroid testAndroid;
            try {
                String SERVER_URL = "http://bridg-dev.cloudapp.net:8080/testandroid";

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SERVER_URL);

                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode());

                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();


                        testAndroid = gson.fromJson(reader, TestAndroid.class);

                        content.close();
                        showInfo(testAndroid);


                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                    }
                } else {
//                    failedLoadingPosts();
                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
            }
            return null;
        }

        private void showInfo(final TestAndroid test) {
//       this.test = test;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), test.getName() + " " + String.valueOf(test.getAge()) + " " + test.getSex(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
