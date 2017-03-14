package itemSystem;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.item.Usable;

public class Coffee extends Item implements Usable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7636337009960023177L;

	public Coffee() {
		super("Coffee","Black tar that will keep you going for a few more hours.");
	}

	@Override
	public void use(Character target) {
		target.modifyField(ModifiableFields.ENERGY, 50);
	}

}
