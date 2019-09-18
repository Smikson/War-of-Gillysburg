package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Invincible extends CrowdControl {
	// Constructs an "invincible" effect
	public Invincible(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
	}
	public Invincible(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invincible: " + super.toString();
	}
}
