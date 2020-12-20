package WyattWitemeyer.WarOfGillysburg;

// Ability 4: "Penetration Arrow"
public class PenetrationArrow extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private Condition enemyArmorReduction;
	
	// Constructor
	public PenetrationArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Penetration Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the Cooldown and the scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
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
	
	
	
	//DE Same as before with the ability to make the next ability empowered
	
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 3? ("\n\t" + this.getEnemyArmorReduction()) : "";
		return ret;
	}
}