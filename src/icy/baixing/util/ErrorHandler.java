//liuchong@baixing.com
package icy.baixing.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ErrorHandler extends Handler{

	/*definition of error code*/
	public final static int ERROR_OK = 0;
	public final static int ERROR_COMMON_WARNING = -2;
	public final static int ERROR_COMMON_FAILURE = -3;
	public final static int ERROR_SERVICE_UNAVAILABLE = -9;
	public final static int ERROR_NETWORK_UNAVAILABLE = -10;
	
	private final String MSG_KEY = "popup_message";
	
	private static ErrorHandler m_instance = null;
	private Context context = null;
	
	public static ErrorHandler getInstance(){
		if(m_instance == null){
			m_instance = new ErrorHandler();
		}
		
		return m_instance;
	}
	
	public void initContext(Context context_) {
		this.context = context_;
	}
	
	public void handleError(int errorCode, String msgContent) {
		Message msg = this.obtainMessage(errorCode);
		if (msgContent != null) {
			Bundle bundle = new Bundle();
			bundle.putString(MSG_KEY, msgContent);
			msg.setData(bundle);
		}
		
		this.sendMessage(msg);
	}
	
	

	@Override
	public void handleMessage(Message msg) {
		
		if(null != msg.obj){
			if(msg.obj instanceof ProgressDialog){
				((ProgressDialog)msg.obj).dismiss();
			}
		}
		
		String strToast = null;
		if(null != msg.getData() && null != msg.getData().getString(MSG_KEY)){
			strToast = msg.getData().getString(MSG_KEY);
		}
		
		if(null == strToast){
			switch (msg.what) {
			case ERROR_OK:
				strToast = "操作已成功！";
				break;
			case ERROR_COMMON_WARNING:
				strToast = "请注意！";
				break;
			case ERROR_COMMON_FAILURE:
				strToast = "操作失败，请检查后重试!";
				break;
			case ERROR_SERVICE_UNAVAILABLE:
				strToast = "服务当前不可用，请稍后重试！";
				break;	
			case ERROR_NETWORK_UNAVAILABLE:
				strToast = "网络连接失败，请检查设置！";
				break;	
			}
		}		
		
		if(null != strToast && 0 != strToast.length()) {
			//ViewUtil.showToast(this.context, strToast, false);
		}
		
		
		if(null != msg.getCallback()){
			msg.getCallback().run();
		}
		
		super.handleMessage(msg);
	}

}
