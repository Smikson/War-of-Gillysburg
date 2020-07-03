package WyattWitemeyer.WarOfGillysburg;

public class FlipStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private int noMissChance;
	private Condition preAttackBonus;
	
	// Constructor
	public FlipStrike(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Flip Strike\"", source, rank);
		this.owner = source;
		
		// Calculate and set the damage scaler
		this.setCooldown();
		this.setScaler();
		
		// Calculate and set the chance to not miss and the pre-attack Condition
		this.setNoMissChance();
		this.setPreAttackBonus();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 5
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		// The Ability always starts off Cooldown
		this.turnCount = this.cooldown;
	}
	
	// Calculates and sets the damage scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = 1.5;
		
		// Set the scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.scaler = 1.5;
				break;
			case 2:
				this.scaler = 1.7;
				break;
			case 3:
				this.scaler = 1.8;
				break;
			case 4:
				this.scaler = 2;
				break;
			case 5:
				this.scaler = 2.2;
				break;
			case 6:
				this.scaler = 2.3;
				break;
			case 7:
				this.scaler = 2.5;
				break;
			case 8:
				this.scaler = 2.7;
				break;
			case 9:
				this.scaler = 2.8;
				break;
			case 10:
				this.scaler = 3;
				break;
		}
	}
	
	// Calculates and sets the additional effects
	private void setNoMissChance() {
		// Default value is 0
		this.noMissChance = 0;
		
		// Rank 7 increases the chance to 15%
		if (this.rank() >= 7) {
			this.noMissChance = 15;
		}
		// Rank 9 increases the chance to 20%
		if (this.rank() >= 9) {
			this.noMissChance = 20;
		}
		// Rank 10 increases the chance to 25%
		if (this.rank() == 10) {
			this.noMissChance = 25;
		}
	}
	
	private void setPreAttackBonus() {
		// Creates the StatusEffect (is always 15%)
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, 15, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		
		// Creates the Condition
		this.preAttackBonus = new Condition("Flip Strike: Pre-Attack Bonus", 0);
		this.preAttackBonus.setSource(this.owner);
		this.preAttackBonus.addStatusEffect(apBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public int getNoMissChance() {
		return this.noMissChance;
	}
	
	public Condition getPreAttackBonus() {
		return this.preAttackBonus;
	}
}
