package com.example.martinrgb.a21nerdlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    //抽象方法,把不管什么Fragment，都丢进fragment_container
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_nerd_launcher);

        //用FragmentManager管理fragment队列
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        //给fragment寻找一个容器
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            //回退栈，为容器加一个新的fragment事务，提交
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }

}
