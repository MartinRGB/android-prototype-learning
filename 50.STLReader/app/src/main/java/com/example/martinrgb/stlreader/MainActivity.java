package com.example.martinrgb.stlreader;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean supportsEs2;
    private GLSurfaceView glView;
    private float rotateDegreen = 0;
    private float rotateDegreen2 = 0;
    private GLRenderer glRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSupported(this);
        setOnTouchListener();
        glView.setOnTouchListener(mOnTouchListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();

            //不断改变rotateDegreen值，实现旋转
//            new Thread() {
//                @Override
//                public void run() {
//                    while (true) {
//                        try {
//                            sleep(100);
//
//                            rotateDegreen += 5;
//                            handler.sendEmptyMessage(0x001);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }.start();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            rotate(rotateDegreen);
//        }
//    };

    public void rotate(float degree,float degree2) {
        glRenderer.rotate(degree,degree2);
        glView.invalidate();
    }

    //检测是否支持OpenGLES
    private void checkSupported(Context context) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000;

        boolean isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));

        supportsEs2 = supportsEs2 || isEmulator;

        if (supportsEs2) {
            glView = new GLSurfaceView(context);
            glRenderer = new GLRenderer(context);
            glView.setRenderer(glRenderer);
            setContentView(glView);
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "当前设备不支持OpenGL ES 2.0!", Toast.LENGTH_SHORT).show();
        }
    }

    private float _touchedX;
    private float _touchedY;

    private View.OnTouchListener mOnTouchListener;

    private void setOnTouchListener(){
        mOnTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    _touchedX = event.getX();
                    _touchedY = event.getY();

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float touchedX = event.getX();
                    float touchedY = event.getY();
                    float dx = Math.abs(_touchedX - touchedX);
                    float distance = _touchedX - touchedX;
                    float distance2 = _touchedY - touchedY;
                    rotate(distance/2.f,distance2/10.f);
//                    _dxFiltered = _dxFiltered * (1.0f - _filterSensitivity) + dx
//                            * _filterSensitivity;
//
//                    rotate(touchedX);
//
//                    if (touchedX < _touchedX) {
//                        _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY * _ANGLE_SPAN;
//                        _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity) + _zAngle * _filterSensitivity;
//
//                        rotate(_zAngleFiltered);
//                        //GLES20Renderer.setZAngle(GLES20Renderer.getZAngle()
//                        + _zAngleFiltered);
//                    } else {
//                        _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY
//                                * _ANGLE_SPAN;
//                        _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity)
//                                + _zAngle * _filterSensitivity;
//
//
//                        //GLES20Renderer.setZAngle(GLES20Renderer.getZAngle() - _zAngleFiltered);
//                    }
                }


                return true;
            }
        };
    }
}
