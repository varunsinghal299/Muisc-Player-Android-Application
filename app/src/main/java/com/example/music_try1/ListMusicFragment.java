package com.example.music_try1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class ListMusicFragment extends Fragment {

    ListView mySongList;
    ArrayList<String> arrayList;

    ArrayAdapter songListAdaptor;

    public ListMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_music, container, false);

        mySongList = view.findViewById(R.id.mySongList);

        arrayList = new ArrayList<String>();
        Field[] fields = R.raw.class.getFields();

        for(int i=0 ; i < fields.length ; i++){
            arrayList.add(fields[i].getName());
        }

        songListAdaptor = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        mySongList.setAdapter(songListAdaptor);

//        mySongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(MPlayer.player != null){
//                    MPlayer.player.release();
//                }
//
//
//                int resId = getResources().getIdentifier(arrayList.get(position), "raw", getActivity().getPackageName());
//                MPlayer.player = MediaPlayer.create(getActivity(), resId);
//                MPlayer.player.start();
//
//            }
//        });

        return view;

    }
}