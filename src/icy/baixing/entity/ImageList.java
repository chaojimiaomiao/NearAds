package icy.baixing.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ImageList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String square;
	private String square180;
	private String small;
	private String big;
	
	public String getSquare() {
		return square;
	}
	
	@JsonIgnore
	public String[] getSquareArray(){
		return parseForArray(square);
	}
	
	public void setSquare(String square) {
		if (this.square == null) {
			this.square = square;
		}else{
			this.square += ","+square;
		}		
	}
	
	public String getSquare180() {
		return square180;
	}
	
	@JsonIgnore
	public String[] getSquare180Array(){
		return parseForArray(square180);
	}
	
	public void setSquare180(String square) {
		if (this.square180 == null) {
			this.square180 = square;
		}else{
			this.square180 += ","+square;
		}		
	}
	
	public String getSmall() {
		return small;
	}
	
	@JsonIgnore
	public String[] getSmallArray() {
		return parseForArray(small);
	}
	
	public void setSmall(String small) {
		if (this.small == null) { 
			this.small = small;
		}else{
				this.small += ","+small;				
		}
	}
	
	public String getBig() {
		return big;
	}
	
	@JsonIgnore
	public String[] getBigArray() {
		return parseForArray(big);
	}
	
	private String[] parseForArray(String str) {
		if (str != null) {
			String[] list = str.split(",");
			return list;
		}
		
		return null;
	}
	
	public void setBig(String big) {
		if (this.big == null) { 
			this.big = big;
		}else{
			this.big += ","+big;	
		}		
	}
	
	@Override
	public String toString() {
		return "ImageList [square=" + square + ", square180=" + square180 + ", small=" + small + ", big="
				+ big +"]";
	}
	
}
