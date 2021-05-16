package WyattWitemeyer.WarOfGillysburg;

// ULTIMATE Ability: "Rain of Arrows"
public class RainOfArrows extends UltimateAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional Variables
	private double baseScaler;
	private double finalScaler;
	private int numArrows;
	
	// Constructor
	public RainOfArrows(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Rain of Arrows\"", source, rank);
		this.owner = source;
		
		// Set the scalers and number of arrows for this Ability
		this.setScalersAndArrows();
	}
	
	// Sets the base scaler and the number of arrows
	private void setScalersAndArrows() {
		// Set a default value for the first rank
		this.baseScaler = 0.5;
		this.finalScaler = 0.75;
		this.numArrows = 7;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = 0.5;
				this.finalScaler = 0.75;
				this.numArrows = 7;
				break;
			case 2:
				this.baseScaler = 0.5;
				this.finalScaler = 1.0;
				this.numArrows = 8;
				break;
			case 3:
				this.baseScaler = 0.5;
				this.finalScaler = 1.0;
				this.numArrows = 10;
				break;
		}
		
		// Set the scaler value to the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	public double getFinalScaler() {
		return this.finalScaler;
	}
	
	public int getNumArrows() {
		return this.numArrows;
	}
	
	// All the things
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBase Scaler: " + this.getBaseScaler();
		ret += "\n\tFinal Scaler: " + this.getFinalScaler();
		ret += "\n\tNumber of Arrows: " + this.getNumArrows();
		return ret;
	}
}
