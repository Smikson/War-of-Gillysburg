package WyattWitemeyer.WarOfGillysburg;

import java.util.LinkedList;

public class Sweep extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private Slow slow;
	
	// Constructor
	public Sweep(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Sweep\"", source, rank);
		this.owner = source;
		
		// Set the damage scaler and Cooldown of the ability
		this.setCooldown();
		this.setScaler();
		
		// Calculate the slow effect
		this.setSlow();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 7
		this.cooldown = 4;
		if (this.rank() >= 7) {
			this.cooldown = 3;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates the damage scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = .8;
		
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .8;
				break;
			case 2:
				this.scaler = 1;
				break;
			case 3:
				this.scaler = 1.2;
				break;
			case 4:
				this.scaler = 1.4;
				break;
			case 5:
				this.scaler = 1.5;
				break;
			case 6:
				this.scaler = 1.7;
				break;
			case 7:
				this.scaler = 1.8;
				break;
			case 8:
				this.scaler = 2;
				break;
			case 9:
				this.scaler = 2.3;
				break;
			case 10:
				this.scaler = 2.5;
				break;
		}
	}
	
	// Calculates and creates the slow condition
	private void setSlow() {
		// Declare the starting amount and duration
		int amount = 0;
		int duration = 0;
		
		// Calculate the slow amount (-2 for rank 3+, -3 for rank 10) and duration (1 for rank 3+, 2 for rank 5+)
		if (this.rank() >= 3) {
			amount = 2;
			duration = 1;
		}
		if (this.rank() >= 5) {
			duration = 2;
		}
		if (this.rank() == 10) {
			amount = 3;
		}
		
		// Create the slow effect
		this.slow = new Slow("Sweep: Slow", duration, amount);
		this.slow.setSource(this.owner);
		this.slow.makeEndOfTurn();
	}
	
	// Get method for the slow
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	public Slow getSlow() {
		return this.slow;
	}
	
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select enemies to attack
		System.out.println("Choose enemies hit by attack:");
    	LinkedList<Character> enemies = BattleSimulator.getInstance().targetMultiple();
        if (enemies.isEmpty()) {
        	return;
        }
        if (enemies.contains(Character.EMPTY)) {
        	enemies.clear();
        }
		
		// Before anything, put Sweep "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack and apply the slow against all enemies affected
		for (Character enemy : enemies) {
			// Applies the attack
			Attack sweepAtk = new AttackBuilder()
					.attacker(this.owner)
					.defender(enemy)
					.isAOE()
					.scaler(this.getScaler())
					.type(Attack.DmgType.SLASHING)
					.range(Attack.RangeType.MELEE)
					.build();
			if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
				sweepAtk = this.getOwner().getDeflectionVersion(sweepAtk);
			}
			sweepAtk.execute();
			
			// Applies the slow
			enemy.addCondition(this.getSlow());
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
}
