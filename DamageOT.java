package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public abstract class DamageOT extends Condition {
	// Constructors, same as the first two of Condition but require a Character as source.
	public DamageOT(Character source, String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		this.setSource(source);
	}
	public DamageOT(Character source, String name, int duration) {
		this(source, name, duration, (Character withEffect) -> {return true;});
	}
	
	// Abstract function that will be called each time the DOT increments its turn
	public abstract void executeDOT();
	
	// Overrides the incrementTurn function to also activate the DOT before incrementing
	@Override
	public void incrementTurn() {
		this.executeDOT();
		super.incrementTurn();
	}
	
	// Abstract function that will return the display String for the DOT as if it were a StatusEffect (should be 1 line long)
	public abstract String getDOTString();
	
	// Overrides the toString function to include the DOT String first, before any Status Effects that could be attached to the DOT (still with two tabs)
	@Override
	public String toString() {
		String[] conditionStrings = super.toString().split("\\n");
		LinkedList<String> list = new LinkedList<>();
		for (String line : conditionStrings) {
			list.add(line);
		}
		list.add(1, "\t\t" + getDOTString());
		
		String ret = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			ret += "\n" + list.get(i);
		}
		
		return ret;
	}
}




