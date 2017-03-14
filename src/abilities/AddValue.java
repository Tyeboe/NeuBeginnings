package abilities;

import character.Character;
import enums.Character.ModifiableFields;
import enums.Character.Stats;
import interfaces.ability.BuffAbility;

public class AddValue extends BuffAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6245588048802577832L;
	private boolean usedBuff = false;

	public AddValue() {
		super("Add Value");
	}

	public boolean getUsedBuff() {
		return usedBuff;
	}

	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;

		if (!usedBuff) {
			int temp = user.getStat(Stats.INTELLIGIENCE);
			user.getStats().remove(Stats.INTELLIGIENCE);
			user.getStats().put(Stats.INTELLIGIENCE, temp + 5);
			user.modifyField(ModifiableFields.HITPOINTS, 50);
			usedBuff = true;
		} else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Adds value to the battle by increasing his teaching effectiveness.";
	}

}
