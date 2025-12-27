package com.example.masomi1;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Handler handler = new Handler();

    TextView txtCurrent, txtTotal;
    ImageButton btnPlayPause;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        seekBar = findViewById(R.id.seekBar);
        txtCurrent = findViewById(R.id.txtCurrent);
        txtTotal = findViewById(R.id.txtTotal);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        url = getIntent().getStringExtra("musicUrl");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {

            if (url.startsWith("content://") || url.startsWith("file://")) {
                Uri uri = Uri.parse(url);
                mediaPlayer.setDataSource(this, uri);
            } else {
                mediaPlayer.setDataSource(url);
            }

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                seekBar.setMax(mp.getDuration());
                txtTotal.setText(formatTime(mp.getDuration()));
                updateSeekBar();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("MP_ERROR", "Error: " + what + " , extra: " + extra);
                return false;
            });

            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    txtCurrent.setText(formatTime(progress));
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int pos = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(pos);
            txtCurrent.setText(formatTime(pos));
        }
        handler.postDelayed(this::updateSeekBar, 200);
    }

    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) mediaPlayer.release();
    }
}