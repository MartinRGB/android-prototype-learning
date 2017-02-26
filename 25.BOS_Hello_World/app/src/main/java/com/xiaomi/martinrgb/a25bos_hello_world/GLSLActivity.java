package com.xiaomi.martinrgb.a25bos_hello_world;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GLSLActivity extends SingleFragmentActivity {
    //Override
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return CanvasFragment.newInstance();
    }
}
