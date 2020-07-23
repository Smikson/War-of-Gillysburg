package WyattWitemeyer.WarOfGillysburg;


public class Attack {
	public static enum DmgType {
		TRUE, SLASHING, SMASHING, PIERCING, MAGIC, FIRE, ICE, LIGHTNING, ARCANE, POISON, BLEED, EXPLOSIVE, LIGHT, NECROMANTIC;
	}
	
	// Constant for Empty Attacks to build up with
	public static final Attack EMPTY = new Attack();
	
	// Variables for each element of an attack
	private Character attacker;		// The Character attacking
	private Character defender;		// The Chatacter being attacked
	private Attack.DmgType type;		// The type of attack made
	private boolean usesScaler;		// Determines if the attack uses a scaler for damage (true) or a specified flat numeric amount (false)
	private double scaler;			// If a scaler is used (the usual), holds the Damage scaler of the attack
	private int flatDamage;			// If a scaler is not used, holds the specified flat numeric damage amount
	private boolean isTargeted;		// Determines if Targeted (true) or AOE (false)
	private boolean canMiss;		// Determines if the attack has the capability of being dodged or blocked
	private boolean canCrit;		// Determines if the attack has the capability of critically striking
	private boolean guaranteedCrit;	// Determines if the attack is a guaranteed critical strike
	private boolean ignoresArmor;	// Determines if the attack "ignores all armor"
	private boolean hasDeviation;	// Determines if the attack uses STDup and STDdown to deviate the attack
	
	// Constructors
	public Attack(Character attacker, Character defender, Attack.DmgType type, boolean usesScaler, double scaler, int flatDamage, boolean isTargeted, boolean canMiss, boolean canCrit, boolean guaranteedCrit, boolean ignoresArmor, boolean hasDeviation) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = type;
		this.usesScaler = usesScaler;
		this.scaler = scaler;
		this.flatDamage = flatDamage;
		this.isTargeted = isTargeted;
		this.canMiss = canMiss;
		this.canCrit = canCrit;
		this.guaranteedCrit = guaranteedCrit;
		this.ignoresArmor = ignoresArmor;
		this.hasDeviation = hasDeviation;
	}
	public Attack() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
		this.type = Attack.DmgType.TRUE;
		this.usesScaler = true;
		this.scaler = 1.0;
		this.flatDamage = 0;
		this.isTargeted = true;
		this.canMiss = true;
		this.canCrit = true;
		this.guaranteedCrit = false;
		this.ignoresArmor = false;
		this.hasDeviation = true;
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
	
	// Overrides the toString function that may be helpful when debugging
	@Override
	public String toString() {
		// Creates the additional String needed for certain variables
		String dmgText = "";
		if (this.usesScaler()) {
			dmgText = "Scaler:   " + this.getScaler();
		}
		else {
			dmgText = "Damage:   " + this.getFlatDamageAmount();
		}
		String critText = "Crit:     ";
		if (this.guaranteedCrit) {
			critText += "Guaranteed";
		}
		else if (this.canCrit) {
			critText += "Possible";
		}
		else {
			critText += "Impossible";
		}
		// Returns the String formatted together
		return  "Attacker: " + this.getAttacker().getName() + "\n" +
				"Defender: " + this.getDefender().getName() + "\n" +
				"DmgType:  " + this.getDmgType() + "\n" +
				dmgText + "\n" +
				"Missable: " + this.canMiss + "\n" +
				critText + "\n" +
				"Armor:    " + (this.ignoresArmor? "Ignored" : "Applies") + "\n" +
				"Deviates: " + this.hasDeviation();
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
			ret *= 2;
			if (this.getAttacker().getCriticalChance()>100) {
				ret += (this.getAttacker().getCriticalChance() - 100)/100; // This was changed to divide by 100
			}
		}
		
		// Return the result
		return ret;
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
		
		// Apply Pre-Attack Effects
		this.getDefender().applyPreAttackEffects(this);
		this.getAttacker().applyPreAttackEffects(this);
		
		// Determine if the attack hits
		boolean didHit = true;
		if (this.canMiss()) {
			didHit = this.landAttack();
		}
		
		// If the attack missed:
		if (!didHit) {
			// Print the result
			System.out.println(this.getAttacker().getName() + " missed " + this.getDefender().getName() + "!");
			
			// Store the attack attempt
			AttackResult atkResult = new AttackResultBuilder()
					.attacker(this.getAttacker())
					.defender(this.getDefender())
					.type(this.getDmgType())
					.didHit(false)
					.didCrit(false)
					.damageDealt(0)
					.didKill(false)
					.build();
			
			this.getDefender().storeAttack(atkResult);
			this.getAttacker().storeAttack(atkResult);
			
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
		
		// The defender takes the damage -- CHANGE? (Includes printing the result)
		// dmgTaken = this.getAttacker().dealDamage(this.getDefender(), dmgTaken, this.getAttackType());
		dmgTaken = this.getDefender().takeDamage(dmgTaken, this.getDmgType());
		
		// Attack output
		if (didCrit) {
			System.out.println(this.getAttacker().getName() + " scored a CRITCAL HIT against " + this.getDefender().getName() + " for a total of " + dmgTaken + " " + this.getDmgType().toString() + " damage!");
		}
		else {
			System.out.println(this.getAttacker().getName() + " hit " + this.getDefender().getName() + " for a total of " + dmgTaken + " " + this.getDmgType().toString() + " damage!");
		}
		
		if (this.getDefender().isDead()) {
			Dice funDie = new Dice(6);
			String funWord = "";
			
			switch(funDie.roll()) {
				case 1: {
					funWord = " utterly annihilated ";
					break;
				}
				case 2: {
					funWord = " defeated ";
					break;
				}
				case 3: {
					funWord = " obliterated ";
					break;
				}
				case 4: {
					funWord = " purged the universe of ";
					break;
				}
				case 5: {
					funWord = " destroyed ";
					break;
				}
				case 6: {
					funWord = " slew ";
					break;
				}
			}
			
			System.out.println(this.getAttacker().getName() + funWord + this.getDefender().getName() + "!");
		}
		else {
			System.out.println(this.getDefender().getName() + " has " + this.getDefender().getCurrentHealth() + " Health remaining.");
		}
		
		
		// Store the attack
		AttackResult atkResult = new AttackResultBuilder()
				.attacker(this.getAttacker())
				.defender(this.getDefender())
				.type(this.getDmgType())
				.didHit(true)
				.didCrit(didCrit)
				.damageDealt(dmgTaken)
				.didKill(this.getDefender().isDead())
				.build();
		
		this.getDefender().storeAttack(atkResult);
		this.getAttacker().storeAttack(atkResult);
		
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
	private boolean usesScaler;
	private double scaler;
	private int flatDamage;
	private boolean isTargeted;
	private boolean canMiss;
	private boolean canCrit;
	private boolean guaranteedCrit;
	private boolean ignoresArmor;
	private boolean hasDeviation;
	
	// Constructors
	public AttackBuilder(Attack base) {
		this.attacker = base.getAttacker();
		this.defender = base.getDefender();
		this.type = base.getDmgType();
		this.usesScaler = base.usesScaler();
		this.scaler = base.getScaler();
		this.flatDamage = base.getFlatDamageAmount();
		this.isTargeted = base.isTargeted();
		this.canMiss = base.canMiss();
		this.canCrit = base.canCrit();
		this.guaranteedCrit = base.guaranteedCrit();
		this.ignoresArmor = base.ignoresArmor();
		this.hasDeviation = base.hasDeviation();
	}
	public AttackBuilder() {
		this(Attack.EMPTY);
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
		this.scaler = scaler;
		return this;
	}
	
	public AttackBuilder flatDamage(int flatDamage) {
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
	
	public AttackBuilder canMiss(boolean canMiss) {
		this.canMiss = canMiss;
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
	
	// Build the attack
	public Attack build() {
		return new Attack(this.attacker, this.defender, this.type, this.usesScaler, this.scaler, this.flatDamage, this.isTargeted, this.canMiss, this.canCrit, this.guaranteedCrit, this.ignoresArmor, this.hasDeviation);
	}
}
