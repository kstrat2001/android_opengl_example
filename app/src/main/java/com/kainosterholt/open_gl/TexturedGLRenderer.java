package com.kainosterholt.open_gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kain on 6/13/15.
 */
public class TexturedGLRenderer extends BaseGLRenderer {

    private Sprite mSprite;

    private float rotation = 0.0f;

    public TexturedGLRenderer(Context context)
    {
        super(context);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);

        mSprite = new Sprite(this);
        mSprite.setModelFile("sphere.obj");
        mSprite.setTextureFile("texture.pkm");
        mSprite.load();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mSprite.setProjection(width, height, 1, 100);
    }

    public void updateAndRender(float dt)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        rotation += 90.0f * dt;

        Matrix.setIdentityM(mSprite.mModelViewMatrix, 0);
        Matrix.translateM(mSprite.mModelViewMatrix, 0, 0, 0, -4.0f);
        Matrix.rotateM(mSprite.mModelViewMatrix, 0, rotation, 0, 1.0f, 0);

        mSprite.update(dt);
        mSprite.draw();
    }
}
