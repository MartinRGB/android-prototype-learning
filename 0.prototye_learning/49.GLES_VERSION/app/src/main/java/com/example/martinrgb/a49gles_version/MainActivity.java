package com.example.martinrgb.a49gles_version;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnTouchListener,SensorEventListener {

    private ActivityManager finalActivityManagerManger;
    private ConfigurationInfo finalConfigurationInfoconfigurationInfo;
    private boolean supportsES2;
    private TextView mTextView;
    private Button buttonUp, buttonDown;
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        finalActivityManagerManger = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        finalConfigurationInfoconfigurationInfo = finalActivityManagerManger.getDeviceConfigurationInfo();
        supportsES2 = finalConfigurationInfoconfigurationInfo.reqGlEsVersion >= 0x20000;

//        mTextView = (TextView) findViewById(R.id.textview);
//        if(supportsES2){
//            mTextView.setText("es2 is supported");
//        }
//        else {
//            mTextView.setText("es2 is not supported");
//        }

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new GLES20Renderer(getApplication().getApplicationContext()));
        setContentView(mGLSurfaceView);

        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);

        //XML 布局
        LinearLayout layout = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout.setOnTouchListener(this);

        LinearLayout.LayoutParams layoutParamsUpDown = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        View linearLayoutView = inflater
                .inflate(R.layout.updown, layout, false);
        layout.addView(linearLayoutView);

        addContentView(layout, layoutParamsUpDown);

        //View textView = inflater.inflate(R.layout.main, layout, false);
        //layout.addView(textView);
        mTextView = (TextView) findViewById(R.id.textview);
        buttonUp = (Button) findViewById(R.id.up);
        buttonDown = (Button) findViewById(R.id.down);
        setUpDownClickListeners();
        getDeviceWidth();
    }


    @Override
    protected void onPause() {
        mGLSurfaceView.onPause();
        // Counter.reset();
        super.onPause();
        if (isFinishing()) {
            // save high scores etc
            GLES20Renderer.setZAngle(0);
            _dxFiltered = 0.0f;
            _zAngle = 0.0f;
            _zAngleFiltered = 0.0f;
            _orientationFiltered = 0.0f;
            _gravityFiltered = 0.0f;
            _accelValsFiltered[0] = 0.0f;
            _accelValsFiltered[1] = 0.0f;
            _accelValsFiltered[2] = 0.0f;
            _magValsFiltered[0] = 0.0f;
            _magValsFiltered[1] = 0.0f;
            _magValsFiltered[2] = 0.0f;
            this.finish();
        }
    }

    private int _width;
    public void getDeviceWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            _width = width;
        } else {
            _width = height;
        }
    }

    public void setUpDownClickListeners() {


        buttonUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Counter.getUpDownNextValue();
                mTextView.setText(String.valueOf(Counter.getUpDownValue()));
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Counter.getUpDownPreviousValue();
                mTextView.setText(String.valueOf(Counter.getUpDownValue()));
            }
        });
    }


    private final float _TOUCH_SENSITIVITY = 0.25f;
    private final float _ANGLE_SPAN = 90.0f;
    private float _dxFiltered = 0.0f;
    private float _zAngle = 0.0f;
    private float _filterSensitivity = 0.1f;
    private float _zAngleFiltered = 0.0f;
    private float _touchedX;

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _touchedX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float touchedX = event.getX();
            float dx = Math.abs(_touchedX - touchedX);
            _dxFiltered = _dxFiltered * (1.0f - _filterSensitivity) + dx
                    * _filterSensitivity;

            if (touchedX < _touchedX) {
                _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY
                        * _ANGLE_SPAN;
                _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity)
                        + _zAngle * _filterSensitivity;
                GLES20Renderer.setZAngle(GLES20Renderer.getZAngle()
                        + _zAngleFiltered);
            } else {
                _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY
                        * _ANGLE_SPAN;
                _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity)
                        + _zAngle * _filterSensitivity;
                GLES20Renderer.setZAngle(GLES20Renderer.getZAngle()
                        - _zAngleFiltered);
            }
        }
        return true;
    }


    private float[] _inR = new float[16];
    private float[] _outR = new float[16];
    private float[] _values = new float[3];
    private float[] _I = new float[16];
    private float[] _accelVals = new float[3];
    private float[] _magVals = new float[3];
    private float[] _gravVals = new float[3];
    private static final float _SENSITIVITY = 0.5f;
    private float _a = 0.1f;
    private float _orientationFiltered = 0.0f;
    private float _gravityFiltered;
    private float[] _accelValsFiltered = new float[3];
    private float[] _magValsFiltered = new float[3];

    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                _accelVals = event.values.clone();
                _accelValsFiltered[0] = _accelValsFiltered[0] * (1.0f - _a)
                        + _accelVals[0] * _a;
                _accelValsFiltered[1] = _accelValsFiltered[1] * (1.0f - _a)
                        + _accelVals[1] * _a;
                _accelValsFiltered[2] = _accelValsFiltered[2] * (1.0f - _a)
                        + _accelVals[2] * _a;
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                _magVals = event.values.clone();
                _magValsFiltered[0] = _magValsFiltered[0] * (1.0f - _a)
                        + _magVals[0] * _a;
                _magValsFiltered[1] = _magValsFiltered[1] * (1.0f - _a)
                        + _magVals[1] * _a;
                _magValsFiltered[2] = _magValsFiltered[2] * (1.0f - _a)
                        + _magVals[2] * _a;
                break;
            }
            case Sensor.TYPE_GRAVITY: {
                _gravVals = event.values.clone();
                break;
            }
        }
        if (_accelVals != null && _magVals != null && _gravVals != null) {
            boolean success = SensorManager.getRotationMatrix(_inR, _I,
                    _accelValsFiltered, _magValsFiltered);
            _gravityFiltered = _gravityFiltered * (1.0f - _a)
                    + Math.abs(_gravVals[2]) * _a;
            float scaling = 0;
            SensorManager.remapCoordinateSystem(_inR, SensorManager.AXIS_Y,
                    SensorManager.AXIS_X, _outR);
            if (success) {
                SensorManager.getOrientation(_outR, _values);
                if (_gravityFiltered >= SensorManager.GRAVITY_EARTH
                        || Math.abs(_values[1]) <= 0.2) {
                    _gravityFiltered = SensorManager.GRAVITY_EARTH;
                }

                if (_gravityFiltered >= 6
                        && _gravityFiltered <= SensorManager.GRAVITY_EARTH * 1) {
                    scaling = _SENSITIVITY
                            + (2 - (_gravityFiltered / SensorManager.GRAVITY_EARTH));
                    _orientationFiltered = _orientationFiltered * (1.0f - _a)
                            + _outR[0] * _a;
                    float zAngle = scaling * _orientationFiltered * 90;
                    GLES20Renderer.setZAngle(zAngle);
                    mTextView.setText("Angle:         "
                            + Float.valueOf(zAngle).toString() + "\n");
                    mTextView.append("Fraction:      "
                            + Float.valueOf(_orientationFiltered).toString()
                            + "\n");
                    mTextView.append("Pitch:         "
                            + Float.valueOf(_values[1]).toString() + "\n");
                    mTextView
                            .append("Gravity:       "
                                    + Float.valueOf(_gravityFiltered)
                                    .toString() + "\n");
                } else {
                    mTextView
                            .setText("Gravity:       "
                                    + Float.valueOf(_gravityFiltered)
                                    .toString() + "\n");
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
