package com.xiaomi.martinrgb.a41noise;

public class GLSLActivity extends SingleFragmentActivity {
    //Override
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return CanvasFragment.newInstance();
    }
}
