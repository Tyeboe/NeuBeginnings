package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class Lecture extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5896455154503658370L;

	public Lecture() {
		super("Lecture");
	}
	
	@Override
	public boolean use(Character user, Character... targets) {
		int init = user.attack();
		targets[0].takeDmg(init+10);
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Give the student a powerful message about life, professionalism, and hard work.";
	}

}
