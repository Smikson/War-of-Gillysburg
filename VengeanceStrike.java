package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class VengeanceStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables needed for effects of the Ability
	public HashMap<Character,Integer> counter;
	
	private Condition enemyDamageReduction;
	private Condition selfPreAttackDmgBonus;
	
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
		this.setSelfPreAttackDmgBonus();
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
	
	private void setSelfPreAttackDmgBonus() {
		// Only occurs at 30% at rank 5
		int amount = 0;
		if (this.rank() >= 5) {
			amount = 30;
		}
		
		// Create the Status Effect
		StatusEffect dmgBuff = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBuff.makePercentage();
		
		// Create the Condition that contains this effect (duration of 0 since immediately unapplied)
		this.selfPreAttackDmgBonus = new Condition("Vengeance Strike: Pre Attack Bonus", 0);
		this.selfPreAttackDmgBonus.setSource(this.owner);
		this.selfPreAttackDmgBonus.addStatusEffect(dmgBuff);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public Condition getEnemyDamageReduction() {
		return this.enemyDamageReduction;
	}
	
	public Condition getSelfPreAttackDmgBonus() {
		return this.selfPreAttackDmgBonus;
	}
}
