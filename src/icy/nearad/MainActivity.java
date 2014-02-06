package icy.nearad;

import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback, SensorEventListener {
	private float lastDirection;
	private Camera camera;
	DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraSurfaceView);
		//surfaceView.setFocusable(true);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
		
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MonocleActivity.class);
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//event.values[0];  方向角:正北0，正西270
		//event.values[1]   倾斜角
		//event.values[2]   旋转角
		//根据角度不断重绘,相差+-30°以内
	    SquareRender.directAngle = (int)event.values[0];
		if (Math.abs(event.values[0] - lastDirection) > 10) {//超过10°刷新一下
			lastDirection = event.values[0];
		}
	}
	
	private boolean weatherToShow(float mobileDirect, float adDirect) {
		if (Math.abs(mobileDirect - adDirect) > 30) {
			return false;
		}
		//小的显示在左边，大的显示在右边
		return false;
	}
	
	private static class SampleView extends View {
		private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private Matrix mMatrix = new Matrix();
		private Paint.FontMetrics mFontMetrics;

		public SampleView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		 @Override
	        protected void onDraw(Canvas canvas) {
			 
		 }
		
		private void doDraw(Canvas canvas, float src[], float dst[]) {
			canvas.save();
			//mMatrix.setp
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try {
			Camera.Parameters param = camera.getParameters();
			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				camera.setDisplayOrientation(90);
			} else {
				camera.setDisplayOrientation(0);
			}
			List<String> colorEffects = param.getSupportedColorEffects();
			Iterator<String> colorItor = colorEffects.iterator();
			while(colorItor.hasNext()){
				String currColor = colorItor.next();
				if(currColor.equals(Camera.Parameters.EFFECT_SOLARIZE)){
					param.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
					break;
				}
			}
			camera.setParameters(param);
			camera.setPreviewDisplay(holder);
			
			int bestWidth = 0;
			int bestHeight = 0;
 
			dm=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm); 

			List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
			//如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
			if(sizeList.size() > 1){
				Iterator<Camera.Size> itor = sizeList.iterator();
				while(itor.hasNext()){
					Camera.Size cur = itor.next();
					if(cur.width > bestWidth && cur.height>bestHeight && cur.width < dm.widthPixels && cur.height < dm.heightPixels ){//dm.widthPixels,dm.heightPixels
						bestWidth = cur.width;
						bestHeight = cur.height;
					}
				}
				if(bestWidth != 0 && bestHeight != 0){
					param.setPreviewSize(bestWidth, bestHeight);
				}
			}
			int smallfps=1;

			param.setPreviewFrameRate(smallfps);
			camera.setParameters(param);
		} catch (Exception e) {
			// TODO: handle exception
		}
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}
	
	
	
	

}
