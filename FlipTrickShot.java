package WyattWitemeyer.WarOfGillysburg;

// Ability 4: "Flip Trick Shot"
public class FlipTrickShot extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public FlipTrickShot(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Flip Trick Shot\"", source, rank);
		this.owner = source;
		
	}
}
