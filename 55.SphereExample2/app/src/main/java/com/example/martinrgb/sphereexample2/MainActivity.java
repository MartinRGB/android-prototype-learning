package com.example.martinrgb.sphereexample2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    private MySurfaceView mySurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySurfaceView=new MySurfaceView(this); //创建MysurfaceView对象
        mySurfaceView.requestFocus();           //获取焦点
        mySurfaceView.setFocusableInTouchMode(true);  //设置为可触控
        LinearLayout ll=(LinearLayout) this.findViewById(R.id.main_liner); //获得线性布局的引用
        ll.addView(mySurfaceView);

        ToggleButton tb1=(ToggleButton) findViewById(R.id.toggleButton1);
        tb1.setOnCheckedChangeListener(new ButtonListener());
    }

    class ButtonListener implements CompoundButton.OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.toggleButton1:
                    mySurfaceView.openLightFlag=!mySurfaceView.openLightFlag;
                    break;
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();  //调用MySurfaceView的onPause()方法
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mySurfaceView.onResume();
    }
}
