package WyattWitemeyer.WarOfGillysburg;

// Ability 1: "Multi-Shot"
public class MultiShot extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional Variables
	private int numArrows;
	private double scalerDeduction;
	
	// Constructor
	public MultiShot(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Multi-Shot\"", source, rank);
		this.owner = source;
		
		// Set the Cooldown (always 4), scalers, and number of arrows
		this.setCooldown();
		this.setScalersAndArrows();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// The Cooldown of this Ability is always 4
		this.cooldown = 4;
		
		// This Ability starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the base scaler and the number of arrows
	private void setScalersAndArrows() {
		// Set a default value for the first rank
		this.scaler = 0.5;
		this.numArrows = 3;
		this.scalerDeduction = 0;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = 0.5;
				this.numArrows = 3;
				this.scalerDeduction = 0;
				break;
			case 2:
				this.scaler = 0.65;
				this.numArrows = 3;
				this.scalerDeduction = 0;
				break;
			case 3:
				this.scaler = 0.7;
				this.numArrows = 3;
				this.scalerDeduction = 0.1;
				break;
			case 4:
				this.scaler = 0.75;
				this.numArrows = 3;
				this.scalerDeduction = 0.1;
				break;
			case 5:
				this.scaler = 0.8;
				this.numArrows = 3;
				this.scalerDeduction = 0.075;
				break;
			case 6:
				this.scaler = 1.0;
				this.numArrows = 3;
				this.scalerDeduction = 0.075;
				break;
			case 7:
				this.scaler = 0.75;
				this.numArrows = 5;
				this.scalerDeduction = 0.075;
				break;
			case 8:
				this.scaler = 0.8;
				this.numArrows = 5;
				this.scalerDeduction = 0.075;
				break;
			case 9:
				this.scaler = 0.85;
				this.numArrows = 5;
				this.scalerDeduction = 0.075;
				break;
			case 10:
				this.scaler = 0.85;
				this.numArrows = 7;
				this.scalerDeduction = 0.05;
				break;
		}
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public int getNumArrows() {
		return this.numArrows;
	}
	public double getScalerDeduction() {
		return this.scalerDeduction;
	}
	
	
	// Lots of things with CDs at rank 15.
	// Note: Target Multiple may not work as intended for this Ability. Maybe create Target Multiple (# of desired targets)
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tNumber of Arrows: " + this.getNumArrows();
		ret += this.rank() >= 3? ("\n\tSequential Arrow Scaler Deduction: " + this.getScalerDeduction()) : "";
		return ret;
	}
}
