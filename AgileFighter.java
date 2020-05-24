package WyattWitemeyer.WarOfGillysburg;

public class AgileFighter extends Ability {
	// Additional variables
	private Condition basePreAttackBonus;
	private Condition abilityPreAttackBonus;
	private Condition baseBlockBonus;
	private Condition abilityBlockBonus;
	
	// Constructor
	public AgileFighter(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Agile Fighter\"";
		this.rank = rank;
		
		// Set the healing scaler of the ability
		this.setScaler();
		
		// Calculate and set the additional Condition effects
		this.setBasePreAttackBonus();
		this.setAbilityPreAttackBonus();
		this.setBaseBlockBonus();
		this.setAbilityBlockBonus();
	}
	
	// Calculates the healing scaler
	private void setScaler() {
		// At rank 0, the healing scaler is at 1%
		this.scaler = .01;
		
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +.1% to the scaler
			if (walker <= 5) {
				this.scaler += .001;
			}
			// Ranks 6-10 grant +.15% to the scaler
			else if (walker <= 10) {
				this.scaler += .0015;
			}
			// Rank 15 grants +.25% to the scaler (no addition in ranks 11-14)
			else if (walker == 15) {
				this.scaler += .0025;
			}
		}
	}
	
	// Calculates and creates the additional Condition effects
	// Calculate the base accuracy and ability accuracy and crit bonus amounts so other calculations are easier
	private int calcBaseAccAmt() {
		// At rank 0, the amount is technically 10%
		int amount = 10;
		
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +1% accuracy
			if (walker <= 5) {
				amount += 1;
			}
			// Ranks 6-10 grant +2% accuracy
			else if (walker <= 10) {
				amount += 2;
			}
			// Ranks 11-15 grant +5% accuracy
			else if (walker <= 15) {
				amount += 5;
			}
		}
		
		return amount;
	}
	private int calcAbilityAccAmt() {
		// Amount starts at the base amount, but increases at different ranks starting at rank 6
		int amount = this.calcBaseAccAmt();
		for (int walker = 6; walker <= this.rank; walker++) {
			// Ranks 6-10 grant +3% bonus accuracy
			if (walker <= 10) {
				amount += 3;
			}
			// Rank 15 grants +5% bonus accuracy (no addition in ranks 11-14)
			else if (walker == 15) {
				amount += 5;
			}
		}
		
		return amount;
	}
	private int calcCritAmount() {
		// Amount for crit is the same for each version, so this calculates it so we don't code it twice. Starts at rank 11
		int amount = 0;
		for (int walker = 11; walker <= this.rank; walker++) {
			// Ranks 11-14 grant +5% crit
			if (walker <= 14) {
				amount += 5;
			}
			// Rank 15 grants +10% crit
			else if (walker == 15) {
				amount += 10;
			}
		}
		
		return amount;
	}
	
	// Makes each Condition
	private void setBasePreAttackBonus() {
		// Declare the starting amounts
		int accAmount = this.calcBaseAccAmt();
		int critAmount = this.calcCritAmount();
		
		// Create the Accuracy and Crit status effects
		StatusEffect accBonus = new StatusEffect(StatVersion.ACCURACY, accAmount, StatusEffectType.OUTGOING);
		accBonus.makePercentage();
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, critAmount, StatusEffectType.OUTGOING);
		critBonus.makeFlat();
		
		// Create the Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.basePreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.basePreAttackBonus.setSource(this.owner);
		this.basePreAttackBonus.addStatusEffect(accBonus);
		if (this.rank >= 11) {
			this.basePreAttackBonus.addStatusEffect(critBonus);
		}
	}
	
	private void setAbilityPreAttackBonus() {
		// Declare the starting amounts
		int accAmount = this.calcAbilityAccAmt();
		int critAmount = this.calcCritAmount();
		
		// Create the Accuracy and Crit status effects
		StatusEffect accBonus = new StatusEffect(StatVersion.ACCURACY, accAmount, StatusEffectType.OUTGOING);
		accBonus.makePercentage();
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, critAmount, StatusEffectType.OUTGOING);
		critBonus.makeFlat();
		
		// Create the Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.abilityPreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(accBonus);
		if (this.rank >= 11) {
			this.abilityPreAttackBonus.addStatusEffect(critBonus);
		}
	}
	
	private void setBaseBlockBonus() {
		// Declare the starting amount (Block amount is half the respective Accuracy amount)
		int blkAmount = this.calcBaseAccAmt()/2;
		
		// Create the Status Effect
		StatusEffect blkBonus = new StatusEffect(StatVersion.BLOCK, blkAmount, StatusEffectType.OUTGOING);
		blkBonus.makePercentage();
		
		// Create the Condition with duration of 1
		this.baseBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.baseBlockBonus.setSource(this.owner);
		this.baseBlockBonus.addStatusEffect(blkBonus);
	}
	
	private void setAbilityBlockBonus() {
		// Declare the starting amount (Block amount is half the respective Accuracy amount)
		int blkAmount = this.calcAbilityAccAmt()/2;
		
		// Create the Status Effect
		StatusEffect blkBonus = new StatusEffect(StatVersion.BLOCK, blkAmount, StatusEffectType.OUTGOING);
		blkBonus.makePercentage();
		
		// Create the Condition with duration of 1
		this.abilityBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.abilityBlockBonus.setSource(this.owner);
		this.abilityBlockBonus.addStatusEffect(blkBonus);
	}
	
	// Get methods for the conditions
	public Condition getBasePreAttackBonus() {
		return this.basePreAttackBonus;
	}
	
	public Condition getAbilityPreAttackBonus() {
		return this.abilityPreAttackBonus;
	}
	
	public Condition getBaseBlockBonus() {
		return this.baseBlockBonus;
	}
	
	public Condition getAbilityBlockBonus() {
		return this.abilityBlockBonus;
	}
}
