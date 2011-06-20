package de.feelx88.cgai.test;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

public class CGAITestActivity extends Activity {
    private GLSurfaceView mGLView;
    
    static public AssetManager sAssets;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        
        mGLView = new TestGLView(this);
        final ClearRenderer x = new ClearRenderer();
        ClearRenderer.sAssets = getAssets();
        mGLView.setRenderer( x );
        setContentView(mGLView);
        
        SensorManager man = (SensorManager)getSystemService( Context.SENSOR_SERVICE );
        man.registerListener( new SensorEventListener() {
			private boolean mInit = false;
			private float mCorr = 0;
			@Override
			public void onSensorChanged(SensorEvent event) {
				if( !mInit)
				{
					mInit = true;
					mCorr = event.values[0];
				}
				if( x.mOb != null )
					x.mOb.mRotation[0] = ( event.values[0] - mCorr ) * 3;
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		}, man.getSensorList( Sensor.TYPE_ORIENTATION ).get( 0 ), SensorManager.SENSOR_DELAY_GAME );
    }
    
}