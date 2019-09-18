package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Snare extends CrowdControl{
	// Constructs a snare
	public Snare(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, linkedConditions, Stat.DODGE, -90);
		this.effects.add(dodgeReduction);
	}
	public Snare(String name, int duration) {
		super(name, duration);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, Stat.DODGE, -90);
		this.effects.add(dodgeReduction);
	}
	
	@Override
	public String toString() {
		return "Snare: " + super.toString();
	}
}
