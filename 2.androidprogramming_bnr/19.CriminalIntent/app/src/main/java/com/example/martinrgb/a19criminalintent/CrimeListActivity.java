package com.example.martinrgb.a19criminalintent;

import android.support.v4.app.Fragment;

import com.example.martinrgb.a19criminalintent.fragment.CrimeListFragment;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
