package WyattWitemeyer.WarOfGillysburg;

public class Hill extends Terrain {
	public Hill(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccBonus, int eEnemyCritBonus) {
		super("Hill");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect("Accuracy Bonus", -1, Stat.ACCURACY, 15 + eAccBonus);
		
		StatusEffect critBonus = new StatusEffect("Critical Chance Bonus", -1, Stat.CRITICAL_CHANCE, 5 + eCritBonus);
		
		StatusEffect dodgeBonus = new StatusEffect("Melee Dodge Bonus", -1, Stat.DODGE, 10 + eDodgeBlockBonus);
		dodgeBonus.addRequirementEqualTo(false, Stat.RANGE, 1);
		StatusEffect blockBonus = new StatusEffect("Melee Block Bonus", -1, Stat.BLOCK, 10 + eDodgeBlockBonus);
		blockBonus.addRequirementEqualTo(false, Stat.RANGE, 1);
		
		StatusEffect enemyAccuracyBonus = new StatusEffect("Incoming Ranged Accuracy Bonus", -1, Stat.ACCURACY, 15 + eEnemyAccBonus);
		enemyAccuracyBonus.addRequirementGreaterThan(false, Stat.RANGE, 1);
		enemyAccuracyBonus.makeIncoming();
		StatusEffect enemyCritBonus = new StatusEffect("Incoming Ranged Crit Bonus", -1, Stat.CRITICAL_CHANCE, 5 + eEnemyCritBonus);
		enemyCritBonus.addRequirementGreaterThan(false, Stat.RANGE, 1);
		enemyCritBonus.makeIncoming();
		
		this.effects.add(accuracyBonus);
		this.effects.add(critBonus);
		this.effects.add(dodgeBonus);
		this.effects.add(blockBonus);
		this.effects.add(enemyAccuracyBonus);
	}
	public Hill() {
		this(0,0,0,0,0);
	}
}
