package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

// Terrain are permanent Conditions (until manually removed), and have a set type: HighGround, Hill, Cover, or Tree
public class Terrain extends Condition {
	// List of static Terrains to be used for basic computations
	public static final HighGround HIGH_GROUND = new HighGround();
	public static final Hill HILL = new Hill();
	public static final Cover COVER = new Cover();
	public static final Tree TREE = new Tree();
	
	public static final LinkedList<Terrain> TERRAINLIST = getList();
	
	// Constructors - Terrain are permanent bonuses (only removed manually)
	public Terrain(String name) {
		super(name, -1);
		this.makePermanent();
	}
	public Terrain() {
		super();
	}
	
	private final static LinkedList<Terrain> getList() {
		LinkedList<Terrain> terrainList = new LinkedList<>();
		terrainList.add(HIGH_GROUND);
		terrainList.add(HILL);
		terrainList.add(COVER);
		terrainList.add(TREE);
		return terrainList;
	}
}

// High Ground Condition
class HighGround extends Terrain {
	public HighGround(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("High Ground");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, 10 + eAccBonus, StatusEffect.Type.BASIC);
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, 3 + eCritBonus, StatusEffect.Type.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(Stat.Version.DODGE, 10 + eDodgeBlockBonus, StatusEffect.Type.BASIC);
		StatusEffect blockBonus = new StatusEffect(Stat.Version.BLOCK, 10 + eDodgeBlockBonus, StatusEffect.Type.BASIC);
		
		StatusEffect accuracyDeduction = new StatusEffect(Stat.Version.ACCURACY, -5 + eEnemyAccDeduction, StatusEffect.Type.INCOMING);
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
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, 15 + eAccBonus, StatusEffect.Type.BASIC);
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, 5 + eCritBonus, StatusEffect.Type.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(Stat.Version.DODGE, 10 + eDodgeBlockBonus, StatusEffect.Type.INCOMING);
		dodgeBonus.setDualRequirement(melee);
		StatusEffect blockBonus = new StatusEffect(Stat.Version.BLOCK, 10 + eDodgeBlockBonus, StatusEffect.Type.INCOMING);
		blockBonus.setDualRequirement(melee);
		
		StatusEffect enemyAccuracyBonus = new StatusEffect(Stat.Version.ACCURACY, 15 + eEnemyAccBonus, StatusEffect.Type.INCOMING);
		enemyAccuracyBonus.setDualRequirement(nonMelee);
		enemyAccuracyBonus.makeAffectOther();
		StatusEffect enemyCritBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, 5 + eEnemyCritBonus, StatusEffect.Type.INCOMING);
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
		StatusEffect dodgeBonus = new StatusEffect(Stat.Version.DODGE, 5 + eDodgeBlockBonus, StatusEffect.Type.INCOMING);
		dodgeBonus.setDualRequirement(nonMelee);
		StatusEffect blockBonus = new StatusEffect(Stat.Version.BLOCK, 5 + eDodgeBlockBonus, StatusEffect.Type.INCOMING);
		blockBonus.setDualRequirement(nonMelee);
		
		StatusEffect accuracyDeduction = new StatusEffect(Stat.Version.ACCURACY, -5 + eEnemyAccDeduction, StatusEffect.Type.INCOMING);
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
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, 15 + eAccBonus, StatusEffect.Type.BASIC);
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, 5 + eCritBonus, StatusEffect.Type.BASIC);
		
		StatusEffect dodgeBonus = new StatusEffect(Stat.Version.DODGE, 15 + eDodgeBlockBonus, StatusEffect.Type.BASIC);
		StatusEffect blockBonus = new StatusEffect(Stat.Version.BLOCK, 15 + eDodgeBlockBonus, StatusEffect.Type.BASIC);
		
		StatusEffect enemyAccuracyDeduction = new StatusEffect(Stat.Version.ACCURACY, -10 + eEnemyAccDeduction, StatusEffect.Type.INCOMING);
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
