package com.example.martinrgb.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

class MainActivity : AppCompatActivity() {

    private var isTriggered = false
    private var mTextView: TextView? = null
    private val mconfig = SpringConfig.fromOrigamiTensionAndFriction(150.0, 10.0)
    private var mSpringSystem: SpringSystem? = null
    private var mSpring: Spring? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextView = findViewById(R.id.textView) as TextView
        setSpringSystem()
        findViewById(R.id.textView).setOnClickListener {
            isTriggered = !isTriggered
            if (isTriggered) {
                mSpring!!.setEndValue(1.0)
            } else {
                mSpring!!.setEndValue(0.0)
            }
        }
    }

    fun setSpringSystem() {
        mSpringSystem = SpringSystem.create()
        mSpring = mSpringSystem!!.createSpring()
        mSpring!!.springConfig = mconfig

        mSpring!!.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(mSpring: Spring?) {
                val value = mSpring!!.currentValue.toFloat()
                val value1 = SpringUtil.mapValueFromRangeToRange(value.toDouble(), 0.0, 1.0, 1.0, 2.0).toFloat()
                mTextView!!.scaleX = value1
                mTextView!!.scaleY = value1
                Log.e("progress", java.lang.Float.toString(value))
            }
        })
    }


}
