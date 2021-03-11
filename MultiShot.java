package WyattWitemeyer.WarOfGillysburg;

// Ability 1: "Multi-Shot"
public class MultiShot extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public MultiShot(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Multi-Shot\"", source, rank);
		this.owner = source;
		
	}
}
