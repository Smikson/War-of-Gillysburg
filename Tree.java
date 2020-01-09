package WyattWitemeyer.WarOfGillysburg;

public class Tree extends Terrain {
	public Tree(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("Tree");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, 15 + eAccBonus, StatusEffectType.BASIC);
		
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, 5 + eCritBonus, StatusEffectType.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(StatVersion.DODGE, 15 + eDodgeBlockBonus, StatusEffectType.BASIC);
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, 15 + eDodgeBlockBonus, StatusEffectType.BASIC);
		
		StatusEffect enemyAccuracyDeduction = new StatusEffect(StatVersion.ACCURACY, -10 + eEnemyAccDeduction, StatusEffectType.INCOMING);
		enemyAccuracyDeduction.makeAffectOther();
		
		this.addStatusEffect(accuracyBonus);
		this.addStatusEffect(critBonus);
		this.addStatusEffect(dodgeBonus);
		this.addStatusEffect(blockBonus);
		this.addStatusEffect(enemyAccuracyDeduction);
	}
	public Tree() {
		this(0,0,0,0);
	}
}
