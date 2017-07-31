package com.example.martinrgb.a57sphereexample3

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLU
import android.opengl.GLSurfaceView.Renderer

class OpenGLRenderer4 : Renderer {

    // Ambient light
    private val mat_ambient = floatArrayOf(0.2f, 0.3f, 0.4f, 1.0f)
    private var mat_ambient_buf: FloatBuffer? = null
    // Parallel incident light
    private val mat_diffuse = floatArrayOf(0.4f, 0.6f, 0.8f, 1.0f)
    private var mat_diffuse_buf: FloatBuffer? = null
    // The highlighted area
    private val mat_specular = floatArrayOf(0.2f * 0.4f, 0.2f * 0.6f, 0.2f * 0.8f, 1.0f)
    private var mat_specular_buf: FloatBuffer? = null

    private val mSphere = Sphere()

     var mLightX = 10f
     var mLightY = 10f
     var mLightZ = 10f

    override fun onDrawFrame(gl: GL10) {
        // To clear the screen and the depth buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        // Reset the modelview matrix
        gl.glLoadIdentity()

        gl.glEnable(GL10.GL_LIGHTING)
        gl.glEnable(GL10.GL_LIGHT0)

        // Texture of material
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient_buf)
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse_buf)
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specular_buf)
        // Specular exponent 0~128 less rough
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 96.0f)

        //The position of the light source
        val light_position = floatArrayOf(mLightX, mLightY, mLightZ, 0.0f)
        val mpbb = ByteBuffer.allocateDirect(light_position.size * 4)
        mpbb.order(ByteOrder.nativeOrder())
        val mat_posiBuf = mpbb.asFloatBuffer()
        mat_posiBuf.put(light_position)
        mat_posiBuf.position(0)
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, mat_posiBuf)

        gl.glTranslatef(0.0f, 0.0f, -3.0f)
        mSphere.draw(gl)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

        // Set the output screen size
        gl.glViewport(0, 0, width, height)

        // Projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION)
        // Reset the projection matrix
        gl.glLoadIdentity()
        // Set the viewport size
        // gl.glFrustumf(0, width, 0, height, 0.1f, 100.0f);

        GLU.gluPerspective(gl, 90.0f, width.toFloat() / height, 0.1f, 50.0f)

        // Select the model view matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        // Reset the modelview matrix
        gl.glLoadIdentity()

    }

    override fun onSurfaceCreated(gl: GL10, arg1: EGLConfig) {
        // On the perspective correction
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
        // Background: Black
        gl.glClearColor(0f, 0.0f, 0.0f, 0.0f)
        // Start the smooth shading
        gl.glShadeModel(GL10.GL_SMOOTH)

        // Reset the depth buffer
        gl.glClearDepthf(1.0f)
        // Start the depth test
        gl.glEnable(GL10.GL_DEPTH_TEST)
        // Type the depth test
        gl.glDepthFunc(GL10.GL_LEQUAL)

        initBuffers()
    }

    private fun initBuffers() {
        var bufTemp = ByteBuffer.allocateDirect(mat_ambient.size * 4)
        bufTemp.order(ByteOrder.nativeOrder())
        mat_ambient_buf = bufTemp.asFloatBuffer()
        mat_ambient_buf!!.put(mat_ambient)
        mat_ambient_buf!!.position(0)

        bufTemp = ByteBuffer.allocateDirect(mat_diffuse.size * 4)
        bufTemp.order(ByteOrder.nativeOrder())
        mat_diffuse_buf = bufTemp.asFloatBuffer()
        mat_diffuse_buf!!.put(mat_diffuse)
        mat_diffuse_buf!!.position(0)

        bufTemp = ByteBuffer.allocateDirect(mat_specular.size * 4)
        bufTemp.order(ByteOrder.nativeOrder())
        mat_specular_buf = bufTemp.asFloatBuffer()
        mat_specular_buf!!.put(mat_specular)
        mat_specular_buf!!.position(0)
    }
}