package interfaces.item;

import java.io.Serializable;

import character.Character;

public interface Usable extends Serializable {
	public void use(Character target);
}
