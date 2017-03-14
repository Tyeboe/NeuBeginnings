package tiles;

import java.io.Serializable;

public abstract class Tile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8817937097669267346L;
	private final int tileSheetNum;
	
	public Tile(int tileSheetNum){
		this.tileSheetNum = tileSheetNum;
	}
	
	public int getTileSheetNum(){
		return this.tileSheetNum;
	}
}
