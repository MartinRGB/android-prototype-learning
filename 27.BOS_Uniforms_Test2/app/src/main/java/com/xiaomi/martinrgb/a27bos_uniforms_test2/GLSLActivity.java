package com.xiaomi.martinrgb.a27bos_uniforms_test2;

public class GLSLActivity extends SingleFragmentActivity {
    //Override
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return CanvasFragment.newInstance();
    }
}
