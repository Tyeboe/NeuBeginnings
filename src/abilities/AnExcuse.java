package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class AnExcuse extends AttackAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AnExcuse() {
		super("An Excuse");
	}

	@Override
	public boolean use(Character user, Character... targets) {
		return false;
	}

	@Override
	public String getDescription() {
		return "Excuses never work.";
	}

}
