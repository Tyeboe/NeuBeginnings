package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class AssignFraculator extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3145717363225657336L;
	private boolean usedBuff = false;
	
	public AssignFraculator() {
		super("Assign Fraculator");
	}
	
	public boolean getUsedBuff() {
		return usedBuff;
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		
		if(!usedBuff) {
			int init = user.attack();
			targets[0].takeDmg(init+30);
			usedBuff = true;
		} else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Assign the lab equivalent of doomsday to wreck the student's life.";
	}

}
