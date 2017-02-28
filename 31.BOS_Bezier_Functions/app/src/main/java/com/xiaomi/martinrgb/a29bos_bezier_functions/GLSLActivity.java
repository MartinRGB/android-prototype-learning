package com.xiaomi.martinrgb.a29bos_bezier_functions;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GLSLActivity extends AppCompatActivity {

    private CanvasGLSurfaceView canvasGLSurfaceView;
    private boolean renderSet = false;
    private CanvasRenderer mCanvasRenderer;
    private Context context;

    //Life Cycle
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        deleteBars();
        context = this;

        canvasGLSurfaceView = new CanvasGLSurfaceView(context);

        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean suppoertsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        //# New Instance Renderer
        mCanvasRenderer = new CanvasRenderer(context);
        if(suppoertsEs2){
            //请求 OpenGL ES 2.0的上下文（Context是一个场景,代表与操作系统的交互的一种过程。）
            canvasGLSurfaceView.setEGLContextClientVersion(2);
            //#分配Renderer
            canvasGLSurfaceView.setRenderer(mCanvasRenderer);
            //canvasGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            renderSet = true;
            Toast.makeText(context,"support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"do not support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
            return;
        }

        setContentView(canvasGLSurfaceView);


    }

    @Override
    public void onPause(){
        super.onPause();
        if(renderSet){
            canvasGLSurfaceView.onPause();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(renderSet){
            canvasGLSurfaceView.onResume();
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
