package com.example.darthvader.headphonescheck;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.KeyEvent;

public class MediaService extends Service {
    private MediaPlayer mediaPlayer;
    //Media Session Created
    MediaSessionCompat mMediaSessionCompat;
    @Override
    public void onCreate() {
        super.onCreate();
        initMediaSession();
        initMediaPlayer();

    }

    MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {

            Log.i("Lol","Callback running");
            String action = mediaButtonEvent.getAction();
            if(Intent.ACTION_MEDIA_BUTTON.equals(action))
            {
                KeyEvent event = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                int even=event.getAction();
//                Log.i("Hellp", String.valueOf(even));
                if(even==KeyEvent.ACTION_DOWN)
                {
                    Log.i("Action","Action received");
                    return true;
                }
            }
            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };
    private void initMediaSession() {
        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);
        mMediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "Tag", mediaButtonReceiver, null);
        mMediaSessionCompat.setFlags( MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS );
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this, MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
        mMediaSessionCompat.setMediaButtonReceiver(pendingIntent);
        mMediaSessionCompat.setActive(true);
    }
    private void initMediaPlayer()
    {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(0.0f,0.0f);
        mediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("Hellp",intent.getAction());
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSessionCompat.release();
        Log.i("Hello","Destroyed");
    }



}
