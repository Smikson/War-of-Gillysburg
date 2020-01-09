package WyattWitemeyer.WarOfGillysburg;

public class ShieldBash extends Ability {
	// Additional Conditions for the Ability
	private Stun stun;
	private Condition enemyAccReduction;
	private Condition selfPreAttackBonus;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Shield Bash misses, if a previous attack has been blocked, and if this Ability critically hit (for bonus effects)
	public int numMisses;
	public boolean didBlock;
	public boolean didCrit;
	
	
	public ShieldBash(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 1: \"Shield Bash\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		this.didBlock = false;
		this.didCrit = false;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets the additional effects of the Ability
		this.setStun();
		this.setEnemyAccReduction();
		this.setPreAttackBonus();
	}
	public ShieldBash(int rank) {
		this(Character.EMPTY, rank, 0);
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 4;
		if (this.rank >= 5) {
			this.cooldown = 3;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// Checks based on this Ability's rank
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,7 grants +.1 to scaler
			if (walker == 2 || walker == 7) {
				this.scaler += .1;
			}
			// Ranks 3,4,6,8,9 grant +.2 to scaler
			else if (walker == 3 || walker == 4 || walker == 6 || walker == 8 || walker == 9) {
				this.scaler += .2;
			}
			// Ranks 5,10 grant +.3 to scaler
			else if (walker == 5 || walker == 10) {
				this.scaler += .3;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 6; walker < this.ssRank; walker++) {
			// Ranks 6-10 grant +.2x damage to scaler if an attack was blocked
			if (walker <= 10 && this.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setStun() {
		int stunDuration = 1;
		
		// Checks based on this Ability's rank
		// Rank 10 increases the duration to 2, then is increased by an additional turn if the Ability didCrit
		if (this.rank >= 10) {
			stunDuration = 2;
			if (this.didCrit) {
				stunDuration++;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		// Rank 15 grants an additional stun turn
		if (this.ssRank >= 15) {
			stunDuration++;
		}
		
		// Sets the calculated Stun
		this.stun = new Stun("Shield Bash Stun", stunDuration);
		this.stun.setSource(this.owner);
		
		// Adds Damage Bonus While Stunned after rank 5
		if (this.rank >= 5) {
			int amount = 0;
			for (int walker = 1; walker <= this.rank; walker++) {
				// Rank 5 grants +20% damage to the stunned target
				if (walker == 5) {
					amount += 20;
				}
				// Ranks 6,10 grants +5% damage to the stunned target
				if (walker == 6 || walker == 10) {
					amount += 5;
				}
			}
			// the value "amount" doubles when you critically strike at rank 10
			if (this.rank >= 10 && this.didCrit) {
				amount *= 2;
			}
			
			// Sets the Calculated Status Effect and adds it to the stun
			StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.INCOMING);
			damageBonus.makeAffectOther();
			this.stun.addStatusEffect(damageBonus);
		}
	}
	
	private void setEnemyAccReduction() {
		int amount = 0;
		// Checks based on this Ability's rank
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 3,7 grant +10% Accuracy Reduction
			if (walker == 3 || walker == 7) {
				amount += 10;
			}
			// Ranks 4,6 grant +5% Accuracy Reduction
			if (walker == 4 || walker == 6) {
				amount += 5;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 11; walker <= this.ssRank; walker++) {
			// Ranks 11-15 grant +2% extra accuracy deduction if an attack was blocked
			if (walker <= 15 && this.didBlock) {
				amount += 2;
			}
			// Rank 15 grants an additional 5% accuracy deduction on top of that (total of a bonus 7% from rank 15) if an attack was blocked
			if (walker == 15 && this.didBlock) {
				amount += 5;
			}
		}
		
		// the value "amount" doubles when you critically strike at rank 10
		if (this.rank >= 10 && this.didCrit) {
			amount *= 2;
		}
		
		// Creates the requirement of the Condition to become Active (occurs after stun)
		Requirement actReq = (Character withEffect) -> {
			return !withEffect.getAllConditions().contains(this.stun);
		};
		
		// Finds the duration: 2 when >= 8
		int duration = this.rank < 8? 1 : 2;
		
		// Create the Enemy Accuracy Reduction Condition with the correct Status Effect based on the numbers calculated
		this.enemyAccReduction = new Condition("Shield Bash: Accuracy Reduction", duration, actReq);
		this.enemyAccReduction.setSource(this.owner);
		// Sets the Calculated Status Effect
		StatusEffect accReduction = new StatusEffect(StatVersion.ACCURACY, -amount, StatusEffectType.OUTGOING);
		this.enemyAccReduction.addStatusEffect(accReduction);
	}
	
	private void setPreAttackBonus() {
		// Accuracy Bonus:
		// Bonuses for "Shield Skills" rank
		int accAmount = 0;
		
		// Base of 10%, increases for each rank
		if (this.ssRank >= 1) {
			accAmount += 10;
		}
		for (int walker = 1; walker <= this.ssRank; walker++) {
			// Ranks 1-5 grant +1% Accuracy (starting at 10%)
			if (walker <= 5) {
				accAmount += 1;
			}
			// Ranks 6-10 grant +2% Accuracy
			else if (walker <= 10) {
				accAmount += 2;
			}
			// Ranks 11-15 grant +3% Accuracy
			else if (walker <= 15) {
				accAmount += 3;
			}
		}
		// Creates the Calculated Status Effect based on the number of misses
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, accAmount*this.numMisses, StatusEffectType.OUTGOING);
		
		// Crit Bonus:
		// Bonuses for "Shield Skills" rank
		int critAmount = 0;
		for (int walker = 6; walker <= this.ssRank; walker++) {
			// Ranks 6-10 grant +2% Critical Chance
			if (walker <= 10) {
				critAmount += 2;
			}
			// Ranks 11-15 grant +3% Critical Chance
			else if (walker <= 15) {
				critAmount += 3;
			}
		}
		// Creates the calculated Status Effect
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, critAmount, StatusEffectType.OUTGOING);
		
		// Creates the pre-attack condition with accuracy and crit bonuses as needed
		this.selfPreAttackBonus = new Condition("Shield Bash: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
		this.selfPreAttackBonus.addStatusEffect(critBonus);
	}
	
	
	// Get methods for additional effects
	public Stun getStunEffect() {
		// Returns the stun, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setStun();
		return this.stun;
	}
	
	public Condition getEnemyAccuracyReduction() {
		// Returns the condition, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setEnemyAccReduction();
		return this.enemyAccReduction;
	}
	
	public Condition getSelfPreAttackBonus() {
		// Returns the condition, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setPreAttackBonus();
		return this.selfPreAttackBonus;
	}
	
	@Override
	public double getScaler() {
		// Returns the scaler, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setScaler();
		return this.scaler;
	}
}
