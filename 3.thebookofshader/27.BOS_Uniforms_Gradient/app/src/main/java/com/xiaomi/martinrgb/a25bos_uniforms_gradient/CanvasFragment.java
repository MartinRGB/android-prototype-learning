package com.xiaomi.martinrgb.a25bos_uniforms_gradient;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class CanvasFragment extends Fragment {

    private CanvasGLSurfaceView canvasGLSurfaceView;
    private boolean renderSet = false;
    private CanvasRenderer mCanvasRenderer;
    private TextView mFPSView;
    //Construcotr
    public static CanvasFragment newInstance(){
        return new CanvasFragment();
    }

    //Life Cycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_canvas,container,false);

        initCanvasSurfaceView(getActivity(),view);
        setFPSView(view);
        setGLTouchListener();

        return view;
    }

    //因为GLSurfaceView土属性，所以必须开一个UI线程做这件事
    private void setText(final TextView text,final String value){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    //Life Cycle
    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
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

    //Init Canvas
    private void initCanvasSurfaceView(Context context,View parent){
        canvasGLSurfaceView = (CanvasGLSurfaceView) parent.findViewById(R.id.mCanvasGLSurfaceView);

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
            renderSet = true;
            Toast.makeText(context,"support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"do not support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void setGLTouchListener(){
        canvasGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent != null) {
                    final float normalizedX = (motionEvent.getX() / (float) view.getWidth());
                    final float normalizedY = ((motionEvent.getY() / (float) view.getHeight()));

                    Log.e("normalX" , String.valueOf(normalizedX));
                    Log.e("normalY" , String.valueOf(normalizedY));

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        canvasGLSurfaceView.queueEvent(new Runnable() {
                            @Override public void run() {
                                mCanvasRenderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        canvasGLSurfaceView.queueEvent(new Runnable() {
                            @Override public void run() {
                                mCanvasRenderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                }

                return false;
            }
        });
    }
    private void setFPSView(View parent){
        mFPSView = (TextView) parent.findViewById(R.id.textView);
        mCanvasRenderer.setOnDrawFrameListener(new CanvasRenderer.onDrawFrameListener() {
            @Override
            public void getDrawFramePerSecond(int fps) {

                setText(mFPSView,String.valueOf(fps) + " fps");
            }
        });
    }
}
