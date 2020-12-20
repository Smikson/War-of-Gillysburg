package WyattWitemeyer.WarOfGillysburg;

// Passive Abilities:
// Unique Passive Ability: "Empowered Arrows"
public class EmpoweredArrows extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double multAbilityScaler;
	private int stackRequirement;
	private Condition abilityPreAttackBonus;
	
	// Constructor
	public EmpoweredArrows(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \"Empowered Arrows\"", source, rank);
		this.owner = source;
		
		// Set the scaler and the stack requirement
		this.setMultAbilityScaler();
		this.setStackRequirement();
		
		// Sets the Ability Pre-Attack Bonus
		this.setPreAttackBonus();
	}
	
	// Sets the scaler for using multiple empowered Abilities at one time (also sets the default scaler equal to this value)
	private void setMultAbilityScaler() {
		switch(this.rank()) {
			case 1:
			case 2:
			case 3:
				this.multAbilityScaler = 1.0;
				break;
			case 4:
				this.multAbilityScaler = 1.5;
				break;
			case 5:
				this.multAbilityScaler = 2.0;
			default:
				this.multAbilityScaler = 1.0;
		}
		this.scaler = this.multAbilityScaler;
	}
	
	// Sets the stack requirement based on level
	private void setStackRequirement() {
		// The stack requirement is always 4 until rank 5 when it is reduced to 3.
		this.stackRequirement = 4;
		if (this.rank() >= 5) {
			this.stackRequirement = 3;
		}
	}
	
	// Sets the Condition that enhances damage for the Empowered Abilities
	private void setPreAttackBonus() {
		// At rank 1, and by default, the damage bonus is 25%
		int amount = 25;
		switch (this.rank()) {
			case 1:
				amount = 25;
				break;
			case 2:
				amount = 30;
				break;
			case 3:
				amount = 35;
				break;
			case 4:
				amount = 40;
				break;
			case 5:
				amount = 50;
				break;
		}
		
		// Creates the StatusEffect for bonus Damage Condition to be added to the respective Abilities attack(s)
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		
		// Create the Condition, duration of 0 since its only used for the one attack
		this.abilityPreAttackBonus = new Condition("Empowered Arrows: Pre Attack Damage Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(dmgBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getMultAbilityScaler() {
		return this.multAbilityScaler;
	}
	
	public int getStackRequirement() {
		return this.stackRequirement;
	}
	
	public Condition getAbilityPreAttackBonus() {
		return new Condition(this.abilityPreAttackBonus);
	}
	
	//DE Masterwork Arrows rank 15 - if "Empowered" misses, effect returns, counts as separate for "Multi-purposed"
	
	
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 3? ("\n\tMultiple Ability Scaler: " + this.getMultAbilityScaler()) : "";
		ret += "\n\tStack Requirement: " + this.getStackRequirement();
		ret += "\n\t" + this.getAbilityPreAttackBonus();
		return ret;
	}
}
