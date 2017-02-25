package com.example.martinrgb.a22photogallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class WebVieActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context, Uri webViewUri){
        Intent i = new Intent(context,WebVieActivity.class);
        i.setData(webViewUri);
        return  i;
    }

    @Override
    protected Fragment createFragment(){
        return WebViewFragment.newInstance(getIntent().getData());
    }

}
