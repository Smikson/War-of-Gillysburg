package WyattWitemeyer.WarOfGillysburg;

public class AgileFighter extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables
	private double maxHealthScaler;
	private double missHealthScaler;
	
	private int speedBonus;
	
	private Condition basePreAttackBonus;
	private Condition abilityPreAttackBonus;
	private Condition baseBlockBonus;
	private Condition abilityBlockBonus;
	
	// Constructor
	public AgileFighter(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Agile Fighter\"", source, rank);
		this.owner = source;
		
		// Set the healing scalers of the ability, default scaler will be the max health scaler
		this.setHealingScalers();
		this.scaler = this.maxHealthScaler;
		
		// Calculate and set the additional Speed and Condition effects
		this.setSpeedBonus();
		this.setBonuses();
	}
	// An additional constructor used to only calculate the bonus stats before a SteelLegionWarrior is created
	public AgileFighter(int rank) {
		super("No Owner: Agile Fighter", Character.EMPTY, rank);
		
		this.setSpeedBonus();
	}
	
	// Calculates the healing scalers
	private void setHealingScalers() {
		// Initialize both healing scalers to 0
		this.maxHealthScaler = 0;
		this.missHealthScaler = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1, the max health scaler is at 1%, and the missing health scaler is not yet active
			if (walker == 1) {
				this.maxHealthScaler += .01;
			}
			// Ranks 2-4 grant +.1% to the max health scaler, and the missing health scaler is not yet active
			else if (walker <= 4) {
				this.maxHealthScaler += .001;
			}
			// Rank 5 grants +.2% to the max health scaler, and the missing health scaler activates starting at 1%
			else if (walker == 5) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .01;
			}
			// Ranks 6-9 grant +.15% to the max health scaler, and +.1% to the missing health scaler
			else if (walker <= 9) {
				this.maxHealthScaler += .0015;
				this.missHealthScaler += .001;
			}
			// Rank 10 grants +.2% to the max health scaler, and +.1% to the missing health scaler
			else if (walker == 10) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .001;
			}
			// Ranks 11-14 grant +.2% to the missing health scaler (max unaffected)
			else if (walker <= 14) {
				this.missHealthScaler += .002;
			}
			// Rank 15 grants +.2% to the max health scaler, and +.2% to the missing health scaler
			else if (walker == 15) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .002;
			}
		}
	}
	
	// Calculates and sets the amount of bonus base Speed the Ability gives
	private void setSpeedBonus() {
		// Initialize the bonus to 0
		this.speedBonus = 0;
		
		// At rank 5 the Ability grants +1 Speed
		if (this.rank() >= 5) {
			this.speedBonus += 1;
		}
		// Rank 10 grants +2 more Speed
		if (this.rank() >= 10) {
			this.speedBonus += 2;
		}
		// Rank 15 grants +3 more Speed
		if (this.rank() >= 15) {
			this.speedBonus += 3;
		}
	}
	
	// Calculates and creates the additional Condition effects
	// Calculate the base accuracy and crit bonus amounts so other calculations are easier
	private double calcBaseAccAmt() {
		// Initialize the amount to 0
		double amount = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1, starts at 10%
			if (walker == 1) {
				amount = 10;
			}
			// Ranks 2-4 grant +1% Accuracy
			else if (walker <= 4) {
				amount += 1;
			}
			// Ranks 5-10 grant +2% Accuracy
			else if (walker <= 10) {
				amount += 2;
			}
			// Ranks 11-15 grant +5% Accuracy
			else if (walker <= 15) {
				amount += 5;
			}
		}
		
		return amount;
	}
	private double calcCritAmount() {
		// Amount for crit is the same for each version, so this calculates it so we don't code it twice. Starts at rank 11
		double amount = 0;
		for (int walker = 11; walker <= this.rank(); walker++) {
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
	private void setBonuses() {
		// Declare the starting amounts
		double accAmount = this.calcBaseAccAmt();
		double blkAmount = accAmount/2.0;
		double critAmount = this.calcCritAmount();
		
		// The amounts are increased if an Ability was used, though this requires an additional condition
		double abilityAccAmt = accAmount;
		double abilityBlkAmt = blkAmount;
		if (this.rank() >= 10) {
			// Rank 15 grants +50% effectiveness
			if (this.rank() == 15) {
				abilityAccAmt *= 1.5;
				abilityBlkAmt *= 1.5;
			}
			// At ranks 10-14 it grants +30% effectiveness
			else {
				abilityAccAmt *= 1.3;
				abilityBlkAmt *= 1.3;
			}
		}
		
		// Create the Status Effects
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, accAmount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		StatusEffect abilityAccBonus = new StatusEffect(Stat.Version.ACCURACY, abilityAccAmt, StatusEffect.Type.OUTGOING);
		abilityAccBonus.makePercentage();
		StatusEffect blkBonus = new StatusEffect(Stat.Version.BLOCK, blkAmount, StatusEffect.Type.OUTGOING);
		blkBonus.makePercentage();
		StatusEffect abilityBlkBonus = new StatusEffect(Stat.Version.BLOCK, abilityBlkAmt, StatusEffect.Type.OUTGOING);
		abilityBlkBonus.makePercentage();
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Create all the Conditions
		// Create the Base Pre Attack Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.basePreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.basePreAttackBonus.setSource(this.owner);
		this.basePreAttackBonus.addStatusEffect(accBonus);
		if (this.rank() >= 11) {
			this.basePreAttackBonus.addStatusEffect(critBonus);
		}
		
		// Create the Ability Pre Attack Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.abilityPreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(abilityAccBonus);
		if (this.rank() >= 11) {
			this.abilityPreAttackBonus.addStatusEffect(critBonus);
		}
		
		// Create the Base Block Condition with duration of 1
		this.baseBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.baseBlockBonus.setSource(this.owner);
		this.baseBlockBonus.addStatusEffect(blkBonus);
		
		// Create the Ability Block Condition with duration of 1
		this.abilityBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.abilityBlockBonus.setSource(this.owner);
		this.abilityBlockBonus.addStatusEffect(abilityBlkBonus);
	}
	
	// Get methods for the Ability
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public double getMaxHealthScaler() {
		return this.maxHealthScaler;
	}
	
	public double getMissingHealthScaler() {
		return this.missHealthScaler;
	}
	
	public int getSpeedBonus() {
		return this.speedBonus;
	}
	
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
	
	// Applies the pre-attack bonus when prompted, and heals the respective amount
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// If the owner is the attacker
		if (this.getOwner().equals(atk.getAttacker())) {
			// Prompt if the Ability should activate
			System.out.println("Did " + this.getOwner().getName() + " move and attack?");
			
			// If not, we are done
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
			
			// Otherwise, above rank 10 prompt for Ability usage to apply the correct pre-attack bonus
			if (this.rank() >= 10) {
				// If an Ability is used apply Ability version
				System.out.println("Was an Ability used?");
				if (BattleSimulator.getInstance().askYorN()) {
					atk.addAttackerCondition(this.getAbilityPreAttackBonus());
					this.getOwner().addCondition(this.getAbilityBlockBonus());
				}
				// If no Ability used, apply base
				else {
					atk.addAttackerCondition(this.getBasePreAttackBonus());
					this.getOwner().addCondition(this.getBaseBlockBonus());
				}
			}
			// Apply base before rank 10
			else {
				atk.addAttackerCondition(this.getBasePreAttackBonus());
				this.getOwner().addCondition(this.getBaseBlockBonus());
			}
			
			// Then, heal the correct amount as prompted
			System.out.println("Enter the number of spaces travelled in each area for healing:");
			System.out.print("Ability: ");
			double abilitySpaces = BattleSimulator.getInstance().promptDouble();
			System.out.print("Regular: ");
			double regularSpaces = BattleSimulator.getInstance().promptDouble();
			System.out.println();
			
			// Restore health to the owner, first based on missing health if at least rank 5
			if (this.rank() >= 5) {
				this.getOwner().restoreHealth((int)Math.round(abilitySpaces * this.getMissingHealthScaler() * this.getOwner().getMissingHealth()));
				this.getOwner().restoreHealth((int)Math.round(regularSpaces * this.getMissingHealthScaler() / 2.0 * this.getOwner().getMissingHealth()));
			}
			// Then based on maximum health at all ranks
			this.getOwner().restoreHealth((int)Math.round(abilitySpaces * this.getMaxHealthScaler() * this.getOwner().getHealth()));
			this.getOwner().restoreHealth((int)Math.round(regularSpaces * this.getMaxHealthScaler() / 2.0 * this.getOwner().getHealth()));
		}
	}
}
