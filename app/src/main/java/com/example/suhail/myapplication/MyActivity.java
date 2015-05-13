package com.example.suhail.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyActivity extends Activity {
    private static final String TAG = "PostsActivity";

//    TestAndroid test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final EditText editText = (EditText) findViewById(R.id.edit_message);
        Button btnSubmit = (Button) findViewById(R.id.btnShowMessage);
//test = new TestAndroid();

        //create a java class
        // /testandroid - return name:sultan, age: 31, sex:male
        //google gson - map the returned json to that class
        //when the button is clicked .. show the first record in that class object in Toast

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set internet permission first in manifest.xml
                //send api call from here

                TestAndroid testAndroid = new TestAndroid();
                testAndroid.setAge(20);
                GetJson request=new GetJson();
                new GetJson().execute();
//                request.doInBackground();


            }
        });
//        btnSubmit.setOnClickListener()

        // Do something in response to button

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
    private void showInfo( TestAndroid test) {
//       this.test = test;


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), String.valueOf(test.getName()), Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(), String.valueOf(test.getAge()), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), String.valueOf(test.getSex()), Toast.LENGTH_LONG).show();


            }
        });
    }

    private class GetJson extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            System.out.println("caling API");

            try {
                //Create an HTTP client
                String SERVER_URL = "http://bridg-dev.cloudapp.net:8080/testandroid";

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SERVER_URL);

                //Perform the request and check the status code
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode());
                if(statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        System.out.println(content.toString());
                        TestAndroid testAndroid = new TestAndroid();
                        testAndroid = gson.fromJson(reader, TestAndroid.class);
                        System.out.println("name" + testAndroid.getName());
                        showInfo(testAndroid);

//                        posts = Arrays.asList(gson.fromJson(reader, Post[].class));
                        content.close();

//                        handlePostsList(posts);

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
//                        failedLoadingPosts();
                    }
                } else {
//                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
//                    failedLoadingPosts();
                }
            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
//                failedLoadingPosts();
            }
            return null;
        }
    }

}
