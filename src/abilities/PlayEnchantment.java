package abilities;

import character.Character;
import enums.Character.ModifiableFields;
import enums.Character.Stats;
import interfaces.ability.BuffAbility;

public class PlayEnchantment extends BuffAbility {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3886616045575217019L;
	private boolean usedBuff = false;

	public PlayEnchantment() {
		super("Play Enchantment");
	}
	
	public boolean getUsedBuff() {
		return usedBuff;
	}

	@Override
	public boolean use(Character user, Character... targets) {
		boolean successful = true;
		if(!usedBuff) {
			user.modifyField(ModifiableFields.HITPOINTS, 20);
			int current = user.getStat(Stats.ENDURANCE);
			user.getStats().remove(Stats.ENDURANCE);
			user.getStats().put(Stats.ENDURANCE, current+2);
			usedBuff = true;
		}else {
			successful = false;
		}
		return successful;
	}

	@Override
	public String getDescription() {
		return "Play an enchantment to increase your defense and heal a small amount.";
	}

}
