package com.example.suhail.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private static final String TAG = "TestApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        new GetServerData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
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

                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
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
                    System.out.println("Request Failed");
                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
            }
            return null;
        }

        private void showInfo(final TestAndroid test) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Data loaded!", Toast.LENGTH_LONG).show();
                    setContentView(R.layout.activity_my);
                    ListView listView ;

                    listView = (ListView) findViewById(R.id.list);
                    String[] values = new String[]{test.getName(),
                            test.getLastname(),
                            test.getEmail(),
                            String.valueOf(test.getAge()),
                            test.getSex(),
                            test.getAdd(),
                            test.getPhone(),
                            test.getOrg(),
                       

                    };
//                    String[] values = new String[] { "Android List View",
//                            "Adapter implementation",
//                            "Simple List View In Android",
//                            "Create List View Android",
//                            "Android Example",
//                            "List View Source Code",
//                            "List View Array Adapter",
//                            "Android Example List View"
//                    };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

                    listView.setAdapter(adapter);
                }
            });
        }
    }

}
