package WyattWitemeyer.WarOfGillysburg;

// Hidden Ability: "Restoration Arrow"
// Makes use of a heal over time defined here (extends DOT, likely should just be named OT)
class SentinelHealOT extends DamageOverTime {
	// Variables needed to complete the restoration (slightly condensed)
	private SentinelSpecialist source;
	private Character affected;
	private int duration;
	private double scaler1, scaler2, scaler3, missHpBonus;
	private boolean isCrit, canRandEmp;
	private int curTurn;
	
	// Constructors
	public SentinelHealOT(SentinelSpecialist source, Character affected, int duration, double scaler1, double scaler2, double scaler3, double missHpBonus, boolean isCrit, boolean canRandEmp) {
		super(source, "Restoration Arrow: Heal Over Time Effect", duration);
		this.source = source;
		this.affected = affected;
		this.duration = duration;
		this.scaler1 = scaler1;
		this.scaler2 = scaler2;
		this.scaler3 = scaler3;
		this.missHpBonus = missHpBonus;
		this.isCrit = isCrit;
		this.canRandEmp = canRandEmp;
		this.curTurn = 0;
	}
	public SentinelHealOT(SentinelHealOT copy) {
		super(copy);
		this.source = copy.source;
		this.affected = copy.affected;
		this.duration = copy.duration;
		this.scaler1 = copy.scaler1;
		this.scaler2 = copy.scaler2;
		this.scaler3 = copy.scaler3;
		this.missHpBonus = copy.missHpBonus;
		this.isCrit = copy.isCrit;
		this.canRandEmp = copy.canRandEmp;
		this.curTurn = 0;
	}
	
	// For the heal over time effect, restore the amount based on the current turn
	@Override
	public void executeDOT() {
		// Increment to know if we are on turn 1, 2, or 3
		this.curTurn++;
		
		// If the current turn is greater than the duration, do nothing
		if (this.curTurn > this.duration) {
			return;
		}
		
		// Figure out which scaler to use based on the current turn
		double scaler = 0;
		switch(this.curTurn) {
			case 1:
				scaler = this.scaler1;
				break;
			case 2:
				scaler = this.scaler2;
				break;
			case 3:
				scaler = this.scaler3;
				break;
		}
		
		// Get the amount that the Character will be healed based on the found scaler
		double amount = this.source.getDamage() * scaler;
		if (this.isCrit) {
			amount *= 2;
		}
		int healAmt = (int)Math.round(amount);
		int missingHealth = this.affected.getMissingHealth();
		
		// Heal the Character for the specified amount
		this.affected.restoreHealth(healAmt);
		
		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
		if (healAmt > missingHealth && this.canRandEmp) {
			this.source.randomlyEmpower();
			this.canRandEmp = false;
		}
		
		// If we're on the third heal and we did not fully restore the character (and the missHpBonus > 0), apply the missing health bonus heal
		if (this.curTurn == 3 && this.affected.getMissingHealth() != 0 && this.missHpBonus > 0) {
			this.affected.restoreHealth((int)Math.round(this.affected.getMissingHealth() * this.missHpBonus));
		}
	}
	
	// Returns the display line (without the tabs) of the Bleed DOT effect in the Condition
	@Override
	public String getDOTString() {
		// Do the same calculations as above to get a good estimate of the amount to be healed
		// Figure out which scaler to use based on the current turn
		double scaler = 0;
		switch(this.curTurn + 1) {
			case 1:
				scaler = this.scaler1;
				break;
			case 2:
				scaler = this.scaler2;
				break;
			case 3:
				scaler = this.scaler3;
				break;
		}
		
		// Get the amount that the Character will be healed based on the found scaler
		double amount = this.source.getDamage() * scaler;
		if (this.isCrit) {
			amount *= 2;
		}
		int estimate = (int)Math.round(amount);
		
		// Return the String with the estimate
		return "Healing Over Time: Next heals ~" + estimate + " healing at the beginning of the turn.";
	}
}


//The class Flaming Arrow itself
public class RestorationArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private double baseScaler;
	private double firstTurnScaler;
	private double secondTurnScaler;
	private double thirdTurnScaler;
	private double missingHealthScaler;
	private Condition selfCritBonus;
	private int numStacks;
	
	// Constructor
	public RestorationArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Flaming Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the Cooldown and scalers of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the self crit bonus the Ability
		this.setCritBonus();
	}
	
	// Sets the Cooldown of the Ability
	private void setCooldown() {
		// The Cooldown is always 7 until rank 5 where it is reduced to 6
		this.cooldown = 7;
		if (this.rank() >= 5) {
			this.cooldown = 6;
		}
	}
	
	// Sets the base scaler, the missing Health scaler, and each heal over time scaler of the Ability
	private void setScalers() {
		// Initialize each scaler to 0
		this.baseScaler = 0;
		this.firstTurnScaler = 0;
		this.secondTurnScaler = 0;
		this.thirdTurnScaler = 0;
		this.missingHealthScaler = 0;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = .2;
				this.firstTurnScaler = .4;
				this.secondTurnScaler = .6;
				break;
			case 2:
				this.baseScaler = .25;
				this.firstTurnScaler = .45;
				this.secondTurnScaler = .65;
				break;
			case 3:
				this.baseScaler = .3;
				this.firstTurnScaler = .45;
				this.secondTurnScaler = .65;
				break;
			case 4:
				this.baseScaler = .35;
				this.firstTurnScaler = .5;
				this.secondTurnScaler = .7;
				break;
			case 5:
				this.baseScaler = .4;
				this.firstTurnScaler = .5;
				this.secondTurnScaler = .7;
				break;
			case 6:
				this.baseScaler = .45;
				this.firstTurnScaler = .55;
				this.secondTurnScaler = .75;
				break;
			case 7:
				this.baseScaler = .45;
				this.firstTurnScaler = .6;
				this.secondTurnScaler = .7;
				this.thirdTurnScaler = .8;
				break;
			case 8:
				this.baseScaler = .5;
				this.firstTurnScaler = .65;
				this.secondTurnScaler = .75;
				this.thirdTurnScaler = .85;
				break;
			case 9:
				this.baseScaler = .55;
				this.firstTurnScaler = .7;
				this.secondTurnScaler = .8;
				this.thirdTurnScaler = .9;
				break;
			case 10:
				this.baseScaler = .55;
				this.firstTurnScaler = .75;
				this.secondTurnScaler = .85;
				this.thirdTurnScaler = .95;
				this.missingHealthScaler = .25;
				break;
			case 11:
				this.baseScaler = .6;
				this.firstTurnScaler = .8;
				this.secondTurnScaler = .9;
				this.thirdTurnScaler = 1.0;
				this.missingHealthScaler = .25;
				break;
			case 12:
				this.baseScaler = .6;
				this.firstTurnScaler = .85;
				this.secondTurnScaler = .95;
				this.thirdTurnScaler = 1.05;
				this.missingHealthScaler = .35;
				break;
			case 13:
				this.baseScaler = .65;
				this.firstTurnScaler = .9;
				this.secondTurnScaler = 1.0;
				this.thirdTurnScaler = 1.1;
				this.missingHealthScaler = .35;
				break;
			case 14:
				this.baseScaler = .7;
				this.firstTurnScaler = .95;
				this.secondTurnScaler = 1.05;
				this.thirdTurnScaler = 1.1;
				this.missingHealthScaler = .35;
				break;
			case 15:
				this.baseScaler = .75;
				this.firstTurnScaler = 1.0;
				this.secondTurnScaler = 1.1;
				this.thirdTurnScaler = 1.25;
				this.missingHealthScaler = .5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Sets the self crit bonus pre attack Condition
	private void setCritBonus() {
		// Initialize the amount to 0
		int amount = 0;
		
		// Set the amount based on the rank of the Ability
		switch(this.rank()) {
			case 7:
				amount = 10;
				break;
			case 8:
				amount = 12;
				break;
			case 9:
				amount = 14;
				break;
			case 10:
				amount = 20;
				break;
			case 11:
				amount = 22;
				break;
			case 12:
				amount = 27;
				break;
			case 13:
				amount = 30;
				break;
			case 14:
				amount = 33;
				break;
			case 15:
				amount = 40;
				break;
		}
		
		// Creates the Status Effect for the Condition
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, amount);
		critBonus.makeFlat();
		
		// Creates the Condition with a duration of 0 since it is only for that heal usage
		this.selfCritBonus = new Condition("Restoration Arrow: Crit Bonus", 0);
		this.selfCritBonus.setSource(this.owner);
		this.selfCritBonus.addStatusEffect(critBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getFirstTurnScaler() {
		return this.firstTurnScaler;
	}
	
	public double getSecondTurnScaler() {
		return this.secondTurnScaler;
	}
	
	public double getThirdTurnScaler() {
		return this.thirdTurnScaler;
	}
	
	public double getMissingHealthScaler() {
		return this.missingHealthScaler;
	}
	
	public Condition getSelfCritBonus() {
		return new Condition(this.selfCritBonus);
	}
	
	// Functions for "Empowered" effects
	public int getNumStacks() {
		return this.numStacks;
	}
	public void incrementStacks() {
		this.numStacks++;
	}
	public void decrementStacks() {
		this.numStacks--;
		if (this.numStacks < 0) {
			this.numStacks = 0;
		}
	}
	public void resetStacks() {
		for (int x = 0; x < this.owner.getEmpoweredStackRequirement(); x++) {
			this.decrementStacks();
		}
	}
	public boolean isEmpowered() {
		return this.numStacks >= this.owner.getEmpoweredStackRequirement();
	}
	public void makeEmpowered() {
		this.numStacks = this.owner.getEmpoweredStackRequirement();
	}
	
	// Function to specify which version of the Ability is to be used (1 for default version, 2 for Empowered version)
	@Override
	public void use(int version) {
		// Default version, what is used as specified from the Command of this Ability
		if (version == 1) {
			this.use();
			return;
		}
		
		// Empowered version, rarely directly specified, is usually called from default version when this Ability isEmpowered().
		if (version == 2) {
			this.useEmpowered();
			return;
		}
		
		// Print a warning if this function is ever actually directly called with a wrong version number
		System.out.println("Warning: The Ability, " + this.getName() + ", does not have a use(" + version + ") function defined, but it was called!");
	}
	
	// Use(1): Default version of Ability
	@Override
	public void use() {
		// First, if we are Empowered, cast the Empowered version of this Ability instead
		if (this.isEmpowered()) {
			this.useEmpowered();
			return;
		}
		
		// Get the target of the heal
		Character affected = BattleSimulator.getInstance().targetSingle();
	    if (affected.equals(Character.EMPTY)) {
	    	return;
	    }
	    
	    // If the ability can crit (at least rank 3), see if it will crit
	    boolean isCrit = false;
	    if (this.rank() >= 3) {
	    	Attack fake = new AttackBuilder().attacker(this.owner).defender(affected).isTargeted().build();
	    	if (fake.calcCrit()) {
	    		isCrit = true;
	    	}
	    }
	    
	    // Get the base heal amount, doubled if we will crit (also notify if we crit, as restore does not notify)
	    double amount = this.getBaseScaler() * this.owner.getDamage();
	    if (isCrit) {
	    	amount *= 2;
	    	System.out.println("Restoration Arrow CRITICALLY Healed!");
	    }
	    int healAmt = (int)Math.round(amount);
	    int missingHealth = affected.getMissingHealth();
	    
	    // Heal the Character for the specified amount
 		affected.restoreHealth(healAmt);
 		
 		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
 		boolean canRandEmp = this.rank() == 15;
 		if (healAmt > missingHealth && canRandEmp) {
 			this.owner.randomlyEmpower();
 			canRandEmp = false;
 		}
 		
 		// Create the heal over time effect and add it to the affected Character (can only crit if we've crit so far and above rank 5)
 		isCrit = isCrit && this.rank() >= 5;
 		int duration = this.rank() >= 7? 3 : 2;
 		SentinelHealOT healOT = new SentinelHealOT(this.owner, affected, duration, this.getFirstTurnScaler(), this.getSecondTurnScaler(), this.getThirdTurnScaler(), this.getMissingHealthScaler(), isCrit, canRandEmp);
 		affected.addCondition(healOT);
 		
 		// Add basic "Restoration Arrow" to the set of unique abilities used
 		this.owner.addToUniqueSet("Restoration Arrow");
 		
 		// This Ability should never miss, add a stack
 		this.incrementStacks();
 		
 		// Put Restoration Arrow "on Cooldown"
		this.setOnCooldown();
		
		// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
		// Get the target of the heal
		Character affected = BattleSimulator.getInstance().targetSingle();
	    if (affected.equals(Character.EMPTY)) {
	    	return;
	    }
	    
	    // If the ability can crit (at least rank 3), see if it will crit
	    boolean isCrit = false;
	    if (this.rank() >= 3) {
	    	Attack fake = new AttackBuilder().attacker(this.owner).defender(affected).isTargeted().build();
	    	if (fake.calcCrit()) {
	    		isCrit = true;
	    	}
	    }
	    
	    // Apply the Empowered Pre Attack Bonus Condition before calculating heal amounts
	    this.owner.apply(this.owner.getEmpoweredPreAttackBonus());
	    
	    // Get the base heal amount, doubled if we will crit (also notify if we crit, as restore does not notify)
	    double amount = (this.getBaseScaler() + this.getFirstTurnScaler() + this.getSecondTurnScaler() + this.getThirdTurnScaler()) * this.owner.getDamage() * scalerPortion;
	    if (isCrit) {
	    	amount *= 2;
	    	System.out.println("Restoration Arrow CRITICALLY Healed!");
	    }
	    int healAmt = (int)Math.round(amount);
	    int missingHealth = affected.getMissingHealth();
	    
	    // Heal the Character for the specified amount
  		affected.restoreHealth(healAmt);
	    
	    // Unapply the Empowered Pre Attack Bonus Condition
	    this.owner.unapply(this.owner.getEmpoweredPreAttackBonus());
	    
	    // Add "Restoration Arrow" or "EMPOWERED Restoration Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Restoration Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Restoration Arrow");
	    }
	    
	    // Reset the stacks, then increment the stacks if Empowered Arrows is at least rank 4
  		this.resetStacks();
  		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
  			this.incrementStacks();
  		}
  		
  		// Put Restoration Arrow "on Cooldown" if Empowered Arrows is less than rank 2
  		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
  			this.setOnCooldown();
  		}
  		
  		// If we are somehow EMPOWERED after adding stacks, refresh all Cooldowns for this ability
  		if (this.isEmpowered()) {
  			this.setOffCooldownAll();
  		}
  		
  		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
  		if (healAmt > missingHealth && this.rank() == 15) {
  			this.owner.randomlyEmpower();
  		}
 		
  		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
	}
	
	
	// Override the to-String to include a description of the number of stacks and if the Ability is Empowered
	@Override
	public String toString() {
		String ori = super.toString();
		if (this.isEmpowered()) {
			return ori + " --EMPOWERED";
		}
		return ori + "  (" + this.getNumStacks() + " stack(s))";
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBase Scaler: " + this.getBaseScaler();
		ret += "\n\tFirst Turn Scaler: " + this.getFirstTurnScaler();
		ret += "\n\tSecond Turn Scaler: " + this.getSecondTurnScaler();
		ret += this.rank() >= 7? ("\n\tThird Turn Scaler: " + this.getThirdTurnScaler()) : "";
		ret += this.rank() >= 10? ("\n\tMissing Health Scaler: " + this.getMissingHealthScaler()) : "";
		ret += this.rank() >= 7? ("\n\t" + this.getSelfCritBonus()) : "";
		return ret;
	}
}
