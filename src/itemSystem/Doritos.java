package itemSystem;

import character.Character;
import enums.Character.ModifiableFields;
import interfaces.item.Usable;

public class Doritos extends Item implements Usable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2324395903496419137L;

	public Doritos() {
		super("Doritos", "Triangular snacks every gamer needs for fuel.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void use(Character target) {
		target.modifyField(ModifiableFields.HITPOINTS, 50);
	}

}
