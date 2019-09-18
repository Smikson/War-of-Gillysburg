package WyattWitemeyer.WarOfGillysburg;

public class ShieldBash extends Ability {
	// Additional Conditions for the Ability
	private Stun stun;
	private StatusEffect accuracyReduction;
	private StatusEffect damageBonus;
	private StatusEffect accuracyBonus;
	private StatusEffect critBonus;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Shield Bash misses, if a previous attack has been blocked, and if this Ability critically hit (for bonus effects)
	public int numMisses;
	public boolean didBlock;
	public boolean didCrit;
	
	
	public ShieldBash(int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
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
		this.setAccuracyReduction();
		this.setDamageBonus();
		this.setAccuracyBonus();
		this.setCritBonus();
		
		// Adds the appropriate linked Conditions to the additional effects
		this.stun.addLinkedCondition(this.damageBonus);
		this.damageBonus.addLinkedCondition(this.stun);
	}
	public ShieldBash(int rank) {
		this(rank, 0);
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
		// Creates the Conditions for the Ability	
		int stunDuration = 1;
		
		// Checks based on this Ability's rank
		// Rank 10 increases the duration to 2
		if (this.rank >= 10) {
			stunDuration = 2;
		}
		
		// Bonuses for "Shield Skills" rank
		// Rank 15 grants an additional stun turn
		if (this.ssRank >= 15) {
			stunDuration++;
		}
		
		// Sets the calculated Stun
		this.stun = new Stun("Shield Bash Stun", stunDuration);
	}
	
	private void setAccuracyReduction() {
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
		
		// Sets the Calculated Status Effect
		this.accuracyReduction = new StatusEffect("Shield Bash Accuracy Reduction", 1, Stat.ACCURACY, -amount);
	}
	
	private void setDamageBonus() {
		int amount = 0;
		// Checks based on this Ability's rank
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
		
		// Sets the Calculated Status Effect
		this.damageBonus = new StatusEffect("Shield Bash Damage Aplification", this.stun.duration(), Stat.DAMAGE, amount);
		this.damageBonus.makeIncoming();
	}
	
	private void setAccuracyBonus() {
		// Bonuses for "Shield Skills" rank
		int amount = 0;
		
		// Base of 10%, increases for each rank
		if (this.ssRank >= 1) {
			amount += 10;
		}
		for (int walker = 1; walker <= this.ssRank; walker++) {
			// Ranks 1-5 grant +1% Accuracy (starting at 10%)
			if (walker <= 5) {
				amount += 1;
			}
			// Ranks 6-10 grant +2% Accuracy
			else if (walker <= 10) {
				amount += 2;
			}
			// Ranks 11-15 grant +3% Accuracy
			else if (walker <= 15) {
				amount += 3;
			}
		}
		
		// Sets the Calculated Status Effect
		this.accuracyBonus = new StatusEffect("Shield Skills Accuracy Bonus to Shield Bash", 0, Stat.ACCURACY, amount);
	}
	
	private void setCritBonus() {
		// Bonuses for "Shield Skills" rank
		int amount = 0;
		for (int walker = 6; walker <= this.ssRank; walker++) {
			// Ranks 6-10 grant +2% Critical Chance
			if (walker <= 10) {
				amount += 2;
			}
			// Ranks 11-15 grant +3% Critical Chance
			else if (walker <= 15) {
				amount += 3;
			}
		}
		
		// Sets the calculated Status Effect
		this.critBonus = new StatusEffect("Shield Skills Crit Bonus to Shield Bash", 0, Stat.CRITICAL_CHANCE, amount);
	}
	
	
	// Get methods for additional effects
	public Stun getStunEffect() {
		// The stun lasts an additional turn at rank 10 if this ability critically hit (must be calculated after constructing)
		if (this.rank >= 10 && this.didCrit) {
			return new Stun(this.stun.getName(), this.stun.duration() + 1);
		}
		return this.stun;
	}
	
	public StatusEffect getAccuracyReduction() {
		// The value of the accuracy reduction is doubled at rank 10 if the ability critically hit (must be calculated after constructing)
		if (this.rank >= 10 && this.didCrit) {
			return new StatusEffect(this.accuracyReduction.getName(),
					this.accuracyReduction.duration(),
					this.accuracyReduction.getAlteredStat(),
					this.accuracyReduction.getValue() * 2);
		}
		return this.accuracyReduction;
	}
	// Above rank 8, and additional turn of the Accuracy Reduction occurs at varying strength, this is simulated with a second consecutive condition
	public StatusEffect getSecondAccuracyReduction() {
		// Initializes default value to 0 (will have no effect until rank 8)
		double value = 0;
		if (this.rank >= 8) {
			value = this.getAccuracyReduction().getValue() / 2;
		}
		if (this.rank == 10) {
			value = this.getAccuracyReduction().getValue();
		}
		
		// Returns the simulated Status Effect to be added consecutively (the two are also linked)
		StatusEffect secondAccuracyReduction = new StatusEffect(this.accuracyReduction.getName(), 1, this.accuracyReduction.getAlteredStat(), value);
		secondAccuracyReduction.addLinkedCondition(this.accuracyReduction);
		this.accuracyReduction.addLinkedCondition(secondAccuracyReduction);
		return secondAccuracyReduction;
	}
	
	public StatusEffect getDamageBonus() {
		// The value of the damage bonus is doubled at rank 10 if the ability critically hit (must be calculated after constructing)
		if (this.rank >= 10 && this.didCrit) {
			return new StatusEffect(this.damageBonus.getName(),
					this.damageBonus.duration(),
					this.damageBonus.getAlteredStat(),
					this.damageBonus.getValue() * 2);
		}
		return this.damageBonus;
	}
	
	public StatusEffect getAccuracyBonus() {
		// The Accuracy Bonus is based on the current number of misses (it's value is multiplied by the number of misses)
		return new StatusEffect(this.accuracyBonus.getName(), 
				this.accuracyBonus.duration(), 
				this.accuracyBonus.getAlteredStat(), 
				this.accuracyBonus.getValue() * this.numMisses);            // This is the only line that is changed.
	}
	
	public StatusEffect getCritBonus() {
		return this.critBonus;
	}
}
