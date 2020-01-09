package WyattWitemeyer.WarOfGillysburg;

public class EnchantedArmor extends Ability {
	// Additional variables for the Ability
	private int bonusArmor;
	private int bonusBlock;
	
	// Constructors
	public EnchantedArmor(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Enchanted Armor\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the healing scaler and base stat amounts
		this.setScaler();
		this.setArmorBonus();
		this.setBlockBonus();
	}
	public EnchantedArmor(int rank) {
		this(Character.EMPTY, rank);
	}
	public EnchantedArmor() {
		super();
		this.bonusArmor = 0;
		this.bonusBlock = 0;
	}
	
	// Calculates the base scaler (used as a healing scaler) for this Ability
	private void setScaler() {
		// At rank 0, this scaler starts at .02
		this.scaler = .02;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +.5% to scaler
			if (walker <= 5) {
				this.scaler += .005;
			}
			// Ranks 6-10 grant +.7% to scaler
			else if (walker <= 10) {
				this.scaler += .007;
			}
			// Ranks 11-14 grant +1% to scaler
			else if (walker <= 14) {
				this.scaler += .01;
			}
			// Rank 15 grants +3% to scaler
			else if (walker <= 15) {
				this.scaler += .03;
			}
		}
	}
	
	
	// Calculates the values for the additional stats of this Ability
	private void setArmorBonus() {
		// Starting at 0:
		this.bonusArmor = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +8 bonus Armor
			if (walker <= 5) {
				this.bonusArmor += 8;
			}
			// Ranks 6-10 grant +10 bonus Armor
			else if (walker <= 10) {
				this.bonusArmor += 10;
			}
			// Ranks 11-14 grant +15 bonus Armor
			else if (walker <= 14) {
				this.bonusArmor += 15;
			}
			// Rank 15 grants +20 bonus Armor
			else if (walker <= 15) {
				this.bonusArmor += 20;
			}
		}
	}
	
	private void setBlockBonus() {
		// Starting at 0:
		this.bonusBlock = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +8 bonus Block
			if (walker <= 5) {
				this.bonusBlock += 2;
			}
			// Ranks 6-10 grant +10 bonus Block
			else if (walker <= 10) {
				this.bonusBlock += 3;
			}
			// Ranks 11-14 grant +15 bonus Block
			else if (walker <= 14) {
				this.bonusBlock += 4;
			}
			// Rank 15 grants +20 bonus Block
			else if (walker <= 15) {
				this.bonusBlock += 7;
			}
		}
	}
	
	// Get methods for additional stats of this Ability and source
	public int getBonusArmor() {
		return this.bonusArmor;
	}
	public int getBonusBlock() {
		return this.bonusBlock;
	}
}
