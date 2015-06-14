package com.kainosterholt.open_gl;

import android.content.Context;

/**
 * Created by kain on 6/11/15.
 */
public interface IAssetManager {
    public Context getContext();

    public String getContentsOfFile(String filename);

    public TDModel loadModel(String filename);

    public int loadShaderProgram(String shaderName);

    public int loadTexture(String textureName);
}
