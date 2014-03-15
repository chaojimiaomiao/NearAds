package icy.nearad;

import icy.baixing.entity.Ad;
import icy.baixing.entity.Ad.EDATAKEYS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextFactory {
    private ArrayList<Integer> distanceList;
    private ArrayList<Ad> adList;
    private DisComparator disComparator;
    private Context context;

    public TextFactory(Context context) {
        disComparator = new DisComparator();
        this.context = context;
    }
    
    public ArrayList<ViewWithDegree> setAds(ArrayList<Ad> ads) {
        this.adList = ads;
        Collections.sort(adList, disComparator);//按距离排过序的ad
        ArrayList<ViewWithDegree> list = generateText();
        return list;
    }
    
    public ArrayList<Ad> getAdList() {
		return adList;
	}

    public class ViewWithDegree {
    	double degree;
    	TextView textView;
    	View view;
    }
    public void draw(ArrayList<ViewWithDegree> lists, View parent) {
		for (int i = 0; i < lists.size(); i++) {
			
		}
	}
    private ArrayList<ViewWithDegree> generateText() {
        ArrayList<ViewWithDegree> dTextViews = new ArrayList<ViewWithDegree>();
        for (int i=0; i<adList.size(); i++) {
        	LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.item_textview, null);
        	TextView textView = (TextView)linearLayout.findViewById(R.id.id_ad_title);
        	//判断linearlayout的背景框,超过1500白线条,低于则黑线条
        	//判断textColor同上
        	//判断text的background，则要每隔500判断一个
        	//judgeTextColor(linearLayout, textView, adList.get(i).getDistance());
        	setTextColor(textView, adList.get(i).getDistance());
            textView.setPadding(10, 10, 10, 10);
            String string = String.valueOf(adList.get(i).getDistance()) + "\n";
            textView.setText(string + adList.get(i).getValueByKey(EDATAKEYS.EDATAKEYS_TITLE));
            ViewWithDegree viewWithDegree = new ViewWithDegree();
            viewWithDegree.degree = calculateDegree(adList.get(i));
            viewWithDegree.textView = textView;
            viewWithDegree.view = (View) linearLayout;
            dTextViews.add(viewWithDegree);
        }
        return dTextViews;
    }
    
    private void setTextColor(TextView textView, double distance) {
		int r=59, g=168, b=26;
		int k = ((int)distance)/10000 + 1;
		r= r * k;
		g= g * k;
		b= b * k;
		textView.setBackgroundColor(Color.argb(160, r, g, b));
		textView.setTextColor(context.getResources().getColor(R.color.black));
	}
    
    private void judgeTextColor(LinearLayout linearLayout, TextView textView, double distance) {
    	linearLayout.setBackgroundResource(R.drawable.white_text_bg);
    	textView.setTextColor(context.getResources().getColor(R.color.white));
    	if (distance <= 500) {
    		textView.setBackgroundColor(0xee111111);//context.getResources().getColor(R.color.d500)
    		return;
        } else if (distance <= 800) {
        	textView.setBackgroundColor(0xee181818);
        	return;
		} else if (distance <= 1000) {
        	textView.setBackgroundColor(0xee222222);
        	return;
        } else if (distance <= 1200) {
        	textView.setBackgroundColor(0xee292929);
        	return;
		} else if (distance <= 1500) {
        	textView.setBackgroundColor(0xee333333);
        	return;
        } else if (distance <= 1800) {
        	textView.setBackgroundColor(0xee393939);
        	return;
        }
    	linearLayout.setBackgroundResource(R.drawable.black_text_bg);
    	textView.setTextColor(context.getResources().getColor(R.color.black));
    	if (distance <= 2000) {
    		textView.setBackgroundColor(0xee444444);
    		return;
        } else if (distance <= 2300) {
        	textView.setBackgroundColor(0xee484848);
        	return;
        } else if (distance <= 2500) {
        	textView.setBackgroundColor(0xee515151);
        	return;
        } else {
        	textView.setBackgroundColor(0xee555555);
        	return;
        }
	}
    private double calculateDegree(Ad ad) {
    	double adLat = Double.parseDouble(ad.getValueByKey(EDATAKEYS.EDATAKEYS_LAT));
		double adLon = Double.parseDouble(ad.getValueByKey(EDATAKEYS.EDATAKEYS_LON));
		double degree = Math.atan2(adLon - SplashCover.getLng(), adLat - SplashCover.getLat()) * 180 / Math.PI;
		//分三种情况
		double adDegree;
		if (degree <= 0) {
			adDegree = 90 + Math.abs(degree);
		} else if (degree <= 90 && degree > 0) {
			adDegree = 90 - degree;
		} else {
			adDegree = 270 + (180 - degree);
		}
		return adDegree;//与北方的夹角,和传感器一致
	}
    
    private int colorResource(double distance) {
        if (distance <= 500) {
            return context.getResources().getColor(R.color.d500);
        } else if (distance <= 1000) {
            return context.getResources().getColor(R.color.d1000);
        } else if (distance <= 1500) {
            return context.getResources().getColor(R.color.d1500);
        } else if (distance <= 2000) {
            return context.getResources().getColor(R.color.d2000);
        } else if (distance <= 2500) {
            return context.getResources().getColor(R.color.d2500);
        } else {
            return context.getResources().getColor(R.color.d3000); 
        }
    }
    
    private class DisComparator implements Comparator<Ad> {

        @Override
        public int compare(Ad lhs, Ad rhs) {
            double dis1 = lhs.getDistance();
            double dis2 = rhs.getDistance();
            if (dis1 == dis2) {
                return 0;
            } else if (dis1 > dis2) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
