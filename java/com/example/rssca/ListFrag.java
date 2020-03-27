package com.example.rssca;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ListFrag extends Fragment {
    private URL url;
    public ListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String country = intent.getStringExtra("locale");
        View myInflatedView = inflater.inflate(R.layout.fragment_list, container,false); // inflate the view

        try {
            url = new URL("https://newsapi.org/v2/top-headlines?country="+ country +"&apiKey=b5c1d85ad31f42919fc82770e0aa98bc");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        (new mAsyncTask(getActivity())).execute(url); // Calling of async task that obtains and parses the news rss feed

        // Inflate the layout for this fragment
        return myInflatedView;

    }




    static class mAsyncTask extends AsyncTask<URL, View, String> {
        String sb = null;
        @SuppressLint("StaticFieldLeak")
        FragmentActivity mActivity;


        mAsyncTask(FragmentActivity activity) {
           mActivity = activity;
        }


        // doInBackground creates a connection and reads in the rss feed and stores it in a string
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert urlConnection != null;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                sb = readStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }

            return sb;
        }



        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            RecyclerView recyclerView = mActivity.findViewById(R.id.recyclerView); //find the recycler view from the xml
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity)); // set a linear layout manager to the recycler view
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(); // instantiation of recyclerAdapter class
            recyclerView.setAdapter(recyclerAdapter); //set the adapter to the recycler view

            List<String> titleList = new ArrayList<>(); // create lists to store titles, descriptions and content
            List<String> descList = new ArrayList<>();
            List<String> conList = new ArrayList<>();


            try {
                final JSONObject obj = new JSONObject(sb);
                final JSONArray geodata = obj.getJSONArray("articles");
                for (int i = 0; i < geodata.length(); i++) {
                    JSONObject article = geodata.getJSONObject(i); // retrieve the json array named 'articles'
                    String title = article.getString("title"); // retrieve strings title from array
                    String desc = article.getString("description");
                    String content = article.getString("content");

                    titleList.add(title);
                    descList.add(desc);
                    conList.add(content);
                }

                recyclerAdapter.setTitles(this.mActivity, titleList); // set titles from json to recycler view using recycler adapter
                recyclerAdapter.setDesc(this.mActivity, descList);
                recyclerAdapter.setContext(conList);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }


        }

        private String readStream(InputStream is) throws IOException {
            String lines = "";
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                lines = line;
            }
            is.close();
            return lines;
        }
    }
}


