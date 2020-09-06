package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

// Implementation of lambda function for altering attached attacks.
interface AttachedAlteration {
	// Function to define an AttachedAlteration to return a new (what will be "attached") Attack for a given AttackResult from the Attack is "attached" to
	Attack evaluate(AttackResult atkRes);
	
	// Default return the EMPTY Attack
	default Attack evalDefault() {
		return Attack.EMPTY;
	}
}

public class Attack {
	// Enums to denote additional types of variables specific for Attacks
	public static enum DmgType {
		TRUE, SLASHING, SMASHING, PIERCING, FLEX, MAGIC, FIRE, ICE, ELECTRIC, ARCANE, POISON, BLEED, EXPLOSIVE, LIGHT, NECROMANTIC;
	}
	
	public static enum RangeType {
		MELEE, RANGED, OTHER
	}
	
	// Constant for Empty Attacks to build up with
	public static final Attack EMPTY = new Attack();
	
	// Variables for each element of an attack
	private Character attacker;		// The Character attacking
	private Character defender;		// The Chatacter being attacked
	private Attack.DmgType type;	// The damage type of the attack made
	private Attack.RangeType range; // The range type of the attack made
	private boolean usesScaler;		// Determines if the attack uses a scaler for damage (true) or a specified flat numeric amount (false)
	private double scaler;			// If a scaler is used (the usual), holds the Damage scaler of the attack
	private int flatDamage;			// If a scaler is not used, holds the specified flat numeric damage amount
	private boolean isTargeted;		// Determines if Targeted (true) or AOE (false)
	private boolean canHit;			// Determines if the attack has the capability of hitting (if somehow canMiss and canHit are both false, this technically has priority)
	private boolean canMiss;		// Determines if the attack has the capability of being dodged or blocked
	private boolean canCrit;		// Determines if the attack has the capability of critically striking
	private boolean guaranteedCrit;	// Determines if the attack is a guaranteed critical strike
	private boolean ignoresArmor;	// Determines if the attack "ignores all armor"
	private boolean hasDeviation;	// Determines if the attack uses STDup and STDdown to deviate the attack
	
	// Additional variables for single-use (this attack only) Conditions to apply and Conditions to apply to the defender after an attack
	private int vorpalChance;
	private double vorpalMultiplier;
	private double lifesteal;
	private LinkedList<Condition> attackerConditions;
	private LinkedList<Condition> defenderConditions;
	private LinkedList<Condition> successAttackConditions;
	private LinkedList<Condition> failAttackConditions;
	
	// Additional variables for attacks "attached" to this Attack and, if this Attack is the attached, for any necessary alterations
	private LinkedList<Attack> attachedAttacks;
	private boolean hasAlteration;
	private AttachedAlteration alteration;
	
	// Helper variable for vorpal hits
	private boolean isVorpal;
	
	// Constructors
	public Attack(Character attacker, Character defender, Attack.DmgType type, Attack.RangeType range, boolean usesScaler, double scaler, int flatDamage, boolean isTargeted, boolean canHit, boolean canMiss, boolean canCrit, boolean guaranteedCrit, boolean ignoresArmor, boolean hasDeviation, int vChance, double vDmg, double lifesteal, LinkedList<Condition> atkCons, LinkedList<Condition> defCons, LinkedList<Condition> sucAtkCons, LinkedList<Condition> failAtkCons, LinkedList<Attack> attached, boolean hasAlteration, AttachedAlteration alteration) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = type;
		this.range = range;
		this.usesScaler = usesScaler;
		this.scaler = scaler;
		this.flatDamage = flatDamage;
		this.isTargeted = isTargeted;
		this.canHit = canHit;
		this.canMiss = canMiss;
		this.canCrit = canCrit;
		this.guaranteedCrit = guaranteedCrit;
		this.ignoresArmor = ignoresArmor;
		this.hasDeviation = hasDeviation;
		
		this.vorpalChance = vChance;
		this.vorpalMultiplier = vDmg;
		this.lifesteal = lifesteal;
		this.attackerConditions = atkCons;
		this.defenderConditions = defCons;
		this.successAttackConditions = sucAtkCons;
		this.failAttackConditions = failAtkCons;
		
		this.attachedAttacks = attached;
		this.hasAlteration = hasAlteration;
		this.alteration = alteration;
		
		this.isVorpal = false;
	}
	public Attack() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
		this.type = Attack.DmgType.TRUE;
		this.range = Attack.RangeType.OTHER;
		this.usesScaler = true;
		this.scaler = 1.0;
		this.flatDamage = 0;
		this.isTargeted = true;
		this.canHit = true;
		this.canMiss = true;
		this.canCrit = true;
		this.guaranteedCrit = false;
		this.ignoresArmor = false;
		this.hasDeviation = true;
		
		this.vorpalChance = 0;
		this.vorpalMultiplier = 1.0;
		this.lifesteal = 0;
		this.attackerConditions = new LinkedList<>();
		this.defenderConditions = new LinkedList<>();
		this.successAttackConditions = new LinkedList<>();
		this.failAttackConditions = new LinkedList<>();
		
		this.attachedAttacks = new LinkedList<>();
		this.hasAlteration = false;
		this.alteration = (AttackResult atkRes) -> {return Attack.EMPTY;};
		
		this.isVorpal = false;
	}
	
	// Get methods for each element
	public Character getAttacker() {
		return this.attacker;
	}
	public Character getDefender() {
		return this.defender;
	}
	public Attack.DmgType getDmgType() {
		return this.type;
	}
	public Attack.RangeType getRangeType() {
		return this.range;
	}
	public boolean usesScaler() {
		return this.usesScaler;
	}
	public double getScaler() {
		return this.scaler;
	}
	public int getFlatDamageAmount() {
		return this.flatDamage;
	}
	public boolean isTargeted() {
		return this.isTargeted;
	}
	public boolean isAOE() {
		return !this.isTargeted();
	}
	public boolean canHit() {
		return this.canHit;
	}
	public boolean canMiss() {
		return this.canMiss;
	}
	public boolean canCrit() {
		return this.canCrit;
	}
	public boolean guaranteedCrit() {
		return this.guaranteedCrit;
	}
	public boolean ignoresArmor() {
		return this.ignoresArmor;
	}
	public boolean hasDeviation() {
		return this.hasDeviation;
	}
	
	public int getVorpalChance() {
		return this.vorpalChance;
	}
	public double getVorpalMultiplier() {
		return this.vorpalMultiplier;
	}
	public double getLifeStealPercentage() {
		return this.lifesteal;
	}
	public LinkedList<Condition> getAttackerConditions() {
		return this.attackerConditions;
	}
	public LinkedList<Condition> getDefenderConditions() {
		return this.defenderConditions;
	}
	public LinkedList<Condition> getSuccessAttackConditions() {
		return this.successAttackConditions;
	}
	public LinkedList<Condition> getFailAttackConditions() {
		return this.failAttackConditions;
	}
	
	public LinkedList<Attack> getAttachedAttacks() {
		return this.attachedAttacks;
	}
	public boolean hasAlteration() {
		return this.hasAlteration;
	}
	public AttachedAlteration getAlteration() {
		return this.alteration;
	}
	
	// Overrides the toString function that may be helpful when debugging
	@Override
	public String toString() {
		// Creates the additional String needed for certain variables
		String atkConText = "";
		for (Condition c : this.getAttackerConditions()) {
			atkConText += c.toString() + "\n";
		}
		
		String defConText = "";
		for (Condition c : this.getDefenderConditions()) {
			defConText += c.toString() + "\n";
		}
		
		String dmgText = "";
		if (this.usesScaler()) {
			dmgText = "Scaler:    " + this.getScaler();
		}
		else {
			dmgText = "Damage:    " + this.getFlatDamageAmount();
		}
		String critText = "Crit:      ";
		if (this.guaranteedCrit) {
			critText += "Guaranteed";
		}
		else if (this.canCrit) {
			critText += "Possible";
		}
		else {
			critText += "Impossible";
		}
		
		String attachedAtkText = "";
		for (int i = 0; i < this.getAttachedAttacks().size(); i++) {
			attachedAtkText += "\nAttached Attack #" + (i+1) + this.getAttachedAttacks().get(i).toString();
		}
		// Returns the String formatted together
		return  "Attacker:  " + this.getAttacker().getName() + "\n" +
				"\t" + "Extra Conditions:" + atkConText +
				"Defender:  " + this.getDefender().getName() + "\n" +
				"\t" + "Extra Conditions:" + defConText +
				"DmgType:   " + this.getDmgType() + "\n" +
				"Range:     " + this.getRangeType().toString() +
				dmgText + "\n" +
				"Version:   " + (this.isTargeted()? "Targeted" : "AOE") +
				"Hittable:  " + this.canHit + "\n" + 
				"Missable:  " + this.canMiss + "\n" +
				critText + "\n" +
				"Armor:     " + (this.ignoresArmor? "Ignored" : "Applies") + "\n" +
				"Deviates:  " + this.hasDeviation() + "\n" + 
				(this.getVorpalChance() > 0? "Vorpal: " + this.getVorpalChance() + "% to strike with " + this.getVorpalMultiplier() + "x damage.\n" : "") +
				(this.getLifeStealPercentage() > 0? "Lifesteal: " + this.getLifeStealPercentage() + "%\n" : "") +
				"Attached Attacks:" + attachedAtkText;
	}
	
	// Function to add, apply, and unapply the single-use (for this attack) conditions [add included publicly so characters can add Conditions in their PreAttackEffects]
	private void applyLifeSteal(int damageDealt) {
		if (this.getLifeStealPercentage() <= 0) {
			return;
		}
		
		this.getAttacker().restoreHealth((int)Math.round(damageDealt * this.getLifeStealPercentage() / 100.0));
	}
	public void addAttackerCondition(Condition added) {
		this.attackerConditions.add(added);
	}
	public void addDefenderCondition(Condition added) {
		this.defenderConditions.add(added);
	}
	private void applyAttackerConditions() {
		for (Condition c : this.getAttackerConditions()) {
			this.attacker.apply(c, this.defender);
		}
	}
	private void applyDefenderConditions() {
		for (Condition c : this.getDefenderConditions()) {
			this.defender.apply(c, this.attacker);
		}
	}
	private void unapplyAttackerConditions() {
		for (Condition c : this.getAttackerConditions()) {
			this.attacker.unapply(c, this.defender);
		}
	}
	private void unapplyDefenderConditions() {
		for (Condition c : this.getDefenderConditions()) {
			this.defender.unapply(c, this.attacker);
		}
	}
	
	// Function to add all the success/fail attack Conditions to the defender
	private void addSuccessConditions() {
		for (Condition c : this.getSuccessAttackConditions()) {
			this.getDefender().addCondition(c);
		}
	}
	private void addFailConditions() {
		for (Condition c : this.getFailAttackConditions()) {
			this.getDefender().addCondition(c);
		}
	}
	
	
	// Functions to help execute the attack held in the variables of the class
	// landAttack: Calculates the chance for the attack to land
	public boolean landAttack() {
		// Denominator or largest possible value for the random generator to decide
		Dice toHit = new Dice(this.getAttacker().getAccuracy());
		
		// If what is rolled is Greater Than the enemy's Dodge/Block, the attack hits
		boolean didHit = toHit.roll() > this.getDefender().getDodge() + this.getDefender().getBlock();
		return didHit;
	}
	
	// criticalEffect: Calculates if the ability critically struck and returns the scaler effect (1 if no effect)
	public double criticalEffect() {
		// Initialize return value
		double ret = 1.0;
		
		// Find if the attack critically struck
		Dice percent = new Dice(100);
		boolean didCrit = percent.roll() <= this.getAttacker().getCriticalChance();
		if (this.guaranteedCrit()) {
			didCrit = true;
		}
		
		// If it did, adjust the effect accordingly
		if (didCrit && this.canCrit()) {
			// Initialize usual critical damage
			double critDamage = 2.0;
			
			// Check for vorpal effect
			if (this.getVorpalChance() > 0) {
				boolean isVorpal = percent.roll() <= this.getVorpalChance();
				if (isVorpal) {
					critDamage = this.getVorpalMultiplier();
					this.isVorpal = true;
				}
				else {
					this.isVorpal = false;
				}
			}
			else {
				this.isVorpal = false;
			}
			
			// Apply critical damage to the returned number
			ret *= critDamage;
			
			// Always add extra critical chance as critical damage
			if (this.getAttacker().getCriticalChance()>100) {
				ret += (this.getAttacker().getCriticalChance() - 100)/100; // This was changed to divide by 100
			}
		}
		
		// Return the result
		return ret;
	}
	
	// Functions to execute the "attached" Attacks with the correct alterations
	private void attachedExecute(AttackResultBuilder atkResBuilder) {
		// First, make sure this attack has the same attacker and defender
		AttackResult atkResSoFar = atkResBuilder.build();
		this.attacker = atkResSoFar.getAttacker();
		this.defender = atkResSoFar.getDefender();
		
		// If this attack has an alteration as an "attached" Attack
		if (this.hasAlteration()) {
			// Create a new attack (based on the specified alterations lambda function and the attack result so far) to execute the "attachedExecuteAttack" function
			Attack atk = this.getAlteration().evaluate(atkResSoFar);
			// If the default occured, or the attack simply is empty. Return and the "attached" Attack had no result
			if (atk.equals(Attack.EMPTY)) {
				return;
			}
			
			// Make sure this attack has the same attacker and defender
			atk.attacker = atkResSoFar.getAttacker();
			atk.defender = atkResSoFar.getDefender();
			
			// Do the execution of the "attached" attack
			atk.attachedExecuteAttack(atkResBuilder);
			return;
		}
		// Otherwise, this attack is fine as it is and can do the execution of the "attached" attack itself
		this.attachedExecuteAttack(atkResBuilder);
	}
	private void attachedExecuteAttack(AttackResultBuilder atkResBuilder) {
		// Apply the single attack (this attack only) Conditions
		this.applyAttackerConditions();
		this.applyDefenderConditions();
		
		// Determine if the attack hits
		boolean didHit = this.canHit();
		if (didHit && this.canMiss()) {
			didHit = this.landAttack();
		}
		
		// If the attack missed:
		if (!didHit) {
			// Unapply the single attack (this attack only) Conditions
			this.unapplyAttackerConditions();
			this.unapplyDefenderConditions();
			
			// Add any attack fail Conditions to the Defender
			this.addFailConditions();
			
			// Add the Attached Attack Result to the current overall Attack Result Builder
			AttackResult attachedAtkResult = new AttackResultBuilder()
					.attacker(this.getAttacker())
					.defender(this.getDefender())
					.type(this.getDmgType())
					.range(this.getRangeType())
					.isTargeted(this.isTargeted())
					.didHit(false)
					.didCrit(false)
					.damageDealt(0)
					.build();
			
			atkResBuilder.addAttachedAttackResult(attachedAtkResult);
			
			// Stop the rest of the attack
			return;
		}
		
		// Calculate base amount, armor effect, critical effect, and deviated effect.
		// Base amount
		double baseDmgAmount;
		if (this.usesScaler()) {
			baseDmgAmount = this.getScaler() * this.getAttacker().getDamage();
		}
		else {
			baseDmgAmount = this.getFlatDamageAmount();
		}
		
		// Armor Effect. If the attack is classified as "ignoring Armor" the minimum armorEffect is 1, and the bonus (amount above 1) is increased by 50%.
		double armorEffect;
		if (this.ignoresArmor()) {
			if (this.getDefender().getArmor() > this.getAttacker().getArmorPiercing()) {
				armorEffect = 1;
			}
			else {
				armorEffect = this.getAttacker().getArmorPiercing()*1d / (this.getDefender().getArmor());
				armorEffect = (armorEffect - 1) * 1.5 + 1;
			}
		}
		// Otherwise, just normal ArmorPiercing/Armor.
		else {
			armorEffect = this.getAttacker().getArmorPiercing()*1d / (this.getDefender().getArmor());
		}
		
		// Critical Effect
		double critEffect = this.criticalEffect();
		boolean didCrit = critEffect > 1.0;
		
		// Calculate Total Damage and Deviation
		double totalDamage = baseDmgAmount * armorEffect * critEffect;
		
		// Deviation Effect
		// Calculates the minimum and maximum Damage possible due to Standard Deviation (minimum is removed if you didCrit)
		int minDamage;
		int maxDamage = (int)Math.round(totalDamage * this.getAttacker().getSTDup() / 100.0);
		if (didCrit) {
			minDamage = (int)Math.round(totalDamage);
		}
		else {
			minDamage = (int)Math.round(totalDamage * this.getAttacker().getSTDdown() / 100.0);
		}
		
		// Determines where on the Damage spectrum created the Ability landed, and calculates the final Damage done
		Dice vary = new Dice(maxDamage-minDamage+1);
		int dmgTaken = minDamage + vary.roll() - 1;
		
		// The defender takes the damage
		dmgTaken = this.getDefender().takeDamage(dmgTaken, this.getDmgType());
		
		// Apply lifesteal and Unapply the single attack (this attack only) Conditions
		this.applyLifeSteal(dmgTaken);
		this.unapplyAttackerConditions();
		this.unapplyDefenderConditions();
		
		// Add any attack success Conditions to the Defender
		this.addSuccessConditions();
		
		// Add the Attached Attack Result to the current overall Attack Result Builder
		AttackResult attachedAtkResult = new AttackResultBuilder()
				.attacker(this.getAttacker())
				.defender(this.getDefender())
				.type(this.getDmgType())
				.range(this.getRangeType())
				.isTargeted(this.isTargeted())
				.didHit(true)
				.didCrit(didCrit)
				.didVorp(this.isVorpal)
				.damageDealt(dmgTaken)
				.build();
		
		atkResBuilder.addAttachedAttackResult(attachedAtkResult);
	}
	
	// Executes the attack
	public void execute() {
		// Make sure neither target is dead:
		if (this.getAttacker().isDead()) {
			System.out.println(this.getAttacker().getName() + " is dead. Thus, " + this.getAttacker().getName() + " is incapable of attacking.");
			System.out.println("Continue with attack anyway?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
		}
		if (this.getDefender().isDead()) {
			System.out.println(this.getDefender().getName() + " is already dead. The attack would have no effect.");
			System.out.println("Continue with attack anyway?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
		}
		
		// Make sure the attacker can attack and the defender is targetable
		if (!this.getAttacker().canAttack()) {
			System.out.println(this.getAttacker().getName() + " cannot attack due to crowd control.");
			System.out.println("Continue with attack anyway?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
		}
		if (!this.getDefender().isTargetable()) {
			System.out.println(this.getDefender().getName() + " cannot be attacked due to crowd control.");
			System.out.println("Continue with attack anyway?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
		}
		
		// Apply Attack Conditions
		this.getAttacker().applyAttackConditions(this);
		this.getDefender().applyAttackConditions(this);
		
		// Apply Pre-Attack Effects
		this.getAttacker().applyPreAttackEffects(this);
		this.getDefender().applyPreAttackEffects(this);
		
		// Apply the single attack (this attack only) Conditions
		this.applyAttackerConditions();
		this.applyDefenderConditions();
		
		// Determine if the attack hits
		boolean didHit = this.canHit;
		if (didHit && this.canMiss()) {
			didHit = this.landAttack();
		}
		
		// If the attack missed:
		if (!didHit) {
			// Start the attack result builder from the results from this missed attack
			AttackResultBuilder atkResultBuilder = new AttackResultBuilder()
					.attacker(this.getAttacker())
					.defender(this.getDefender())
					.type(this.getDmgType())
					.range(this.getRangeType())
					.isTargeted(this.isTargeted())
					.didHit(false)
					.didCrit(false)
					.damageDealt(0);
			
			
			// Unapply the single attack (this attack only) Conditions
			this.unapplyAttackerConditions();
			this.unapplyDefenderConditions();
			
			// Execute Attached Attacks, pass the AttackResultBuilder so far
			for (Attack attached : this.getAttachedAttacks()) {
				attached.attachedExecute(atkResultBuilder);
			}
			
			// Print the final attack result and store it for both Characters
			AttackResult atkResult = atkResultBuilder.didKill(this.getDefender().isDead()).build();
			System.out.println(atkResult.toString());
			this.getDefender().storeAttack(atkResult);
			this.getAttacker().storeAttack(atkResult);
			
			// Unapply Attack Conditions
			this.getDefender().unapplyAttackConditions(atkResult);
			this.getAttacker().unapplyAttackConditions(atkResult);
			
			// Add any attack fail Conditions to the Defender
			this.addFailConditions();
			
			// Apply Post-Attack Effects
			this.getDefender().applyPostAttackEffects(atkResult);
			this.getAttacker().applyPostAttackEffects(atkResult);
			
			// Stop the rest of the attack
			return;
		}
		
		// Calculate base amount, armor effect, critical effect, and deviated effect.
		// Base amount
		double baseDmgAmount;
		if (this.usesScaler()) {
			baseDmgAmount = this.getScaler() * this.getAttacker().getDamage();
		}
		else {
			baseDmgAmount = this.getFlatDamageAmount();
		}
		
		// Armor Effect. If the attack is classified as "ignoring Armor" the minimum armorEffect is 1, and the bonus (amount above 1) is increased by 50%.
		double armorEffect;
		if (this.ignoresArmor()) {
			if (this.getDefender().getArmor() > this.getAttacker().getArmorPiercing()) {
				armorEffect = 1;
			}
			else {
				armorEffect = this.getAttacker().getArmorPiercing()*1d / (this.getDefender().getArmor());
				armorEffect = (armorEffect - 1) * 1.5 + 1;
			}
		}
		// Otherwise, just normal ArmorPiercing/Armor.
		else {
			armorEffect = this.getAttacker().getArmorPiercing()*1d / (this.getDefender().getArmor());
		}
		
		// Critical Effect
		double critEffect = this.criticalEffect();
		boolean didCrit = critEffect > 1.0;
		
		// Calculate Total Damage and Deviation
		double totalDamage = baseDmgAmount * armorEffect * critEffect;
		
		// Deviation Effect
		// Calculates the minimum and maximum Damage possible due to Standard Deviation (minimum is removed if you didCrit)
		int minDamage;
		int maxDamage = (int)Math.round(totalDamage * this.getAttacker().getSTDup() / 100.0);
		if (didCrit) {
			minDamage = (int)Math.round(totalDamage);
		}
		else {
			minDamage = (int)Math.round(totalDamage * this.getAttacker().getSTDdown() / 100.0);
		}
		
		// Determines where on the Damage spectrum created the Ability landed, and calculates the final Damage done
		Dice vary = new Dice(maxDamage-minDamage+1);
		int dmgTaken = minDamage + vary.roll() - 1;
		
		// The defender takes the damage
		dmgTaken = this.getDefender().takeDamage(dmgTaken, this.getDmgType());
		
		
		// Start the attack result builder from the results from this successful attack
		AttackResultBuilder atkResultBuilder = new AttackResultBuilder()
				.attacker(this.getAttacker())
				.defender(this.getDefender())
				.type(this.getDmgType())
				.range(this.getRangeType())
				.isTargeted(this.isTargeted())
				.didHit(true)
				.didCrit(didCrit)
				.didVorp(this.isVorpal)
				.damageDealt(dmgTaken);
		
		
		// Apply lifesteal and Unapply the single attack (this attack only) Conditions
		this.applyLifeSteal(dmgTaken);
		this.unapplyAttackerConditions();
		this.unapplyDefenderConditions();
		
		// Execute Attached Attacks, pass the AttackResultBuilder so far
		for (Attack attached : this.getAttachedAttacks()) {
			attached.attachedExecute(atkResultBuilder);
		}
		
		// Print the final attack result and store it for both Characters
		AttackResult atkResult = atkResultBuilder.didKill(this.getDefender().isDead()).build();
		System.out.println(atkResult.toString());
		this.getDefender().storeAttack(atkResult);
		this.getAttacker().storeAttack(atkResult);
		
		// Unapply Attack Conditions
		this.getDefender().unapplyAttackConditions(atkResult);
		this.getAttacker().unapplyAttackConditions(atkResult);
		
		// Add any attack success Conditions to the Defender
		this.addSuccessConditions();
		
		// Apply Post-Attack Effects
		this.getDefender().applyPostAttackEffects(atkResult);
		this.getAttacker().applyPostAttackEffects(atkResult);
	}
}

// A helpful class used to build an Attack from the default static EMPTY Attack
class AttackBuilder {
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private Attack.DmgType type;
	private Attack.RangeType range;
	private boolean usesScaler;
	private double scaler;
	private int flatDamage;
	private boolean isTargeted;
	private boolean canHit;
	private boolean canMiss;
	private boolean canCrit;
	private boolean guaranteedCrit;
	private boolean ignoresArmor;
	private boolean hasDeviation;
	
	// Additional Variables for single-use (this attack only) Conditions to apply and Conditions to apply to the defender after an attack
	private int vorpalChance;
	private double vorpalMultiplier;
	private double lifesteal;
	private LinkedList<Condition> attackerConditions;
	private LinkedList<Condition> defenderConditions;
	private LinkedList<Condition> successAttackConditions;
	private LinkedList<Condition> failAttackConditions;
	
	// Additional variables for attacks "attached" to this Attack and, if this Attack is the attached, for any necessary alterations
	private LinkedList<Attack> attachedAttacks;
	private boolean hasAlteration;
	private AttachedAlteration alteration;
	
	// Constructors
	public AttackBuilder(Attack base) {
		this.attacker = base.getAttacker();
		this.defender = base.getDefender();
		this.type = base.getDmgType();
		this.range = base.getRangeType();
		this.usesScaler = base.usesScaler();
		this.scaler = base.getScaler();
		this.flatDamage = base.getFlatDamageAmount();
		this.isTargeted = base.isTargeted();
		this.canHit = base.canHit();
		this.canMiss = base.canMiss();
		this.canCrit = base.canCrit();
		this.guaranteedCrit = base.guaranteedCrit();
		this.ignoresArmor = base.ignoresArmor();
		this.hasDeviation = base.hasDeviation();
		
		this.vorpalChance = base.getVorpalChance();
		this.vorpalMultiplier = base.getVorpalMultiplier();
		this.lifesteal = base.getLifeStealPercentage();
		this.attackerConditions = base.getAttackerConditions();
		this.defenderConditions = base.getDefenderConditions();
		this.successAttackConditions = base.getSuccessAttackConditions();
		this.failAttackConditions = base.getFailAttackConditions();
		
		this.attachedAttacks = base.getAttachedAttacks();
		this.hasAlteration = base.hasAlteration();
		this.alteration = base.getAlteration();
	}
	public AttackBuilder() {
		this(Attack.EMPTY);
	}
	public AttackBuilder(AttackResult prevResult) {
		this();
		this.attacker = prevResult.getAttacker();
		this.defender = prevResult.getDefender();
		this.type = prevResult.getDmgType();
		this.range = prevResult.getRangeType();
		this.isTargeted = prevResult.isTargeted();
	}
	
	// Methods for build process
	public AttackBuilder attacker(Character attacker) {
		this.attacker = attacker;
		return this;
	}
	
	public AttackBuilder defender(Character defender) {
		this.defender = defender;
		return this;
	}
	
	public AttackBuilder type(Attack.DmgType aType) {
		this.type = aType;
		return this;
	}
	
	public AttackBuilder range(Attack.RangeType range) {
		this.range = range;
		return this;
	}
	
	public AttackBuilder usesScaler(boolean usesScaler) {
		this.usesScaler = usesScaler;
		return this;
	}
	public AttackBuilder usesScaler() {
		return this.usesScaler(true);
	}
	public AttackBuilder usesFlatDamage() {
		return this.usesScaler(false);
	}
	
	public AttackBuilder scaler(double scaler) {
		this.usesScaler();
		this.scaler = scaler;
		return this;
	}
	
	public AttackBuilder flatDamage(int flatDamage) {
		this.usesFlatDamage();
		this.flatDamage = flatDamage;
		return this;
	}
	
	public AttackBuilder isTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
		
		// By default, Targeted attacks can miss and crit while AOE ones cannot
		this.canMiss = isTargeted;
		this.canCrit = isTargeted;
		
		return this;
	}
	public AttackBuilder isTargeted() {
		return this.isTargeted(true);
	}
	public AttackBuilder isAOE() {
		return this.isTargeted(false);
	}
	
	public AttackBuilder canHit(boolean canHit) {
		this.canHit = canHit;
		return this;
	}
	public AttackBuilder canHit() {
		return this.canHit(true);
	}
	public AttackBuilder cannotHit() {
		return this.canHit(false);
	}
	
	public AttackBuilder canMiss(boolean canMiss) {
		this.canMiss = canMiss;
		// If the attack cannot miss, this changes canHit to True if previously false
		if (!this.canMiss) {
			this.canHit = true;
		}
		return this;
	}
	public AttackBuilder canMiss() {
		return this.canMiss(true);
	}
	public AttackBuilder cannotMiss() {
		return this.canMiss(false);
	}
	
	public AttackBuilder canCrit(boolean canCrit) {
		this.canCrit = canCrit;
		return this;
	}
	public AttackBuilder canCrit() {
		return this.canCrit(true);
	}
	public AttackBuilder cannotCrit() {
		return this.canCrit(false);
	}
	
	public AttackBuilder guaranteedCrit() {
		this.canCrit();
		this.guaranteedCrit = true;
		return this;
	}
	
	public AttackBuilder ignoresArmor() {
		this.ignoresArmor = true;
		return this;
	}
	
	public AttackBuilder hasDeviation(boolean hasDeviation) {
		this.hasDeviation = hasDeviation;
		return this;
	}
	public AttackBuilder isStagnate() {
		this.hasDeviation = false;
		return this;
	}
	
	public AttackBuilder vorpal(int chance, double multiplier) {
		this.vorpalChance = chance;
		this.vorpalMultiplier = multiplier;
		return this;
	}
	public AttackBuilder vorpal(double multiplier) {
		return this.vorpal(100, multiplier);
	}
	public AttackBuilder lifestealPercentage(double lifesteal) {
		this.lifesteal = lifesteal;
		return this;
	}
	public AttackBuilder addAttackerCondition(Condition added) {
		this.attackerConditions.add(added);
		return this;
	}
	public AttackBuilder addDefenderCondition(Condition added) {
		this.defenderConditions.add(added);
		return this;
	}
	public AttackBuilder addSuccessCondition(Condition added) {
		this.successAttackConditions.add(added);
		return this;
	}
	public AttackBuilder addFailCondition(Condition added) {
		this.failAttackConditions.add(added);
		return this;
	}
	
	public AttackBuilder addAttachedAttack(Attack atk) {
		this.attachedAttacks.add(atk);
		return this;
	}
	public AttackBuilder attachedAlteration(AttachedAlteration alteration) {
		this.hasAlteration = true;
		this.alteration = alteration;
		return this;
	}
	
	// Build the attack
	public Attack build() {
		return new Attack(this.attacker, this.defender, this.type, this.range, this.usesScaler, this.scaler, this.flatDamage, this.isTargeted, this.canHit, this.canMiss, this.canCrit, this.guaranteedCrit, this.ignoresArmor, this.hasDeviation, this.vorpalChance, this.vorpalMultiplier, this.lifesteal, this.attackerConditions, this.defenderConditions, this.successAttackConditions, this.failAttackConditions, this.attachedAttacks, this.hasAlteration, this.alteration);
	}
}
