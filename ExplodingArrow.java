package WyattWitemeyer.WarOfGillysburg;

// Ability 3: "Exploding Arrow"
public class ExplodingArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double baseScaler;
	private double explosionScaler;
	private int numStacks;
	
	// Constructor
	public ExplodingArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Exploding Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the scalers and the Cooldown of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables
		this.numStacks = 0;
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
	
	// Functions for 'Empowered" effects
	public int getNumStacks() {
		return this.numStacks;
	}
	public void incrementStacks() {
		this.numStacks++;
	}
	public boolean isEmpowered() {
		return this.numStacks >= this.owner.getEmpoweredStackRequirement();
	}
	public void makeEmpowered() {
		this.numStacks = this.owner.getEmpoweredStackRequirement();
	}
	
	//DE When the use function is called in a different Ability it needs to call a function from the main class to add it to the list
	//DE Each Ability will need to call something from the base Sentinel Specialist to get the scaler bonus from here
	//DE Needs a use(3) for when it is used randomly by Multi-purposed
	//DE Start use(3) by clearing the unique list, above rank 5 also add the Ability at the end to the unique set
	//DE Masterwork rank 15 -> Empowered counts as separate for Multi-Purposed
	
	
	//DE Watch for dealing less damage to allies and primary target taking bonus explosive damage
	//DE Needs a check for number of enemies hit to grant "next Ability is Empowered" status
	
	//DE in use, set numStacks + or - 1 based on rank of EmpoweredArrows and include use(2) for using the Empowered version
	
	
	// Override the to-String to include a description of the number of stacks and if the Ability is Empowered
	@Override
	public String toString() {
		String ori = super.toString();
		if (this.isEmpowered()) {
			return ori + " --EMPOWERED";
		}
		return ori + "  (" + this.getNumStacks() + " stack(s))";
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBase Scaler: " + this.getBaseScaler();
		ret += "\n\tExplosion Scaler: " + this.getExplosionScaler();
		return ret;
	}
}