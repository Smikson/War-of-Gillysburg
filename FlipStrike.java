package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class FlipStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private double noMissChance;
	private Condition preAttackBonus;
	
	// Constructor
	public FlipStrike(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Flip Strike\"", source, rank);
		this.owner = source;
		
		// Calculate and set the damage scaler
		this.setCooldown();
		this.setScaler();
		
		// Calculate and set the chance to not miss and the pre-attack Condition
		this.setNoMissChance();
		this.setPreAttackBonus();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 5
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		// The Ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates and sets the damage scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = 1.5;
		
		// Set the scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.scaler = 1.5;
				break;
			case 2:
				this.scaler = 1.7;
				break;
			case 3:
				this.scaler = 1.8;
				break;
			case 4:
				this.scaler = 2;
				break;
			case 5:
				this.scaler = 2.2;
				break;
			case 6:
				this.scaler = 2.3;
				break;
			case 7:
				this.scaler = 2.5;
				break;
			case 8:
				this.scaler = 2.7;
				break;
			case 9:
				this.scaler = 2.8;
				break;
			case 10:
				this.scaler = 3;
				break;
		}
	}
	
	// Calculates and sets the additional effects
	private void setNoMissChance() {
		// Default value is 0
		this.noMissChance = 0;
		
		// Rank 7 increases the chance to 15%
		if (this.rank() >= 7) {
			this.noMissChance = .15;
		}
		// Rank 9 increases the chance to 20%
		if (this.rank() >= 9) {
			this.noMissChance = .20;
		}
		// Rank 10 increases the chance to 25%
		if (this.rank() == 10) {
			this.noMissChance = .25;
		}
	}
	
	private void setPreAttackBonus() {
		// Calculates the amount of increased Armor Piercing based on rank
		int apAmount = 10;
		switch(this.rank()) {
			case 1:
				apAmount = 10;
				break;
			case 3:
			case 2:
				apAmount = 12;
				break;
			case 4:
				apAmount = 14;
				break;
			case 5:
				apAmount = 15;
				break;
			case 6:
				apAmount = 17;
				break;
			case 7:
				apAmount = 18;
				break;
			case 8:
				apAmount = 20;
				break;
			case 9:
				apAmount = 22;
				break;
			case 10:
				apAmount = 25;
				break;
		}
		
		// Creates the StatusEffect
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, apAmount, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		
		// Creates the Condition
		this.preAttackBonus = new Condition("Flip Strike: Pre-Attack Bonus", 0);
		this.preAttackBonus.setSource(this.owner);
		this.preAttackBonus.addStatusEffect(apBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public double getNoMissChance() {
		return this.noMissChance;
	}
	
	public Condition getPreAttackBonus() {
		return this.preAttackBonus;
	}
	
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
        // Select the target enemy
 		Enemy enemy = BattleSimulator.getInstance().targetSingleEnemy();
	    if (enemy.equals(Enemy.EMPTY)) {
	    	return;
	    }
		
		// Before anything, put Flip Strike "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack against the targeted enemy with the self-buff
		Attack flipAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.range(Attack.RangeType.MELEE)
				.addAttackerCondition(this.getPreAttackBonus())
				.build();
		if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
			flipAtk = this.getOwner().getDeflectionVersion(flipAtk);
		}
		// Above rank 7, there is a chance the attack cannot miss
		if (this.rank() >= 7) {
			// Get the chance the attack cannot miss based on the enemy difficulty
			double chance = this.getNoMissChance();
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE) && this.rank() < 10) {
				chance /= 2.0;
			}
			else if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS) && this.rank() < 10) {
				chance = 0;
			}
			
			// Get a random probability
			Random rd = new Random();
			double prob = rd.nextDouble();
			
			// If the probability falls under the chance, it is a success
			if (prob <= chance) {
				flipAtk = new AttackBuilder(flipAtk).cannotMiss().build();
			}
		}
		flipAtk.execute();
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	public void useVengeanceStrikeVersion(Attack original) {
		// Make the attack against the targeted enemy with the self-buff based on the original VengeanceStrike
		Enemy enemy = (Enemy)original.getDefender(); // Guaranteed to be of type Enemy due to Vengeance Strike check
		Attack flipAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.range(Attack.RangeType.MELEE)
				.addAttackerCondition(this.getPreAttackBonus())
				.build();
		if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
			flipAtk = this.getOwner().getDeflectionVersion(flipAtk);
		}
		
		// Above rank 7, there is a chance the attack cannot miss
		if (this.rank() >= 7) {
			// Get the chance the attack cannot miss based on the enemy difficulty
			double chance = this.getNoMissChance();
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE) && this.rank() < 10) {
				chance /= 2.0;
			}
			else if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS) && this.rank() < 10) {
				chance = 0;
			}
			
			// Get a random probability
			Random rd = new Random();
			double prob = rd.nextDouble();
			
			// If the probability falls under the chance, it is a success
			if (prob <= chance) {
				flipAtk = new AttackBuilder(flipAtk).cannotMiss().build();
			}
		}
		// If Vengeance Strike is rank 5, the scaler is increased by 50% (based on Vengeance Strike scaler) and there is a bonus 30% lifesteal
		if (this.getOwner().getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike) == 5) {
			flipAtk = new AttackBuilder(flipAtk).scaler(this.getScaler() * original.getScaler()).lifestealPercentage(30).build();
		}
		flipAtk.execute();
	}
}
