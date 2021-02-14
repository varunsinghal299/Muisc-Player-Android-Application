package com.example.music_try1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class download extends AppCompatActivity {

    Button checknet, play, stop;
    TextView result;

    String song_url = "https://faculty.iiitd.ac.in/~mukulika/s1.mp3";
    String music_name = "music1";

    public int checkConnection(){
        int flag = 0;
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(activeNetwork!=null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                flag = 1;
                Toast.makeText(this, "WIFI ENABLE", Toast.LENGTH_SHORT).show();
            }

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                flag = 1;
                Toast.makeText(this, "DATA NETWORK ENABLE", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            flag=0;
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }
        return flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        checknet = findViewById(R.id.checknet);
        result = findViewById(R.id.result);

        play = findViewById(R.id.playsong);
        stop = findViewById(R.id.stopSong);

        checknet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int network = checkConnection();
                String s;
                if(network == 1){
                    s = "Internet is Available and Start Download";
                    Toast.makeText(download.this, "Internet Available", Toast.LENGTH_SHORT).show();

                    // Code to download mp3 file on givrn url and save it in raw folder.
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(song_url);
                    //
                }
                else{
                    s = "Internet is Not Available";
                    Toast.makeText(download.this, "Internet Not Available", Toast.LENGTH_SHORT).show();
                }
                result.setText(s);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//                Context context = getApplicationContext();
//                File file = context.getFileStreamPath(music_name);
//
//                Uri main_url = Uri.fromFile(file);
//                Log.i("varun singhal", "onPostExecute: First step");
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioAttributes(
//                        new AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                                .setUsage(AudioAttributes.USAGE_MEDIA)
//                                .build()
//                );
//                Log.i("varun singhal", "onPostExecute: second step");
//                try {
//                    mediaPlayer.setDataSource(getApplicationContext(), main_uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.i("varun singhal", "onPostExecute: third step");
//
//
//                Log.i("varun singhal", "onPostExecute: 4th step");
////            Toast.makeText(download.this, Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                mediaPlayer.start();

                MPlayer.player = MediaPlayer.create(download.this, Uri.parse("/data/data/com.example.music_try1/files/music1"));
                MPlayer.player.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MPlayer.player!=null){
                    MPlayer.player.stop();
                    MPlayer.player = null;
                }
            }
        });

    }


    class DownloadTask extends AsyncTask<String, Integer, String>
    {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//           super.onPreExecute();
            progressDialog = new ProgressDialog(download.this);
            progressDialog.setTitle("Downloading in progress");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = (URLConnection) url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

//                File new_folder = new File("varun/music_ass2");
//                if(!new_folder.exists()){
//                    new_folder.mkdir();
//                }
//                File input_file = new File(new_folder, "Download.mp3");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data =  new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = openFileOutput("music1", Context.MODE_PRIVATE);
                while( (count=inputStream.read(data))!=-1){
                    total+=count;
                    outputStream.write(data, 0, count);
                    int progress = (int) total*100/file_length;
                    publishProgress(progress);
                }

                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(aVoid);
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }



}