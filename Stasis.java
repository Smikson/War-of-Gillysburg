package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Stasis extends CrowdControl {
	// Constructs a stasis
	public Stasis(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
	}
	public Stasis(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Statsis: " + super.toString();
	}
}
