package abilities;

import character.Character;
import interfaces.ability.AttackAbility;

public class PullAnAllNighter extends AttackAbility {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4660865307316702884L;

	public PullAnAllNighter(){
		super("Pull An All Nighter!");
	}

	@Override
	public boolean use(Character user, Character... targets) {
		if(user.getEnergy() > 50 && user.useEnergy(user.getEnergyProperty().get())){
			targets[0].takeDmg(user.attack() * 3);
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Ultimate attack that will drain all energy but inflict massive damage. A minimum of 50 energy is required to use this ability.";
	}

}
