package WyattWitemeyer.WarOfGillysburg;

// Ability 1: "Flaming Arrow"
public class FlamingArrow extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private double baseScaler;
	private double fireScaler;
	private int fireDuration;
	private int numStacks;
	
	// Constructor
	public FlamingArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Flaming Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the Cooldown and scalers of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the burn duration of the Ability
		this.setFireDuration();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 3, reduced to 2 at rank 7
		this.cooldown = 3;
		if (this.rank() >= 7) {
			this.cooldown = 2;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the base and fire scalers of the Ability (and sets the default equal to the base)
	private void setScalers() {
		// Set a default value for the first rank
		this.baseScaler = 1.0;
		this.fireScaler = .2;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = 1.0;
				this.fireScaler = .2;
				break;
			case 2:
				this.baseScaler = 1.2;
				this.fireScaler = .2;
				break;
			case 3:
				this.baseScaler = 1.3;
				this.fireScaler = .25;
				break;
			case 4:
				this.baseScaler = 1.5;
				this.fireScaler = .25;
				break;
			case 5:
				this.baseScaler = 1.7;
				this.fireScaler = .25;
				break;
			case 6:
				this.baseScaler = 1.8;
				this.fireScaler = .3;
				break;
			case 7:
				this.baseScaler = 2.0;
				this.fireScaler = .3;
				break;
			case 8:
				this.baseScaler = 2.1;
				this.fireScaler = .35;
				break;
			case 9:
				this.baseScaler = 2.2;
				this.fireScaler = .4;
				break;
			case 10:
				this.baseScaler = 2.5;
				this.fireScaler = .5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Sets the duration of the burn/fire effect
	private void setFireDuration() {
		// The fire duration is 2 until rank 5 where it is increased to 3
		this.fireDuration = 2;
		if (this.rank() >= 5) {
			this.fireDuration = 3;
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getFireScaler() {
		return this.fireScaler;
	}
	
	public int getFireDuration() {
		return this.fireDuration;
	}
	
	public Blind getBlind(int duration) {
		return new Blind("Flaming Arrow: Blind", duration);
	}
	public Stun getStun() {
		return new Stun("Flaming Arrow: Stun", 1);
	}
	
	// Functions for "Empowered" effects
	public int getNumStacks() {
		return this.numStacks;
	}
	public boolean isEmpowered() {
		return this.numStacks >= this.owner.getEmpoweredStackRequirement();
	}
	public void makeEmpowered() {
		this.numStacks = this.owner.getEmpoweredStackRequirement();
	}
	
	
	//DE Need to create a "SentinelBurnDOT" class that is basically exactly the same as "BleedDOT" but no vorpal and deals fire damage
	//DE For rank 10, might want a list of already burned enemies so it doesnt affect the same enemy twice, but can just add a prompt in "SentinelBurnDOT"
	
	//DE in use, set numStacks + or - 1 based on rank of EmpoweredArrows and include use2 for using the Empowered version
	
	
	
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
		ret += "\n\tFire Scaler: " + this.getFireScaler();
		ret += "\n\tFire Duration: " + this.getFireDuration();
		return ret;
	}
}