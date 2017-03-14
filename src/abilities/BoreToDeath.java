package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class BoreToDeath extends AttackAbility{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5926423918984508126L;

	public BoreToDeath() {
		super("Bore To Death");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean use(Character user, Character... targets) {
		targets[0].takeDmg(30);
		return true;
	}

	@Override
	public String getDescription() {
		return "Bore the target to death with a bunch of dull and uninteresting conversation and lecturing.";
	}

}
