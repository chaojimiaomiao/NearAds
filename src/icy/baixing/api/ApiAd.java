package icy.baixing.api;

import icy.baixing.api.Api.ApiCallback;
import icy.baixing.api.Api.JsonHandler;
import icy.baixing.util.JsonUtil;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.baixing.network.api.ApiParams;
import com.baixing.network.api.BaseApiCommand;

public class ApiAd{
	public static void insertAd(Context ctx, Map<String, String> params, String uuid, 
			String cityEnglishName, String categoryEnglishName, String userToken, boolean pay, ApiCallback cb){
		ApiParams apiParam =  new ApiParams();
		apiParam.addParam("params", params);
		apiParam.addParam("uuid", uuid);
		apiParam.addParam("cityEnglishName", cityEnglishName);
		apiParam.addParam("categoryEnglishName", categoryEnglishName);
		apiParam.addParam("payment",pay);
		apiParam.setAuthToken(userToken);
		BaseApiCommand.createCommand("ad.insert/", false, apiParam).execute(ctx, new Api(cb, new JsonHandler(){
			@Override
			public Object processJson(String json){
				String id = "";
				try{
					JSONObject obj = new JSONObject(json);
					JSONObject result = obj.getJSONObject("result");
					id = result.getString("id");
				}catch(JSONException e){
					e.printStackTrace();
				}
				return id;

			}
		}));
	}
	
	public static void updateAd(Context ctx, String adId, Map<String, String> params, String userToken, String uuid, ApiCallback cb){
		ApiParams apiParam =  new ApiParams();
		apiParam.addParam("params", params);
		apiParam.addParam("adId", adId);
		apiParam.addParam("uuid", uuid);
		apiParam.setAuthToken(userToken);
		BaseApiCommand.createCommand("ad.update/", false, apiParam).execute(ctx, new Api(cb, new JsonHandler(){
			@Override
			public Object processJson(String json){
				String id = "";
				try{
					JSONObject obj = new JSONObject(json);
					JSONObject result = obj.getJSONObject("result");
					id = result.getString("id");
				}catch(JSONException e){
					e.printStackTrace();
				}
				return id;

			}
		}));
	}
	
	public static void getAdByIds(Context ctx, String adIds, ApiCallback cb){
		ApiParams param = new ApiParams();
		param.addParam("adIds", adIds);
		BaseApiCommand.createCommand("ad.ads/", true, param).execute(ctx, new Api(cb, new JsonHandler(){

			@Override
			public Object processJson(String json) {
				// TODO Auto-generated method stub
				return JsonUtil.getGoodsListFromJsonByJackson(json);
			}
			
		}));
	}
	
	public static void getAdsByGraph(Context ctx, ApiCallback cb, String categoryName, Map<String, String> coordinates, int size) {
		if (categoryName.length() == 0) {
			categoryName = "ershou";
		}
		String apiName = categoryName + "/ad";
		ApiParams params = new ApiParams();
		/*for (int i = 0; i < coordinates.size(); i++) {
			params.addParam(coordinates.get(i)., value);
		}*/
		params.addAll(coordinates);
		params.addParam("size", size);
		params.addParam("from", 0);
		//Log.e("getAdsByGraph", params.toString());
		BaseApiCommand.createCommand(apiName, true, params).execute(ctx, new Api(cb, new JsonHandler(){
			@Override
			public Object processJson(String json) {
				Log.e("getAdsByGraph", "json: " + json);
				return JsonUtil.getGoodsListFromJsonByJackson(json);
			}
		}));
	}
	/*
	public static void searchAd(Context ctx, String cityId, String keywords, ApiCallback cb){
		ApiParams params = new ApiParams();
		params.addParam("keywords", keywords);
		params.addParam("cityId", cityId);
		
		BaseApiCommand.createCommand("Ad.search/", true, params).execute(ctx, new Api(cb, new JsonHandler(){

			@Override
			public Object processJson(String json) {
				// TODO Auto-generated method stub
				return JsonUtil.parseAdSearchCategoryCountResult(json);
			}
			
		}));
	}*/
}