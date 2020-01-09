package WyattWitemeyer.WarOfGillysburg;

public class ProfessionalLaughter extends Ability {
	// Additional variables for the Ability
	private int bonusThreat;
	private int bonusTacticalThreat;
	private int bonusHealth;
	
	public ProfessionalLaughter(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Professional Laughter\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the healing scaler and base stat amounts
		this.setThreatBonus();
		this.setTacticalThreatBonus();
		this.setHealthBonus();
	}
	public ProfessionalLaughter(int rank) {
		this(Character.EMPTY, rank);
	}
	
	// Calculates the values for the additional stats of this Ability
	private void setThreatBonus() {
		// Starting at 0
		this.bonusThreat = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-2 grant +5 bonus Threat
			if (walker <= 2) {
				this.bonusThreat += 5;
			}
			// Ranks 3-5 grant +7 bonus Threat
			else if (walker <= 5) {
				this.bonusThreat += 7;
			}
			// Ranks 6-10 grant +8 bonus Threat
			else if (walker <= 10) {
				this.bonusThreat += 8;
			}
			// Ranks 11-14 grant +10 bonus Threat
			else if (walker <= 14) {
				this.bonusThreat += 10;
			}
			// Rank 15 grants +15 bonus Threat
			else if (walker <= 15) {
				this.bonusThreat += 15;
			}
		}
	}
	
	private void setTacticalThreatBonus() {
		// Starting at 0
		this.bonusTacticalThreat = 0;
		
		// First rank that grants bonus Tactical Threat is Rank 11
		for (int walker = 11; walker <= this.rank; walker++) {
			// Ranks 11-14 grant +3 bonus Tactical Threat
			if (walker <= 14) {
				this.bonusTacticalThreat += 3;
			}
			// Rank 15 grants +5 bonus Tactical Threat
			else if (walker <= 15) {
				this.bonusTacticalThreat += 5;
			}
		}
	}
	
	private void setHealthBonus() {
		// Starting at 0
		this.bonusHealth = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-2 grant +100 bonus Health
			if (walker <= 2) {
				this.bonusHealth += 100;
			}
			// Ranks 3-5 grant +225 bonus Health
			else if (walker <= 5) {
				this.bonusHealth += 225;
			}
			// Ranks 6-10 grant +350 bonus Health
			else if (walker <= 10) {
				this.bonusHealth += 350;
			}
			// Ranks 11-14 grant +625 bonus Health
			else if (walker <= 14) {
				this.bonusHealth += 625;
			}
			// Rank 15 grants +1725 bonus Health
			else if (walker <= 15) {
				this.bonusHealth += 1725;
			}
		}
	}
	
	// Get methods for additional stats of this Ability
	public int getBonusThreat() {
		return this.bonusThreat;
	}
	public int getBonusTacticalThreat() {
		return this.bonusTacticalThreat;
	}
	public int getBonusHealth() {
		return this.bonusHealth;
	}
}
