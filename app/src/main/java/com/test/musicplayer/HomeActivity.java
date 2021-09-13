package com.test.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.songSeekbar)
    SeekBar songSeekbar;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.buttonPlay)
    Button buttonPlay;
    @BindView(R.id.volumeSekkbar)
    SeekBar volumeSekkbar;
    MediaPlayer musicPlayer;
    @BindView(R.id.songName)
    TextView songName;
    @BindView(R.id.artistName)
    TextView artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

//        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Song song = (Song) getIntent().getSerializableExtra("song");




        if (song != null){
            songName.setText(song.getName());
        }

        if (song != null){
            artistName.setText(song.getArtist());
        }


        // Music Player
        musicPlayer = new MediaPlayer();
        try {

            if (song != null){
                musicPlayer.setDataSource(song.getPath());
                musicPlayer.prepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);

        //Song Time
        String duration = millisecondsToString(musicPlayer.getDuration());
        tvDuration.setText(duration);

        // Duration Seekbar;
        songSeekbar.setMax(musicPlayer.getDuration());
        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null) {
                    if (musicPlayer.isPlaying()) ;
                    try {
                        final double current = musicPlayer.getCurrentPosition();
                        final String elapsedTime = millisecondsToString((int) current);

                        // run with main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTime.setText(elapsedTime);
                                songSeekbar.setProgress((int) current);
                            }
                        });

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

        // Play and pause button.
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.buttonPlay) {
                    if (musicPlayer.isPlaying()) {
                        musicPlayer.pause();
                        buttonPlay.setBackgroundResource(R.drawable.ic_play);
                    } else {
                        musicPlayer.start();
                        buttonPlay.setBackgroundResource(R.drawable.ic_pause);
                    }
                }
            }
        });

        //Volume seekbar
        volumeSekkbar.setProgress(50);
        volumeSekkbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                musicPlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    } // end main


    // Duration Meyhod
    public String millisecondsToString(int time) {
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes + ":";
        if (seconds < 10) {
            elapsedTime += "0";
        }
        elapsedTime += seconds;
        return elapsedTime;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        if (musicPlayer.isPlaying()){
            musicPlayer.stop();
        }
        return super.onOptionsItemSelected(item);
    }
}