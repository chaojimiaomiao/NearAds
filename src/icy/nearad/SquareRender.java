package icy.nearad;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;

public class SquareRender implements GLSurfaceView.Renderer {
    static int directAngle;
	static int one = 0x10000;
	private IntBuffer squareBuffer;
	//不是改变这个是改变translate
	private int[] square = new int[] {
			one, one,  0,
			-one, one, 0,
			one, -one, 0,
			-one, -one, 0
	};
	private float rotateQuad;
	private float axisY = -9;
	private float axisZ = -6;
	/*private float[] vertices = {
            -1.0f, 1.0f, 0.0f, // 0, Top Left
            -1.0f, -1.0f, 0.0f, // 1, Bottom Left
            1.0f, -1.0f, 0.0f, // 2, Bottom Right
            1.0f, 1.0f, 0.0f, // 3, Top Right
    };*/
	private void initSquare() {/*
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length *4);//1floate4byte
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);*/
	}
	
	public void SquareRender() {
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//清除颜色和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		//重置
		gl.glLoadIdentity();
		
		gl.glColor4f(0.5f, 0.5f, 1.0f, 0.8f);//rgba
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(square.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        squareBuffer = byteBuffer.asIntBuffer();
        squareBuffer.put(square);
        squareBuffer.position(0);
        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
        //gl.glRotatef(rotateQuad, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, squareBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        
        for (int i = 0; i < SplashCover.degreeList.size(); i++) {
            int delta = (int) (SplashCover.degreeList.get(i) - directAngle);
            if (Math.abs(delta) < 40) {//决定要显示
                axisZ = (float) (-6 - SplashCover.distanceList.get(i));
                Log.e("", "axisZ: " + axisZ);
                gl.glLoadIdentity(); 
                gl.glTranslatef(delta/10*3, axisY, axisZ);
                gl.glVertexPointer(3, GL10.GL_FIXED, 0, squareBuffer);
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
                axisY += 3;
            }
        }
        axisY = -9;
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        rotateQuad += 0.5f;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		float ratio = (float) width/height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// 告诉系统对透视进行修正
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        // 透明背景
        gl.glClearColor(0,0,0,0);
        gl.glEnable(GL10.GL_CULL_FACE);
        // 启用阴影平滑
        gl.glShadeModel(GL10.GL_SMOOTH);

        // 设置深度缓存
        gl.glClearDepthf(1.0f);
        // 启用深度测试
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // 所做深度测试的类型
        gl.glDepthFunc(GL10.GL_LEQUAL);
		
	}

	
	
	
	
	
	
	
}





