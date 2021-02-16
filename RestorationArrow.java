package WyattWitemeyer.WarOfGillysburg;

// Hidden Ability: "Restoration Arrow"
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
		// The Cooldown is always 6 until rank 5 where it is reduced to 5
		this.cooldown = 6;
		if (this.rank() >= 5) {
			this.cooldown = 5;
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
	
	// Functions for 'Empowered" effects
	public int getNumStacks() {
		return this.numStacks;
	}
	public void incrementStacks() {
		this.numStacks++;
	}
	public boolean isEmpowered() {
		return this.numStacks >= this.owner.getEmpoweredStackRequirement();
	}
	public void makeEmpowered() {
		this.numStacks = this.owner.getEmpoweredStackRequirement();
	}
	
	//DE When the use function is called in a different Ability it needs to call a function from the main class to add it to the list
	//DE Each Ability will need to call something from the base Sentinel Specialist to get the scaler bonus from here
	
	
	//DE Same need another class that extends DamageOverTime but instead Heals over time based on the scalers
	
	
	//DE in use, set numStacks + or - 1 based on rank of EmpoweredArrows and include use2 for using the Empowered version
	
	
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
