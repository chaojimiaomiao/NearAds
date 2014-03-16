package icy.nearad;

import icy.baixing.entity.Ad;
import icy.baixing.entity.Ad.EDATAKEYS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextFactory {
    private ArrayList<Integer> distanceList;
    private ArrayList<Ad> adList;
    //private DisComparator disComparator;
    private Context context;

    public TextFactory(Context context) {
        //disComparator = new DisComparator();
        this.context = context;
    }
    
    public ArrayList<ViewWithDegree> setAds(ArrayList<Ad> ads) {
        this.adList = ads;
        Collections.sort(adList, distanceComparator);//按距离排过序的ad
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
    	int topMargin;
    }
    public void draw(ArrayList<ViewWithDegree> lists, View parent) {
		for (int i = 0; i < lists.size(); i++) {
			
		}
	}
    //已排序过的adlist
    private ArrayList<ViewWithDegree> generateText() {
        ArrayList<ViewWithDegree> dTextViews = new ArrayList<ViewWithDegree>();
        for (int i=0; i<adList.size(); i++) {
        	Ad detail = adList.get(i);
        	LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.item_textview, null);
        	TextView textView = (TextView)linearLayout.findViewById(R.id.id_ad_title);
        	//判断linearlayout的背景框,超过1500白线条,低于则黑线条
        	//判断textColor同上
        	//判断text的background，则要每隔500判断一个
        	//judgeTextColor(linearLayout, textView, detail.getDistance());
        	setTextColor(textView, detail.getDistance());
            textView.setPadding(10, 10, 10, 10);
            String string = "";//String.valueOf(detail.getDistance() + "  " + detail.getDegree()) + "\n";
            textView.setText(string + detail.getValueByKey(EDATAKEYS.EDATAKEYS_TITLE));
            textView.setMaxWidth(260);
            if (textView.getLineCount() > 3) {
				textView.setMaxWidth(320);
				textView.setMaxLines(4);
			}
            //textView.measure(MeasureSpec., heightMeasureSpec)
            ViewWithDegree viewWithDegree = new ViewWithDegree();
            viewWithDegree.degree = detail.getDegree();//calculateDegree(adList.get(i));
            viewWithDegree.textView = textView;
            viewWithDegree.view = (View) linearLayout;
            viewWithDegree.topMargin = detail.getTopMargin();
            dTextViews.add(viewWithDegree);
        }
        return dTextViews;
    }
    
    private void setTextColor(TextView textView, double distance) {
		int r=89, g=168, b=66, a=240;//g168  a远的淡k越大越小
		double k = distance/3000 + 1;
		//Log.e("", "系数k: " + k); 
		a = (int) (a - k * 20); if (a >255) { a = 255;}
		r= (int) (r * k);
		//g= (int) (g * k); if(g > 255) {g=255;Log.e("", "has more 255");}
		b= (int) (b * k);
		textView.setBackgroundColor(Color.argb(a, r, g, b));
		textView.setTextColor(context.getResources().getColor(R.color.black));
	}
    
    private void judgeTextColor(LinearLayout linearLayout, TextView textView, double distance) {
    	linearLayout.setBackgroundResource(R.drawable.white_text_bg);
    	textView.setTextColor(context.getResources().getColor(R.color.white));
    	if (distance <= 500) {
    		textView.setBackgroundColor(0xe0111111);//context.getResources().getColor(R.color.d500)
    		return;
        } else if (distance <= 800) {
        	textView.setBackgroundColor(0xe0181818);
        	return;
		} else if (distance <= 1000) {
        	textView.setBackgroundColor(0xe0222222);
        	return;
        } else if (distance <= 1200) {
        	textView.setBackgroundColor(0xe0292929);
        	return;
		} else if (distance <= 1500) {
        	textView.setBackgroundColor(0xe0333333);
        	return;
        } else if (distance <= 1800) {
        	textView.setBackgroundColor(0xe0393939);
        	return;
        }
    	linearLayout.setBackgroundResource(R.drawable.black_text_bg);
    	textView.setTextColor(context.getResources().getColor(R.color.black));
    	if (distance <= 2000) {
    		textView.setBackgroundColor(0xe0444444);
    		return;
        } else if (distance <= 2300) {
        	textView.setBackgroundColor(0xe0484848);
        	return;
        } else if (distance <= 2500) {
        	textView.setBackgroundColor(0xe0515151);
        	return;
        } else {
        	textView.setBackgroundColor(0xe0555555);
        	return;
        }
	}
/*    private double calculateDegree(Ad ad) {
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
    */
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
    
    static Comparator<Ad> distanceComparator = new Comparator<Ad>() {

		@Override
		public int compare(Ad lhs, Ad rhs) {
			if (lhs.getDistance() < rhs.getDistance()) {
				return 1;
			} else if (lhs.getDistance() > rhs.getDistance()) {
				return -1;
			}
			return 0;
		}
	};
    /*
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
    }*/
}
