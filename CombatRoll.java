package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Combat Roll"
public class CombatRoll extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public CombatRoll(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Combat Roll\"", source, rank);
		this.owner = source;
		
	}
}
