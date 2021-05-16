package WyattWitemeyer.WarOfGillysburg;

// Ability 3: "Concentrate"
public class Concentrate extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional variables
	private Condition postTurnBonus;
	private Condition enemyCondition;
	
	// Constructor
	public Concentrate(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Concentrate\"", source, rank);
		this.owner = source;
		
		// Sets the Cooldown and scaler of the Ability (scaler is scaler for effect of "Quick Shot")
		this.setCooldown();
		this.setScaler();
		
		// Sets the Conditions for the Ability
		this.setConditions();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// The Cooldown of this Ability is 7 until rank 5 where it is reduced to 6
		this.cooldown = 7;
		if (this.rank() >= 5) {
			this.cooldown = 6;
		}
		
		// This Ability starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the Scaler percentage of "Quick Shot" damage
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = 0.5;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = 0.5;
				break;
			case 2:
				this.scaler = 0.6;
				break;
			case 3:
				this.scaler = 0.6;
				break;
			case 4:
				this.scaler = 0.7;
				break;
			case 5:
				this.scaler = 0.7;
				break;
			case 6:
				this.scaler = 0.8;
				break;
			case 7:
				this.scaler = 0.8;
				break;
			case 8:
				this.scaler = 0.9;
				break;
			case 9:
				this.scaler = 1.0;
			case 10:
				this.scaler = 1.0;
				break;
		}
	}
	
	// Sets the Conditions of the Ability
	private void setConditions() {
		// Set initial amounts to 0
		int dmgAmt = 0;
		int enemyAmt = 0;
		
		// Set the amounts based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				dmgAmt = 25;
				enemyAmt = 0;
				break;
			case 2:
				dmgAmt = 30;
				enemyAmt = 0;
				break;
			case 3:
				dmgAmt = 30;
				enemyAmt = 0;
				break;
			case 4:
				dmgAmt = 35;
				enemyAmt = 0;
				break;
			case 5:
				dmgAmt = 35;
				enemyAmt = 0;
				break;
			case 6:
				dmgAmt = 40;
				enemyAmt = 0;
				break;
			case 7:
				dmgAmt = 40;
				enemyAmt = 10;
				break;
			case 8:
				dmgAmt = 45;
				enemyAmt = 10;
				break;
			case 9:
				dmgAmt = 45;
				enemyAmt = 10;
			case 10:
				dmgAmt = 50;
				enemyAmt = 15;
				break;
		}
		
		// Create the StatusEffects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, dmgAmt, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, 30, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, 15, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		StatusEffect dodgeReduction = new StatusEffect(Stat.Version.DODGE, -enemyAmt, StatusEffect.Type.OUTGOING);
		dodgeReduction.makePercentage();
		
		StatusEffect blockReduction = new StatusEffect(Stat.Version.BLOCK, -enemyAmt, StatusEffect.Type.OUTGOING);
		blockReduction.makePercentage();
		
		StatusEffect armorReduction = new StatusEffect(Stat.Version.ARMOR, -enemyAmt, StatusEffect.Type.OUTGOING);
		armorReduction.makePercentage();
		
		// Create the two Conditions
		this.postTurnBonus = new Condition("Concentrate: Post-Turn Bonus", 1);
		this.postTurnBonus.setSource(this.owner);
		this.postTurnBonus.makeEndOfTurn();
		this.postTurnBonus.addStatusEffect(dmgBonus);
		if (this.rank() >= 10) {
			this.postTurnBonus.addStatusEffect(accBonus);
			this.postTurnBonus.addStatusEffect(critBonus);
		}
		
		this.enemyCondition = new Condition("Concentrate: Weapon Knocked", 1);
		this.enemyCondition.setSource(this.owner);
		this.enemyCondition.addStatusEffect(dodgeReduction);
		this.enemyCondition.addStatusEffect(blockReduction);
		this.enemyCondition.addStatusEffect(armorReduction);
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public Condition getPostTurnBonus() {
		return new Condition(this.postTurnBonus);
	}
	public Condition getEnemyCondition() {
		return new Condition(this.enemyCondition);
	}
	
	// All the things. Pre attack effects for allies*****
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\t" + this.getPostTurnBonus().toString();
		ret += this.rank() >= 7? ("\n\t" + this.getEnemyCondition().toString()) : "";
		return ret;
	}
}
