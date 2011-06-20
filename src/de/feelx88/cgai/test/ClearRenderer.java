package de.feelx88.cgai.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.res.AssetManager;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class ClearRenderer implements Renderer {
	
	static public AssetManager sAssets;
	
	public GLObject mOb, mOb2;
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		
		gl.glLoadIdentity();
		
		GLU.gluLookAt(gl, 0, 5, 5, 0, 0, 0, 0, 1, 0);
		
		mOb.draw(gl);
		/*mOb.mRotation[0]++;*/
		mOb2.draw(gl);
		mOb2.mRotation[0]++;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.5f, 0.5f);  // OpenGL docs.
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.
		// Depth buffer setup.
		gl.glClearDepthf(1);// OpenGL docs.
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glEnable( GL10.GL_LIGHTING );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{ 0, 10, 0, 0 }, 0 );
		gl.glLightfv( GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[]{ 1, 1, 1, 1 }, 0 );
		
		gl.glEnable( GL10.GL_LIGHT0 );
		
		mOb = new GLObject();
		mOb2 = new GLObject();
		try {
			mOb.loadFile( sAssets.open( "models/test.x3d" ) );
			mOb2.loadFile( sAssets.open( "models/test3.x3d" ) );
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
		mOb.mPosition[0] = -2;
		//mOb.mRotation[1] = -1;
		mOb2.mPosition[0] = 2;
		mOb2.mRotation[1] = -1;
		mOb2.mRotation[2] = -1;
		 
	}

}
