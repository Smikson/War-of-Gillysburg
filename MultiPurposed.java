package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Multi-Purposed"
public class MultiPurposed extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private Condition abilityDamageBonus;
	//DE either here or in Character, need a set/list of the distinct Abilities used
	private double scalerBonusPerCooldown;
	private double critBonusPerCooldown;
	private int uniqueRequirement;
	
	// Constructor
	public MultiPurposed(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Multi-Purposed\"", source, rank);
		this.owner = source;
		
		// Set the bonus effects of the Ability and the unique requirement
		this.setAbilityDamageBonus();
		this.setCooldownBonuses();
		this.setUniqueRequirement();
	}
	
	// Sets the damage bonus applied to the random Ability cast when enough unique abilities have been used
	private void setAbilityDamageBonus() {
		// Initialize the amount to 0
		int amount = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants 10% damage
			if (walker == 1) {
				amount += 10;
			}
			// Ranks 2-4 grant +1% damage per rank
			else if (walker <= 4) {
				amount += 1;
			}
			// Rank 5 grants +2% damage
			else if (walker == 5) {
				amount += 2;
			}
			// Ranks 6-10 grant +3% damage per rank
			else if (walker <= 10) {
				amount += 3;
			}
			// Ranks 11-15 grant +4% damage per rank
			else if (walker <= 15) {
				amount += 4;
			}
		}
		
		// Create the StatusEffect
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		
		// Create the Condition with the StatusEffect with a duration of 0 since it is 1-time use
		this.abilityDamageBonus = new Condition("Multi-Purposed: Random Ability Damage Bonus", 0);
		this.abilityDamageBonus.setSource(this.owner);
		this.abilityDamageBonus.addStatusEffect(dmgBonus);
	}
	
	// Sets the scaler and critical chance bonuses based on number of turns in Cooldown
	private void setCooldownBonuses() {
		// Intitalize both to be 0
		this.scalerBonusPerCooldown = 0;
		this.critBonusPerCooldown = 0;
		
		// Neither starts until rank 5
		for (int walker = 5; walker <= this.rank(); walker++) {
			// Rank 5 grants +.05x to scalers (no effect yet on crit)
			if (walker == 5) {
				this.scalerBonusPerCooldown += .05;
			}
			// Ranks 6-9 grant +.01x to scalers per rank
			else if (walker <= 9) {
				this.scalerBonusPerCooldown += .01;
			}
			// Rank 10 grants +.01x to scalers and +1% crit
			else if (walker == 10) {
				this.scalerBonusPerCooldown += .01;
				this.critBonusPerCooldown += 1;
			}
			// Ranks 11-14 grant +.02x to scalers and +.5% crit per rank
			else if (walker <= 14) {
				this.scalerBonusPerCooldown += .02;
				this.critBonusPerCooldown += .5;
			}
			// Rank 15 grants +.02x to scalers and +2% crit
			else if (walker == 15) {
				this.scalerBonusPerCooldown += .02;
				this.critBonusPerCooldown += 2;
			}
		}
	}
	
	// Sets the number of unique Abilities that need to be cast to get the random basic Ability bonus
	private void setUniqueRequirement() {
		// The unique requirement is always 4 until rank 15, then it is 3
		this.uniqueRequirement = 4;
		if (this.rank() == 15) {
			this.uniqueRequirement = 3;
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public Condition getAbilityDamageBonus() {
		return new Condition(this.abilityDamageBonus);
	}
	
	public double getScalerBonus(int cdTurns) {
		return this.scalerBonusPerCooldown * cdTurns;
	}
	//DE Possibly create conditions for these two based on specifed number of Cooldown turns
	public double getCritBonus(int cdTurns) {
		return this.critBonusPerCooldown * cdTurns;
	}
	public double getAccBonus(int cdTurns) {
		return this.getCritBonus(cdTurns);
	}
	
	public int getUniqueRequirement() {
		return this.uniqueRequirement;
	}
	
	//DE Each Ability will need to call something from the base Sentinel Specialist to get the scaler bonus from here
	//DE put the crit bonus in as a Condition that updates at the beginning of each turn
	//DE Will need to somehow connect the "use" of this Ability to the "use of other Abilities and that of the Basic Attack
	
	//DE have use function (make name of this Random Ability or something) that selects random Ability (remove Basic Attack as option replace with this in main class)
	//DE have an "isActive" for when the version is the random Ability
	//DE have a single "getBuffs" function that includes the bonus damage for random Ability when "isActive" is true, all abilities should call before use (to also get Cooldown bonus etc)
	//DE or make a permanent Condition on character? All Abilities have a version 2 that is case from this Ability -- best way
	
	//DE Masterwork rank 15 -> Empowered counts as separate
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tNumber of Required Unique Abilities: " + this.getUniqueRequirement();
		ret += "\n\t" + this.getAbilityDamageBonus();
		ret += this.rank() >= 10? ("\n\tBonus Crit Per Turn of Cooldown: " + this.getCritBonus(1)) : "";
		ret += this.rank() >= 10? ("\n\tBonus Accuracy Per Turn of Cooldown: " + this.getAccBonus(1)) : "";
		return ret;
	}
}