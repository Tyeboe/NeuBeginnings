package floors;

import java.io.Serializable;

public class Note implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8390542394587805007L;
	private final String message;
//	private final Coordinates coord;
	
	public Note(String message/*, Coordinates coord*/) {
		if (message != null) {
			
			this.message = message;
		}else{
			throw new IllegalArgumentException("A notes message cannot be null.");
		}
//		this.coord = coord;
	}
	
	public String getMessage() {
		return message;
	}
	
//	public Coordinates getCoord() {
//		return coord;
//	}
}
