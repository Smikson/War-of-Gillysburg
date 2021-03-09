package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

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
		
		// Creates the StatusEffect for bonus Damage and Accuracy Conditions to be added to the respective Abilities attack(s)
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Create the Condition, duration of 0 since its only used for the one attack
		this.abilityPreAttackBonus = new Condition("Empowered Arrows: Pre Attack Damage Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(dmgBonus);
		this.abilityPreAttackBonus.addStatusEffect(accBonus);
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
	
	// Use function called when the action is chosen from the possible Commands (uses multiple empowered abilities)
	@Override
	public void use() {
		// If unavailable (no abilities currently empowered), state the issue and immediately return
		if (!this.owner.hasEmpoweredAbility()) {
			System.out.println("There are no empowered abilities, select a different action.");
			return;
		}
		
		// Get the available Empowered Abilities as options for multiple selection
		LinkedList<String> abilityChoices = new LinkedList<>();
		for (int abilityNum : this.owner.getEmpoweredAbilities()) {
			if (abilityNum == 1) {
				abilityChoices.add("Flaming Arrow");
			}
			else if (abilityNum == 2) {
				abilityChoices.add("Frozen Arrow");
			}
			else if (abilityNum == 3) {
				abilityChoices.add("Exploding Arrow");
			}
			else if (abilityNum == 4) {
				abilityChoices.add("Penetration Arrow");
			}
			else if (abilityNum == 5) {
				abilityChoices.add("Restoration Arrow");
			}
		}
		
		// Select the Empowered Abilities that will be used
		LinkedList<Integer> toUse = new LinkedList<>();
		toUse = BattleSimulator.getInstance().promptSelectMultiple("Select all available Empowered Abilities you wish to use this turn.", abilityChoices);
		//DE Exit Point!!
		// Use each ability marked in toUse (holds index from owner's list of empowered abilities (toUse is 1-indexed)
		for (int index : toUse) {
			int ability = this.owner.getEmpoweredAbilities().get(index - 1);
			// Flaming Arrow
			if (ability == 1) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.FlamingArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Frozen Arrow
			else if (ability == 2) {
				//DE use Frozen Arrow
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.FrozenArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Exploding Arrow
			else if (ability == 3) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.ExplodingArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Penetration Arrow
			else if (ability == 4) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.PenetrationArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Restoration Arrow
			else if (ability == 5) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.RestorationArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
		}
		
		// Lastly, using this Ability in this manner also uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Overrides the to-String to more correctly display the function of this Ability's use function
	public String toString() {
		return "Passive: \"Empowered Arrows\" (Use multiple EMPOWERED Abilities) - " + (this.owner.hasEmpoweredAbility()? "Available" : "Unavailable");
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 3? ("\n\tMultiple Ability Scaler: " + this.getMultAbilityScaler()) : "";
		ret += "\n\tStack Requirement: " + this.getStackRequirement();
		ret += "\n\t" + this.getAbilityPreAttackBonus();
		return ret;
	}
}
