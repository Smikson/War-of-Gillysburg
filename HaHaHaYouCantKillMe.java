package WyattWitemeyer.WarOfGillysburg;

public class HaHaHaYouCantKillMe extends Ability {
	// Additional variables for the Ability
	private StatusEffect armorBonus;
	private StatusEffect damageReduction;
	private StatusEffect damageBonus;
	
	
	// NOTE: This Ability does not yet implement the Character Requirement for decreased Damage.
	public HaHaHaYouCantKillMe(int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.name = "Ability 5 (ULTIMATE): \"HaHaHa You Can't Kill Me!\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the additional effects of the Ability
		this.setArmorBonus();
		this.setDamageReduction();
		this.setDamageBonus();
	}
	
	// Calculates the values for the additional Effects for this Ability
	public void setArmorBonus() {
		// At rank 1, this increases Armor by 25% for 2 turns
		int amount = 25;
		int duration = 2;
		// Increased to 30% at rank 2
		if (this.rank == 2) {
			amount = 30;
		}
		// Increased to 50% at rank 3 for 3 turns
		else if (this.rank >= 3) {
			amount = 50;
			duration = 3;
		}
		this.armorBonus = new StatusEffect("HaHaHa You Can't Kill Me Armor Bonus", duration, Stat.ARMOR, amount);
	}
	// This currently only deals with the incoming damage reduction for Normal/Advanced enemies, but currently affects all
	public void setDamageReduction() {
		// At rank 1, this does not occur
		int amount = 0;
		int duration = 0;
		// Increased to 50% at rank 2 for 2 turns
		if (this.rank >= 2) {
			amount = 50;
			duration = 2;
		}
		this.damageReduction = new StatusEffect("HaHaHa You Can't Kill Me Damage Reduction", duration, Stat.DAMAGE, -amount);
		this.damageReduction.makeIncoming();
	}
	
	public void setDamageBonus() {
		// Only occurs at rank 3
		int amount = 0;
		int duration = 0;
		if (this.rank >= 3) {
			amount = 50;
			duration = 1;
		}
		this.damageBonus = new StatusEffect("HaHaHa You Can't Kill Me Damage Bonus", duration, Stat.DAMAGE, amount);
	}
	
	// Get methods for the additional variables of this Ability
	public StatusEffect getArmorBonus() {
		return this.armorBonus;
	}
	public StatusEffect getDamageReduction() {
		return this.damageReduction;
	}
	public StatusEffect getDamageBonus() {
		return this.damageBonus;
	}
}
