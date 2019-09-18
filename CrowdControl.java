package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class CrowdControl extends Condition {
	// Additional Required Variable
	public LinkedList<StatusEffect> effects;
	
	public CrowdControl(String name, int duration, HashSet<Condition> linkedConditions) {
		super(name, duration, linkedConditions);
		this.effects = new LinkedList<>();
	}
	public CrowdControl(String name, int duration) {
		super(name, duration);
		this.effects = new LinkedList<>();
	}
	public CrowdControl() {
		super();
		this.effects = null;
	}
	
	// To String override for clarity when playing
	@Override
	public String toString() {
		String ret = this.getName() + ": ";
		if (this.isPermanent()) {
			ret += "Permanent Effect";
		}
		else {
			ret += (this.duration() - this.turnCount.value()) + " turns left";
		}
		for (StatusEffect se : this.effects) {
			ret += "\n\t\t" + se.valueString();
		}
		
		return ret;
	}
}
