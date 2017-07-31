package com.example.martinrgb.a57sphereexample3

import android.os.Bundle
import android.app.Activity
import android.view.Window
import android.view.WindowManager

class MainActivity : Activity() {

    private var mOpenGLView: OpenGLView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Go to the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //Set screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mOpenGLView = OpenGLView(this)
        setContentView(mOpenGLView)
    }
}