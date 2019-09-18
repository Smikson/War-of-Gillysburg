package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Slow extends CrowdControl {
	// Added Variable
	private double value;
	
	// Constructs a slow
	public Slow(String name, int duration, HashSet<Condition> linkedConditions, double value) {
		super(name, duration, linkedConditions);
		this.value = -value;
		
		StatusEffect speedReduction = new StatusEffect(name, duration, linkedConditions, Stat.SPEED, value);
		speedReduction.makeFlat();
		this.effects.add(speedReduction);
	}
	public Slow(String name, int duration, double value) {
		super(name, duration);
		this.value = -value;
		
		StatusEffect speedReduction = new StatusEffect(name, duration, Stat.SPEED, value);
		speedReduction.makeFlat();
		this.effects.add(speedReduction);
	}
	public Slow() {
		super();
		this.value = 0;
	}
	
	// Returns the value of the slow.
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Slow: " + super.toString();
	}
}
