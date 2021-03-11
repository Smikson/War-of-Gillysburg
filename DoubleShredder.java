package WyattWitemeyer.WarOfGillysburg;

// Ability 2: "Double Shredder"
public class DoubleShredder extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public DoubleShredder(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"Double Shredder\"", source, rank);
		this.owner = source;
		
	}
}
