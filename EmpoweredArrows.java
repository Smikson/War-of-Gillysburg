package WyattWitemeyer.WarOfGillysburg;

// Passive Abilities:
// Unique Passive Ability: "Empowered Arrows"
public class EmpoweredArrows extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Constructor
	public EmpoweredArrows(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \"Empowered Arrows\"", source, rank);
		this.owner = source;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
}
