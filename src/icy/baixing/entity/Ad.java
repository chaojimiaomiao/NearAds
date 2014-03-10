//liuchong@baixing.com
package icy.baixing.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class Ad implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public ImageList imageList = new ImageList();
	public double distance = 0;
	public ArrayList<String> metaData = new ArrayList<String>();
	public HashMap<String, String> data = new HashMap<String, String>();
	
	public boolean hasBuySuperUrgent;
	public boolean hasBuyUrgent;
	public boolean hasBuyDing;
	
	public enum EDATAKEYS{
		EDATAKEYS_LICENCE("bindBusinessLicence"),
		EDATAKEYS_TITLE("title"),
		EDATAKEYS_DESCRIPTION("content"),
		EDATAKEYS_LAT("lat"),
		EDATAKEYS_LON("lng"),
		EDATAKEYS_DATE("createdTime"),
        EDATAKEYS_POST_TIME("insertedTime"),
		EDATAKEYS_ID("id"),
		EDATAKEYS_CATEGORYENGLISHNAME("categoryId"),
		EDATAKEYS_CITYENGLISHNAME("cityEnglishName"),
		EDATAKEYS_AREANAME("areaNames"),
		EDATAKEYS_MOBILE("mobile"),
		EDATAKEYS_MOBILE_AREA("mobileArea"),
		EDATAKEYS_WANTED("wanted"),
		EDATAKEYS_CONTACT("contact"),
		EDATAKEYS_LINK("link"),
		EDATAKEYS_PRICE("价格"),
		EDATAKEYS_URGENT("urgent"),
		EDATAKEYS_CPMSIGN("cpm_sign"),
		EDATAKEYS_INSERT_SERVICE_TYPE("insert_service_type"),
		EDATAKEYS_DING("dingEnd"),
		EDATAKEYS_SUPERURGENT("superUrgent"),
		EDATAKEYS_CATEGORYNAME("categroyName"),
		;
		
		public String metaKey;
		private EDATAKEYS(String key)
		{
			this.metaKey = key;
		}
	}
	@JsonIgnore
	public Set<String> getKeys(){
		if(null == data) return null;
		return data.keySet();
	}
	@JsonIgnore
	private String getStringByEnum(EDATAKEYS e){
		String key = "";
		switch(e){
		case EDATAKEYS_CATEGORYNAME:
			key = "categroyName";
			break;
		case EDATAKEYS_URGENT:
			key = "urgent";
			break;
		case EDATAKEYS_TITLE:
			key = "title";
			break;
		case EDATAKEYS_DESCRIPTION:
			key = "content";
			break;
		case EDATAKEYS_LAT:
			key = "lat";
			break;
		case EDATAKEYS_LON:
			key = "lng";
			break;
		case EDATAKEYS_DATE:
			key = "createdTime";
			break;
        case EDATAKEYS_POST_TIME:
            key = "insertedTime";
            break;
		case EDATAKEYS_ID:
			key = "id";
			break;
		case EDATAKEYS_CATEGORYENGLISHNAME:
			key = "categoryEnglishName";
			break;
		case EDATAKEYS_CITYENGLISHNAME:
			key = "cityEnglishName";
			break;
		case EDATAKEYS_AREANAME:
			key = "areaNames";
			break;
		case EDATAKEYS_MOBILE:
			key = "mobile";
			break;
		case EDATAKEYS_WANTED:
			key = "wanted";
			break;
		case EDATAKEYS_CONTACT:
			key = "contact";
			break;
		case EDATAKEYS_LINK:
			key = "link";
			break;
		case EDATAKEYS_PRICE:
			key ="价格";
			break;
		case EDATAKEYS_CPMSIGN:
			key = "cpm_sign";
			break;
		case EDATAKEYS_INSERT_SERVICE_TYPE:
			key = "insert_service_type";
			break;
		default:
			break;
		}
		return key;
	}
	@JsonIgnore
	public String getValueByKey(EDATAKEYS e){
		String key = e.metaKey;
		return getValueByKey(key);
	}
	@JsonIgnore
	public String getValueByKey(String key){
		if(key.equals("")) return "";
		String value = "";
		if(data.containsKey(key)){
			value = data.get(key);
		}else{
			value = this.getMetaValueByKey(key);			
		}
		return  value;
	}	
	@JsonIgnore
	public void setValueByKey(EDATAKEYS e, String value){
		String key =  getStringByEnum(e);
		if(key.equals("") || TextUtils.isEmpty(value) || value.equals("null")) return;
		data.put(key, value);
	}
	@JsonIgnore
	public void setValueByKey(String key, String value){
		if(key == null || key.equals("")) return;
		if(value != null && !value.equals("") && !value.equals("null")){
			String strDecoded = value.replaceAll("&amp;", "&").replace("&#039;", "'").replaceAll("&quot;", "\"");
			data.put(key, strDecoded);
		}
			
	}
	@JsonIgnore
	private String getMetaValueByKey(String key){
		for(int i = 0; i < metaData.size(); ++ i){
			String[] meta = metaData.get(i).split(" ");
			if(meta.length >= 2){
				if(meta[0].equals(key)){
					return meta[1];
				}
			}
		}
		return "";
	}
	
	public ArrayList<String> getMetaData() {
		return metaData;
	}
	public void setMetaData(ArrayList<String> metaData) {
		this.metaData = metaData;
	}
	public ImageList getImageList() {
		return imageList;
	}
	public void setImageList(ImageList imageList) {
		this.imageList = imageList;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public boolean equals (Object o){
		if(o instanceof Ad){
			String idOfthis = getValueByKey(EDATAKEYS.EDATAKEYS_ID);
			String idOfo = ((Ad)o).getValueByKey(EDATAKEYS.EDATAKEYS_ID);
			boolean bRet = idOfthis.equals(idOfo);
			
			return bRet;
		}
		
		return false;
	}
	
	@JsonIgnore
	public boolean isValidMessage() {
		return !this.getValueByKey("status").equals("4") && !this.getValueByKey("status").equals("20");
	}
	
	public void setIsBuying(){
		this.hasBuyDing = isPayValid(EDATAKEYS.EDATAKEYS_DING);
		this.hasBuyUrgent = isPayValid(EDATAKEYS.EDATAKEYS_URGENT);
		this.hasBuySuperUrgent = isPayValid(EDATAKEYS.EDATAKEYS_SUPERURGENT);
	}
	
	private Boolean isPayValid(EDATAKEYS key){
		Long timestamp = getTimeStamp(key);
		return timestamp*1000 > Calendar.getInstance().getTimeInMillis(); 
	}
	
	public long getTimeStamp(EDATAKEYS key){
		if(key.equals(EDATAKEYS.EDATAKEYS_DING) || key.equals(EDATAKEYS.EDATAKEYS_URGENT) || key.equals(EDATAKEYS.EDATAKEYS_SUPERURGENT)){
			String timestamp = getValueByKey(key);
			if(!TextUtils.isEmpty(timestamp)){
				return Long.valueOf(timestamp);
			}			
		}
		return 0;
	}
}