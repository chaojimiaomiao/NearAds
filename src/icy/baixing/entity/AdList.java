package icy.baixing.entity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class AdList implements Serializable {
	private static final long serialVersionUID = -2158869923050057462L;

	private List<Ad> data;
	public AdList(){}
	public AdList(List<Ad> data__){
		data = data__;
	}
	public List<Ad> getData() {
		return data;
	}
	public void setData(List<Ad> data) {
		this.data = data;
	}
	
	public Object clone(){
		List<Ad> temp = new ArrayList<Ad>();
		temp.addAll(data);
		return new AdList(temp);
	}
}
