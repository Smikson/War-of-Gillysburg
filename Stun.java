package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Stun extends CrowdControl{
	// Constructs a stun
	public Stun(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, linkedConditions, Stat.DODGE, -100);
		StatusEffect blockReduction = new StatusEffect(name, duration, linkedConditions, Stat.BLOCK, -75);
		this.effects.add(dodgeReduction);
		this.effects.add(blockReduction);
	}
	public Stun(String name, int duration) {
		super(name, duration);
		
		StatusEffect dodgeReduction = new StatusEffect(name, duration, Stat.DODGE, -100);
		StatusEffect blockReduction = new StatusEffect(name, duration, Stat.BLOCK, -75);
		this.effects.add(dodgeReduction);
		this.effects.add(blockReduction);
	}
	
	@Override
	public String toString() {
		return "Stun: " + super.toString();
	}
}
