package character;

import interfaces.ability.BuffAbility;

public class PickOnYou extends BuffAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6303857220113685554L;

	public PickOnYou() {
		super("Pick On You");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean use(Character user, Character... targets) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Buffs Mr. Krebs' wit stat.";
	}

}
