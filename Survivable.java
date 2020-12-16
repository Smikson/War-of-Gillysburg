package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Survivable"
public class Survivable extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Constructor
	public Survivable(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Survivable\"", source, rank);
		this.owner = source;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
}