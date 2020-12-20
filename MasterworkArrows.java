package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Masterwork Arrows"
public class MasterworkArrows extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private int bonusDamageStat;
	private int bonusArmorPiercingStat;
	private int bonusAccuracyStat;
	
	// Constructor
	public MasterworkArrows(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Masterwork Arrows\"", source, rank);
		this.owner = source;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Specialist
	public MasterworkArrows(int rank) {
		super("No Owner: Masterwork Arrows", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Calculates the flat stat bonuses for Damage, Armor Piercing, and Accuracy
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusDamageStat = 0;
		this.bonusArmorPiercingStat = 0;
		this.bonusAccuracyStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Ranks 1-2 grant +20 Damage and +3 Armor Piercing per rank
			if (walker <= 2) {
				this.bonusDamageStat += 20;
				this.bonusArmorPiercingStat += 3;
			}
			// Ranks 3-5 grant +35 Damage, +5 Armor Piercing, and +3 Accuracy per rank
			else if (walker <= 5) {
				this.bonusDamageStat += 35;
				this.bonusArmorPiercingStat += 5;
				this.bonusAccuracyStat += 3;
			}
			// Ranks 6-10 grant +55 Damage, +7 Armor Piercing, and +5 Accuracy per rank
			else if (walker <= 10) {
				this.bonusDamageStat += 55;
				this.bonusArmorPiercingStat += 7;
				this.bonusAccuracyStat += 5;
			}
			// Ranks 11-14 grant +75 Damage, +10 Armor Piercing, and +7 Accuracy per rank
			else if (walker <= 14) {
				this.bonusDamageStat += 75;
				this.bonusArmorPiercingStat += 10;
				this.bonusAccuracyStat += 7;
			}
			// Rank 15 grants +200 Damage, +15 Armor Piercing, and +10 Accuracy
			else if (walker == 15) {
				this.bonusDamageStat += 200;
				this.bonusArmorPiercingStat += 15;
				this.bonusAccuracyStat += 10;
			}
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public int getArmorPiercingBonus() {
		return this.bonusArmorPiercingStat;
	}
	
	public int getAccuracyBonus() {
		return this.bonusAccuracyStat;
	}
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Damage Stat: " + this.getDamageBonus();
		ret += "\n\tBonus Armor Piercing Stat: " + this.getArmorPiercingBonus();
		ret += this.rank() >= 3? ("\n\tBonus Accuracy Stat: " + this.getAccuracyBonus()) : "";
		return ret;
	}
}