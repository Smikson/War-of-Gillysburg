package WyattWitemeyer.WarOfGillysburg;

// Ability 3: "Concentrate"
public class Concentrate extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public Concentrate(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Concentrate\"", source, rank);
		this.owner = source;
		
	}
}
