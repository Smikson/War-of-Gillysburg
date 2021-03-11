package WyattWitemeyer.WarOfGillysburg;

// ULTIMATE Ability: "Rain of Arrows"
public class RainOfArrows extends UltimateAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Constructor
	public RainOfArrows(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Rain of Arrows\"", source, rank);
		this.owner = source;
		
	}
}
