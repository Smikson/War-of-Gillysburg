package WyattWitemeyer.WarOfGillysburg;

// Ability 2: "Double Shredder"
public class DoubleShredder extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional variables (1 and 2 are for 1st and 2nd arrow)
	private double scaler1;
	private double scaler2;
	private int ignoreArmorChance;
	private Condition critBonus;
	
	// Constructor
	public DoubleShredder(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"Double Shredder\"", source, rank);
		this.owner = source;
		
		// Set the Cooldown and Scalers for this Ability
		this.setCooldown();
		this.setScalers();
		
		// Set the additional effects of the Ability
		this.setIgnoreArmorChance();
		this.setCritBonus();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// The Cooldown of this Ability is 4 until rank 5 where it is reduced to 3
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		
		// This Ability starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the Scalers for the 2 arrows used for this Ability (default set to arrow1)
	private void setScalers() {
		// Set a default value for the first rank
		this.scaler1 = 0.5;
		this.scaler2 = 1.0;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler1 = 0.5;
				this.scaler2 = 1.0;
				break;
			case 2:
				this.scaler1 = 0.55;
				this.scaler2 = 1.05;
				break;
			case 3:
				this.scaler1 = 0.55;
				this.scaler2 = 1.05;
				break;
			case 4:
				this.scaler1 = 0.6;
				this.scaler2 = 1.1;
				break;
			case 5:
				this.scaler1 = 0.65;
				this.scaler2 = 1.15;
				break;
			case 6:
				this.scaler1 = 0.7;
				this.scaler2 = 1.2;
				break;
			case 7:
				this.scaler1 = 0.75;
				this.scaler2 = 1.25;
				break;
			case 8:
				this.scaler1 = 0.85;
				this.scaler2 = 1.35;
				break;
			case 9:
				this.scaler1 = 0.95;
				this.scaler2 = 1.45;
				break;
			case 10:
				this.scaler1 = 1.0;
				this.scaler2 = 1.5;
				break;
		}
		
		// Set the default scaler to scaler1
		this.scaler = this.scaler1;
	}
	
	// Sets the chance to ignore all armor
	private void setIgnoreArmorChance() {
		// The ignore armor chance is 60% at rank 1, 70% at rank 2, and 100% at rank 3+
		this.ignoreArmorChance = 60;
		if (this.rank() >= 2) {
			this.ignoreArmorChance = 70;
		}
		if (this.rank() >= 3) {
			this.ignoreArmorChance = 100;
		}
	}
	
	// Creates and set the Condition for the extra critical chance of the second arrow
	private void setCritBonus() {
		// Set the bonus amount based on rank, starting at 0 and going to 15
		int critAmt = 0;
		if (this.rank() >= 5) {
			critAmt = 10;
		}
		if (this.rank() >= 6) {
			critAmt = 12;
		}
		if (this.rank() >= 8) {
			critAmt = 13;
		}
		if (this.rank() >= 9) {
			critAmt = 15;
		}
		
		// Create the Status Effect
		StatusEffect critEffect = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmt, StatusEffect.Type.OUTGOING);
		critEffect.makeFlat();
		
		// Create the Condition (0 duration since only for 1 attack)
		this.critBonus = new Condition("Double Shredder: Crit Bonus", 0);
		this.critBonus.setSource(this.owner);
		this.critBonus.addStatusEffect(critEffect);
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public double getScalerArrow1() {
		return this.scaler1;
	}
	public double getScalerArrow2() {
		return this.scaler2;
	}
	
	public int getIgnoreArmorPercentage() {
		return this.ignoreArmorChance;
	}
	
	public Condition getCritBonusCondition() {
		return new Condition(this.critBonus);
	}
	
	
	// Remember upgrade where second arrow continues if first arrow kills -- create prompt and all that. Keep critical chance???
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tScaler of Arrow 1: " + this.getScalerArrow1();
		ret += "\n\tScaler of Arrow 2: " + this.getScalerArrow2();
		ret += "\n\tChance to Ignore All Armor: " + this.getIgnoreArmorPercentage();
		ret += this.rank() >= 5? ("\n\t" + this.getCritBonusCondition().toString()) : "";
		return ret;
	}
}
