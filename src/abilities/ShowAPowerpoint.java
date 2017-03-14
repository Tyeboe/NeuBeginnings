package abilities;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.ability.AttackAbility;

public class ShowAPowerpoint extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8703550585773401954L;
	private int timesForUse = 2;
	
	public ShowAPowerpoint() {
		super("Show a Powerpoint");
	}
	

	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		if(timesForUse > 0) {
			user.attack();
			targets[0].modifyField(ModifiableFields.ENERGY, -20);
			timesForUse--;
		}else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Show a powerpoint with graphics from the 90s";
	}



	public int getTimesForUse() {
		return timesForUse;
	}
	
}
