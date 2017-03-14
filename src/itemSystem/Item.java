package itemSystem;

import java.io.Serializable;

public abstract class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6732819968550686679L;
	public final String NAME;
	protected String description;
	
	public Item(String name, String description) {
		this.NAME = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return NAME;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}
}
