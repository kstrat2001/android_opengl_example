package com.kainosterholt.open_gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kain on 6/13/15.
 */
public class BlendedGLRenderer extends BaseGLRenderer {

    private Sprite mSprite;
    private Sprite mSprite2;

    private float rotation = 0.0f;

    public BlendedGLRenderer(Context context)
    {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);

        mSprite = new Sprite(this);
        mSprite.setModelFile("sphere.obj");
        mSprite.setShaderFile("blend");
        mSprite.setObjectColor(1.0f, 0.0f, 0.0f, 0.5f);
        mSprite.load();

        mSprite2 = new Sprite(this);
        mSprite2.setModelFile("sphere.obj");
        mSprite2.setShaderFile("blend");
        mSprite2.setObjectColor(0.0f, 1.0f, 1.0f, 1.0f);
        mSprite2.load();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        super.onSurfaceChanged(unused, width, height);

        GLES20.glViewport(0, 0, width, height);
        mSprite.setProjection(width, height, 1, 100);
        mSprite2.setProjection(width, height, 1, 100);
    }

    @Override
    public void updateAndRender(float dt)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        rotation += 90.0f * dt;

        Matrix.setIdentityM(mSprite.mModelViewMatrix, 0);
        Matrix.translateM(mSprite.mModelViewMatrix, 0, 0, 0, -4.0f);

        Matrix.setIdentityM(mSprite2.mModelViewMatrix, 0);
        Matrix.rotateM(mSprite2.mModelViewMatrix, 0, rotation, 0, 0, 1.0f);
        Matrix.translateM(mSprite2.mModelViewMatrix, 0, 1.0f, 0, -4.0f);
        Matrix.scaleM(mSprite2.mModelViewMatrix, 0, 0.3f, 0.3f, 0.3f);

        mSprite2.update(dt);
        mSprite2.draw();

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        mSprite.update(dt);
        mSprite.draw();

        GLES20.glDisable(GLES20.GL_BLEND);
    }
}

