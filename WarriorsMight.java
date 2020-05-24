package WyattWitemeyer.WarOfGillysburg;

public class WarriorsMight extends Ability {
	// Additional variables
	private int bonusHealthStat;
	private int bonusArmorStat;
	private int bonusThreatStat;
	private int bonusDamageStat;
	private Stun stun;
	
	// Constructor
	public WarriorsMight(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Warrior's Might\"";
		this.rank = rank;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
		
		// Calculate the stun effect (is always the same: 1 turn, end of turn)
		this.stun = new Stun("Warrior's Might (Vengeance Strike): Stun", 1);
		this.stun.makeEndOfTurn();
	}
	
	// Calculates the flat stat bonuses for Health, Armor, and Threat
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusHealthStat = 0;
		this.bonusArmorStat = 0;
		this.bonusThreatStat = 0;
		this.bonusDamageStat = 0;
		
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-2 grant +90 Health and +5 Armor per rank
			if (walker <= 2) {
				this.bonusHealthStat += 90;
				this.bonusArmorStat += 5;
			}
			// Ranks 3-5 grant +175 Health, +7 Armor, and +3 Threat per rank
			else if (walker <= 5) {
				this.bonusHealthStat += 175;
				this.bonusArmorStat += 7;
				this.bonusThreatStat += 3;
			}
			// Ranks 6-10 grant +285 Health, +8 Armor, +3 Threat, and +20 Damage per rank
			else if (walker <= 10) {
				this.bonusHealthStat += 285;
				this.bonusArmorStat += 8;
				this.bonusThreatStat += 3;
				this.bonusDamageStat += 20;
			}
			// Ranks 11-14 grant 575 Health, +10 Armor, +5 Threat, and +35 Damage per rank
			else if (walker <= 14) {
				this.bonusHealthStat += 575;
				this.bonusArmorStat += 10;
				this.bonusThreatStat += 5;
				this.bonusDamageStat += 35;
			}
			// Rank 15 grants +1225 Health, +15 Armor, +10 Threat, and +100 Damage
			else if (walker == 15) {
				this.bonusHealthStat += 1225;
				this.bonusArmorStat += 15;
				this.bonusThreatStat += 10;
				this.bonusDamageStat += 100;
			}
		}
	}
	
	// Get methods for additional effects
	public int getHealthBonus() {
		return this.bonusHealthStat;
	}
	
	public int getArmorBonus() {
		return this.bonusArmorStat;
	}
	
	public int getThreatBonus() {
		return this.bonusThreatStat;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public Stun getStun() {
		return this.stun;
	}
}
