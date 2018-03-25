package com.sourcey.movnpack.Alarm;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.sourcey.movnpack.Helpers.ApplicationContextProvider;

import static android.content.Context.AUDIO_SERVICE;

public class AudioPlayer {

    private MediaPlayer mMediaPlayer;
    private static AudioPlayer audioPlayer;

    private AudioPlayer() {

    }

    public static AudioPlayer getInstance() {
        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer();
        }
        return audioPlayer;
    }



    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(int rid) {
        stop();
        Context c = ApplicationContextProvider.getContext();
        //
        final AudioManager mAudioManager = (AudioManager) c.getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        //
        mMediaPlayer = MediaPlayer.create(c, rid);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                stop();
            }
        });

        mMediaPlayer.start();
    }

}
