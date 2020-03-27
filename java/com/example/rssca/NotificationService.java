package com.example.rssca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.View;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
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

public class NotificationService extends BroadcastReceiver {
    public static String lastTitle;
    public static FragmentActivity mActivity;
    public URL url;
    public static Context cont;


    @Override
    public void onReceive(Context context, Intent intent) {

        cont = context;

        try {
            url = new URL("https://newsapi.org/v2/top-headlines?country=IE&apiKey=b5c1d85ad31f42919fc82770e0aa98bc");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ListFrag Obj  = new ListFrag();
        (new mAsyncTask()).execute(url);


    }


    static class mAsyncTask extends AsyncTask<URL, View, String>
    {
        String sb = null;
        String title = "";




        // doInBackground creates a connection and reads in the rss feed and stores it in a string
        protected String doInBackground(URL... urls)
        {

            URL url = urls[0];
            HttpURLConnection urlConnection = null;
            try
            {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                assert urlConnection != null;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                sb = readStream(in);

            } catch (IOException e)
            {
                e.printStackTrace();
            } finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }

            return sb;
        }



        @Override
        protected void onPostExecute(String aVoid)
        {
            super.onPostExecute(aVoid);
            final JSONObject obj;
            try
            {
                obj = new JSONObject(sb);
                final JSONArray geodata = obj.getJSONArray("articles");
                JSONObject article = geodata.getJSONObject(0); // retrieve the json array named 'articles'
                title = article.getString("title"); // retrieve strings title from array

            } catch (JSONException e)
            {
                e.printStackTrace();
            }


            if (lastTitle != null)
            {
                if (lastTitle.equals(title))
                {

                }
                else
                {
                    lastTitle = title;

                    NotificationCompat.Builder notification = new NotificationCompat.Builder(cont, "news") //create a notification
                            .setContentTitle("News Reader")
                            .setContentText(title)
                            .setSmallIcon(R.drawable.googleg_standard_color_18)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(cont); // get context for notification

                    notificationManagerCompat.notify(17, notification.build()); //build notification
                }
            }
            else
            {
            lastTitle = title;
            }
            }




        private String readStream(InputStream is) throws IOException
        {
            String lines = "";
            BufferedReader r = new BufferedReader(new InputStreamReader(is));

            for (String line = r.readLine(); line != null; line = r.readLine())
            {
                lines = line;
            }
            is.close();
            return lines;
        }
    }

}
