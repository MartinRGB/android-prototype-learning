package com.xiaomi.martinrgb.a36bos_translate;

public class GLSLActivity extends SingleFragmentActivity {
    //Override
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return CanvasFragment.newInstance();
    }
}
