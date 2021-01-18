package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Base Passive Ability: "Survivable"
public class Survivable extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private int bonusHealthStat;
	private int bonusArmorStat;
	private int bonusDodgeStat;
	
	private double blockChance;
	private double healingPercentage;
	private Condition nextAttackBonus;
	
	private boolean didAvoid;
	
	// Constructor
	public Survivable(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Survivable\"", source, rank);
		this.owner = source;
		this.didAvoid = false;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
		
		// Calculate the chance to block, healing amount, and Condition effectiveness
		this.setStatBlockChance();
		this.setHealingPercentage();
		this.setNextAttackBonus();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Specialist
	public Survivable(int rank) {
		super("No Owner: Survivable", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Calculates the flat stat bonuses for Damage, Armor Piercing, and Accuracy
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusHealthStat = 0;
		this.bonusArmorStat = 0;
		this.bonusDodgeStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants +40 Health and +2 Armor
			if (walker == 1) {
				this.bonusHealthStat += 40;
				this.bonusArmorStat += 2;
			}
			// Ranks 2-4 grant +75 Health, +3 Armor, and +1 Dodge per rank
			else if (walker <= 4) {
				this.bonusHealthStat += 75;
				this.bonusArmorStat += 3;
				this.bonusDodgeStat += 1;
			}
			// Rank 5 grants +150 Health, +5 Armor, and +1 Dodge
			else if (walker == 5) {
				this.bonusHealthStat += 150;
				this.bonusArmorStat += 5;
				this.bonusDodgeStat += 1;
			}
			// Ranks 6-9 grant +325 Health, +7 Armor, and +2 Dodge per rank
			else if (walker <= 9) {
				this.bonusHealthStat += 325;
				this.bonusArmorStat += 7;
				this.bonusDodgeStat += 2;
			}
			// Rank 10 grants +375 Health, +8 Armor, and +3 Dodge
			else if (walker == 10) {
				this.bonusHealthStat += 375;
				this.bonusArmorStat += 8;
				this.bonusDodgeStat += 3;
			}
			// Ranks 11-14 grant +450 Health, +10 Armor, and +3 Dodge per rank
			else if (walker <= 14) {
				this.bonusHealthStat += 450;
				this.bonusArmorStat += 10;
				this.bonusDodgeStat += 3;
			}
			// Rank 15 grants +750 Health, +15 Armor, and +5 Dodge
			else if (walker == 15) {
				this.bonusHealthStat += 750;
				this.bonusArmorStat += 15;
				this.bonusDodgeStat += 5;
			}
		}
	}
	
	// Sets the chance to block if a Dodge attempt fails
	public void setStatBlockChance() {
		// Start at zero
		this.blockChance = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants 1% block chance
			if (walker == 1) {
				this.blockChance += .01;
			}
			// Ranks 2-4 grant +0.3% block chance per rank
			else if (walker <= 4) {
				this.blockChance += .003;
			}
			// Rank 5 grants +0.6% block chance
			else if (walker == 5) {
				this.blockChance += .006;
			}
			// Ranks 6-9 grant +0.8% block chance per rank
			else if (walker <= 9) {
				this.blockChance += .008;
			}
			// Rank 10 grants +1.3% block chance
			else if (walker == 10) {
				this.blockChance += .013;
			}
			// Ranks 11-14 grant +1.5% block chance per rank
			else if (walker <= 14) {
				this.blockChance += .015;
			}
			// Rank 15 grants +2% block chance
			else if (walker == 15) {
				this.blockChance += .02;
			}
		}
	}
	
	// Sets the healing percentage for healing based on missing Health
	public void setHealingPercentage() {
		// Start at zero
		this.healingPercentage = 0;
		
		for (int walker = 10; walker <= this.rank(); walker++) {
			// Rank 10 grants +10% missing health
			if (walker == 10) {
				this.healingPercentage += 10;
			}
			// Ranks 11-14 grant +3% missing health per rank
			else if (walker <= 14) {
				this.healingPercentage += 3;
			}
			// Rank 15 grants +2% block chance
			else if (walker == 15) {
				this.healingPercentage += 8;
			}
		}
	}
	
	// Sets the Condition for the bonus that occurrs for the next attack after successfully blocking
	public void setNextAttackBonus() {
		// The amount for each status effect (damage and accuracy bonus) is equal to the healing percentage based on missing Health at all ranks
		double amount = this.getHealingPercentage();
		
		// Create the StatusEffects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Set the Condition to have the two StatusEffects, but decrement based on Charges instead of turns (permanent otherwise)
		this.nextAttackBonus = new Condition("Survivable: Next Attack Bonus", -1);
		this.nextAttackBonus.setSource(this.owner);
		this.nextAttackBonus.makeChargeBased(1);
		this.nextAttackBonus.addStatusEffect(dmgBonus);
		this.nextAttackBonus.addStatusEffect(accBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	// For Stat Bonuses
	public int getHealthBonus() {
		return this.bonusHealthStat;
	}
	
	public int getArmorBonus() {
		return this.bonusArmorStat;
	}
	
	public int getDodgeBonus() {
		return this.bonusDodgeStat;
	}
	// For other effects
	public double getBlockChance() {
		return this.blockChance;
	}
	
	public double getHealingPercentage() {
		return this.healingPercentage;
	}
	
	public Condition getNextAttackBonus() {
		return new Condition(this.nextAttackBonus);
	}
	
	// Applies the pre attack effects of the chance to block when Dodging fails
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// Resets didAvoid if need be
		this.didAvoid = false;
		
		// Only acts if the owner is the defender
		if (atk.getDefender().equals(this.getOwner())) {
			// First find the results of if the incoming attack would normally land
			boolean willHit = atk.canHit();
			if (willHit && atk.canMiss()) {
				willHit = atk.landAttack();
			}
			
			// If the attack will hit, but can miss, instead add a chance to block
			if (willHit && atk.canMiss()) {
				// Get the chance to block the attack
				double blkChance = this.getBlockChance();
				
				// Possible bonuses based off Enemy attacker
				if (atk.getAttacker() instanceof Enemy) {
					Enemy attacker = (Enemy)atk.getAttacker();
					
					// At rank 5 the chance is doubled vs Normal and Advanced enemies
					if (this.rank() >= 5 && (attacker.getDifficulty().equals(Enemy.Difficulty.NORMAL) || attacker.getDifficulty().equals(Enemy.Difficulty.ADVANCED))) {
						blkChance *= 2;
					}
					
					// At rank 15 the chance is increased by 50% vs Elite enemies
					if (this.rank() >= 15 && attacker.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
						blkChance *= 1.5;
					}
				}
				
				// Create a random probability
				Random rd = new Random();
				double prob = rd.nextDouble();
				
				// If the random probably is less than the chance to block, the attack will be blocked instead
				if (prob <= blkChance) {
					// Above rank 10, extra bonuses need to be applied in the PostAttackEffects
					if (this.rank() >= 10) {
						this.didAvoid = true;
					}
					
					// Change the attack so that is cannot hit, and state that the Armor protected the Sentinel Specialist
					atk.setCanHit(false);
					System.out.println(this.getOwner().getName() + "\'s Armor protected " + this.getOwner().getName() + " from " + atk.getAttacker().getName() + "\'s attack!");
				}
				// Otherwise, the attack must hit since the land attack has already been calculated, change the attack to represent this
				else {
					atk.makeCannotMiss();
				}
			}
			// If the attack will not hit, change the attack to reflect that, and at rank 15 the extra post-attack bonuses still occur
			else if (!willHit) {
				atk.setCanHit(false);
				if (this.rank() >= 15) {
					this.didAvoid = true;
				}
			}
		}
	}
	
	// Applies the post attack effects of the chance to block when Dodging fails
	@Override
	public void applyPostAttackEffects(AttackResult atkRes) {
		// Bonues are only applied when the owner is the defender and the local "didAvoid" is true (which as an additional check must happen after rank 10
		if (atkRes.getDefender().equals(this.getOwner()) && this.didAvoid && this.rank() >= 10) {
			// Heal the owner for the amount of missing health based on the scaler of this Ability
			this.getOwner().restoreHealth((int)Math.round(this.getOwner().getMissingHealth() * this.getHealingPercentage() / 100.0));
			
			// Add the next-attack buff to the owner
			this.getOwner().addCondition(this.getNextAttackBonus());
			
			// Reset didAvoid to false
			this.didAvoid = false;
		}
	}
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Health Stat: " + this.getHealthBonus();
		ret += "\n\tBonus Armor Stat: " + this.getArmorBonus();
		ret += this.rank() >= 2? ("\n\tBonus Dodge Stat: " + this.getDodgeBonus()) : "";
		ret += "\n\tBlock Chance: " + (this.getBlockChance() * 100.0) + "%";
		ret += "\n\tHealing Percentage (missing health): " + this.getHealingPercentage() + "%";
		ret += "\n\t" + this.getNextAttackBonus();
		return ret;
	}
}