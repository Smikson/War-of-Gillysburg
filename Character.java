package WyattWitemeyer.WarOfGillysburg;
import java.util.*;
public class Character {
	// Static Characters to aid with Character building and leveling up from a base level.
	public static final Character EMPTY = new Character("Null",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character STEEL_LEGION_TANK = new Character("Tank",0,750,70,185,100,125,0,38,2,3,1,1,20,3,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character STEEL_LEGION_WARRIOR = new Character("Warrior",0,635,85,162,118,125,0,30,5,6,5,1,15,5,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character STEEL_LEGION_BARBARIAN = new Character("Barbarian",0,530,125,158,124,100,0,28,7,6,6,1,12,8,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SENTINEL_SNIPER = new Character("Sniper",0,400,140,118,146,160,27,0,8,5,8,8,5,20,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SENTINEL_SPECIALIST = new Character("Specialist",0,500,100,138,140,140,25,0,5,5,7,5,7,18,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SENTINEL_ARC_ARCHER = new Character("Arc Archer",0,350,85,125,130,150,43,0,7,6,35,6,6,19,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SILENT_DEATH_SHADOW = new Character("Shadow",0,375,150,117,147,118,33,0,10,7,5,1,2,25,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SILENT_DEATH_POISON_SPECIALIST = new Character("Poison Specialist",0,480,110,140,128,115,24,0,5,4,4,1,6,15,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character SILENT_DEATH_HUNTER = new Character("Hunter",0,520,100,146,126,125,23,0,6,4,5,4,4,16,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character KINITCHU_ORDER_DRAGON_FIRE_WIZARD = new Character("Pyromancer",0,370,155,121,142,120,20,0,8,4,4,5,4,23,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character KINITCHU_ORDER_THAUMRATURGE = new Character("Ice Wizard",0,510,105,150,121,122,21,0,6,4,4,5,17,7,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character KINITCHU_ORDER_ARCANA = new Character("Arcana",0,325,165,115,152,140,18,0,9,4,6,5,6,17,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character KINITCHU_ORDER_LUMINESCENT_WIZARD = new Character("Luminescent Wizard",0,425,120,120,134,135,21,0,8,4,5,5,3,24,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	public static final Character KINITCHU_ORDER_NECROMANCER = new Character("Necromancer",0,410,130,119,138,125,20,0,5,4,4,5,6,19,90,110, new HashSet<String>(), new HashSet<String>(), new LinkedList<String>());
	
	// Variables of each stat of the character
	private String name;
	private int Level;
	private Stat Health;
	private Stat Damage;
	private Stat Armor;
	private Stat ArmorPiercing;
	private Stat Accuracy;
	private Stat Dodge;
	private Stat Block;
	private Stat CriticalChance;
	private Stat Speed;
	private Stat AttackSpeed;
	private Stat Range;
	private Stat Threat;
	private Stat TacticalThreat;
	private Stat STDdown;
	private Stat STDup;
	
	private int CurrentHealth;
	private int Shields;
	
	private HashSet<String> resistances;
	private HashSet<String> vulnerabilities;
	private LinkedList<String> attackType;
	
	// Contains a list of the basic stats (everything but STDup/down and Current Health) and conditions for looping through
	protected LinkedList<Stat> stats;
	protected LinkedList<LinkedList<Condition>> conditions;
	
	// Holds the Conditions that have expired but wait until the end of the turn to be removed
	private LinkedList<Condition> expiredConditions;
	
	// Safety measure to have a way to clear the conditions if necessary (If works consistently, REMOVE THIS)
	public void clearConditions() {
		this.conditions.clear();
	}
	
	// Store the previous attack made (for use in Character Abilities sometimes)
	protected LinkedList<Attack> AttacksMade;
	protected LinkedList<Attack> AttacksDefended;
	
	// Constructor (sets each stat variable)
	public Character(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		this.name = nam;
		this.Level = lvl;
		this.Health = new Stat(hp, Stat.HEALTH);
		this.Damage = new Stat(dmg, Stat.DAMAGE);
		this.Armor = new Stat(arm, Stat.ARMOR);
		this.ArmorPiercing = new Stat(armp, Stat.ARMOR_PIERCING);
		this.Accuracy = new Stat(acc, Stat.ACCURACY);
		this.Dodge = new Stat(dod, Stat.DODGE);
		this.Block = new Stat(blk, Stat.BLOCK);
		this.CriticalChance = new Stat(crit, Stat.CRITICAL_CHANCE);
		this.Speed = new Stat(spd, Stat.SPEED);
		this.AttackSpeed = new Stat(atkspd, Stat.ATTACK_SPEED);
		this.Range = new Stat(range, Stat.RANGE);
		this.Threat = new Stat(thrt, Stat.THREAT);
		this.TacticalThreat = new Stat(tactthrt, Stat.TACTICAL_THREAT);
		this.STDdown = new Stat(stdDown, Stat.STANDARD_DEVIATION_DOWN);
		this.STDup = new Stat(stdUp, Stat.STANDARD_DEVIATION_UP);
		
		this.CurrentHealth = this.Health.getTotal();
		this.Shields = 0;
		
		this.resistances = resis;
		this.vulnerabilities = vuls;
		this.attackType = aType;
		
		// Puts all the basic stats in the list.
		this.stats = new LinkedList<Stat>();
		this.stats.add(this.Health);
		this.stats.add(this.Damage);
		this.stats.add(this.Armor);
		this.stats.add(this.ArmorPiercing);
		this.stats.add(this.Accuracy);
		this.stats.add(this.Dodge);
		this.stats.add(this.Block);
		this.stats.add(this.CriticalChance);
		this.stats.add(this.Speed);
		this.stats.add(this.AttackSpeed);
		this.stats.add(this.Range);
		this.stats.add(this.Threat);
		this.stats.add(this.TacticalThreat);
		this.stats.add(this.STDdown);
		this.stats.add(this.STDup);
		
		// Initializes Condition and Attack lists
		this.conditions = new LinkedList<>();
		this.expiredConditions = new LinkedList<>();
		this.AttacksMade = new LinkedList<>();
		this.AttacksDefended = new LinkedList<>();
	}
	public Character(Character copy) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getResistances(), copy.getVulnerabilities(), copy.getAttackType());
	}
	public Character() {
		this(Character.EMPTY);
	}
	
	
	// Methods to return the value of a specific stat
	public String getName() {
		return this.name;
	}
	public int getLevel() {
		return this.Level;
	}
	public int getHealth() {
		return this.Health.getTotal() + this.Health.bonus;
	}
	public int getDamage() {
		return this.Damage.getTotal() + this.Damage.bonus;
	}
	public int getArmor() {
		return this.Armor.getTotal() + this.Armor.bonus;
	}
	public int getArmorPiercing() {
		return this.ArmorPiercing.getTotal() + this.ArmorPiercing.bonus;
	}
	public int getAccuracy() {
		return this.Accuracy.getTotal() + this.Accuracy.bonus;
	}
	public int getDodge() {
		return this.Dodge.getTotal() + this.Dodge.bonus;
	}
	public int getBlock() {
		return this.Block.getTotal() + this.Block.bonus;
	}
	public int getCriticalChance() {
		return this.CriticalChance.getTotal() + this.CriticalChance.bonus;
	}
	public int getSpeed() {
		return this.Speed.getTotal() + this.Speed.bonus;
	}
	public int getAttackSpeed() {
		return this.AttackSpeed.getTotal() + this.AttackSpeed.bonus;
	}
	public int getRange() {
		return this.Range.getTotal() + this.Range.bonus;
	}
	public int getThreat() {
		return this.Threat.getTotal() + this.Threat.bonus;
	}
	public int getTacticalThreat() {
		return this.TacticalThreat.getTotal() + this.TacticalThreat.bonus;
	}
	
	public HashSet<String> getResistances() {
		return this.resistances;
	}
	public HashSet<String> getVulnerabilities() {
		return this.vulnerabilities;
	}
	public LinkedList<String> getAttackType() {
		return this.attackType;
	}
	
	public int getSTDdown() {
		return this.STDdown.getTotal() + this.STDdown.bonus;
	}
	public int getSTDup() {
		return this.STDup.getTotal() + this.STDup.bonus;
	}
	
	public int getCurrentHealth() {
		return this.CurrentHealth;
	}
	public int getShields() {
		return this.Shields;
	}
	public void setCurrentHealth(int newCurrentHealth) {
		if (newCurrentHealth < this.getHealth()) {
			this.CurrentHealth = newCurrentHealth;
		}
		else {
			this.CurrentHealth = this.getHealth();
		}
	}
	public boolean isDead() {
		return this.CurrentHealth <= 0;
	}
	
	
	// Condition methods
	// Helpful method for checking stats
	private Stat getStat(Stat version) {
		Stat requested = new Stat(); // Initializes to empty stat in case of a faulty Status Effect
		
		// Find the matching Stat for this Character
		for (Stat s : this.stats) {
			if (s.statVersion.equals(version)) {
				requested = s;
			}
		}
		
		// Return the matching stat, or an empty stat if none were found
		return requested;
	}
	public int getStatValue(Stat version) {
		Stat requested = this.getStat(version);
		return requested.getTotal() + requested.bonus;
	}
	
	// To add conditions to this Character
	protected void addCondition(LinkedList<Condition> added) {
		this.conditions.add(added);
	}
	protected void addCondition(Condition c) {
		LinkedList<Condition> added = new LinkedList<>();
		added.add(c);
		this.conditions.add(added);
	}
	protected void addConsecutiveCondition(Condition inConsecList, Condition added) {
		// Adds the condition after "inConsecList" if present
		boolean isAdded = false;
		for (LinkedList<Condition> list : this.conditions) {
			if (list.contains(inConsecList)) {
				list.add(added);
				isAdded = true;
			}
		}
		// Otherwise, just adds it in a new spot.
		if (!isAdded) {
			this.addCondition(added);
		}
	}
	
	// To remove conditions from this Character
	private void removeEmptyConditionLists() {
		LinkedList<LinkedList<Condition>> toRemove = new LinkedList<>();
		for (LinkedList<Condition> list : this.conditions) {
			if (list.isEmpty()) {
				toRemove.add(list);
			}
		}
		this.conditions.removeAll(toRemove);
	}
	private void removeConditionHelper(Condition removed) {
		// Remove the Condition from whichever list
		for (LinkedList<Condition> list : this.conditions) {
			if (list.contains(removed)) {
				// Make sure the condition is unapplied before removing
				this.unapply(removed);
				
				// Remove condition
				list.remove(removed);
			}
		}
	}
	protected void removeCondition(Condition removed) {
		// If the Condition is already removed (or wasn't there in the first place), we are done.
		if (!this.getAllConditions().contains(removed)) {
			return;
		}
		
		// Removes the condition passed
		this.removeConditionHelper(removed);
		
		// Removes linked conditions
		for (Condition c : removed.getLinkedConditions()) {
			this.removeCondition(c);
		}
		
		// Remove any empty lists
		this.removeEmptyConditionLists();
	}
	
	// Terrain Conditions
	public void addHighGround() {
		this.addCondition(Terrain.HIGH_GROUND);
	}
	public void addHill() {
		this.addCondition(Terrain.HILL);
	}
	public void addCover() {
		this.addCondition(Terrain.COVER);
	}
	public void addTree() {
		this.addCondition(Terrain.TREE);
	}
	public void removeTerrain(Terrain removed) {
		this.removeCondition(removed);
	}
	public LinkedList<Terrain> getTerrainConditions() {
		LinkedList<Terrain> ret = new LinkedList<>();
		LinkedList<Condition> possibleConditions = this.getActiveConditions();
		
		for (Condition c : possibleConditions) {
			if (c instanceof Terrain) {
				ret.add((Terrain) c);
			}
		}
		
		return ret;
	}
	
	// To see all Conditions in a single list (just active ones or all)
	protected LinkedList<Condition> getActiveConditions() {
		LinkedList<Condition> ret = new LinkedList<>();
		for (LinkedList<Condition> list : this.conditions) {
			ret.add(list.getFirst()); // Only the first ones are active
		}
		return ret;
	}
	protected LinkedList<Condition> getAllConditions() {
		LinkedList<Condition> ret = new LinkedList<>();
		for (LinkedList<Condition> list : this.conditions) {
			for (Condition c : list) {
				ret.add(c);
			}
		}
		return ret;
	}
	
	// To have easier access to basic status effects, incoming status effects, and all status effects
	protected LinkedList<StatusEffect> getStatusEffectsBasic() {
		// Create return list
		LinkedList<StatusEffect> ret = new LinkedList<>();
		
		// Find each basic status effect (non-Incoming)
		LinkedList<Condition> cons = this.getActiveConditions();
		for (Condition c : cons) {
			// If the condition is a Status Effect already, simply make sure it is basic
			if (c instanceof StatusEffect) {
				if ( !((StatusEffect) c).isIncoming() ) {
					// Add the basic status effect
					ret.add((StatusEffect) c);
				}
			}
			// If the condition is a Crowd Control, make sure each status effect is basic
			else if (c instanceof CrowdControl) {
				for (StatusEffect se : ((CrowdControl) c).effects) {
					if ( !se.isIncoming() ) {
						// Add each basic status effect
						ret.add(se);
					}
				}
			}
		}
		
		// Return the result
		return ret;
	}
	protected LinkedList<StatusEffect> getStatusEffectIncoming() {
		// Create return list
		LinkedList<StatusEffect> ret = new LinkedList<>();
		
		// Find each Incoming status effect
		LinkedList<Condition> cons = this.getActiveConditions();
		for (Condition c : cons) {
			// If the condition is a Status Effect already, simply make sure it is Incoming
			if (c instanceof StatusEffect) {
				if ( ((StatusEffect) c).isIncoming() ) {
					// Add the Incoming status effect
					ret.add((StatusEffect) c);
				}
			}
			// If the condition is a Crowd Control, make sure each status effect is Incoming
			else if (c instanceof CrowdControl) {
				for (StatusEffect se : ((CrowdControl) c).effects) {
					if ( se.isIncoming() ) {
						// Add each Incoming status effect
						ret.add(se);
					}
				}
			}
		}
		
		// Return the result
		return ret;
	}
	protected LinkedList<StatusEffect> getStatusEffectAll() {
		LinkedList<StatusEffect> ret = this.getStatusEffectsBasic();
		ret.addAll(this.getStatusEffectIncoming());
		return ret;
	}
	
	// These functions apply and unapply Status Effects to this Character's basic stats
	protected void applySE(StatusEffect se) {
		// If the exact Status Effect is already applied, do not apply it again.
		if (se.isApplied) {
			return;
		}
		
		// Selects the affected stat
		Stat changed = this.getStat(se.getAlteredStat());
		
		// Calculates the amount of change in the bonus
		int amount = 0;
		if (se.isFlat()) {
			amount = (int) Math.round(se.getValue());
		}
		else {
			amount = (int) Math.round(se.getValue() / 100 * (changed.getTotal() + changed.bonus));
		}
		
		// Applies the amount to the changed stat
		changed.bonus += amount;
		se.amountAdded = amount;
		se.isApplied = true;
	}
	protected void applyBasicStatusEffects() {
		for (StatusEffect se : this.getStatusEffectsBasic()) {
			if (se.passRequirements(this)) {
				this.applySE(se);
			}
		}
	}
	protected void applyIncomingStatusEffects(Character attacker) {
		for (StatusEffect se : this.getStatusEffectIncoming()) {
			if (se.passRequirements(this, attacker)) {
				attacker.applySE(se);
			}
		}
	}
	protected void apply(Condition c) {
		// Automated Apply of Conditions does not apply incoming Status Effects, but all others simply use "applySE"
		if (c instanceof StatusEffect) {
			if ( ((StatusEffect) c).isIncoming() ) {
				return;
			}
			if (((StatusEffect) c).passRequirements(this)) {
				this.applySE(((StatusEffect) c));
			}
		}
		else if (c instanceof CrowdControl) {
			for (StatusEffect se : ((CrowdControl) c).effects) {
				if ( se.isIncoming() ) {
					return;
				}
				if (se.passRequirements(this)) {
					this.applySE(se);
				}
			}
		}
	}
	protected void unapplySE(StatusEffect se) {
		// If the exact Status Effect is already unapplied, do not unapply it again.
		if (!se.isApplied) {
			return;
		}
		
		// Selects the affected stat
		Stat changed = this.getStat(se.getAlteredStat());
		
		// Unapplies the amount to the changed stat
		changed.bonus -= se.amountAdded;
		se.amountAdded = 0;
		se.isApplied = false;
	}
	protected void unapplyBasicStatusEffects() {
		for (StatusEffect se : this.getStatusEffectsBasic()) {
			this.unapplySE(se);
		}
	}
	protected void unapplyIncomingStatusEffects(Character other) {
		for (StatusEffect se : this.getStatusEffectIncoming()) {
			other.unapplySE(se);
		}
	}
	protected void unapply(Condition c) {
		// Automated Unapply of Conditions does not apply incoming Status Effects, but all others simply use "applySE"
		if (c instanceof StatusEffect) {
			if ( ((StatusEffect) c).isIncoming() ) {
				return;
			}
			this.unapplySE((StatusEffect) c);
		}
		else if (c instanceof CrowdControl) {
			for (StatusEffect se : ((CrowdControl) c).effects) {
				if ( se.isIncoming() ) {
					return;
				}
				this.unapplySE(se);
			}
		}
	}
	
	
	// Methods for a Character's basic turn (beginning-end)
	// Start of turn
	public String beginTurn(LinkedList<Character> combatants) {
		String ret = "";
		
		// Increment all condition TurnCounters and remove expired conditions
		for (Character combatant : combatants) {
			LinkedList<Condition> toRemove = new LinkedList<>();
			for (Condition con : combatant.getActiveConditions()) {
				con.turnCount.increment();
				if (con.isExpired()) {
					toRemove.add(con);
				}
			}
			for (Condition c : toRemove) {
				if (c.isEndOfTurn()) {
					combatant.expiredConditions.add(c);
				}
				else {
					combatant.removeCondition(c);
				}
			}
		}
		
		// Apply Base Status Effects to all combatants
		for (Character combatant : combatants) {
			combatant.applyBasicStatusEffects();
		}
		
		// State current status of Character
		if (this.getCurrentHealth() < 0) {
			return this.getName() + " is dead.";
		}
		ret += "It is " + this.getName() + "'s turn.\n";
		ret += "Current Health: " + this.getCurrentHealth() + ".\n";
		ret += "Current Conditions:\n";
		LinkedList<Condition> curConditions = this.getActiveConditions();
		for (Condition c : curConditions) {
			ret += "\t" + c.toString() + "\n";
		}
		
		
		return ret;
	}
	// Returns a list of possible commands
	public LinkedList<String> commands() {
		LinkedList<String> ret = new LinkedList<>();
		ret.add("Basic Attack");
		ret.add("Add Condition");
		ret.add("Remove Condition");
		ret.add("End Turn");
		
		return ret;
	}
	// End of turn
	public String endTurn(LinkedList<Character> combatants) {
		String ret = this.getName() + "'s turn is over.";
		
		// Unapply Base Status Effects from all combatants
		for (Character combatant : combatants) {
			combatant.unapplyBasicStatusEffects();
		}
		
		// Remove any expired Conditions from all combatants
		for (Character combatant : combatants) {
			if (!combatant.expiredConditions.isEmpty()) {
				for (Condition c : combatant.expiredConditions) {
					combatant.removeCondition(c);
				}
				combatant.expiredConditions.clear();
			}
		}
		
		return ret;
	}
	
	
	// Methods used to store attacks made/defended (used in subclasses for interactions)
	protected void hitAttack(Attack atk) {
		this.AttacksMade.add(atk);
	}
	protected void missAttack(Attack atk) {
		this.AttacksMade.add(atk);
	}
	protected void receivedAttack(Attack atk) {
		this.AttacksDefended.add(atk);
	}
	protected void avoidAttack(Attack atk) {
		this.AttacksDefended.add(atk);
	}
	
	// Get method for previous attack made (for convenience)
	protected Attack previousAttack() {
		if (!this.AttacksMade.isEmpty()) {
			return this.AttacksMade.getLast();
		}
		return Attack.EMPTY;
	}
	
	
	// Direct combat methods (attacking, healing, dealing damage...)
	// Restores the health of the target by a certain amount, then returns the amount actually healed (if someone tried to heal above the maximum)
	protected int restoreHealth(int amount) {
		int healingReceived = amount;
		this.CurrentHealth += amount;
		if (this.CurrentHealth > this.getHealth()) {
			healingReceived = amount - (this.CurrentHealth - this.getHealth());
			this.CurrentHealth = this.getHealth();
		}
		return healingReceived;
	}
	// Takes damage by numerical amount
	protected void takeDamage(int damageDealt) {
		if (this.Shields > 0) {
			int remainingShields = this.Shields - damageDealt;
			if (remainingShields > 0) {
				this.Shields = remainingShields;
				return;
			}
			else {
				this.Shields = 0;
				damageDealt -= this.Shields;
			}
		}
		this.setCurrentHealth(this.getCurrentHealth() - damageDealt);
	}
	
	// Calculates the chance for the attack to land (is public so checks for "Advantage" can be done)
	public boolean landAttack(Character enemy, int bonusAvoidance) {
		Dice toHit = new Dice(this.getAccuracy());                           // Denominator or largest possible value for the random generator to decide
		boolean didHit = toHit.roll() > enemy.getDodge() + enemy.getBlock() + bonusAvoidance; // If what is rolled is Greater Than the enemy's Dodge/Block, the attack hits
		return didHit;
	}
	public boolean landAttack(Character enemy) {
		return landAttack(enemy, 0);
	}
	
	// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler when attacking
	protected double calcArmorEffect(Character enemy, int bonusArmor, boolean armorApplies) {
		
		double armorEffect;
		
		// If the attack is classified as "ignoring Armor" (armor does not apply) the minimum armorEffect is 1, and the bonus (amount above 1) is increased by 50%.
		if (!armorApplies) {
			if (enemy.getArmor() + bonusArmor > this.getArmorPiercing()) {
				armorEffect = 1;
			}
			else {
				armorEffect = this.getArmorPiercing()*1d / (enemy.getArmor() + bonusArmor);
				armorEffect = (armorEffect - 1) * 1.5 + 1;
			}
		}
		// Otherwise, just normal ArmorPiercing/Armor.
		else {
			armorEffect = this.getArmorPiercing()*1d / (enemy.getArmor() + bonusArmor);
		}
		
		return armorEffect;
	}
	protected double calcArmorEffect(Character enemy) {
		return this.calcArmorEffect(enemy, 0, true);
	}
	protected double calcArmorEffect(Character enemy, boolean armorApplies) {
		return this.calcArmorEffect(enemy, 0, armorApplies);
	}
	
	// Calculates whether an attack would be a Critical Strike
	protected boolean landCrit(Character enemy) {
		Dice percent = new Dice(100);
		return percent.roll() <= this.getCriticalChance();
	}
	
	// Calculates the final Damage done using the ranges from STDup and STDdown
	protected int calcFinalDamage(Character enemy, int finalDamageStat, double finalScaler, boolean didCrit, LinkedList<String> attackTypes) {
		// Count the number of things the enemy resists or is vulnerable to in the single attack (equal amounts of each type)
		int resistanceCount = 0, vulnerabilityCount = 0;
		for (String aType : attackTypes) {
			if (enemy.getResistances().contains(aType)) {
				resistanceCount++;
			}
			if (enemy.getVulnerabilities().contains(aType)) {
				vulnerabilityCount++;
			}
		}
		
		// Always just deal normal damage unless there are special attack types in which half the portion of the attack attributed is effected.
		double percentNormalDamage = 1;
		if (!attackTypes.isEmpty()) {
			percentNormalDamage = 1 - (resistanceCount - vulnerabilityCount)/(2.0*attackTypes.size());
		}
		
		// Calculate the totalDamage before STD is applied
		int totalDamage = (int)Math.round(finalDamageStat*finalScaler*percentNormalDamage);
		
		// Calculates the minimum and maximum Damage possible due to Standard Deviation (minimum is removed if you didCrit)
		int minDamage;
		int maxDamage = (int)(1.0 * totalDamage * this.getSTDup() / 100);
		
		if (didCrit) {
			minDamage = totalDamage;
		}
		else {
			minDamage = (int)(1.0 * totalDamage * this.getSTDdown());
		}
		
		// Determines where on the Damage spectrum created the Ability landed, and calculates the final Damage done
		Dice vary = new Dice(maxDamage-minDamage+1);
		return minDamage + vary.roll() -1;
	}
	
	// Deals a flat amount of damage to another Character and returns the output string based on damage and stating if they died.
	protected String dealDamage(Character enemy, int damageDealt, boolean didCrit) {
		// Apply all unapplied incoming Status Effects of the Defender to the Attacker (enemy to this)
		// (If it is already applied from "attack", the "applySE" function call will return before applying again)
		enemy.applyIncomingStatusEffects(this);
		
		enemy.takeDamage(damageDealt);
		
		String ret = "";
		
		// Attack output
		if (didCrit) {
			ret += this.getName() + " scored a CRITCAL HIT against " + enemy.getName() + " for a total of " + damageDealt + " damage!\n";
		}
		else {
			ret += this.getName() + " hit " + enemy.getName() + " for a total of " + damageDealt + " damage!\n";
		}
		
		if (enemy.isDead()) {
			Dice fundie = new Dice(5);
			String funword = "";
			
			switch(fundie.roll()) {
				case 1: {
					funword = "annihilated ";
					break;
				}
				case 2: {
					funword = "defeated ";
					break;
				}
				case 3: {
					funword = "obliterated ";
					break;
				}
				case 4: {
					funword = "purged the universe of ";
					break;
				}
				case 5: {
					funword = "destroyed ";
					break;
				}
			}
			
			ret += this.getName() + " has " + funword + enemy.getName() + "!";
		}
		else {
			ret += enemy.getName() + " has " + enemy.getCurrentHealth() + " Health remaining.";
		}
		
		// Store the attack made
		Attack atk = new AttackBuilder()
				.attacker(this)
				.defender(enemy)
				.didHit(true)
				.didCrit(didCrit)
				.damageDealt(damageDealt)
				.didKill(enemy.isDead())
				.build();
		this.hitAttack(atk);
		enemy.receivedAttack(atk);
		
		// Unapply the Incoming Status Effects
		enemy.unapplyIncomingStatusEffects(this);
		
		// Return the result
		return ret;
	}
	protected String dealDamage(Character enemy, int damageDealt) {
		return this.dealDamage(enemy, damageDealt, false);
	}
	
	public String attack(Character enemy, double scaler, boolean isTargeted, boolean canMiss, boolean armorApplies) {
		// Add: Check for being attacked conditions (Steel Legion Tank: Hold It Right There)
		
		// Make sure neither target is dead.
		if (this.isDead()) {
			return this.getName() + " is dead. Thus, " + this.getName() + " is incapable of attacking.";
		}
		if (enemy.isDead()) {
			return enemy.getName() + " is already dead. The attack had no effect.";
		}
		
		// Apply incoming Status Effects of the Defender to the Attacker (enemy to this)
		enemy.applyIncomingStatusEffects(this);
		
		// Attack always hits unless it is a Targeted attack and can miss (some targeted attacks cannot miss)
		boolean didHit = true;
		
		if (isTargeted && canMiss) {
			// Finds if the attack landed
			didHit = this.landAttack(enemy);
		}
		
		// If the attack missed
		if (!didHit) {
			// Store the attack attempt, then return
			Attack atk = new AttackBuilder()
					.attacker(this)
					.defender(enemy)
					.didHit(false)
					.didCrit(false)
					.damageDealt(0)
					.didKill(false)
					.build();
			this.missAttack(atk);
			enemy.avoidAttack(atk);
			return this.getName() + " missed " + enemy.getName() + "!";
		}
		// If the attack hits, now calculate Damage
		else {
			// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in
			double armorEffect;
			armorEffect = calcArmorEffect(enemy, armorApplies);
			scaler*=armorEffect;
			
			// Only Targeted attacks can critically hit
			boolean didCrit = false;
			if (isTargeted) {
				// Calculates whether the attack was a Critical Strike
				didCrit = landCrit(enemy);
			}
			
			if (didCrit) {
				scaler *= 2;
				if (this.getCriticalChance()>100) {
					scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
				}
			}
			
			// Calculates the final damage dealt over the deviation range
			int damageDealt = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit, this.getAttackType());
			
			// Damages the enemy and determines whether enemy died (Storing of attacks that hit occur in the "dealDamage" function)
			return this.dealDamage(enemy, damageDealt, didCrit);
		}
	}
	public String attack(Character enemy, double scaler) {
		return this.attack(enemy, scaler, true, true, true);
	}
	public String attack(Character enemy) {
		return this.attack(enemy, 1);
	}
	public String attack(Character enemy, double scaler, boolean isTargeted) {
		return this.attack(enemy, scaler, isTargeted, true, true);
	}
	public String attack(Character enemy, boolean isTargeted) {
		return this.attack(enemy, 1, isTargeted);
	}
	public String attack(Character enemy, double scaler, boolean isTargeted, boolean canMiss) {
		return this.attack(enemy, scaler, isTargeted, canMiss, true);
	}
	public String attack(Character enemy, boolean isTargeted, boolean canMiss) {
		return this.attack(enemy, 1, isTargeted, canMiss);
	}
	
	// Damages a player knocked into an object or another Character
	public String knockBackDamage(Character enemy, Obstacle obj, int extraSpaces) {
		String ret = "";
		
		// Calculate the damage the Character will take.
		int percentage = obj.calculatePercentage(extraSpaces);
		int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		double armorEffect = 1.0 * obj.getArmorScore() / enemy.getArmor();
		int damage = (int)Math.round(damageMax * armorEffect);
		
		// Deal the damage to the enemy
		enemy.takeDamage(damage);
		ret += "The " + obj.getName() + " dealt " + damage + " to " + enemy.getName() + "!";
		if (enemy.isDead()) {
			ret += "\n" + enemy.getName() + " was defeated from the damage taken from the " + obj.getName() + "!";
		}
		
		return ret;
	}
	public String knockBackDamage(Character enemy, Character collided, int extraSpaces) {
		String ret = "";
		
		// See if the collided Character blocked the incoming Character
		boolean wasBlocked = false;
		if (collided.getBlock() > 0) {
			// Base 30% + .5% (rounding up) per point of Block, -5% for each extra space.
			int chanceDeduction = 5 * extraSpaces;
			if (chanceDeduction > 30) {
				chanceDeduction = 30;
			}
			int toBlock = 30 + (int)Math.round(.5 * collided.getBlock()) - chanceDeduction;
			if (toBlock > 90) {
				toBlock = 90;
			}
			Dice percent = new Dice(100);
			wasBlocked = percent.roll() < toBlock;
		}
		
		// Different Damage Calculation if the Character was blocked
		if (wasBlocked) {
			// Calculate the damage the Character will take.
			// Calculate percentage of Health taken
			int percentage = 10 + 2 * extraSpaces;
			if (percentage > 20) {
				percentage = 20;
			}
			
			// Calculate and apply damage
			int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
			double armorEffect = 1.0 * collided.getArmor() / enemy.getArmor();
			int damage = (int)Math.round(damageMax * armorEffect);
			ret += collided.getName() + " successfully blocked " + enemy.getName() + "!\n";
			ret += collided.dealDamage(enemy, damage);
			
			// Add stun effect
			int stunDuration = 1;
			if (percentage >= 15) {
				stunDuration = 2;
			}
			if (percentage >= 20) {
				stunDuration = 3;
			}
			Stun stunEffect = new Stun("Knock-Back Stun", stunDuration);
			enemy.addCondition(stunEffect);
			
			// Return the result
			return ret;
		}
		
		// Otherwise, the Character was not blocked, and both parties take various damage
		
		// First, damage is dealt to the Character knocked-back (enemy)
		// Calculate percentage of Health taken
		int percentage = 5 + 2 * extraSpaces;
		if (percentage > 15) {
			percentage = 15;
		}
		
		// Calculate and apply damage
		int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		double armorEffect = 1.0 * collided.getArmor() / enemy.getArmor();
		int damage = (int)Math.round(damageMax * armorEffect);
		ret += collided.dealDamage(enemy, damage) + "\n";
		
		// Add Accuracy Reduction Effect
		StatusEffect enemyAccReduction = new StatusEffect("Knock-back Accuracy Reduction", 1, Stat.ACCURACY, -75);
		enemy.addCondition(enemyAccReduction);
		
		
		// Second, damage is dealt to the Character knocked into (collided)
		// Calculate percentage of Health taken
		percentage = 2 + 2 * extraSpaces;
		if (percentage > 10) {
			percentage = 10;
		}
		
		// Calculate and apply damage
		damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		armorEffect = 1.0 * enemy.getArmor() / collided.getArmor();
		damage = (int)Math.round(damageMax * armorEffect);
		ret += enemy.dealDamage(collided, damage);
		
		// Add Accuracy Reduction Effect
		StatusEffect collidedAccReduction = new StatusEffect("Knock-back Accuracy Reduction", 1, Stat.ACCURACY, -25);
		collided.addCondition(collidedAccReduction);
		
		
		// Return the result
		return ret;
	}
	
	
	// Prints the Character, listing the stats after the name.
	@Override
	public String toString() {
		String ret = "Name = " + this.getName() + "\n" +
					 "Level = " + this.getLevel() + "\n";
		for(Stat s : stats) {
			ret += s.toString() + "\n";
		}
		return ret;
	}
}

