package WyattWitemeyer.WarOfGillysburg;

// Passive Abilities:
// Unique Passive Ability: "Quick Shot"
public class QuickShot extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional Variables
	private Condition enemyPreAttack;
	private Condition selfPreAttack;
	private double affectAllyChance;
	
	// Constructor
	public QuickShot(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \"Quick Shot\"", source, rank);
		this.owner = source;
		
		// Set the scaler of the Ability
		this.setScaler();
		
		// Set the enemy pre-attack condition and chance to occur on allies
		this.setEnemyPreAttackCondition();
		this.setSelfPreAttackBonus();
		this.setAffectAllyChance();
	}
	
	// Sets the scaler for the attack of the Ability
	private void setScaler() {
		// Scaler based on rank, use default value of rank 1
		switch(this.rank()) {
			case 1:
				this.scaler = .75;
				break;
			case 2:
				this.scaler = .85;
				break;
			case 3:
				this.scaler = 1.0;
				break;
			case 4:
				this.scaler = 1.25;
				break;
			case 5:
				this.scaler = 1.5;
			default:
				this.scaler = .75;
		}
	}
	
	// Sets the pre-attack condition to reduce incoming damage and accuracy
	private void setEnemyPreAttackCondition() {
		// Set the amount based on rank, use default value of rank 1, that being 0.
		int dmgAmt = 0;
		int accAmt = 0;
		switch(this.rank()) {
			case 2:
				dmgAmt = 10;
				accAmt = 20;
				break;
			case 3:
				dmgAmt = 15;
				accAmt = 25;
				break;
			case 4:
				dmgAmt = 20;
				accAmt = 30;
				break;
			case 5:
				dmgAmt = 25;
				accAmt = 35;
				break;
		}
		
		// Create the status effects reducing by the amount for outgoing (to be attached to the enemy)
		StatusEffect dmgRed = new StatusEffect(Stat.Version.DAMAGE, -dmgAmt, StatusEffect.Type.OUTGOING);
		dmgRed.makePercentage();
		StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -accAmt, StatusEffect.Type.OUTGOING);
		accRed.makePercentage();
		
		// Create the Condition, duration of 0 since its only used for the one attack
		this.enemyPreAttack = new Condition("Quick Shot: Pre Attack Damage and Accuracy Reduction", 0);
		this.enemyPreAttack.setSource(this.owner);
		this.enemyPreAttack.addStatusEffect(dmgRed);
		this.enemyPreAttack.addStatusEffect(accRed);
	}
	
	// Sets the pre-attack bonus for the Ability at rank 4/5
	private void setSelfPreAttackBonus() {
		// The amount will be 0 until rank 4
		int amount = 0;
		if (this.rank() == 4) {
			amount = 10;
		}
		if (this.rank() == 5) {
			amount = 15;
		}
		
		// Create the two Status Effects using the amount (percent Armor Piercing and flat Critical Chance)
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, amount, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, amount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Create the Condition, duration of 0 since its only used for the one attack
		this.selfPreAttack = new Condition("Quick Shot: Pre Attack Bonus", 0);
		this.selfPreAttack.setSource(this.owner);
		this.selfPreAttack.addStatusEffect(apBonus);
		this.selfPreAttack.addStatusEffect(critBonus);
	}
	
	// Sets the chance this Ability will affect allies
	private void setAffectAllyChance() {
		// The amount is always 0 until ranks 4 and 5 where it is 5% and 15% respectively
		this.affectAllyChance = 0;
		if (this.rank() == 4) {
			this.affectAllyChance = .05;
		}
		if (this.rank() == 5) {
			this.affectAllyChance = .15;
		}
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public Condition getEnemyPreAttackEffects() {
		return new Condition(this.enemyPreAttack);
	}
	public Condition getSelfPreAttackBonus() {
		return new Condition(this.selfPreAttack);
	}
	public Stun getStun() {
		Stun chanceStun = new Stun("Quick Shot: Chance Stun", 1);
		chanceStun.makeEndOfTurn();
		return chanceStun;
	}
	
	public double getAffectAllyChance() {
		return this.affectAllyChance;
	}
	
	
	//DE Will need a new type of function for chance to occur for allies (also used by Concentrate).
	//DE Basically the rest can all be done in Pre-Attack Effects
	
	
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 2? (this.getEnemyPreAttackEffects().toString()) : "";
		ret += this.rank() >= 4? ("Affect Ally Chance: " + (this.getAffectAllyChance()*100) + "%") : "";
		return ret;
	}
}
