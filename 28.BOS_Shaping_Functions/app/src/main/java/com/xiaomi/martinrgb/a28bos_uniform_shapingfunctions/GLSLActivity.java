package com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions;

public class GLSLActivity extends SingleFragmentActivity {
    //Override
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return CanvasFragment.newInstance();
    }
}
