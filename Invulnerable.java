package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Invulnerable extends CrowdControl {
	// Constructs an "invulnerable" effect
	public Invulnerable(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
	}
	public Invulnerable(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invulnerable: " + super.toString();
	}
}
