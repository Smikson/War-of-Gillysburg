package WyattWitemeyer.WarOfGillysburg;

// Base Passive Ability: "Attack Speed"
public class AttackSpeed extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelArcArcher owner;
	
	// Additional variables
	private int critStacks;
	private int conditionalStacks;
	
	private Condition bonusTurnAlterations;
	
	// Constructor
	public AttackSpeed(SentinelArcArcher source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"AttackSpeed\"", source, rank);
		this.owner = source;
		
		// Calculate the number of stacks given by critical strikes or certain conditions
		this.setStackAmounts();
		
		// Calculate and set the portions of the status effects used in bonus turns
		this.setBonusTurnCondition();
	}
	
	// Sets the number of stacks given by critical strikes or certain conditions
	private void setStackAmounts() {
		// Initialize each to 0
		this.critStacks = 0;
		this.conditionalStacks = 0;
		
		// Set the crit stacks to start at 5 and increase to 10 along the level interval 1->5
		for (int walker = 1; walker <= this.rank() && walker <= 5; walker++) {
			// Rank 1 grants +5 stacks for critical hits
			if (walker == 1) {
				this.critStacks += 5;
			}
			// Ranks 2-4 grant +1 stack for critical hits per rank
			else if (walker <= 4) {
				this.critStacks += 1;
			}
			// Rank 5 grants +2 stacks for critical hits
			else if (walker == 5) {
				this.critStacks += 2;
			}
		}
		
		// Set the conditional stacks to start at 5 and increase to 10 along the level interval 10->15
		for (int walker = 10; walker <= this.rank() && walker <= 15; walker++) {
			// Rank 10 grants +10 stacks for conditional circumstances
			if (walker == 10) {
				this.conditionalStacks += 5;
			}
			// Ranks 11-15 grant +1 stack for conditional circumstances per rank
			else if (walker <= 15) {
				this.conditionalStacks += 1;
			}
		}
	}
	
	// Sets the condition used by the ability during bonus turns
	private void setBonusTurnCondition() {
		// The bonus for each stat starts at 0
		int dmgAmt = 0;
		int spdAmt = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 starts with a 50% damage reduction
			if (walker == 1) {
				dmgAmt += -50;
			}
			// Ranks 2-4 grant +10% bonus Damage per rank (reduces reduction by 10% per rank)
			else if (walker <= 4) {
				dmgAmt += 10;
			}
			// Rank 5 changes the bonus Damage to +15% and grants +50% bonus Speed
			else if (walker == 5) {
				dmgAmt = 15;
				spdAmt += 50;
			}
			// Ranks 6-9 grant +3% bonus Damage per rank
			else if (walker <= 9) {
				dmgAmt += 3;
			}
			// Ranks 11-14 grant +4% bonus Damage per rank
			else if (walker >= 11 && walker <= 14) {
				dmgAmt += 4;
			}
			// Rank 15 grants +7% bonus Damage and +50% bonus Speed (total 100%)
			else if (walker == 15) {
				dmgAmt += 7;
				spdAmt += 50;
			}
		}
		
		// Create the StatusEffects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, dmgAmt, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect spdBonus = new StatusEffect(Stat.Version.SPEED, spdAmt, StatusEffect.Type.BASIC);
		spdBonus.makePercentage();
		
		// Create the Condition
		this.bonusTurnAlterations = new Condition("Attack Speed: Bonus Turn Alterations", 1);
		this.bonusTurnAlterations.setSource(this.owner);
		this.bonusTurnAlterations.addStatusEffect(dmgBonus);
		if (spdAmt > 0) {
			this.bonusTurnAlterations.addStatusEffect(spdBonus);
		}
		this.bonusTurnAlterations.makeEndOfTurn();
	}
	
	// Get methods for additional effects
	@Override
	public SentinelArcArcher getOwner() {
		return this.owner;
	}
	
	public int getCritStacks() {
		return this.critStacks;
	}
	public int getConditionalStacks() {
		return this.conditionalStacks;
	}
	
	public Condition getBonusTurnCondition() {
		return new Condition(this.bonusTurnAlterations);
	}
	
	// All the things
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tCritical Strikes Stack Gain: " + this.getCritStacks();
		ret += this.rank() >= 10? ("\n\tConditional Stack Gain: " + this.getConditionalStacks()) : "";
		ret += "\n\t" + this.getBonusTurnCondition().toString();
		return ret;
	}
}
