package com.example.martinrgb.multiplytest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    @BindView(R.id.background)
    ImageView mBackground;
    @BindView(R.id.overlay)
    ImageView mOverlay;
    @BindView(R.id.spotlightview)
    SpotlightView mSpotlightview;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteBars();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSpotlightview.setMaskX(540);
        mSpotlightview.setMaskY(1720);
        mSpotlightview.setMaskScale(1);

        setClickListener();

    }

    private boolean hasClicked = false;

    private void setClickListener() {
        mSpotlightview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasClicked) {

                    ValueAnimator mValueAnimator = ValueAnimator.ofFloat(1720, 200);
                    mValueAnimator.setInterpolator(new DecelerateInterpolator());
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mSpotlightview.setMaskY((float) animation.getAnimatedValue());
                        }
                    });
                    mValueAnimator.setDuration(1500);
                    mValueAnimator.start();

                } else {
                    ValueAnimator mValueAnimator = ValueAnimator.ofFloat(200, 1720);
                    mValueAnimator.setInterpolator(new DecelerateInterpolator());
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mSpotlightview.setMaskY((float) animation.getAnimatedValue());
                        }
                    });
                    mValueAnimator.setDuration(600);
                    mValueAnimator.start();

                }


                hasClicked = !hasClicked;
            }
        });
    }


    private void deleteBars() {


        //Delete Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
    }

}
