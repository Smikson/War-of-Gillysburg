package WyattWitemeyer.WarOfGillysburg;

public class ShieldReflection extends Ability {
	// Additional Condition for the Ability
	private Blind blind;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of if a previous attack has been blocked
	public boolean didBlock;
	
	public ShieldReflection(int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.name = "Ability 2: \"Shield Reflection\"";
		this.ssRank = ShieldSkillsRank;
		this.didBlock = false;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets additional effects of the Ability
		this.setBlind();
	}
	public ShieldReflection(int rank) {
		this(rank, 0);
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 5;
		if (this.rank >= 5) {
			this.cooldown = 4;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .1
		this.scaler = .1;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2-6 but not 3 grant +.05 to scaler
			if (walker <= 6 && walker != 3) {
				this.scaler += .05;
			}
			// Ranks 8 and 10 grant +.1 to scaler
			else if (walker == 8 || walker == 10) {
				this.scaler += .1;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 6; walker < this.ssRank; walker++) {
			// Ranks 6-10 grant +.2x damage to scaler if an attack was blocked
			if (walker <= 10 && this.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the value for the additional blind effect for this Ability
	public void setBlind() {
		// Calculates blind duration
		int blindDuration = 1;
		if (this.rank >= 3) {
			blindDuration = 2;
		}
		
		// Sets the blind effect
		this.blind = new Blind("Shield Reflection Blind", blindDuration);
	}
	
	// Get method for additional blind effect
	public Blind getBlindEffect() {
		return this.blind;
	}
}
