package tiles;

import character.Player;
import floors.Note;
import interfaces.note.Noteable;
import interfaces.tile.Interactable;

public class FloorMessage extends Tile implements Interactable, Noteable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 19751879742944256L;
	//TODO(andrew): set message sometime
	private Note note;
	
	public FloorMessage(int tileSheetNum) {
		super(tileSheetNum);
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew): Display message from tile here
		
	}

	@Override
	public String getMessage() {
		return note.getMessage();
	}

	@Override
	public void setNote(Note note) {
		this.note = note;
	}
}
