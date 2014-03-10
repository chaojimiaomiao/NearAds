package icy.nearad;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;

/**
 * 透明立体
 * @author icy
 */
public class MonocleActivity extends Activity {
	private GLSurfaceView mGlSurfaceView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mGlSurfaceView = new GLSurfaceView(MonocleActivity.this);
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.setRenderer(new SquareRender());
        mGlSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(mGlSurfaceView);
        
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }
}
