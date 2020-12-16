package WyattWitemeyer.WarOfGillysburg;

// ULTIMATE Ability: "Black Arrow"
public class BlackArrow extends UltimateAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Constructor
	public BlackArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Black Arrow\"", source, rank);
		this.owner = source;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
}