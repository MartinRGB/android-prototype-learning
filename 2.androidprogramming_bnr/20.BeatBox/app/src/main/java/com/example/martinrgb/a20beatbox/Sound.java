package com.example.martinrgb.a20beatbox;

/**
 * Created by MartinRGB on 2017/2/24.
 */

//Model层
public class Sound {
    private String mAssetPath;
    private String mName;
    private Integer mSoundId;

    public Sound(String assetPath){
        mAssetPath = assetPath;
        //分离出 /
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        //去掉名称后缀的.wav
        mName = filename.replace(".wav","");
    }
    public String getAssetPath(){
        return mAssetPath;
    }
    public String getName(){
        return mName;
    }


    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }

}
