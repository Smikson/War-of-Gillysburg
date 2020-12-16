package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Multi-Purposed"
public class MultiPurposed extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Constructor
	public MultiPurposed(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Multi-Purposed\"", source, rank);
		this.owner = source;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
}