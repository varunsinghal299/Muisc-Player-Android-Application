package com.example.music_try1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button start, pause, stop, download, next, FullList;
    ArrayList<String> arrayList;

    // For Broadcasting
    MyReceiver myReceiver = new MyReceiver();
    IntentFilter filter =new IntentFilter();

    ArrayAdapter songListAdaptor;

    int resId;
    int pos=0;
    int max_song;
    int currentmusicposition;

    protected void updateSongs(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        download = findViewById(R.id.download);

        next = findViewById(R.id.next);
        FullList = findViewById(R.id.FullList);

        ListMusicFragment firstfragment = new ListMusicFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.abc,firstfragment);
        transaction.commit();


        // For Boardcasting Battery
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);

        MainActivity.this.registerReceiver(myReceiver,filter);


        arrayList = new ArrayList<String>();
        Field[] fields = R.raw.class.getFields();

        // Update the max no of songs

        max_song = fields.length;
        for(int i=0 ; i < fields.length ; i++){
            arrayList.add(fields[i].getName());
        }

        songListAdaptor = new ArrayAdapter(this,  android.R.layout.simple_list_item_1, arrayList);
//        mySongList.setAdapter(songListAdaptor);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentMusicFragment secondfragment = new CurrentMusicFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.abc,secondfragment);
                transaction.commit();

                if(MPlayer.player==null){
                    resId = getResources().getIdentifier(arrayList.get(pos), "raw", getPackageName());
                    MPlayer.player = MediaPlayer.create(MainActivity.this, resId);

                    MPlayer.player.start();
                }
                else if(!MPlayer.player.isPlaying()){
                    MPlayer.player.seekTo(currentmusicposition);
                    MPlayer.player.start();
                }

                //
                Log.i("ForeGround", "varun singhal");
//                String input = arrayList.get(pos).toString();
//                Intent serviceIntent = new Intent(MainActivity.this, ForeGroundService.class);
//                serviceIntent.putExtra("inputExtra", input);
//                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                //

                Log.i("ForeGround", "varun singhal");

                Intent intent = new Intent(MainActivity.this, ForeGroundService.class);
                intent.setAction(ForeGroundService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);

            }

        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MPlayer.player!=null){
                    MPlayer.player.pause();
                    currentmusicposition = MPlayer.player.getCurrentPosition();
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MPlayer.player!=null){
                    MPlayer.player.stop();
                    MPlayer.player = null;
                    pos=0;
//                    Intent serviceIntent = new Intent(MainActivity.this, ForeGroundService.class);
//                    stopService(serviceIntent);
                    Intent intent = new Intent(MainActivity.this, ForeGroundService.class);
                    intent.setAction(ForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
                    startService(intent);

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MPlayer.player!=null){
                    pos++;
                    pos = pos % max_song;

                    MPlayer.player.stop();
                    MPlayer.player = null;

                    Intent intent1 = new Intent(MainActivity.this, ForeGroundService.class);
                    intent1.setAction(ForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
                    startService(intent1);

                    resId = getResources().getIdentifier(arrayList.get(pos), "raw", getPackageName());
                    MPlayer.player = MediaPlayer.create(MainActivity.this, resId);

                    MPlayer.player.start();

                    Intent intent2 = new Intent(MainActivity.this, ForeGroundService.class);
                    intent2.setAction(ForeGroundService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent2);
                }

            }
        });

        FullList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListMusicFragment firstfragment = new ListMusicFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.abc,firstfragment);
                transaction.commit();

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, download.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MPlayer.player.stop();
        MPlayer.player = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        filter.addAction(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_BATTERY_OKAY);
//
//        MainActivity.this.registerReceiver(myReceiver,filter);

    }
}

