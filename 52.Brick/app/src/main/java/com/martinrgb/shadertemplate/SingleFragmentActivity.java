package com.martinrgb.shadertemplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    //Abstarct Mehotd
    protected  abstract Fragment createFragment();

    //Life Cycle
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        deleteBars();
        setContentView(R.layout.activity_glsl);
        addFragment();


    }

    //Add Fragment
    private void addFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }

    //Utils - Delete
    private void deleteBars(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

    }

}
