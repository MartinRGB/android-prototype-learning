package com.example.martinrgb.a19criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    //Getter ;取CrimeLab
    public static CrimeLab get(Context context) {
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    //Constructor - 新建Crime List列表
    //Singleton 开始
    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        //含量为100的crimeList;
        for(int i =0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("丑态记录" + i);
            crime.setSolved(i%2 == 0);//每隔一个设置
            mCrimes.add(crime);
        }
    }

    //取Crime List列表
    public List<Crime> getCrimes(){
        return mCrimes;
    }

    //取出指定Id的Crime
    public Crime getCrime(UUID id){
        //遍历所有的crime，如果id匹配，那么返回该Crime
        for(Crime crime:mCrimes){
            if(crime.getID().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
