package com.example.martinrgb.a19criminalintent;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.martinrgb.a19criminalintent.fragment.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    //Activity传 intent 传输数据虽然靠谱，但是导致 fragment 必须于这个 Activity 绑定
    //public static final String EXTRA_CRIME_ID = "犯罪编号";

    //让别人告知这个Activity的ID
    public static Intent newIntent(Context context, UUID crimeID){
        Intent intent = new Intent(context,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return intent;
    }

    public static final String EXTRA_CRIME_ID = "犯罪编号";

    @Override
    protected Fragment createFragment() {
        //返送一个CrimeFragment
        //ListFragment会将数据传输到 UUID 里，然后直接输送给 CrimeFragment
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //实例化在CrimeFragment自身里完成
        return CrimeFragment.newInstance(crimeId);
    }


}
