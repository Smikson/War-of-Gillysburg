package WyattWitemeyer.WarOfGillysburg;

// Ability 4: "Penetration Arrow"
public class PenetrationArrow extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Constructor
	public PenetrationArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Penetration Arrow\"", source, rank);
		this.owner = source;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
}