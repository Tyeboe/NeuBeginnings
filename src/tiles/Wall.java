package tiles;

import interfaces.tile.Collidable;

public class Wall extends Tile implements Collidable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8865839842430549504L;

	public Wall(int tileSheetNum) {
		super(tileSheetNum);
	}
}
