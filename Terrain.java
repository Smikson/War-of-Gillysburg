package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Terrain give multiple Status Effects as a single Condition just like Crowd Control, but are permanent until removed
public class Terrain extends CrowdControl {
	// List of static Terrains to be used for basic computations
	public static final HighGround HIGH_GROUND = new HighGround();
	public static final Hill HILL = new Hill();
	public static final Cover COVER = new Cover();
	public static final Tree TREE = new Tree();
	
	
	// Constructors - Terrain are permanent bonuses (only removed manually)
	public Terrain(String name, HashSet<Condition> linkedConditions) {
		super(name, -1, linkedConditions);
		this.makePermanent();
	}
	public Terrain(String name) {
		super(name, -1);
		this.makePermanent();
	}
	public Terrain() {
		this("Unknown Terrain Bonus");
	}
	
	// To String override for clarity when playing
	@Override
	public String toString() {
		String ret = this.getName();
		for (StatusEffect se : this.effects) {
			ret += "\n\t\t" + se.toString();
		}
		
		return ret;
	}
}
