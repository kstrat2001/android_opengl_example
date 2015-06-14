package com.kainosterholt.open_gl;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by kain on 6/12/15.
 */
public class Sprite
{
    private IAssetManager mAssetManager;

    private TDModel mModel;

    private int mProgram = 0;
    private int mIBO = 0;
    private int mVBO = 0;
    private int mTex = 0;

    private String mModelName = "plane.obj";
    private String mShaderName = "default";
    private String mTextureName = null;

    public final float[] mProjectionMatrix = new float[16];
    public final float[] mModelViewMatrix = new float[16];
    public final float[] mObjectColor = new float[4];
    public final float[] mAmbientColor = new float[4];
    public final float[] mLightPosition = new float[3];

    public Sprite(IAssetManager assetManager) {
        mAssetManager = assetManager;

        mObjectColor[0] = 1.0f;
        mObjectColor[1] = 0.0f;
        mObjectColor[2] = 0.0f;
        mObjectColor[3] = 1.0f;

        mAmbientColor[0] = 0.2f;
        mAmbientColor[1] = 0.2f;
        mAmbientColor[2] = 0.2f;
        mAmbientColor[3] = 1.0f;

        mLightPosition[0] = 0.0f;
        mLightPosition[1] = 0.0f;
        mLightPosition[2] = 0.0f;

        Matrix.setIdentityM(mModelViewMatrix, 0);
        Matrix.translateM(mModelViewMatrix, 0, 0, 0, -4.0f);
    }

    public void setModelFile(String modelFile) { mModelName = modelFile; }
    public void setShaderFile(String shaderName) { mShaderName = shaderName; }
    public void setTextureFile(String textureFile) { mTextureName = textureFile; }

    public void setObjectColor(float r, float g, float b, float a)
    {
        mObjectColor[0] = r;
        mObjectColor[1] = g;
        mObjectColor[2] = b;
        mObjectColor[3] = a;
    }

    public void load()
    {
        mModel = mAssetManager.loadModel(mModelName);
        mProgram = mAssetManager.loadShaderProgram(mShaderName);

        int [] buffers = { 0, 0};
        GLES20.glGenBuffers(2, buffers, 0);
        mIBO = buffers[0];
        mVBO = buffers[1];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mModel.getVBOSize(),
                mModel.vertexBuffer,
                GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                mModel.getIBOSize(),
                mModel.indexBuffer,
                GLES20.GL_STATIC_DRAW);

        GLES20.glUseProgram(mProgram);

        int uniformObjectColor = GLES20.glGetUniformLocation(mProgram, "ObjectColor");
        GLES20.glUniform4fv(uniformObjectColor, 1, mObjectColor, 0);

        int uniformAmbientGlobal = GLES20.glGetUniformLocation(mProgram, "AmbientGlobal");
        GLES20.glUniform4fv(uniformAmbientGlobal, 1, mAmbientColor, 0);

        int uniformLightPosition = GLES20.glGetUniformLocation(mProgram, "LightPosition");
        GLES20.glUniform3fv(uniformLightPosition, 1, mLightPosition, 0);

        GLES20.glUseProgram(0);

        if( mTextureName != null) {
            mTex = mAssetManager.loadTexture(mTextureName);
        }
    }

    public void setProjection(int width, int height, float near, float far)
    {
        float ratio = (float) width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, ratio, 1, 100);

        GLES20.glUseProgram(mProgram);

        // Set matrices
        int uniformModelview = GLES20.glGetUniformLocation(mProgram, "Projection");
        GLES20.glUniformMatrix4fv(uniformModelview, 1, false, mProjectionMatrix, 0);

        GLES20.glUseProgram(0);
    }

    public void update(float dt)
    {
        // Set matrices
        GLES20.glUseProgram(mProgram);
        int uniformModelview = GLES20.glGetUniformLocation(mProgram, "Modelview");
        GLES20.glUniformMatrix4fv(uniformModelview, 1, false, mModelViewMatrix, 0);

        int uniformNormalMat = GLES20.glGetUniformLocation(mProgram, "NormalMat");
        float [] normalMatrix = new float[16];
        Matrix.invertM(normalMatrix, 0, mModelViewMatrix, 0);
        float [] normsOut = new float[16];
        Matrix.transposeM(normsOut, 0, normalMatrix, 0);
        GLES20.glUniformMatrix4fv(uniformNormalMat, 1, false, normsOut, 0);
    }

    public void draw() {

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // get handle to vertex shader's vPosition member
        int positionAttrib = GLES20.glGetAttribLocation(mProgram, "vtxPosition");
        GLES20.glEnableVertexAttribArray(positionAttrib);
        GLES20.glVertexAttribPointer(positionAttrib, 3,
                GLES20.GL_FLOAT, false,
                mModel.getStride(), 0);

        int normalAttrib = GLES20.glGetAttribLocation(mProgram, "vtxNormal");
        GLES20.glEnableVertexAttribArray(normalAttrib);
        GLES20.glVertexAttribPointer(normalAttrib, 3,
                GLES20.GL_FLOAT, false,
                mModel.getStride(), 12);

        int uvAttrib = -1;
        if( mTex != 0 ) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTex);

            uvAttrib = GLES20.glGetAttribLocation(mProgram, "vtxUV");
            GLES20.glEnableVertexAttribArray(uvAttrib);
            GLES20.glVertexAttribPointer(uvAttrib, 2,
                    GLES20.GL_FLOAT, false,
                    mModel.getStride(), 24);
        }

        // Draw
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mModel.getNumIndices(),
                GLES20.GL_UNSIGNED_SHORT, 0);

        // Clean up
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
        GLES20.glDisableVertexAttribArray(positionAttrib);
        GLES20.glDisableVertexAttribArray(normalAttrib);

        if( uvAttrib != -1 ) {
            GLES20.glDisableVertexAttribArray(uvAttrib);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }
}
