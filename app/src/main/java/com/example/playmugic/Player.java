package com.example.playmugic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Player extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

    ArrayList<File> songs;
    String [] songList;
    Intent intent;
    TextView songName;
    MediaPlayer player;
    SeekBar seekBar;
    Thread updateSeek;
    ImageView play;
    ImageView next;
    int position;
    ImageView repet;
    ImageView shuffle;
    ImageView previous;
    String currentSong;
    Boolean repeatBoolean=false;
    Boolean shuffleBoolean=false;
//    ImageView pause;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_player);


            play = findViewById(R.id.play);
            seekBar = findViewById(R.id.seekBar);
            songName = findViewById(R.id.songName);
            next = findViewById(R.id.next);
            repet = findViewById(R.id.repeat);
            shuffle= findViewById(R.id.shuffle);
            previous= findViewById(R.id.previous);


            intent = getIntent();
            currentSong = intent.getStringExtra("currentSong");
            position = intent.getIntExtra("position", 0);

            songs = MainActivity.getMySongs();
            songList = MainActivity.getItems();

              play();
            seekBar.setMax(player.getDuration());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    player.seekTo(seekBar.getProgress());
                }
            });

            updateSeek = new Thread() {
                        @Override
                        public void run() {
                            int currentPosition = 0;
                            try {
                                while (currentPosition < player.getDuration()) {
                                    currentPosition = player.getCurrentPosition();
                                    seekBar.setProgress(currentPosition);
                                    sleep(800);
                                }
                            } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            updateSeek.start();

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.isPlaying()) {
                        play.setImageResource(R.drawable.play);
                        player.pause();
                    } else {
                        play.setImageResource(R.drawable.pause);
                        player.start();
                    }
                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.stop();
                    player.release();

                    if (shuffleBoolean){
                        Random random = new Random();
                        position = random.nextInt(songs.size() - 1);
                    }else if (position != songs.size() - 1){
                        position = position+1;
                    }else{
                        position = 0;
                    }
                    play();

                }
            });
            repet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(Player.this, "thiche", Toast.LENGTH_SHORT).show();
                    if (!repeatBoolean){
                        repeatBoolean= true;
                        repet.setImageResource(R.drawable.repeat_on);
                        shuffleBoolean= false;
                        shuffle.setImageResource(R.drawable.shuffle_off);
                    }else if (repeatBoolean){
                        repeatBoolean= false;
                        repet.setImageResource(R.drawable.repeat);
                    }


                }
            });

            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Player.this, "thiche", Toast.LENGTH_SHORT).show();
                    if (!shuffleBoolean){
                        repeatBoolean= false;
                        repet.setImageResource(R.drawable.repeat);
                        shuffleBoolean= true;
                        shuffle.setImageResource(R.drawable.shuffle);
                    }else if (shuffleBoolean){
                        shuffleBoolean= false;
                        shuffle.setImageResource(R.drawable.shuffle_off);

                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.stop();
                    player.release();
                    if (position != 0){
                        position = position-1;
                    }else{
                        position = songs.size() - 1;
                    }
                    play();
                }
            });
            }

            public void play(){
                songName.setText(songList[position]);
                Uri uri = Uri.parse(songs.get(position).toString());
                player = MediaPlayer.create(getApplicationContext(), uri);
                player.start();
                seekBar.setProgress(0);
                seekBar.setMax(player.getDuration());
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        next();
                        }

                });
            }
            public void next(){
                if (shuffleBoolean && !repeatBoolean){
                    Random random = new Random();
                    position = random.nextInt(songs.size() - 1);
                }else if (!shuffleBoolean && repeatBoolean){
                    player.setLooping(true);
                }else{
                    position++;
                }
                play();
            }
}



