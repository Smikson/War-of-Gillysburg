package WyattWitemeyer.WarOfGillysburg;

public class Charge extends Ability{
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private double AOEScaler;
	private double targetedScaler;
	
	private int speedPercentage;
	private Condition targetedPreAttackBonus;
	
	// Constructor
	public Charge(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"CHARGE!\"", source, rank);
		this.owner = source;
		
		// Calculate and set the Cooldown and each scaler (base scaler set to targeted scaler)
		this.setCooldown();
		this.setTargetedScaler();
		this.setAOEScaler();
		
		// Calculate the additional Condition effects
		this.setSpeedPercentage();
		this.setTargetedPreAttackBonus();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 5, reduced to 4 at rank 10
		this.cooldown = 5;
		if (this.rank() == 10) {
			this.cooldown = 4;
		}
		// The Ability always starts off Cooldown
		this.turnCount = this.cooldown;
	}
	
	// Calculates each scaler
	private void setTargetedScaler() {
		// Set a default value for the first rank
		this.targetedScaler = 1;
		
		// Set the targeted scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.targetedScaler = 1;
				break;
			case 2:
				this.targetedScaler = 1.2;
				break;
			case 3:
				this.targetedScaler = 1.3;
				break;
			case 4:
				this.targetedScaler = 1.5;
				break;
			case 5:
				this.targetedScaler = 1.5;
				break;
			case 6:
				this.targetedScaler = 1.7;
				break;
			case 7:
				this.targetedScaler = 1.8;
				break;
			case 8:
				this.targetedScaler = 2;
				break;
			case 9:
				this.targetedScaler = 2.3;
				break;
			case 10:
				this.targetedScaler = 2.5;
				break;
		}
		
		// Set the base scaler equal to this scaler for default purposes
		this.scaler = this.targetedScaler;
	}
	
	private void setAOEScaler() {
		// Set a default value for the first rank
		this.AOEScaler = .5;
		
		// Set the AOE scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.AOEScaler = .5;
				break;
			case 2:
				this.AOEScaler = .6;
				break;
			case 3:
				this.AOEScaler = .7;
				break;
			case 4:
				this.AOEScaler = .8;
				break;
			case 5:
				this.AOEScaler = 1;
				break;
			case 6:
				this.AOEScaler = 1.2;
				break;
			case 7:
				this.AOEScaler = 1.3;
				break;
			case 8:
				this.AOEScaler = 1.5;
				break;
			case 9:
				this.AOEScaler = 1.8;
				break;
			case 10:
				this.AOEScaler = 2;
				break;
		}
	}
	
	// Calculate the Speed percentage
	private void setSpeedPercentage() {
		// Set a default value for the first rank (50%)
		this.speedPercentage = 50;
		
		// Rank 3 increases the amount to 100%
		if (this.rank() >= 3) {
			this.speedPercentage = 100;
		}
		
		// Rank 7 increases the amount to 150%
		if (this.rank() >= 7) {
			this.speedPercentage = 150;
		}
		
		// Rank 10 increases the amount to 200%
		if (this.rank() == 10) {
			this.speedPercentage = 200;
		}
	}
	
	// Calculate and create the pre-attack Condition
	private void setTargetedPreAttackBonus() {
		// Declare the starting amounts at Rank 1
		int critAmount = 10;
		int apAmount = 0;
		
		// Calculate the crit amount based on rank
		// Rank 3 increases the amount to 15%
		if (this.rank() >= 3) {
			critAmount = 15;
		}
		// Rank 5 increases the amount to 20%
		if (this.rank() >= 5) {
			critAmount = 20;
		}
		// Rank 7 increases the amount to 25%
		if (this.rank() >= 7) {
			critAmount = 25;
		}
		// At Rank 10, the amount is "a guaranteed crit", so we take current crit from 100 to make 100% chance
		if (this.rank() == 10) {
			critAmount = 100 - this.owner.getCriticalChance();
		}
		
		// Calculate the Armor Piercing amount based on rank
		// Rank 9 the amount is 10%
		if (this.rank() >= 9) {
			apAmount = 10;
		}
		// Rank 10 the amount is 15%
		if (this.rank() == 10) {
			apAmount = 15;
		}
		
		// Create the Status Effects
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, apAmount, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		
		// Create the Condition (only add Armor Piercing bonus if at rank 9+) with duration 0 since it immediately disappears.
		this.targetedPreAttackBonus = new Condition("CHARGE!: Targeted Pre-Attack Bonus", 0);
		this.targetedPreAttackBonus.setSource(this.owner);
		this.targetedPreAttackBonus.addStatusEffect(critBonus);
		if (this.rank() >= 9) {
			this.targetedPreAttackBonus.addStatusEffect(apBonus);
		}
	}
	
	// Get methods for each variable
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	public double getTargetedScaler() {
		return this.targetedScaler;
	}
	
	public double getAOEScaler() {
		return this.AOEScaler;
	}
	
	public int getSpeedPercentage() {
		return this.speedPercentage;
	}
	
	public Condition getTargetedPreAttackBonus() {
		return this.targetedPreAttackBonus;
	}
}
