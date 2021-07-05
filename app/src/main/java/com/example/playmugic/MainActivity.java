package com.example.playmugic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    public static ArrayList<File> getMySongs() {
        return mySongs;
    }

    static ArrayList<File> mySongs;

    public static String[] getItems() {
        return items;
    }

    static String[] items;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                     mySongs= fetchSongs(Environment.getExternalStorageDirectory());
                     items= new String[mySongs.size()];
                        for(int i = 0; i<mySongs.size();i++){
                            if (mySongs.get(i).getName().endsWith(".mp3")) {
                                items[i] = mySongs.get(i).getName().replace("mp3", "");
                            }
                            if (mySongs.get(i).getName().endsWith(".mp4")) {

                                items[i] = mySongs.get(i).getName().replace("mp4", "");
                            }
                        }

                        CustomAdapter adapter = new CustomAdapter(items, MainActivity.this);

                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    }
                })
                .check();
    }
    public ArrayList<File> fetchSongs (File file){
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if (songs!=null){
            for (File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if (myFile.getName().endsWith(".mp4") && !myFile.getName().startsWith("..") || myFile.getName().endsWith(".mp3")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }


}