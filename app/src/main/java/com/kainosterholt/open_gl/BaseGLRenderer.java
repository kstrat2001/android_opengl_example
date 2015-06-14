package com.kainosterholt.open_gl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by kain on 6/11/15.
 */
public abstract class BaseGLRenderer implements GLSurfaceView.Renderer, IAssetManager {

    private Context mContext;

    private static long lastTime = 0;

    public BaseGLRenderer(Context context)
    {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    }

    public void onDrawFrame(GL10 unused)
    {
        long time = SystemClock.uptimeMillis();
        long delta = time - lastTime;
        lastTime = time;
        float dt = (float)delta/1000.0f;

        // clamp the time delta to 50ms in case of a pause
        dt = Math.min(dt, 0.05f);

        updateAndRender(dt);
    }

    public abstract void updateAndRender(float dt);

    public void onSurfaceChanged(GL10 unused, int width, int height) {
    }

    public String getContentsOfFile(String filename)
    {
        String result;
        try {
            AssetManager am = getContext().getAssets();
            InputStream in_s = am.open(filename);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            result = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            result = "Error: can't show file.";
        }

        return result;
    }

    public Context getContext()
    {
        return mContext;
    }

    public TDModel loadModel(String filename)
    {
        OBJParser parser = new OBJParser(this);
        return parser.parseOBJ(filename);
    }

    public int loadShaderProgram(String shaderName)
    {
        int vertexShader = this.loadShader(GLES20.GL_VERTEX_SHADER, shaderName + ".vtx");
        int fragmentShader = this.loadShader(GLES20.GL_FRAGMENT_SHADER, shaderName + ".frg");

        // create empty OpenGL ES Program
        int program = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        return program;
    }

    private int loadShader(int type, String shaderName)
    {
        String shaderCode = getContentsOfFile(shaderName);

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compileSuccess = new int[10];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileSuccess, 0);
        if (compileSuccess[0] == GLES20.GL_FALSE) {

            String log = GLES20.glGetShaderInfoLog(shader);
            Log.v("shader error:", shaderName + log);
            throw new RuntimeException("Shader compilation error");
        }

        return shader;
    }

    public int loadTexture(String textureName)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
        else
        {
            AssetManager am = getContext().getAssets();

            try
            {
                InputStream in_s = am.open(textureName);

                // Bind to the texture in OpenGL
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

                ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D, 0, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, in_s);

                // Unbind after loading
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            }
            catch (Exception e)
            {
                Log.v("Texture Loading Error:", e.toString());
            }
        }

        return textureHandle[0];
    }
}
