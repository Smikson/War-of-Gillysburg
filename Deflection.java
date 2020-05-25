package WyattWitemeyer.WarOfGillysburg;

public class Deflection extends UltimateAbility {
	private double healingScaler;
	private Stun stunEffect;
	
	// Constructor
	public Deflection(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "ULTIMATE Ability: \"Deflection\"";
		this.rank = rank;
		
		// Calculate the damage and healing scalers of the Ability
		this.setScalers();
		
		// Calculate and set the additional effects
		this.setStun();
	}
	
	// Calculates the damage scaler
	private void setScalers() {
		// Set a default value for the first rank
		this.scaler = .3;
		this.healingScaler = .25;
		
		// Set the scalers based on the rank of the ability
		switch(this.rank) {
			case 1:
				this.scaler = .3;
				this.healingScaler = .25;
				break;
			case 2:
				this.scaler = .5;
				this.healingScaler = 1.0/3.0;
				break;
			case 3:
				this.scaler = .75;
				this.healingScaler = .5;
				break;
		}
	}
	
	// Calculates and creates the additional effects
	private void setStun() {
		this.stunEffect = new Stun("Deflection: Stun", 1);
		this.stunEffect.setSource(this.owner);
		this.stunEffect.makeSourceIncrementing();
		this.stunEffect.makeEndOfTurn();
	}
	
	// Get methods for each variable
	public double getHealingScaler() {
		return this.healingScaler;
	}
	
	public Stun getStun() {
		return this.stunEffect;
	}
}
