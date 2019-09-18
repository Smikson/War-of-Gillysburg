package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Blind extends CrowdControl {
	// Constructs a blind
	public Blind(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, linkedConditions, Stat.DODGE, -90);
		StatusEffect blockReduction = new StatusEffect(name, duration, linkedConditions, Stat.BLOCK, -70);
		StatusEffect speedReduction = new StatusEffect(name, duration, linkedConditions, Stat.SPEED, -50);
		this.effects.add(dodgeReduction);
		this.effects.add(blockReduction);
		this.effects.add(speedReduction);
	}
	public Blind(String name, int duration) {
		super(name, duration);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, Stat.DODGE, -90);
		StatusEffect blockReduction = new StatusEffect(name, duration, Stat.BLOCK, -70);
		StatusEffect speedReduction = new StatusEffect(name, duration, Stat.SPEED, -50);
		this.effects.add(dodgeReduction);
		this.effects.add(blockReduction);
		this.effects.add(speedReduction);
	}
	
	@Override
	public String toString() {
		return "Blind: " + super.toString();
	}
}
