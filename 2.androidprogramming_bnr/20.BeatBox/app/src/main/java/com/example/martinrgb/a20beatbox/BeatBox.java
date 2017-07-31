package com.example.martinrgb.a20beatbox;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MartinRGB on 2017/2/24.
 */

//Model层的管理层(List & 声音处理)
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS_NUM = 5;
    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;


    public BeatBox(Context context) {
        //拿到Context,能干寻址到Assets
        mAssets = context.getAssets();
        //加入SoundPool播放器
        mSoundPool = new SoundPool(MAX_SOUNDS_NUM, AudioManager.STREAM_MUSIC,0);
        //遍历读取Assets中的Sounds，并且加入到List中
        loadSounds();
    }
    //找出有多少个声音文件，并加载.
    private void loadSounds(){
        String[] soundNames;
        try{
            soundNames = mAssets.list(SOUND_FOLDER);
            Log.i(TAG,"Found " + soundNames.length + " sounds");
        }catch (IOException ioe){
            Log.e(TAG,"Could not list assets",ioe);
            return;
        }

        //遍历 sample_sounds里面的文件,把文件都加载到Sound对象之中,而且把文件里面的声音加入到SoundPool中
        for (String filename :soundNames){
            try{

                String assetPath = SOUND_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);

            }catch (IOException ioe){
                Log.e(TAG,"Could not load sound " + filename,ioe);
            }
        }
    }

    //openFD可能抛出 IOE;
    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        //把具体声音文件加入到 SoundPool 中待播
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }

    //具体播放func
    public void play (Sound sound){
        Integer soundId = sound.getSoundId();
        if(soundId ==null){
            return;
        }
        mSoundPool.play(soundId,0.1f,0.1f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release();
    }

    public List<Sound> getSounds(){
        return mSounds;
    }
}
