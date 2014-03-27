package icy.nearad;

import icy.baixing.entity.Ad;
import icy.baixing.entity.Ad.EDATAKEYS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TextFactory {
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
        ArrayList<ViewWithDegree> list = generateText1();
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
    private ArrayList<ViewWithDegree> generateText1() {
    	ArrayList<ViewWithDegree> dTextViews = new ArrayList<ViewWithDegree>();
        for (int i=0; i<adList.size(); i++) {
        	final Ad detail = adList.get(i);
        	final LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.item_tag, null);
        	TextView distanceView = (TextView) linearLayout.findViewById(R.id.id_ad_distance);
        	String string = String.valueOf((int)detail.getDistance() + "米");
        	distanceView.setText(string);
            linearLayout.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					linearLayout.findViewById(R.id.id_bg).setBackgroundResource(R.drawable.sign_green_click);
					String location = detail.getValueByKey("具体地点");
					String title = detail.getValueByKey(EDATAKEYS.EDATAKEYS_TITLE);
					View view = LayoutInflater.from(context).inflate(R.layout.content, null);
					((TextView)view.findViewById(R.id.id_adress)).setText("地址：" + location);
					((TextView)view.findViewById(R.id.id_content)).setText("标题：" + title);
					/*Toast toast = new Toast(context);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, -40);
					toast.setView(view);
					toast.show();*/
					PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,  
				            LayoutParams.WRAP_CONTENT);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.showAtLocation(v, Gravity.CENTER, 0, -40);
					return false;
				}
            });
        	ViewWithDegree viewWithDegree = new ViewWithDegree();
            viewWithDegree.degree = detail.getDegree();
            viewWithDegree.view = (View) linearLayout;
            viewWithDegree.topMargin = getTopMargin(detail.getDistance(), 3000);//.................detail.getTopMargin();
            dTextViews.add(viewWithDegree);
        }
        return dTextViews;
    }
/*    public void resetSize(LinearLayout linearLayout) {
		ImageView imageView = (ImageView)linearLayout.findViewById(R.id.id_bg);
		int width = imageView.getWidth();
		int height = imageView.getHeight();
		imageView.
	}*/
    public int getTopMargin(double distance, int maxDis) {
		int maxHeight = (int)TextActivity.dHeight - 20;
		int topMargin = maxHeight - (int)(maxHeight * distance /maxDis);//
		return topMargin;
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
}
