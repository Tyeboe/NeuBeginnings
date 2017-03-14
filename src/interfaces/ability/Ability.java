package interfaces.ability;

import java.io.Serializable;

import character.Character;

public abstract class Ability implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8165600092077902345L;
	public final String NAME;
	
	public Ability(String name){
		NAME = name;
	}
	
	public String getName(){
		return NAME;
	}
	/**
	 *  
	 * @param user The user of the Ability
	 * @param targets The target of the ability.
	 * @return A boolean value representing the success of the ability's use.
	 */
	public abstract boolean use(Character user, Character... targets);
	public abstract String getDescription();
	
	@Override
	public String toString(){
		return NAME;
	}

}
