package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class AssignATeamAssignment extends AttackAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3048351020096971965L;
	private int timesForUse = 2;
	
	public AssignATeamAssignment() {
		super("Assign a Team Assignment");
	}

	
	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		if(timesForUse > 0) {
			int init = user.attack();
			targets[0].takeDmg(init+10);
			timesForUse--;
		} else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Assign a team assignment to make the student dread group work and lazy teammates.";
	}


	public int getTimesForUse() {
		return timesForUse;
	}

}
