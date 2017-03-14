package abilities;

import character.Character;
import interfaces.ability.BuffAbility;

public class Procrastinate extends BuffAbility{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5111937462586556702L;

	public Procrastinate() {
		super("Procrastinate");
	}

	@Override
	public boolean use(Character user, Character... targets) {
		if(user.getHPProperty().get() > 30) {
			user.takeDmg(30);
			user.gainEnergy(30);
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Converts 30 hitpoints into energy. Fails if user's hitpoints would fall to zero as a result of this abilities use.";
	}

}
