package WyattWitemeyer.WarOfGillysburg;

// Terrain are permanent Conditions (until manually removed), and have a set type: HighGround, Hill, Cover, or Tree
public class Terrain extends Condition {
	// List of static Terrains to be used for basic computations
	public static final HighGround HIGH_GROUND = new HighGround();
	public static final Hill HILL = new Hill();
	public static final Cover COVER = new Cover();
	public static final Tree TREE = new Tree();
	
	
	// Constructors - Terrain are permanent bonuses (only removed manually)
	public Terrain(String name) {
		super(name, -1);
		this.makePermanent();
	}
	public Terrain() {
		super();
	}
}

// High Ground Condition
class HighGround extends Terrain {
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

// Hill Condition
class Hill extends Terrain {
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

// Cover Condition
class Cover extends Terrain {
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

// Tree Condition
class Tree extends Terrain {
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
