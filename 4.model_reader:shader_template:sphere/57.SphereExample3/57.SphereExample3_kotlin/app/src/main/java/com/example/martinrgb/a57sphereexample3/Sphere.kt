package com.example.martinrgb.a57sphereexample3

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.opengles.GL10

// Calculation of spherical vertex
class Sphere {

    fun draw(gl: GL10) {

        var angleA: Float
        var angleB: Float
        var cos: Float
        var sin: Float
        var r1: Float
        var r2: Float
        var h1: Float
        var h2: Float
        val step = 2.0f
        val v = Array(32) { FloatArray(3) }
        val vbb: ByteBuffer
        val vBuf: FloatBuffer

        vbb = ByteBuffer.allocateDirect(v.size * v[0].size * 4)
        vbb.order(ByteOrder.nativeOrder())
        vBuf = vbb.asFloatBuffer()

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY)

        angleA = -90.0f
        while (angleA < 90.0f) {
            var n = 0

            r1 = Math.cos(angleA * Math.PI / 180.0).toFloat()
            r2 = Math.cos((angleA + step) * Math.PI / 180.0).toFloat()
            h1 = Math.sin(angleA * Math.PI / 180.0).toFloat()
            h2 = Math.sin((angleA + step) * Math.PI / 180.0).toFloat()

            // Fixed latitude, 360 degrees rotation to traverse a weft
            angleB = 0.0f
            while (angleB <= 360.0f) {

                cos = Math.cos(angleB * Math.PI / 180.0).toFloat()
                sin = -Math.sin(angleB * Math.PI / 180.0).toFloat()

                v[n][0] = r2 * cos
                v[n][1] = h2
                v[n][2] = r2 * sin
                v[n + 1][0] = r1 * cos
                v[n + 1][1] = h1
                v[n + 1][2] = r1 * sin

                vBuf.put(v[n])
                vBuf.put(v[n + 1])

                n += 2

                if (n > 31) {
                    vBuf.position(0)

                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf)
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf)
                    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n)

                    n = 0
                    angleB -= step
                }
                angleB += step

            }
            vBuf.position(0)

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf)
            gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf)
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n)
            angleA += step
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY)
    }
}