package com.med.superapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Music extends AppCompatActivity {
    private TextView textMaxTime;
    private TextView textCurrentPosition;
    private Button buttonPause;
    private Button buttonStart;
    private Button buttonRewind;
    private Button buttonFastForward;

    private Button buttonSelect;

    private SeekBar seekBar;
    private Handler threadHandler = new Handler();
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        this.textCurrentPosition = (TextView)this.findViewById(R.id.textView_currentPosition);
        this.textMaxTime=(TextView) this.findViewById(R.id.textView_maxTime);

        this.buttonSelect = (Button) this.findViewById(R.id.button_select);

        this.buttonStart= (Button) this.findViewById(R.id.button_start);
        this.buttonPause= (Button) this.findViewById(R.id.button_pause);
        this.buttonRewind= (Button) this.findViewById(R.id.button_rewind);
        this.buttonFastForward= (Button) this.findViewById(R.id.button_fastForward);

        this.buttonStart.setEnabled(false);
        this.buttonPause.setEnabled(false);
        this.buttonRewind.setEnabled(false);
        this.buttonFastForward.setEnabled(false);


        this.seekBar= (SeekBar) this.findViewById(R.id.seekBar);
        this.seekBar.setClickable(false);

        this.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select new media source.
                selectMediaResource();
            }
        });

        this.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart( );
            }
        });
        this.buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPause( );
            }
        });
        this.buttonRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRewind( );
            }
        });
        this.buttonFastForward .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFastForward( );
            }
        });

        // Create MediaPlayer.
        this.mediaPlayer=  new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                doStop(); // Stop current media.
            }
        });
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                doComplete();
            }
        });
    }

    // When user click "Select Media Source" button.
    private void selectMediaResource()  {
        // this.selectRawMediaSource();
        // this.selectURLMediaSource();
        // this.selectLocalMediaSource();
        this.selectRawMediaSource();
    }

    private void selectRawMediaSource() {
        // "mysong.mp3" ==> resName = "mysong".
        String resName = MediaPlayerUtils.RAW_MEDIA_SAMPLE;
        MediaPlayerUtils.playRawMedia(this, this.mediaPlayer, resName);
    }

    private void selectURLMediaSource()  {
        // http://example.coom/mysong.mp3
        String mediaURL = MediaPlayerUtils.URL_MEDIA_SAMPLE;
        MediaPlayerUtils.playURLMedia(this, this.mediaPlayer, mediaURL);
    }

    private void selectLocalMediaSource()  {
        // @localPath = "/storage/emulated/0/DCIM/Music/mysong.mp3"; (For example).
        String localPath = MediaPlayerUtils.LOCAL_MEDIA_SAMPLE;
        MediaPlayerUtils.playLocalMedia(this, this.mediaPlayer, localPath);
    }

    // Convert millisecond to string.
    private String millisecondsToString(int milliseconds)  {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) ;
        return minutes + ":"+ seconds;
    }


    private void doStart( )  {
        if(this.mediaPlayer.isPlaying()) {
            return;
        }
        // The duration in milliseconds
        int duration = this.mediaPlayer.getDuration();

        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if(currentPosition== 0)  {
            this.seekBar.setMax(duration);
            String maxTimeString = this.millisecondsToString(duration);
            this.textMaxTime.setText(maxTimeString);
        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.
            this.mediaPlayer.reset();
        }
        this.mediaPlayer.start();
        // Create a thread to update position of SeekBar.
        UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread,50);

        this.buttonPause.setEnabled(true);
        this.buttonStart.setEnabled(false);
        this.buttonRewind.setEnabled(true);
        this.buttonFastForward.setEnabled(true);
    }

    // Called by MediaPlayer.OnCompletionListener
    // When Player cocmplete
    private void doComplete()  {
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonRewind.setEnabled(true);
        buttonFastForward.setEnabled(false);
    }

    // Called by MediaPlayer.OnPreparedListener.
    // When user select a new media source, then stop current.
    private void doStop()  {
        if(this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonRewind.setEnabled(false);
        buttonFastForward.setEnabled(false);
    }

    // When user click to "Pause".
    private void doPause( )  {
        this.mediaPlayer.pause();
        this.buttonPause.setEnabled(false);
        this.buttonStart.setEnabled(true);
    }

    // When user click to "Rewind".
    private void doRewind( )  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int SUBTRACT_TIME = 5000;

        if(currentPosition - SUBTRACT_TIME > 0 )  {
            this.mediaPlayer.seekTo(currentPosition - SUBTRACT_TIME);
        }
        this.buttonFastForward.setEnabled(true);
    }

    // When user click to "Fast-Forward".
    private void doFastForward( )  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int ADD_TIME = 5000;

        if(currentPosition + ADD_TIME < duration)  {
            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
        }
    }


    // Thread to Update position for SeekBar.
    class UpdateSeekBarThread implements Runnable {

        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);

            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }
}