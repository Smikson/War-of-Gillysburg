package WyattWitemeyer.WarOfGillysburg;

public class Hill extends Terrain {
	public Hill(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccBonus, int eEnemyCritBonus) {
		super("Hill");
		
		// Create the requirement
		DualRequirement melee = (Character withEffect, Character other) -> {
			return (other.getRange() == 1);
		};
		DualRequirement nonMelee = (Character withEffect, Character other) -> {
			return (other.getRange() > 1);
		};
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, 15 + eAccBonus, StatusEffectType.BASIC);
		
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, 5 + eCritBonus, StatusEffectType.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(StatVersion.DODGE, 10 + eDodgeBlockBonus, StatusEffectType.INCOMING);
		dodgeBonus.setDualRequirement(melee);
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, 10 + eDodgeBlockBonus, StatusEffectType.INCOMING);
		blockBonus.setDualRequirement(melee);
		
		StatusEffect enemyAccuracyBonus = new StatusEffect(StatVersion.ACCURACY, 15 + eEnemyAccBonus, StatusEffectType.INCOMING);
		enemyAccuracyBonus.setDualRequirement(nonMelee);
		enemyAccuracyBonus.makeAffectOther();
		StatusEffect enemyCritBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, 5 + eEnemyCritBonus, StatusEffectType.INCOMING);
		enemyCritBonus.setDualRequirement(nonMelee);
		enemyCritBonus.makeAffectOther();
		
		this.addStatusEffect(accuracyBonus);
		this.addStatusEffect(critBonus);
		this.addStatusEffect(dodgeBonus);
		this.addStatusEffect(blockBonus);
		this.addStatusEffect(enemyAccuracyBonus);
	}
	public Hill() {
		this(0,0,0,0,0);
	}
}
