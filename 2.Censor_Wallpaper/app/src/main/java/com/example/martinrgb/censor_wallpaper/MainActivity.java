package com.example.martinrgb.censor_wallpaper;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinrgb.censor_wallpaper.messages.MeasurementStepMessage;
import com.example.martinrgb.censor_wallpaper.messages.MessageHUB;
import com.example.martinrgb.censor_wallpaper.messages.MessageListener;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.facebook.rebound.SpringConfigRegistry;
import com.facebook.rebound.ui.SpringConfiguratorView;
import com.zhy.autolayout.AutoLayoutActivity;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AutoLayoutActivity implements MessageListener {

    public static final String CAM_SIZE_WIDTH = "intent_cam_size_width";
    public static final String CAM_SIZE_HEIGHT = "intent_cam_size_height";
    public static final String AVG_NUM = "intent_avg_num";
    public static final String PROBANT_NAME = "intent_probant_name";

    private com.example.martinrgb.censor_wallpaper.CameraSurfaceView _mySurfaceView;
    Camera _cam;

    private final static DecimalFormat _decimalFormater = new DecimalFormat(
            "0.00");

    private float _currentDevicePosition;

    private int _cameraHeight;
    private int _cameraWidth;
    TextView _currentDistanceView;

    private boolean isScaledUp = false;
    private boolean eyeScaledUp = false;
    private static final SpringConfig mconfig = SpringConfig.fromOrigamiTensionAndFriction(66, 10);
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    private Spring backtoZeroSpring,backtoSquareZeroSpring;
    private SpringConfiguratorView mSpringConfiguratorView;

    private long startTime,currentTime;


    private SensorManager mSensorManager;
    private Sensor mSensor;
    static final float ALPHA = 0.2f;
    protected float[] accelVals;
    private float azimuth, pitch, roll,transitionX,transitionSquareX,newPitch,prevPitch;


    /**
     * Abusing the media controls to create a remote control
     */
    // ComponentName _headSetButtonReceiver;
    // AudioManager _audioManager;

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //删除Bars
        deleteBars();
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //申请权限
        askForPermission(Manifest.permission.CAMERA,CAMERA);

        //相机预览Surface层，获取到距离
        _mySurfaceView = (com.example.martinrgb.censor_wallpaper.CameraSurfaceView) findViewById(R.id.surface_camera);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                (int) (0.95 * this.getResources().getDisplayMetrics().widthPixels),
                (int) (0.6 * this.getResources().getDisplayMetrics().heightPixels));
        layout.setMargins(0, (int) (0.05 * this.getResources()
                .getDisplayMetrics().heightPixels), 0, 0);
        _mySurfaceView.setLayoutParams(layout);
        _mySurfaceView.calibrate();
        _mySurfaceView.showMiddleEye(true);
        _mySurfaceView.showEyePoints(true);
        _currentDistanceView = (TextView) findViewById(R.id.currentDistance);

        //弹性系统初始化
        setSpringSystem();
        mSpring.setCurrentValue(0);
        //点击事件
        setClickEvent();
        initSensorManager();


        startTime = System.currentTimeMillis();




        //悬浮层系列
        //FPS侦测
        //TinyDancer.create().show(getApplicationContext());
        //动画调节
        //interpolatorConfig();

    }

    //要权限
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    //###################生命周期###################状态恢复
    @Override
    protected void onResume() {
        super.onResume();

        MessageHUB.get().registerListener(this);
        initSensorManager();
        // _audioManager.registerMediaButtonEventReceiver(_headSetButtonReceiver);

        // 1 for front cam. No front cam ? Not my fault!
        _cam = Camera.open(1);
        Camera.Parameters param = _cam.getParameters();

        // Find the best suitable camera picture size for your device. Competent
        // research has shown that a smaller size gets better results up to a
        // certain point.
        // http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=6825217&url=http%3A%2F%2Fieeexplore.ieee.org%2Fiel7%2F6816619%2F6825201%2F06825217.pdf%3Farnumber%3D6825217
        List<Size> pSize = param.getSupportedPictureSizes();
        double deviceRatio = (double) this.getResources().getDisplayMetrics().widthPixels
                / (double) this.getResources().getDisplayMetrics().heightPixels;

        Size bestSize = pSize.get(0);
        double bestRation = (double) bestSize.width / (double) bestSize.height;

        for (Size size : pSize) {
            double sizeRatio = (double) size.width / (double) size.height;

            if (Math.abs(deviceRatio - bestRation) > Math.abs(deviceRatio
                    - sizeRatio)) {
                bestSize = size;
                bestRation = sizeRatio;
            }
        }
        _cameraHeight = bestSize.height;
        _cameraWidth = bestSize.width;

        Log.d("PInfo", _cameraWidth + " x " + _cameraHeight);

        param.setPreviewSize(_cameraWidth, _cameraHeight);
        _cam.setParameters(param);

        _mySurfaceView.setCamera(_cam);

        _mySurfaceView.calibrate();
        _mySurfaceView.showMiddleEye(true);
        _mySurfaceView.showEyePoints(true);
    }

    //###################生命周期###################暂停
    @Override
    protected void onPause() {
        super.onPause();


        MessageHUB.get().unregisterListener(this);
        mSensorManager.unregisterListener(mEventListener);

        resetCam();
    }

    //###################Utils###################update 距离给文字
    public void updateUI(final MeasurementStepMessage message) {

        float fontRatio = message.getDistToFace() / 29.7f;
        _currentDistanceView.setText("D: " + _decimalFormater.format(Math.round(message.getDistToFace())) + " cm");
        _currentDistanceView.setTextColor(Color.BLUE);
        //_currentDistanceView.setTextSize(fontRatio * 20);

        if(currentTime >4000 && eyeScaledUp == false && (Math.round(message.getDistToFace()) < 22f)){
            mSpring.setEndValue(1);
            backtoZeroSpring.setEndValue(0);
            prevPitch = pitch;
            eyeScaledUp = true;
            isScaledUp = true;
        }
        else if (eyeScaledUp == true && (Math.round(message.getDistToFace()) > 26f)){
            mSpring.setEndValue(0);
            backtoZeroSpring.setEndValue(1);
            backtoSquareZeroSpring.setEndValue(0);
            eyeScaledUp = false;
            isScaledUp = false;
        }

        //_currentDistanceView.setText(String.valueOf(message.getDistToFace()) + " cm");


    }

    //###################Utils###################重置摄像机，释放摄像机，停止预览
    private void resetCam() {
        _mySurfaceView.reset();

        _cam.stopPreview();
        _cam.setPreviewCallback(null);
        _cam.release();
    }

    //###################Utils###################和Message直接的通信
    @Override
    public void onMessage(final int messageID, final Object message) {

        switch (messageID) {

            case MessageHUB.MEASUREMENT_STEP:
                updateUI((MeasurementStepMessage) message);

                break;

            case MessageHUB.DONE_CALIBRATION:

                break;
            default:
                break;
        }

    }

    //###################Utils###################清空Bars
    private void deleteBars() {

        //Delete Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hide Navigation
        View decorView = getWindow().getDecorView();int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //###################Utils###################弹性系统
    private void setSpringSystem() {
        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.setSpringConfig(mconfig);
        backtoZeroSpring = mSpringSystem.createSpring();
        backtoZeroSpring.setSpringConfig(mconfig);
        backtoSquareZeroSpring = mSpringSystem.createSpring();
        backtoSquareZeroSpring.setSpringConfig(mconfig);

        mSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring mSpring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) mSpring.getCurrentValue();
                float scale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 1, 2.5);
                float alpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);

                findViewById(R.id.image).setScaleX(scale);
                findViewById(R.id.image).setScaleY(scale);
                findViewById(R.id.mask).setAlpha(alpha);
                findViewById(R.id.square).setAlpha(alpha);
            }
        });


        backtoZeroSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring backtoZeroSpring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) backtoZeroSpring.getCurrentValue();
                float newTransition = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, findViewById(R.id.image).getX(), 0);
                findViewById(R.id.image).setX(newTransition);
            }
        });


        backtoSquareZeroSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring backtoSquareZeroSpring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) backtoSquareZeroSpring.getCurrentValue();
                float newSquareTransition = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 343, findViewById(R.id.image).getX());
                findViewById(R.id.square).setX(newSquareTransition);
            }
        });
    }

    //###################Events###################点击事件
    private void setClickEvent(){
        //设置动画
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isScaledUp = !isScaledUp;

                if(isScaledUp){
                    mSpring.setEndValue(1);
                    backtoZeroSpring.setEndValue(0);
                    prevPitch = pitch;
                }
                else{
                    mSpring.setEndValue(0);
                    backtoZeroSpring.setEndValue(1);
                    backtoSquareZeroSpring.setEndValue(0);
                }

            }
        });
    }


    //###################Utils###################动画调节面板
    private void interpolatorConfig(){

        //Find InterpolatorConfigurationView in XML
        mSpringConfiguratorView = (SpringConfiguratorView) findViewById(R.id.interpolator_configurator);

        mSpringConfiguratorView.setVisibility(View.VISIBLE);

        //Add Interpolator Config into ConfigurationView
        SpringConfigRegistry.getInstance().removeAllSpringConfig();
        SpringConfigRegistry.getInstance().addSpringConfig(mconfig, "AnimOne");

        mSpringConfiguratorView.refreshSpringConfigurations();
        mSpringConfiguratorView.bringToFront();
    }



    //###################Events###################传感器系列
    private void initSensorManager(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }



    private final SensorEventListener mEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            // Handle the events for which we registered
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    onOrientChanged(event);
                    break;

            }


        }

    };

    public void onOrientChanged(SensorEvent event) {

        accelVals = lowPass( event.values, accelVals );
        azimuth = accelVals[0];
        pitch = accelVals[1];
        roll = accelVals[2];
        ((TextView) findViewById(R.id.azimuth)).setText("Y:" + _decimalFormater.format(azimuth));
        ((TextView) findViewById(R.id.pitch)).setText("X:" +  _decimalFormater.format(pitch));
        ((TextView) findViewById(R.id.roll)).setText("Z:" +  _decimalFormater.format(roll));


        //获取到程序运行时间。
        currentTime = System.currentTimeMillis() - startTime;

        //因为每次X轴转向的Pitch会改变，所以每次放大时取当前的Pitch值，正负0.1作为移动范围

        newPitch = (float) Math.min(prevPitch+0.1,Math.max(prevPitch-0.1,pitch));

        if(isScaledUp== true ) {
            transitionX = (float) SpringUtil.mapValueFromRangeToRange(newPitch, prevPitch-0.1, prevPitch+0.1, -740, 740);

            transitionSquareX = (float) SpringUtil.mapValueFromRangeToRange(newPitch, prevPitch-0.1, prevPitch+0.1, 613, 72);

            findViewById(R.id.image).animate().x(transitionX).setDuration(30).start();
            findViewById(R.id.square).animate().x(transitionSquareX).setDuration(30).start();
            //findViewById(R.id.image).setX(transitionX);

            //findViewById(R.id.square).setX(transitionSquareX);
        }
        else {

        }

    }

    //###################Utils###################低通滤波，平滑传感器数据
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }



}