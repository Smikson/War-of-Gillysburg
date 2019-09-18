package WyattWitemeyer.WarOfGillysburg;

public class ShieldSkills extends Ability {
	// Additional variables for ability
	private double scalerBlind3;
	private double scalerBlind5;
	
	public ShieldSkills(int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.name = "Base Passive Ability: \"Shield Skills\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// At rank 1, the healing scaler for blinding 3 enemies starts at .02, and there is no extra benefit for blinding 5
		this.scalerBlind3 = .02;
		this.scalerBlind5 = 0;
		
		// Adjust for current rank
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2-5 grant +2% to scaler for blinding 3
			if (walker <= 5) {
				this.scalerBlind3 += .02;
			}
			// Ranks 11-15 grant +3% to a second scaler for each rank
			else if (walker >= 11 && walker <= 15) {
				this.scalerBlind5 += .03;
			}
		}
		// Use Scaler for blinding 3 people for default
		this.scaler = this.scalerBlind3;
	}
	
	// Get methods for additional scalers
	public double getScalerBlind3() {
		return this.scalerBlind3;
	}
	public double getScalerBlind5() {
		return this.scalerBlind5;
	}
}
