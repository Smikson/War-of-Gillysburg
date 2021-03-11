package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Flawlessness"
public class Flawlessness extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional variables
	private int bonusCritStat;
	private int bonusAccuracyStat;
	private int bonusDodgeStat;
	
	// Base Constructor
	public Flawlessness(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Flawlessness\"", source, rank);
		this.owner = source;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Arc Archer
	public Flawlessness(int rank) {
		super("No Owner: Flawlessness", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Sets the constant stat bonuses for the Arc Archer creation
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusCritStat = 0;
		this.bonusAccuracyStat = 0;
		this.bonusDodgeStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Ranks 1-2 grant +2% Critical Chance and +2 Accuracy per rank
			if (walker <= 2) {
				this.bonusCritStat += 2;
				this.bonusAccuracyStat += 2;
			}
			// Ranks 3-5 grant +2% Critical Chance, +3 Accuracy, and +2 Dodge per rank
			else if (walker <= 5) {
				this.bonusCritStat += 2;
				this.bonusAccuracyStat += 3;
				this.bonusDodgeStat += 2;
			}
			// Ranks 6-10 grant +4% Critical Chance, +5 Accuracy, and +3 Dodge per rank
			else if (walker <= 10) {
				this.bonusCritStat += 4;
				this.bonusAccuracyStat += 5;
				this.bonusDodgeStat += 3;
			}
			// Ranks 11-14 grant +5% Critical Chance, +8 Accuracy, and +4 Dodge per rank
			else if (walker <= 14) {
				this.bonusCritStat += 5;
				this.bonusAccuracyStat += 8;
				this.bonusDodgeStat += 4;
			}
			// Rank 15 grants +10% Critical Chance, +20 Accuracy, and +5 Dodge
			else if (walker == 15) {
				this.bonusCritStat += 10;
				this.bonusAccuracyStat += 20;
				this.bonusDodgeStat += 5;
			}
		}
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public int getCritBonus() {
		return this.bonusCritStat;
	}
	public int getAccuracyBonus() {
		return this.bonusAccuracyStat;
	}
	public int getDodgeBonus() {
		return this.bonusDodgeStat;
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Critical Chance Stat: " + this.getCritBonus();
		ret += "\n\tBonus Accuracy Stat: " + this.getAccuracyBonus();
		ret += this.rank() >= 3? ("\n\tBonus Dodge Stat: " + this.getDodgeBonus()) : "";
		return ret;
	}
}
