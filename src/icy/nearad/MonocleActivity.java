package icy.nearad;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;

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
	/*private class MyGLSurfaceClass extends GLSurfaceView {

		public MyGLSurfaceClass(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		public MyGLSurfaceClass(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}
		
	}*/
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
