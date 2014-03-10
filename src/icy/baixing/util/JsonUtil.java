package icy.baixing.util;

import icy.baixing.entity.Ad;
import icy.baixing.entity.AdList;
import icy.baixing.entity.ImageList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
public class JsonUtil {
	public static AdList getGoodsListFromJsonByJackson(String jsonData) {
		JsonFactory factory = new JsonFactory();
		AdList goodsList = new AdList();
		List<Ad> list = new ArrayList<Ad>();
		try {
			JsonParser parser = factory.createJsonParser(jsonData);
			JsonToken jt = parser.nextToken();
			jt = parser.nextToken();
			String fieldname = parser.getCurrentName();

			if (fieldname.equals("result")) {// /start_array
				jt = parser.nextToken();

				while (jt != JsonToken.END_ARRAY) {
					jt = parser.nextToken();// /start_object

					if (jt == JsonToken.END_ARRAY) {
						break;
					}

					Ad ad = new Ad();
					setAdFromJsonByJackson(ad, jt, parser);
					list.add(ad);
				}
			}

			parser.close();

		} catch (JsonParseException e) {
			
		} catch (IOException e) {
			
		} catch (Throwable t) {

		}
		goodsList.setData(list);
		return goodsList;
	}
    private static void setAdFromJsonByJackson(Ad detail, JsonToken jt, JsonParser parser) throws JsonParseException, IOException {
		
		while(jt != JsonToken.END_OBJECT){
			String fname = parser.getCurrentName();
			if(fname == null){
				jt = parser.nextToken();
				continue;
			}
			
			if(fname.equals("images")){
				jt = parser.nextToken();//START_ARRAY
				jt = parser.nextToken();
				ImageList il = new ImageList();
				
				while(jt != JsonToken.END_ARRAY){
					jt = parser.nextToken();///start_object		
					
					while(jt != JsonToken.END_OBJECT){
						String imgType = parser.getCurrentName();
						jt = parser.nextToken();///value
						String imgUrl = parser.getText();

						
						if(imgUrl != null && imgUrl.length() > 0){
							if(imgType.equals("big")){
								il.setBig(imgUrl);
							}else if(imgType.equals("small")){
								il.setSmall(imgUrl);
							}else if(imgType.equals("square_180")){
								il.setSquare180(imgUrl);
							}else if(imgType.equals("square")) {
								il.setSquare(imgUrl);
							}
						}
						jt = parser.nextToken();
					}
					jt = parser.nextToken();
				}
				detail.setImageList(il);
				jt = parser.nextToken();//end of image object
			}else if(fname.equals("metaData")){
				jt = parser.nextToken();//START_OBJECT
				ArrayList<String> metas = new ArrayList<String>();
				jt = parser.nextToken();
				
				while(jt != JsonToken.END_OBJECT){
					String text=parser.getCurrentName();
					jt = parser.nextToken();
					jt = parser.nextToken();
					
					while (jt != JsonToken.END_OBJECT) {
						String meta = parser.getCurrentName();
						jt = parser.nextToken();
						
						if (meta != "label"){ 
							if (jt == JsonToken.START_ARRAY) {
								while (jt != JsonToken.END_ARRAY) {
									jt = parser.nextToken();
								}
								jt = parser.nextToken();
							} else if (jt == JsonToken.START_OBJECT) {
								while (jt != JsonToken.END_OBJECT) {
									jt = parser.nextToken();
								}
								jt = parser.nextToken();
							}else {
								jt = parser.nextToken();
							}
							continue;
						}

						String data = parser.getText();
						if (data != null && data.length() > 0) {
							text += " " + data;
							//metas.add(text);
						}
						
						jt = parser.nextToken();
					}
					metas.add(text);
					jt = parser.nextToken();
				}		
				detail.setMetaData(metas);
			} else if (fname.equals("user")) {
				jt = parser.nextToken(); // START_OBJECT
				jt = parser.nextToken();
				
				while (jt != JsonToken.END_OBJECT) {
					String key = parser.getCurrentName();
					if (key == null) {
						jt = parser.nextToken();
						continue;
					}
					jt = parser.nextToken();
					if (key.equals("id")) {
						String value = parser.getText();
						if (value != null && value.length() > 0) {
							detail.setValueByKey("userId", value.substring(1));
						}
					} else if (key.equals("name")) {
						String value = parser.getText();
						if (value != null && value.length() > 0) {
							detail.setValueByKey("userNick", value);
						}
					} else if (key.equals("createdTime")) {
						String value = parser.getText();
						if (value != null && value.length() > 0) {
							detail.setValueByKey("userCreatedTime", value);
						}
					} else if (key.equals("mobile")) {
						String value = parser.getText();
						if (value != null && value.length() > 0) {
							detail.setValueByKey(key, value);
						}
					} else if (key.equals("bindBusinessLicence")) {//zhaibingjie@baixing.com
						String value = parser.getText();
						if (value != null && value.length() > 0) {
							detail.setValueByKey(key, value);
						}
					}else{
						if(jt == JsonToken.START_ARRAY){
							while(jt != JsonToken.END_ARRAY){
								jt = parser.nextToken();
							}
						}else if(jt == JsonToken.START_OBJECT){
							while(jt != JsonToken.END_OBJECT){
								jt = parser.nextToken();
							}
						}else{
							String text = parser.getText();
							detail.setValueByKey(key, text);
						}
					}
					jt = parser.nextToken();
				}
				jt = parser.nextToken();
			} else if (fname.equals("areaNames")) {
				jt = parser.nextToken(); // START_ARRAY
				StringBuffer valueBuffer = new StringBuffer();
				jt = parser.nextToken();
				
				while (jt != JsonToken.END_ARRAY) {
					String val = parser.getText();
					if (val != null && val.length() > 0) {
						valueBuffer.append(val);
						valueBuffer.append(",");
					}
					jt = parser.nextToken();
				}
				if (valueBuffer.length() > 0) {
					detail.setValueByKey(fname, valueBuffer.substring(0, valueBuffer.length() - 1));
				}
				jt = parser.nextToken();
			} else if (fname == "area") {
				while (jt != JsonToken.END_OBJECT){
					jt = parser.nextToken();
				}
				jt = parser.nextToken();	
			}else{
				jt = parser.nextToken();
				if(jt == JsonToken.START_ARRAY){
					while(jt != JsonToken.END_ARRAY){
						jt = parser.nextToken();
					}
					jt = parser.nextToken();
				}else if(jt == JsonToken.START_OBJECT){
					while(jt != JsonToken.END_OBJECT){
						jt = parser.nextToken();
					}
					jt = parser.nextToken();
				}else{
					String text = parser.getText();
					detail.setValueByKey(fname, text);
					jt = parser.nextToken();
				}
			}
		}
		
		jt = parser.nextToken();
		detail.setIsBuying();
	}
}
