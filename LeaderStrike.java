package WyattWitemeyer.WarOfGillysburg;

public class LeaderStrike extends Ability {
	// Additional variables for the Ability
	private StatusEffect accuracyBonus;
	private StatusEffect damageBonus;
	private double healingScaler;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Leader Strike misses (for bonus effects)
	public int numMisses;
	
	public LeaderStrike(int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.name = "Ability 4: \"Leader Strike\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Set the additional effects of the Ability
		this.setAccuracyBonus();
		this.setDamageBonus();
		this.setHealingScaler();
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 5;
		if (this.rank >= 7) {
			this.cooldown = 4;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Rank 2 grants +.05 to scaler
			if (walker == 2) {
				this.scaler += .05;
			}
			// Ranks 4,8 grant +.1 to scaler
			else if (walker == 4 || walker == 8) {
				this.scaler += .1;
			}
			// Rank 6,9 grant +.15 to scaler
			else if (walker == 6 || walker == 9) {
				this.scaler += .15;
			}
			// Rank 10 grants +.25 to scaler
			else if (walker == 10) {
				this.scaler += .25;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setAccuracyBonus() {
		int amount = 0;
		if (this.ssRank >= 15) {
			amount = 40;
		}
		this.accuracyBonus = new StatusEffect("Shield Skills Accuracy Bonus to Leader Strike", 0, Stat.ACCURACY, amount);
	}
	
	private void setDamageBonus() {
		// At rank 1, this damage bonus starts at 20% and lasts 1 turn
		int amount = 20;
		int duration = 1;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,8 grants +2% increased damage
			if (walker == 2 || walker == 8) {
				amount += 2;
			}
			// Ranks 4,6,9 grant +3% increased damage
			else if (walker == 4 || walker == 6 || walker == 9) {
				amount += 3;
			}
			// Rank 10 grants +7% increased damage
			else if (walker == 10) {
				this.scaler += 7;
			}
			
			// Rank 3 increases the duration to 2
			if (walker == 3) {
				duration++;
			}
		}
		this.damageBonus = new StatusEffect("Leader Strike Damage Bonus", duration, Stat.DAMAGE, amount);
		this.damageBonus.makeEndOfTurn();
	}
	
	private void setHealingScaler() {
		// At rank 1, this healing scaler starts at .05
		this.healingScaler = .05;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,8 grants +1% to scaler
			if (walker == 2 || walker == 8) {
				this.scaler += .01;
			}
			// Ranks 4,9 grant +1.5% to scaler
			else if (walker == 4 || walker == 9) {
				this.scaler += .015;
			}
			// Ranks 6,10 grant +2.5% to scaler
			else if (walker == 6 || walker == 10) {
				this.scaler += .025;
			}
		}
	}
	
	// Get methods for additional variables for this Ability
	public StatusEffect getAccuracyBonus() {
		// The Accuracy Bonus is based on the current number of misses (it's value is multiplied by the number of misses)
		return new StatusEffect(this.accuracyBonus.getName(), 
				this.accuracyBonus.duration(), 
				this.accuracyBonus.getAlteredStat(), 
				this.accuracyBonus.getValue() * this.numMisses);            // This is the only line that is changed.
	}
	public StatusEffect getDamageBonus() {
		return this.damageBonus;
	}
	public double getHealingScaler() {
		return this.healingScaler;
	}
	
	// At certain ranks, the next attack of each ally has a chance to stun, this returns true if it does
	public boolean willStun() {
		// 0% chance until rank 5, which gives a 10% chance
		int chance = 0;
		if (this.rank >= 5) {
			chance += 10;
		}
		for (int walker = 6; walker < this.rank; walker++) {
			// Rank 6 increases stun chance by 3
			if (walker == 6) {
				chance += 3;
			}
			// Rank 7 increases stun chance by 2
			else if (walker == 7) {
				chance += 2;
			}
			// Ranks 8,9 increase stun chance by 5
			else if (walker == 8 || walker == 9) {
				chance += 5;
			}
			// Rank 10 increases stun chance by 15
			else if (walker == 10) {
				chance += 15;
			}
		}
		
		// Returns true if the chance succeeded, false otherwise.
		Dice percent = new Dice(100);
		if (percent.roll() <= chance) {
			return true;
		}
		return false;
	}
}
