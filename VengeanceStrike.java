package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class VengeanceStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables needed for effects of the Ability
	public HashMap<Character,Integer> counter;
	
	private Condition enemyDamageReduction;
	
	// Constructor
	public VengeanceStrike(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \\\"Vengeance Strike\\\"\"", source, rank);
		this.owner = source;
		
		// Set scaler and counter
		this.setScaler();
		counter = new HashMap<Character,Integer>();
		
		// Calculate the additional Staus Effects
		this.setEnemyDamageReduction();
	}
	
	// Calculates the basic values for this Ability
	private void setScaler() {
		// Checks based on this Ability's rank
		if (this.rank() >= 3) {
			this.scaler = 1.5;
		}
		else if (this.rank() == 2) {
			this.scaler = 1.3;
		}
		else {
			this.scaler = 1.2;
		}
	}
	
	// Functions for setting Conditions
	private void setEnemyDamageReduction() {
		// Set the amount of Damage Reduction
		int amount = 0;
		// Checks based on this Ability's rank (0 at rank 1, increases to 10% at rank 2, then by 5% each rank
		for (int walker = 2; walker <= this.rank(); walker++) {
			if (walker == 2) {
				amount += 10;
			}
			else if (walker <= 5) {
				amount += 5;
			}
		}
		
		// Create the Dual Requirement needed for it to only be in effect vs the owner
		DualRequirement isOwner = (Character withEffect, Character other) -> {
			return other.equals(this.owner);
		};
		
		// Create the Status Effect
		StatusEffect dmgRed = new StatusEffect(Stat.Version.DAMAGE, -amount, StatusEffect.Type.OUTGOING);
		dmgRed.makePercentage();
		dmgRed.setDualRequirement(isOwner);
		
		// Create the Condition that contains this effect for only 1 turn (ends end of turn)
		this.enemyDamageReduction = new Condition("Vengeance Strike: Damage Reduction", 1);
		this.enemyDamageReduction.setSource(this.owner);
		this.enemyDamageReduction.addStatusEffect(dmgRed);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public Condition getEnemyDamageReduction() {
		return this.enemyDamageReduction;
	}
	
	// Clears the counter map (should occur at the beginning of each turn for ranks 1 and 2)
	public void clearCounterMap() {
		this.counter.clear();
	}
	
	// Creates an execute function to make the Vengeance Strike attack, applying the correct Conditions
	public void execute(Character enemy) {
		// Keep track of wether or not the attack should occur (can be overridden by user)
		boolean shouldAttack = true;
		
		// At rank 1, the attack can only be used once per turn (and shouldn't be used unless the counter map is empty)
		if (this.rank() == 1 && !this.counter.isEmpty()) {
			shouldAttack = false;
		}
		// At rank 2, the attack can only be used once per enemy
		if (this.rank() == 2 && this.counter.get(enemy) != 1) {
			shouldAttack = false;
		}
		// At rank 3, the attack has no limitations except Ranged enemies out of range.
		
		// Tell the user if the attack should(n't) occur, and prompt for usage
		String negation = shouldAttack? "" : "NOT";
		System.out.println(this.getOwner().getName() + " should " + negation + " use Vengeance Strike. Proceed with attack?");
		if (!BattleSimulator.getInstance().askYorN()) {
			return;
		}
		
		// At this point the attack occurs.
		// If the rank is 4 or 5, the Ability uses a version of Flip Strike for the attack (accessible by use(2))
		if (this.rank() >= 4) {
			this.getOwner().useVengeanceFlipStrike(enemy);
		}
		// Otherwise, do the normal Vengeance Strike attack
		else {
			// Build the attack
			Attack VenStr = new AttackBuilder()
					.attacker(this.getOwner())
					.defender(enemy)
					.type(Attack.DmgType.SLASHING)
					.isTargeted()
					.scaler(this.scaler)
					.build();
			VenStr.execute();
		}
	}
}
