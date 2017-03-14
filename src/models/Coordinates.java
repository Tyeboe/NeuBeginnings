package models;

import java.io.Serializable;

public class Coordinates implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8071405808640612785L;
	private int x;
	private int y;
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinates(Coordinates place) {
		this.x = place.getX();
		this.y = place.getY();
	}

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + "]";
	}

	public boolean equals(Coordinates c){
		if(getX() == c.getX() && getY() == c.getY()){
			return true;
		}
		return false;
	}

}
