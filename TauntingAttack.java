package WyattWitemeyer.WarOfGillysburg;

public class TauntingAttack extends Ability {
	// Additional Conditions for the Ability
	private Condition selfPreAttackBonus;
	private Condition taunt;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Taunting Attack misses (for bonus effects)
	public int numMisses;
	
	public TauntingAttack(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 3: \"Taunting Attack\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Set the additional effects of the Ability
		this.setPreAttackBonus();
		this.setTauntEffect();
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
		// At rank 1, this scaler starts at 1.0
		this.scaler = 1.0;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 3,4,5,10 grants +.1 to scaler
			if (walker == 3 || walker == 4 || walker == 5 || walker == 10) {
				this.scaler += .1;
			}
			// Ranks 2,6,8,9 grant +.2 to scaler
			else if (walker == 2 || walker == 6 || walker == 8 || walker == 9) {
				this.scaler += .2;
			}
			// Rank 7 grant +.3 to scaler
			else if (walker == 7) {
				this.scaler += .3;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setPreAttackBonus() {
		int amount = 0;
		if (this.ssRank >= 15) {
			amount = 40;
		}
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, amount*this.numMisses, StatusEffectType.OUTGOING);
		
		// Creates the pre-attack condition with accuracy bonus as needed
		this.selfPreAttackBonus = new Condition("Taunting Attack: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
	}
	
	private void setTauntEffect() {
		int duration = 1;
		if (this.rank >= 10) {
			duration = 2;
		}
		this.taunt = new Condition("Taunting Attack: Taunted", duration);
	}
	
	// Get methods for additional effects of this Ability
	public Condition getTauntEffectHit() {
		return this.taunt;
	}
	
	public Condition getTauntEffectMiss(Character enemy) {
		// If the Ability misses, there is still a chance (based on rank) that a Taunt effect occurs (returns a Condition with duration = 0 if it does not occur)
		int basicChance = 0;  // For Normal and Advanced Enemies (Level = 1,2)
		int eliteChance = 0;  // For Elite Enemies (Level = 3)
		int bossChance = 0;   // For Boss Enemies (Level = 4)
		
		// At rank 3, there is a chance to still Taunt basic (Normal/Advanced) and Elite enemies
		if (this.rank >= 3) {
			basicChance = 25;
			eliteChance = 10;
		}
		// This then extends at various ranks
		for (int walker = 4; walker <= this.rank; walker++) {
			// First, basic chance increases:
			// Ranks 4-9 except 5 grant +5% chance for basic enemies
			if (walker <= 9 && walker != 5) {
				basicChance += 5;
			}
			// Rank 10 grants +25% chance for basic enemies
			else if (walker == 10) {
				basicChance += 25;
			}
			
			// Second, elite and boss chance increases (only rank 10 differs):
			// Ranks 4-7 except 5 grant +5% chance for elite and boss enemies
			if (walker <= 7 && walker !=5) {
				eliteChance += 5;
				bossChance += 5;
			}
			// Rank 10 grants +25% chance for elite enemies and +10% for boss enemies
			else if(walker == 10) {
				eliteChance += 25;
				bossChance += 10;
			}
		}
		
		// Creates a percent die to determine if the taunt effect happens (will have duration of 1 rather than 0)
		Dice percent = new Dice(100);
		int result = percent.roll();
		int tauntDuration = 0;
		// If basic enemy (Level = 1 or 2)
		if (enemy.getLevel() == 1 || enemy.getLevel() == 2) {
			if (result <= basicChance) {
				tauntDuration = 1;
			}
		}
		// If Elite enemy (Level = 3)
		else if (enemy.getLevel() == 3) {
			if (result <= eliteChance) {
				tauntDuration = 1;
			}
		}
		// If Boss enemy (Level = 4)
		else if (enemy.getLevel() == 4) {
			if (result <= bossChance) {
				tauntDuration = 1;
			}
		}
		
		// Return the result
		return new Condition(this.taunt.getName(), tauntDuration);
	}
	
	public Condition getPreAttackBonus() {
		// Returns the condition, but sets it again first in case things changed because of "numMisses"
		this.setPreAttackBonus();
		return this.selfPreAttackBonus;
	}
}
