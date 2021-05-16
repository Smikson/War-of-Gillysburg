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
		
		// Sets the Cooldown and scaler of the Ability
		this.setCooldown();
		this.setScaler();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// The Cooldown of this Ability is 4 until rank 5 where it is reduced to 5
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 5;
		}
		
		// This Ability starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the Scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = 1.2;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = 1.2;
				break;
			case 2:
				this.scaler = 1.4;
				break;
			case 3:
				this.scaler = 1.5;
				break;
			case 4:
				this.scaler = 1.7;
				break;
			case 5:
				this.scaler = 1.7;
				break;
			case 6:
				this.scaler = 1.9;
				break;
			case 7:
				this.scaler = 2.0;
				break;
			case 8:
				this.scaler = 2.2;
				break;
			case 9:
				this.scaler = 2.3;
			case 10:
				this.scaler = 2.5;
				break;
		}
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	// Use prompt select of some sort for ability select
	
	//DE Curently does not have an additional description
}
