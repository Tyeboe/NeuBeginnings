package abilities;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.ability.BuffAbility;

public class PlayDivision extends BuffAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7103691996894286086L;
	private boolean usedBuff = false;
	
	public PlayDivision() {
		super("Play Division");
	}
	
	public boolean getUsedBuff() {
		return usedBuff;
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		if(!usedBuff) {
			user.modifyField(ModifiableFields.HITPOINTS, 100);
			usedBuff = true;
		}else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Unwind with some nice Division gameplay and recover even more motivated than before!";
	}

}
