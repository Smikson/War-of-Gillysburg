package WyattWitemeyer.WarOfGillysburg;

// Ability 2: "Frozen Arrow"
public class FrozenArrow extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private int ccBaseDuration;
	private int numStacks;
	
	// Constructor
	public FrozenArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"Frozen Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the scaler and Cooldown of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the base duration for Crowd Control Effects
		this.setBaseDuration();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 5
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the default scaler of the Ability
	private void setScaler() {
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .8;
				break;
			case 2:
				this.scaler = 1;
				break;
			case 3:
				this.scaler = 1;
				break;
			case 4:
				this.scaler = 1.2;
				break;
			case 5:
				this.scaler = 1.3;
				break;
			case 6:
				this.scaler = 1.5;
				break;
			case 7:
				this.scaler = 1.5;
				break;
			case 8:
				this.scaler = 1.7;
				break;
			case 9:
				this.scaler = 1.9;
				break;
			case 10:
				this.scaler = 2;
				break;
		}
	}
	
	// Sets the base duration for Crowd Control effects (varies slightly based on rank and what enemy is attacked)
	private void setBaseDuration() {
		// The base duration starts at 1, upgrading to 2 and rank 3 and to 3 at rank 10
		this.ccBaseDuration = 1;
		if (this.rank() >= 3) {
			this.ccBaseDuration = 2;
		}
		if (this.rank() >= 10) {
			this.ccBaseDuration = 3;
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public int getBaseDuration() {
		return this.ccBaseDuration;
	}
	
	public Slow getSlow(int duration, int amount) {
		return new Slow("Frozen Arrow: Slow", duration, amount);
	}
	public Snare getSnare(int duration) {
		return new Snare("Frozen Arrow: Snare", duration);
	}
	public Stun getStun(int duration) {
		return new Stun("Frozen Arrow: Stun", duration);
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
	
	
	//DE Will need several if statement for affecting fire/ice/hairy targets differently
	
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
		ret += "\n\tBase CC Duration: " + this.getBaseDuration();
		return ret;
	}
}