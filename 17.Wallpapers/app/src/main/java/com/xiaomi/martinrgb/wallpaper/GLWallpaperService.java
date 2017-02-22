package com.xiaomi.martinrgb.wallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.xiaomi.martinrgb.wallpaper.util.LoggerConfig;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class GLWallpaperService extends WallpaperService {


    private static final String TAG = "GLEngine";
    private ParticlesRenderer particlesRenderer;
    private GLEngine.WallpaperGLSurfaceView glSurfaceView;
    private boolean rendererSet;

    @Override
    public Engine onCreateEngine(){
        return new GLEngine();
    }
    public class GLEngine extends Engine {
        private static final String TAG = "GLEngine";
        private WallpaperGLSurfaceView glSurfaceView;
        private ParticlesRenderer particlesRenderer;
        private boolean rendererSet;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            if (LoggerConfig.ON) {
                Log.d(TAG, "onCreate(" + surfaceHolder + ")");
            }

            //实例化
            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);

            //检测是否支持2.0
            ActivityManager activityManager =
                    (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager
                    .getDeviceConfigurationInfo();

            final boolean supportsEs2 =
                    configurationInfo.reqGlEsVersion >= 0x20000
                            // Check for emulator.
                            || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                            && (Build.FINGERPRINT.startsWith("generic")
                            || Build.FINGERPRINT.startsWith("unknown")
                            || Build.MODEL.contains("google_sdk")
                            || Build.MODEL.contains("Emulator")
                            || Build.MODEL.contains("Android SDK built for x86")));

            particlesRenderer = new ParticlesRenderer(GLWallpaperService.this);

            //如果支持，设置渲染
            if (supportsEs2) {
                glSurfaceView.setEGLContextClientVersion(2);

                //防止切换主屏后，效果被切断，保留上下文，不必重新加载
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    glSurfaceView.setPreserveEGLContextOnPause(true);
                }
                glSurfaceView.setRenderer(particlesRenderer);
                rendererSet = true;
            } else {
                Toast.makeText(GLWallpaperService.this,
                        "This device does not support OpenGL ES 2.0.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (LoggerConfig.ON) {
                Log.d(TAG, "onVisibilityChanged(" + visible + ")");
            }
            if (rendererSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }

        //壁纸滑动偏移使用
        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset,
                                     float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    particlesRenderer.handleOffsetsChanged(xOffset, yOffset);
                }
            });
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            if (LoggerConfig.ON) {
                Log.d(TAG, "onDestroy()");
            }
            glSurfaceView.onWallpaperDestroy();
        }
        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";
            WallpaperGLSurfaceView(Context context) {
                super(context);

                if (LoggerConfig.ON) {
                    Log.d(TAG, "WallpaperGLSurfaceView(" + context + ")");
                }
            }
            @Override
            public SurfaceHolder getHolder() {
                if (LoggerConfig.ON) {
                    Log.d(TAG, "getHolder(): returning " + getSurfaceHolder());
                }
                return getSurfaceHolder();
            }
            public void onWallpaperDestroy() {
                if (LoggerConfig.ON) {
                    Log.d(TAG, "onWallpaperDestroy()");
                }
                super.onDetachedFromWindow();
            }
        }
    }
}


