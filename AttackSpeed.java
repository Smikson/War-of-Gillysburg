package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Attack Speed"
public class AttackSpeed extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public AttackSpeed(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"AttackSpeed\"", source, rank);
		this.owner = source;
		
	}
}
