package WyattWitemeyer.WarOfGillysburg;

// Ability 4: "Penetration Arrow"
public class PenetrationArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private Condition enemyArmorReduction;
	private int numStacks;
	
	// Constructor
	public PenetrationArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Penetration Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the Cooldown and the scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the enemy Condition of the Ability
		this.setEnemyArmorCondition();
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
	
	// Sets the default scaler of the Ability
	private void setScaler() {
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .75;
				break;
			case 2:
				this.scaler = .8;
				break;
			case 3:
				this.scaler = .9;
				break;
			case 4:
				this.scaler = 1;
				break;
			case 5:
				this.scaler = 1;
				break;
			case 6:
				this.scaler = 1.2;
				break;
			case 7:
				this.scaler = 1.3;
				break;
			case 8:
				this.scaler = 1.4;
				break;
			case 9:
				this.scaler = 1.5;
				break;
			case 10:
				this.scaler = 1.5;
				break;
		}
	}
	
	// Sets the enemy Armor Reduction Condition for the Ability
	private void setEnemyArmorCondition() {
		// Calculate the amount of armor ignored, default of 0
		int amount = 0;
		
		// Increases at the specified ranks in the for loop
		for (int walker = 3; walker <= this.rank(); walker++) {
			// Rank 3 starts the amount at 10%
			if (walker == 3) {
				amount += 10;
			}
			// Rank 4 increases the amount by 2%
			if (walker == 4) {
				amount += 2;
			}
			// Rank 6 increases the amount by 3%
			if (walker == 6) {
				amount += 3;
			}
			// Ranks 8, 9, and 10 increase the amount by 5%
			if (walker == 8 || walker == 9 || walker == 10) {
				amount += 5;
			}
		}
		
		// Create the Status Effect
		StatusEffect armorRed = new StatusEffect(Stat.Version.ARMOR, -amount, StatusEffect.Type.INCOMING);
		armorRed.makePercentage();
		
		// Create the Condition with a duration of 0 since it is only for the one attack
		this.enemyArmorReduction = new Condition("Penetration Arrow: Armor Reduction", 0);
		this.enemyArmorReduction.setSource(this.owner);
		this.enemyArmorReduction.addStatusEffect(armorRed);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public Condition getEnemyArmorReduction() {
		return new Condition(this.enemyArmorReduction);
	}
	
	// Functions for "Empowered" effects
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
	
	
	//DE Same as before with the ability to make the next ability empowered
	
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
		ret += this.rank() >= 3? ("\n\t" + this.getEnemyArmorReduction()) : "";
		return ret;
	}
}