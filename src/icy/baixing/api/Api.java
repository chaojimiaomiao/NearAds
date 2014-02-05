//xumengyi@baixing.com
package icy.baixing.api;

import icy.baixing.util.ErrorHandler;
import icy.baixing.util.JsonUtil;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.baixing.network.api.ApiError;
import com.baixing.network.api.ApiParams;
import com.baixing.network.api.BaseApiCommand;
import com.baixing.network.api.BaseApiCommand.Callback;

public class Api implements Callback{
	static final int CUSTOM_ERROR = 0x00001234;
	public static abstract class ApiCallback{
		abstract public void handleSuccess(String apiName, Object result);
		public void handleFail(String apiName, ApiError error){
			if(error != null && !TextUtils.isEmpty(error.getMsg())){
				ErrorHandler.getInstance().handleError(CUSTOM_ERROR, error.getMsg());
			}else{
				ErrorHandler.getInstance().handleError(ErrorHandler.ERROR_SERVICE_UNAVAILABLE, null);
			}
		}
	}
	
	static class JsonHandler{
		public Object processJson(String json){
			return json;
		}
	}
	
	private ApiCallback mCB;
	private JsonHandler jsonHandler;
	Api(ApiCallback cb, JsonHandler jsonHandler){
		mCB = cb;
		this.jsonHandler = jsonHandler;
	}

	@Override
	public void onNetworkDone(String apiName, String responseData) {
		// TODO Auto-generated method stub
		if(mCB != null){
			mCB.handleSuccess(apiName, jsonHandler != null ? jsonHandler.processJson(responseData) : responseData);
		}
	}

	@Override
	public void onNetworkFail(String apiName, ApiError error) {
		// TODO Auto-generated method stub
		if(mCB != null){
			mCB.handleFail(apiName, error);
		}
	}
	
	public static void getCityList(Context ctx, ApiCallback cb){
		BaseApiCommand.createCommand("City.all/", true, null).execute(ctx, new Api(cb, new JsonHandler()));
	}
	
	public static void getAllCategory(Context ctx, ApiCallback cb){
		BaseApiCommand.createCommand("Category.all/", true, null).execute(ctx, new Api(cb, new JsonHandler()));
	}
	

	public static void sendMobileCode(Context ctx, String mobile, ApiCallback cb){
		ApiParams param = new ApiParams();
		param.addParam("mobile", mobile);
		BaseApiCommand.createCommand("user.sendMobileCode/", true, param).execute(ctx, new Api(cb, null));
	}
	

	
	public static void getFeatureConfig(Context ctx, String key, boolean useCache, ApiCallback cb){
		ApiParams params = new ApiParams();
        params.addParam("key", key);
        if(useCache){
        	params.useCache = true;
        }
        
		BaseApiCommand.createCommand("Mobile.featureConfig/", true, params).execute(ctx, new Api(cb, new JsonHandler(){

			@Override
			public Object processJson(String json) {
				JSONObject obj = null;
                try {
					JSONObject respond = new JSONObject(json);
					obj = respond.getJSONObject("result");
				} catch (JSONException e) {
				}
                return obj;
			}
			
		}));
	}
	
	public static void getMetaChildren(Context ctx, String id, ApiParams params, ApiCallback cb) {
		BaseApiCommand.createCommand(id + "/children", true, params).execute(ctx, new Api(cb, new JsonHandler()));
	}

	public static void getMetaObject(Context ctx, String id, ApiCallback cb){
		BaseApiCommand.createCommand(id, true, null).execute(ctx, new Api(cb, new JsonHandler()));
	}
	
	public static void freeRefresh(Context ctx, String adId, String userToken, ApiCallback cb){
    	ApiParams params = new ApiParams();    	
        params.addParam("id", adId);
        params.setAuthToken(userToken);
        BaseApiCommand.createCommand("ad.freeRefresh/", true, params).execute(ctx, new Api(cb, null));
	}
	
	public static void deleteAd(Context ctx, String adId, String userToken, ApiCallback cb){
    	ApiParams params = new ApiParams();
    	params.addParam("id", adId);
    	params.setAuthToken(userToken);
    	
    	BaseApiCommand.createCommand("ad.delete/", true, params).execute(ctx, new Api(cb, null));
	}

	public static void getPostMeta(Context ctx, String categoryEnglishName, ApiCallback cb){
		ApiParams param = new ApiParams();
		param.addParam("categoryId", categoryEnglishName);
		BaseApiCommand.createCommand("Meta.post/", true, param).execute(ctx, new Api(cb, new JsonHandler()));		
	}
	
	public static void getFilterMeta(Context ctx, String categoryEnglishName, String cityId, ApiCallback cb){
		ApiParams params = new ApiParams();
		params.addParam("categoryId", categoryEnglishName);
		params.addParam("cityId", cityId);
		BaseApiCommand.createCommand("Meta.filter/", true, params).execute(ctx, new Api(cb, new JsonHandler()));
	}
	
	public static void getCityAreas(Context ctx, String cityId, ApiCallback cb){
		ApiParams areaParam = new ApiParams();
		areaParam.addParam("cityId", cityId);
		BaseApiCommand.createCommand("City.areas/", true, areaParam).execute(ctx, new Api(cb, new JsonHandler()));
	}

	public static String getCityAreasSync(Context ctx, String cityId){
		ApiParams areaParam = new ApiParams();
		areaParam.addParam("cityId", cityId);
		return BaseApiCommand.createCommand("City.areas/", true, areaParam).executeSync(ctx);
	}

	public static boolean sendTrackDataSync(Context ctx, String json, Map<String, String> commonJson){
		ApiParams params = new ApiParams();
		params.zipRequest = true;
		params.addParam("json", json);		
		params.addParam("commonJson", commonJson);
		
		String result = BaseApiCommand.createCommand("mobile.trackdata/", false, params).executeSync(ctx);
		boolean ret = false;
		try{
			JSONObject obj = new JSONObject(result);
			ret = obj.has("result") ? obj.getBoolean("result") : false;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return ret;

	}

	public static void updateToken(Context ctx, String appBundleId, String appVersion, String deviceToken, String deviceUniqueIdentifier, ApiCallback cb){
		ApiParams params = new ApiParams();
		params.addParam("appBundleId", appBundleId);
		params.addParam("appVersion", appBundleId);
		params.addParam("deviceToken", deviceToken);
		params.addParam("deviceUniqueIdentifier", deviceUniqueIdentifier);
		BaseApiCommand.createCommand("mobile.updateToken/", true, params).execute(ctx, new Api(cb, null));
	}
	
	public static void getHotListing(Context context, ApiParams params, ApiCallback cb) {
		BaseApiCommand.createCommand("mobile.hotCategoryAds/", true, params).execute(context,
				new Api(cb, new JsonHandler() {
			@Override
			public Object processJson(String json){
				return JsonUtil.getGoodsListFromJsonByJackson(json);
			}
		}));
	}
}