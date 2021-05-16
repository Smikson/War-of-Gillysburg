package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Combat Roll"
public class CombatRoll extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional variables
	private int bonusDodgeStat;
	private int bonusSpeedStat;

	private Condition speedBonus;
	private Condition attackBonus;
	
	// Constructor
	public CombatRoll(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Combat Roll\"", source, rank);
		this.owner = source;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
		
		// Set the Conditions for the Ability
		this.setConditions();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Arc Archer
	public CombatRoll(int rank) {
		super("No Owner: Combat Roll", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Sets the constant stat bonuses for the Arc Archer creation
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusDodgeStat = 0;
		this.bonusSpeedStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants +2 Dodge
			if (walker == 1) {
				this.bonusDodgeStat += 2;
			}
			// Ranks 2-4 grant +3 Dodge per rank
			else if (walker <= 4) {
				this.bonusDodgeStat += 3;
			}
			// Rank 5 grants +3 Dodge and +1 Speed
			else if (walker == 5) {
				this.bonusDodgeStat += 3;
				this.bonusSpeedStat += 1;
			}
			// Ranks 6-9 grant +4 Dodge per rank
			else if (walker <= 9) {
				this.bonusDodgeStat += 4;
			}
			// Rank 10 grants +4 Dodge and +2 Speed
			else if (walker == 10) {
				this.bonusDodgeStat += 4;
				this.bonusSpeedStat += 2;
			}
			// Ranks 11-14 grant +5 Dodge per rank
			else if (walker <= 14) {
				this.bonusDodgeStat += 5;
			}
			// Rank 15 grants +10 Dodge and +3 Speed
			else if (walker == 15) {
				this.bonusDodgeStat += 10;
				this.bonusSpeedStat += 3;
			}
		}
	}
	
	// Sets the bonus Conditions for the Ability
	//DE Make a Charged Condition???
	private void setConditions() {
		// The bonus for each stat starts at 0
		int spdAmt = 0;
		int dmgAmt = 0;
		int accAmt = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants +1 bonus Speed and +10 bonus Damage
			if (walker == 1) {
				spdAmt += 1;
				dmgAmt += 10;
			}
			// Ranks 2-4 grant +1 bonus Damage per rank
			else if (walker <= 4) {
				dmgAmt += 1;
			}
			// Rank 5 grants +1 bonus Speed and +2 bonus Damage
			else if (walker == 5) {
				spdAmt += 1;
				dmgAmt += 2;
			}
			// Ranks 6-9 grant +3 bonus Damage per rank
			else if (walker <= 9) {
				dmgAmt += 3;
			}
			// Rank 10 grants +3 bonus Damage and +10 bonus Accuracy
			else if (walker == 10) {
				dmgAmt += 3;
				accAmt += 10;
			}
			// Ranks 11-14 grant +5 bonus Damage and +5 bonus Accuracy per rank
			else if (walker <= 14) {
				dmgAmt += 5;
				accAmt += 5;
			}
			// Rank 15 grants +1 bonus Speed and +5 bonus Damage
			else if (walker == 15) {
				spdAmt += 1;
				dmgAmt += 5;
			}
		}
		
		// Set the duration of each condition (start at rank 1 at 1 turn each)
		int spdDur = 1;
		int atkDur = 1;
		
		// For the speed duration, it goes up to 2 at rank 10
		if (this.rank() >= 10) {
			spdDur = 2;
		}
		
		// For the attack duration, it goes up to 2 at rank 5 and 3 at rank 15
		if (this.rank() >= 5) {
			atkDur = 2;
		}
		if (this.rank() >= 15) {
			atkDur = 3;
		}
		
		// Create the StatusEffects
		StatusEffect spdBonus = new StatusEffect(Stat.Version.SPEED, spdAmt, StatusEffect.Type.BASIC);
		spdBonus.makeFlat();
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, dmgAmt, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, accAmt, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		//DE Edit this for charged version of Conditions
		// Create the Conditions
		this.speedBonus = new Condition("Combat Roll: Speed Bonus", spdDur);
		this.speedBonus.setSource(this.owner);
		this.speedBonus.addStatusEffect(spdBonus);
		this.speedBonus.makeEndOfTurn();
		
		this.attackBonus = new Condition("Combat Roll: Attack Bonus", atkDur);
		this.attackBonus.setSource(this.owner);
		this.attackBonus.makeChargeBased(1); //DE This is correct but it won't add a charge to stack is what needs to be added
		this.attackBonus.addStatusEffect(dmgBonus);
		if (accAmt > 0) {
			this.attackBonus.addStatusEffect(accBonus);
		}
		this.attackBonus.makeEndOfTurn();
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public int getDodgeBonus() {
		return this.bonusDodgeStat;
	}
	public int getSpeedBonus() {
		return this.bonusSpeedStat;
	}
	
	public Condition getSpeedCondition() {
		return new Condition(this.speedBonus);
	}
	public Condition getAttackCondition() {
		return new Condition(this.attackBonus);
	}
	
	
	//DE Attack Condition is correct but it won't add a charge to stack is what needs to be added.
	// Pre Attack Effects Check for Roll Opportunity (NOTE: Ordering between this Ability and Quick Shot / Concentration will matter! Think about it!)
	// Post Attack Effects Check if Dodged -- Apply buffs as appropriate
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Dodge Stat: " + this.getDodgeBonus();
		ret += this.rank() >= 5? ("\n\tBonus Speed Stat: " + this.getSpeedBonus()) : "";
		ret += "\n\t" + this.getSpeedCondition().toString();
		ret += "\n\t" + this.getAttackCondition().toString();
		return ret;
	}
}
