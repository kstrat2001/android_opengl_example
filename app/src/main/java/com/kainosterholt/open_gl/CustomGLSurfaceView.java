package com.kainosterholt.open_gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class CustomGLSurfaceView extends GLSurfaceView {

    private final BaseGLRenderer mRenderer;

    public CustomGLSurfaceView(Context context, int viewNumber){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        if( viewNumber == 1 ) {
            mRenderer = new TexturedGLRenderer(context);
        }
        else {
            mRenderer = new BlendedGLRenderer(context);
        }

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    public void setRenderModeContinuous()
    {
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void setRenderModeWhenDirty()
    {
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}