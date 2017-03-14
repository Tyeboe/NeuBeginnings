package itemSystem;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.item.Usable;

public class MountainDew extends Item implements Usable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8078987520690372465L;

	public MountainDew() {
		super("Mountain Dew", "The sweet, energizing syrup of the gods.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void use(Character target) {
		target.modifyField(ModifiableFields.ENERGY, 100);
	}

}
