package com.xiaomi.martinrgb.skybox;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ParticlesActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    private ParticlesRenderer mParticlesRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteBars();
        glSurfaceView = new GLSurfaceView(this);

        //检测系统是否支持 OpenGL ES 2.0
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean suppoertsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        mParticlesRenderer = new ParticlesRenderer(this);

        if(suppoertsEs2){
            //请求 OpenGL ES 2.0的上下文（Context是一个场景,代表与操作系统的交互的一种过程。）
            glSurfaceView.setEGLContextClientVersion(2);
            //分配Renderer
            glSurfaceView.setRenderer(mParticlesRenderer);
            renderSet = true;
        } else {
            Toast.makeText(this,"do not support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
            return;
        }

        glSurfaceViewTouchEvent();

        setContentView(glSurfaceView);

    }

    //处理生命周期事件，正确暂停并继续后台渲染县城，释放和续用 OpenGL 上下文
    @Override
    protected void onPause(){
        super.onPause();
        if(renderSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(renderSet){
            glSurfaceView.onResume();
        }
    }

    private void glSurfaceViewTouchEvent(){
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            float previousX,previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                return false;
                if(event !=null){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        previousX = event.getX();
                        previousY = event.getY();
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        final float deltaX = event.getX() - previousX;
                        final float deltaY = event.getY() - previousY;

                        previousX = event.getX();
                        previousY = event.getY();

                        //两个线程之间的通信可以使用如下方法：在主线程中的GLSurfaceView实例可以调用queueEvent()方法传递一个Runnable给后台渲染线程，
                        //渲染线程就可以调用Activity的runOnUIThread()来传递事件（event）给主线程。

                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mParticlesRenderer.handleTouchDrag(deltaX,deltaY);
                            }
                        });
                    }
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    private void deleteBars(){
        //Delete Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
    }


}

