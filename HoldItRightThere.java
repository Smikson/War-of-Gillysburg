package WyattWitemeyer.WarOfGillysburg;

public class HoldItRightThere extends Ability {
	// Additional Effects of Ability
	private StatusEffect blockBonus;
	private StatusEffect damageBonus;
	
	public HoldItRightThere(int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.name = "Unique Passive Ability: \"Hold It Right There!\"";
		this.rank = rank;
		
		// Calculate the additional Staus Effects
		this.blockBonus = this.calculateBlockBonus();
		this.damageBonus = this.calculateDamageBonus();
	}
	
	
	// Additional Status Effects for this Ability:
	
	// Increased Block when blocking for an ally
	private StatusEffect calculateBlockBonus() {
		// Find block amount based on rank
		int amount = 0;
		for (int i = 1; i <= this.rank; i++) {
			// Ranks 2,5 grant +10% increased Block
			if (i == 2 || i == 5) {
				amount += 10;
			}
			// Ranks 3,4 grant +5% increased Block
			if (i == 3 || i == 4) {
				amount += 5;
			}
		}
		
		// Return the Calculated Status Effect
		StatusEffect calcBlockBonus = new StatusEffect("Hold It Right There Block Bonus", -1, Stat.BLOCK, amount);
		calcBlockBonus.makePermanent();
		return calcBlockBonus;
	}
	
	// Increased damage to halted enemies
	// NOTE: This Ability does not yet implement the Character Requirement for increased Damage.
	private StatusEffect calculateDamageBonus() {
		// Find damage bonus amount based on rank
		int amount = 0;
		if (this.rank == 4) {
			amount = 20;
		}
		if (this.rank == 5) {
			amount = 30;
		}
		
		// Return the Calulated Status Effect
		StatusEffect calcDamageBonus = new StatusEffect("Hold It Right There Damage Bonus", 2, Stat.DAMAGE, amount);
		calcDamageBonus.makeIncoming();
		return calcDamageBonus;
	}
	
	// Get methods for additional effects
	public StatusEffect getBlockBonus() {
		return this.blockBonus;
	}
	public StatusEffect getDamageBonus() {
		return this.damageBonus;
	}
}
