package com.example.martinrgb.a57sphereexample3

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class OpenGLView(context: Context) : GLSurfaceView(context) {

    private val mRenderer: OpenGLRenderer4

    private var mDownX = 0.0f
    private var mDownY = 0.0f

    init {

        mRenderer = OpenGLRenderer4()
        this.setRenderer(mRenderer)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x
                mDownY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> return true
            MotionEvent.ACTION_MOVE -> {
                val mX = event.x
                val mY = event.y
                mRenderer.mLightX = mRenderer.mLightX + (mX - mDownX) / 10
                mRenderer.mLightY = mRenderer.mLightY - (mY - mDownY) / 10
                mDownX = mX
                mDownY = mY
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }
}