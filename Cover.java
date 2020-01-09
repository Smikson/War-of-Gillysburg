package WyattWitemeyer.WarOfGillysburg;

public class Cover extends Terrain {
	public Cover(int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("Cover");
		
		// Create the requirement
		DualRequirement nonMelee = (Character withEffect, Character other) -> {
			return (other.getRange() > 1);
		};
		
		// Add Bonuses
		StatusEffect dodgeBonus = new StatusEffect(StatVersion.DODGE, 5 + eDodgeBlockBonus, StatusEffectType.INCOMING);
		dodgeBonus.setDualRequirement(nonMelee);
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, 5 + eDodgeBlockBonus, StatusEffectType.INCOMING);
		blockBonus.setDualRequirement(nonMelee);
		
		StatusEffect accuracyDeduction = new StatusEffect(StatVersion.ACCURACY, -5 + eEnemyAccDeduction, StatusEffectType.INCOMING);
		accuracyDeduction.setDualRequirement(nonMelee);
		accuracyDeduction.makeAffectOther();
		
		this.addStatusEffect(dodgeBonus);
		this.addStatusEffect(blockBonus);
		this.addStatusEffect(accuracyDeduction);
	}
	public Cover() {
		this(0,0);
	}
}
