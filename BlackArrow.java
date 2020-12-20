package WyattWitemeyer.WarOfGillysburg;

// ULTIMATE Ability: "Black Arrow"
public class BlackArrow extends UltimateAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double scalerDeduction;
	private Condition selfAccuracyBonus;
	
	// Constructor
	public BlackArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Black Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the scaler and the scaler deduction of the Ability
		this.setScalers();
		
		// Sets the pre-attack bonus
		this.setAccuracyBonus();
	}
	
	// Sets the damage scaler and the amount by which it decreases per enemy hit
	private void setScalers() {
		// At rank 1 (default) the Ability deals 3x damage with a 1x deduction
		this.scaler = 3.0;
		this.scalerDeduction = 1.0;
		// At rank 2, the Ability deals 3.5x damage with a 0.5x damage deduction
		if (this.rank() == 2) {
			this.scaler = 3.5;
			this.scalerDeduction = 0.5;
		}
		// At rank 3, the Ability deals 4x damage with no damage deduction
		if (this.rank() == 3) {
			this.scaler = 4.0;
			this.scalerDeduction = 0;
		}
	}
	
	// Sets the Accuracy Pre-Attack Bonus for this Ability
	private void setAccuracyBonus() {
		// At rank 1 it does not exist, has value 0
		int amount = 0;
		// At rank 2 it is +25% Accuracy
		if (this.rank() == 2) {
			amount = 25;
		}
		// At rank 3 it is +30% Accuracy
		if (this.rank() == 3) {
			amount = 30;
		}
		
		// Creates the Status Effect
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Creates the Condition with a duration of 0 since it is used only for this attack
		this.selfAccuracyBonus = new Condition("Black Arrow: Accuracy Bonus", 0);
		this.selfAccuracyBonus.setSource(this.owner);
		this.selfAccuracyBonus.addStatusEffect(accBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getScalerDeduction() {
		return this.scalerDeduction;
	}
	
	public Condition getSelfAccuracyBonus() {
		return new Condition(this.selfAccuracyBonus);
	}
	
	
	//DE either remove the extra damage effect and replace with something better and cooler or it will be included as an additional Condition
	
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tScaler Deduction Per Enemy Hit: " + this.getScalerDeduction();
		ret += this.rank() >= 2? ("\n\t" + this.getSelfAccuracyBonus()) : "";
		return ret;
	}
}