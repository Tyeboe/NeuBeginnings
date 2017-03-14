package abilities;

import character.Character;
import interfaces.ability.AttackAbility;
import interfaces.ability.GroupAbility;

public class ExpertTimeManagement extends AttackAbility implements GroupAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050692586161746365L;

	public ExpertTimeManagement() {
		super("Expert Time Management");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean use(Character user, Character... targets) {
		// TODO Auto-generated method stub
		if (user.useEnergy(13)) {
			for (Character c : targets) {
				c.takeDmg(user.attack());
			}
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Attacks all enemies. Fails if the user has less than 5 energy to expend.";
	}

}
