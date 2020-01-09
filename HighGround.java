package WyattWitemeyer.WarOfGillysburg;

public class HighGround extends Terrain {
	public HighGround(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("High Ground");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, 10 + eAccBonus, StatusEffectType.BASIC);
		
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, 3 + eCritBonus, StatusEffectType.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(StatVersion.DODGE, 10 + eDodgeBlockBonus, StatusEffectType.BASIC);
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, 10 + eDodgeBlockBonus, StatusEffectType.BASIC);
		
		StatusEffect accuracyDeduction = new StatusEffect(StatVersion.ACCURACY, -5 + eEnemyAccDeduction, StatusEffectType.INCOMING);
		accuracyDeduction.makeAffectOther();
		
		this.addStatusEffect(accuracyBonus);
		this.addStatusEffect(critBonus);
		this.addStatusEffect(dodgeBonus);
		this.addStatusEffect(blockBonus);
		this.addStatusEffect(accuracyDeduction);
	}
	public HighGround() {
		this(0,0,0,0);
	}
}
