package com.martinrgb.shadertemplate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class CanvasFragment extends Fragment {

    private CanvasGLSurfaceView canvasGLSurfaceView;
    private boolean renderSet = false;
    private CanvasRenderer mCanvasRenderer;
    //Construcotr
    public static CanvasFragment newInstance(){
        return new CanvasFragment();
    }

    //Life Cycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_canvas,container,false);
        initCanvasSurfaceView(getActivity(),view);

        //#Test
        //TextView textView = (TextView) view.findViewById(R.id.textView);

        return view;
    }
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


    private float prevX;
    private float prevY;
    private float animX;
    private float animY;

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

        canvasGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if(event !=null){
                    //*把坐标系转换成OpenGL坐标系(后期还要转换成GLSL内部归一化坐标系)
                    //u_mouse
                    //final float normalizedX = (float)event.getX() / (float) v.getWidth();
                    //final float normalizedY = -(float)event.getY() / (float) v.getHeight()+ 1;

                    //# u_mouse/u_resolution
                    final float normalizedX = event.getX();
                    final float normalizedY = -event.getY() + v.getHeight();

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            canvasGLSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {

                                    mCanvasRenderer.handleTouchDown(normalizedX,normalizedY);

                                }
                            });
                            break;

                        case MotionEvent.ACTION_UP:
                            canvasGLSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {

                                    prevX = normalizedX;
                                    prevY = normalizedY;
                                    mCanvasRenderer.handleTouchUp(normalizedX,normalizedY);
                                }
                            });
                            break;

                        case MotionEvent.ACTION_MOVE:
                            canvasGLSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    mCanvasRenderer.handleTouchDrag(normalizedX,normalizedY);
                                }
                            });
                            break;
                        default:
                    }

                    return true;
                }

                return false;
            }
        });
    }


}
