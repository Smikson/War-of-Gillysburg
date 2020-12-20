package WyattWitemeyer.WarOfGillysburg;

// Ability 3: "Exploding Arrow"
public class ExplodingArrow extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double baseScaler;
	private double explosionScaler;
	
	// Constructor
	public ExplodingArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Exploding Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the scalers and the Cooldown of the Ability
		this.setCooldown();
		this.setScalers();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 3, reduced to 2 at rank 5
		this.cooldown = 3;
		if (this.rank() >= 5) {
			this.cooldown = 2;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the base and fire scalers of the Ability (and sets the default equal to the base)
	private void setScalers() {
		// Set a default value for the first rank
		this.baseScaler = .5;
		this.explosionScaler = .75;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = .5;
				this.explosionScaler = .75;
				break;
			case 2:
				this.baseScaler = .65;
				this.explosionScaler = .8;
				break;
			case 3:
				this.baseScaler = .65;
				this.explosionScaler = .8;
				break;
			case 4:
				this.baseScaler = .75;
				this.explosionScaler = 1;
				break;
			case 5:
				this.baseScaler = .75;
				this.explosionScaler = 1;
				break;
			case 6:
				this.baseScaler = .85;
				this.explosionScaler = 1.2;
				break;
			case 7:
				this.baseScaler = 1;
				this.explosionScaler = 1.2;
				break;
			case 8:
				this.baseScaler = 1.2;
				this.explosionScaler = 1.3;
				break;
			case 9:
				this.baseScaler = 1.5;
				this.explosionScaler = 1.5;
				break;
			case 10:
				this.baseScaler = 1.5;
				this.explosionScaler = 1.5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getExplosionScaler() {
		return this.explosionScaler;
	}
	
	
	//DE Watch for dealing less damage to allies and primary target taking bonus explosive damage
	//DE Needs a check for number of enemies hit to grant "next Ability is Empowered" status
	
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBase Scaler: " + this.getBaseScaler();
		ret += "\n\tExplosion Scaler: " + this.getExplosionScaler();
		return ret;
	}
}