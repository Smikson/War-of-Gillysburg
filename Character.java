package WyattWitemeyer.WarOfGillysburg;
import java.util.*;


public class Character {
	// Lists Types that can be added to a Character
	public static enum Type {
		NONE, PLAYER, FIRE, ICE, HAIRY, VERMIN, DRAGON, ARMORED
	}
	
	// Static Characters to aid with Character building and leveling up from a base level.
	public static final Character EMPTY = new Character("Null",0,0,0,0,0,0,0,0,0,0,0,0,0,0,90,110, Attack.DmgType.SLASHING,new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.NONE);
	public static final Character STEEL_LEGION_TANK = new Character("Tank",0,750,70,185,100,125,0,38,2,3,1,1,20,3,90,110, Attack.DmgType.SLASHING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character STEEL_LEGION_WARRIOR = new Character("Warrior",0,635,85,162,118,125,0,30,5,6,5,1,15,5,90,110, Attack.DmgType.SLASHING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character STEEL_LEGION_BARBARIAN = new Character("Barbarian",0,530,125,158,124,100,0,28,7,6,6,1,12,8,90,110, Attack.DmgType.SMASHING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SENTINEL_SNIPER = new Character("Sniper",0,400,140,118,146,160,27,0,8,5,8,8,5,20,90,110, Attack.DmgType.PIERCING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SENTINEL_SPECIALIST = new Character("Specialist",0,500,100,138,140,140,25,0,5,5,7,5,7,18,90,110, Attack.DmgType.PIERCING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SENTINEL_ARC_ARCHER = new Character("Arc Archer",0,350,85,125,130,150,43,0,7,6,35,6,6,19,90,110, Attack.DmgType.PIERCING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SILENT_DEATH_SHADOW = new Character("Shadow",0,375,150,117,147,118,33,0,10,7,5,1,2,25,90,110, Attack.DmgType.PIERCING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SILENT_DEATH_POISON_SPECIALIST = new Character("Poison Specialist",0,480,110,140,128,115,24,0,5,4,4,1,6,15,90,110, Attack.DmgType.SLASHING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character SILENT_DEATH_HUNTER = new Character("Hunter",0,520,100,146,126,125,23,0,6,4,5,4,4,16,90,110, Attack.DmgType.PIERCING, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character KINITCHU_ORDER_DRAGON_FIRE_WIZARD = new Character("Pyromancer",0,370,155,121,142,120,20,0,8,4,4,5,4,23,90,110, Attack.DmgType.FIRE, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character KINITCHU_ORDER_THAUMRATURGE = new Character("Ice Wizard",0,510,105,150,121,122,21,0,6,4,4,5,17,7,90,110, Attack.DmgType.ICE, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character KINITCHU_ORDER_ARCANA = new Character("Arcana",0,325,165,115,152,140,18,0,9,4,6,5,6,17,90,110, Attack.DmgType.ARCANE, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character KINITCHU_ORDER_LUMINESCENT_WIZARD = new Character("Luminescent Wizard",0,425,120,120,134,135,21,0,8,4,5,5,3,24,90,110, Attack.DmgType.LIGHT, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	public static final Character KINITCHU_ORDER_NECROMANCER = new Character("Necromancer",0,410,130,119,138,125,20,0,5,4,4,5,6,19,90,110, Attack.DmgType.NECROMANTIC, new HashMap<Attack.DmgType,Double>(), new HashMap<Attack.DmgType,Double>(), Character.Type.PLAYER);
	
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
	
	private Attack.DmgType baseDmgType;
	private Attack.RangeType baseRangeType;
	private HashMap<Attack.DmgType,Double> resistances;
	private HashMap<Attack.DmgType,Double> vulnerabilities;
	
	private Type CharacterType;
	
	// Contains a list of the basic stats (everything but STDup/down, Current Health, and shields), conditions, and damage over time effects for looping through
	protected LinkedList<Stat> stats;
	protected LinkedList<Condition> conditions;
	
	// Holds the possible actions a Character can take on their turn and a boolean to control when the Character's turn is over
	private LinkedList<Command> commands;
	private boolean turnActionsSpent;
	
	// Store the previous attacks made (for use in Character Abilities sometimes)
	protected LinkedList<AttackResult> AttacksMade;
	protected LinkedList<AttackResult> AttacksDefended;
	
	// Boolean to keep track of when its this Character's turn
	private boolean inTurn;
	
	// Constructor (sets each stat variable)
	public Character(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type) {
		this.name = nam;
		this.Level = lvl;
		this.Health = new Stat(hp, Stat.Version.HEALTH);
		this.Damage = new Stat(dmg, Stat.Version.DAMAGE);
		this.Armor = new Stat(arm, Stat.Version.ARMOR);
		this.ArmorPiercing = new Stat(armp, Stat.Version.ARMOR_PIERCING);
		this.Accuracy = new Stat(acc, Stat.Version.ACCURACY);
		this.Dodge = new Stat(dod, Stat.Version.DODGE);
		this.Block = new Stat(blk, Stat.Version.BLOCK);
		this.CriticalChance = new Stat(crit, Stat.Version.CRITICAL_CHANCE);
		this.Speed = new Stat(spd, Stat.Version.SPEED);
		this.AttackSpeed = new Stat(atkspd, Stat.Version.ATTACK_SPEED);
		this.Range = new Stat(range, Stat.Version.RANGE);
		this.Threat = new Stat(thrt, Stat.Version.THREAT);
		this.TacticalThreat = new Stat(tactthrt, Stat.Version.TACTICAL_THREAT);
		this.STDdown = new Stat(stdDown, Stat.Version.STANDARD_DEVIATION_DOWN);
		this.STDup = new Stat(stdUp, Stat.Version.STANDARD_DEVIATION_UP);
		
		this.CurrentHealth = this.Health.getTotal();
		this.Shields = 0;
		
		this.baseDmgType = dmgType;
		this.baseRangeType = range == 1? Attack.RangeType.MELEE : Attack.RangeType.RANGED;
		
		this.resistances = new HashMap<>();
		this.vulnerabilities = new HashMap<>();
		
		this.resistances.putAll(resis);
		this.vulnerabilities.putAll(vuls);
		
		this.CharacterType = type;
		
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
		
		// Initializes Condition, Damage over Time, and Attack lists
		this.conditions = new LinkedList<>();
		this.AttacksMade = new LinkedList<>();
		this.AttacksDefended = new LinkedList<>();
		
		// Initializes the possible commands for a basic Character
		this.commands = new LinkedList<>();
		this.commands.add(new BasicAttackCommand(this, this.baseDmgType, this.baseRangeType));
		this.commands.add(new AlterCharacterCommand(this));
		this.commands.add(new EndTurnCommand(this));
		
		// Initializes in turn to false
		this.inTurn = false;
	}
	public Character(Character copy) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getType());
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
	
	public Attack.DmgType getBaseDmgType() {
		return this.baseDmgType;
	}
	public Attack.RangeType getRangeType() {
		return this.baseRangeType;
	}
	public HashMap<Attack.DmgType,Double> getResistances() {
		// Copies the list so alterations do not occur on the Character itself
		HashMap<Attack.DmgType,Double> copy = new HashMap<>();
		copy.putAll(this.resistances);
		return copy;
	}
	public HashMap<Attack.DmgType,Double> getVulnerabilities() {
		// Copies the list so alterations do not occur on the Character itself
		HashMap<Attack.DmgType,Double> copy = new HashMap<>();
		copy.putAll(this.vulnerabilities);
		return copy;
	}
	
	public Type getType() {
		return this.CharacterType;
	}
	
	public int getSTDdown() {
		return this.STDdown.getTotal() + this.STDdown.bonus;
	}
	public int getSTDup() {
		return this.STDup.getTotal() + this.STDup.bonus;
	}
	
	public boolean turnActionsSpent() {
		return this.turnActionsSpent;
	}
	public void useTurnActions() {
		this.turnActionsSpent = true;
	}
	
	public int getCurrentHealth() {
		return this.CurrentHealth;
	}
	public int getMissingHealth() {
		return this.getHealth() - this.getCurrentHealth();
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
	
	// Functions that show the status of the Character
	public boolean inTurn() {
		return this.inTurn;
	}
	public boolean isDead() {
		return this.getCurrentHealth() <= 0;
	}
	public boolean canAttack() {
		for (Condition c : this.getAllConditions()) {
			if (c instanceof Stun || c instanceof Blind || c instanceof Stasis) {
				return false;
			}
		}
		return true;
	}
	public boolean isTargetable() {
		for (Condition c : this.getAllConditions()) {
			if (c instanceof Stasis || c instanceof Invulnerable) {
				return false;
			}
		}
		return true;
	}
	
	
	// Condition/Stat methods
	// Helpful method for checking stats
	private Stat getStat(Stat.Version type) {
		// Find the matching Stat for this Character
		for (Stat s : this.stats) {
			if (s.getVersion().equals(type)) {
				return s;
			}
		}
		
		// Return the matching stat, or an empty stat if none were found
		return new Stat();
	}
	public int getStatValue(Stat.Version type) {
		Stat requested = this.getStat(type);
		return requested.getTotal() + requested.bonus;
	}
	
	// To see if a condition is contained by the Character
	protected boolean containsCondition(Condition c) {
		for (Condition con : this.conditions) {
			if (con.getName().equals(c.getName())) {
				return true;
			}
		}
		return false;
	}
	
	// To add conditions to this Character
	protected void addCondition(Condition c) {
		// If the Condition is already present and the Condition is not "stacking", first remove any existing Conditions with the same name, then proceed
		if (this.containsCondition(c) && !c.isStacking()) {
			while (this.containsCondition(c)) {
				this.removeCondition(c);
			}
		}
		this.conditions.add(c);
	}
	
	// To remove conditions from this Character
	protected void removeCondition(Condition removed) {
		// If the Condition is already removed (or wasn't there in the first place), we are done.
		Condition toRemove = new Condition();
		for (Condition con : this.conditions) {
			if (con.getName().equals(removed.getName())) {
				toRemove = con;
			}
		}
		
		// Unapply condition if need be
		this.unapply(toRemove);
		
		// Remove condition
		this.conditions.remove(toRemove);
		
		// Removes linked conditions
		for (Condition c : toRemove.getLinkedConditions()) {
			this.removeCondition(c);
		}
	}
	
	
	// To see all Conditions in a single list (just active ones or all)
	protected LinkedList<Condition> getActiveConditions() {
		LinkedList<Condition> ret = new LinkedList<>();
		for (Condition c : this.conditions) {
			if (c.isActive() || c.getActiveRequirement().evaluate(this)) {
				c.activate();  // Once active, always active (till removed)
				ret.add(c);
			}
		}
		return ret;
	}
	protected LinkedList<Condition> getAllConditions() {
		LinkedList<Condition> ret = new LinkedList<>();
		for (Condition c : this.conditions) {
			ret.add(c);
		}
		return ret;
	}
	
	// To have easier access to basic status effects, incoming status effects, and all status effects
	protected LinkedList<StatusEffect> getStatusEffectsBasic() {
		// Create return list
		LinkedList<StatusEffect> ret = new LinkedList<>();
		
		// Find each active condition
		for (Condition c : this.getActiveConditions()) {
			// For each basic status effect (non-Incoming) in that condition
			for (StatusEffect se : c.getStatusEffects()) {
				if (se.getType().equals(StatusEffect.Type.BASIC)) {
					ret.add(se);
				}
			}
		}
		
		// Return the result
		return ret;
	}
	protected LinkedList<StatusEffect> getStatusEffectsIncoming() {
		// Create return list
		LinkedList<StatusEffect> ret = new LinkedList<>();
		
		// Find each active condition
		for (Condition c : this.getActiveConditions()) {
			// For each Incoming status effect in that condition
			for (StatusEffect se : c.getStatusEffects()) {
				if (se.getType().equals(StatusEffect.Type.INCOMING)) {
					ret.add(se);
				}
			}
		}
		
		// Return the result
		return ret;
	}
	protected LinkedList<StatusEffect> getStatusEffectsOutgoing() {
		// Create return list
		LinkedList<StatusEffect> ret = new LinkedList<>();
		
		// Find each active condition
		for (Condition c : this.getActiveConditions()) {
			// For each Incoming status effect in that condition
			for (StatusEffect se : c.getStatusEffects()) {
				if (se.getType().equals(StatusEffect.Type.OUTGOING)) {
					ret.add(se);
				}
			}
		}
		
		// Return the result
		return ret;
	}
	protected LinkedList<StatusEffect> getStatusEffectAll() {
		LinkedList<StatusEffect> ret = this.getStatusEffectsBasic();
		ret.addAll(this.getStatusEffectsIncoming());
		ret.addAll(this.getStatusEffectsOutgoing());
		return ret;
	}
	
	// These functions apply and unapply Status Effects to this Character's basic stats
	// For Applying
	protected void applySE(StatusEffect se) {
		// If the exact Status Effect is already applied, do not apply it again.
		if (se.isApplied) {
			return;
		}
		
		// Increment the number of uses of the StatusEffect
		se.incrementUses();
		
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
			if (se.getApplyRequirement().evaluate(this)) {
				this.applySE(se);
			}
		}
	}
	protected void applyIncomingStatusEffects(Character attacker) {
		for (StatusEffect se : this.getStatusEffectsIncoming()) {
			if (se.getApplyDualRequirement().evaluate(this, attacker)) {
				if (se.affectsSelf()) {
					this.applySE(se);
				}
				else {
					attacker.applySE(se);
				}
			}
		}
	}
	protected void applyOutgoingStatusEffects(Character defender) {
		for (StatusEffect se : this.getStatusEffectsOutgoing()) {
			if (se.getApplyDualRequirement().evaluate(this, defender)) {
				if (se.affectsSelf()) {
					this.applySE(se);
				}
				else {
					defender.applySE(se);
				}
			}
		}
	}
	protected void apply(Condition c, Character other) {
		// Automated Apply of Conditions that applies all its Status Effects using "applySE" assuming they all directly affect the current Character
		// Make a note that each time this function is called, "unapply" should be called for the same condition when finished.
		for (StatusEffect se : c.getStatusEffects()) {
			if (se.getApplyRequirement().evaluate(this) && se.getApplyDualRequirement().evaluate(this, other)) {
				if (se.affectsSelf() || other.equals(Character.EMPTY)) {
					this.applySE(se);
				}
				else {
					other.applySE(se);
				}
			}
		}
	}
	protected void apply(Condition c) {
		this.apply(c, Character.EMPTY);
	}
	// For Unapplying
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
	protected void unapplyIncomingStatusEffects(Character attacker) {
		for (StatusEffect se : this.getStatusEffectsIncoming()) {
			if (se.affectsSelf()) {
				this.unapplySE(se);
			}
			else {
				attacker.unapplySE(se);
			}
		}
	}
	protected void unapplyOutgoingStatusEffects(Character defender) {
		for (StatusEffect se : this.getStatusEffectsOutgoing()) {
			if (se.affectsSelf()) {
				this.unapplySE(se);
			}
			else {
				defender.unapplySE(se);
			}
		}
	}
	protected void unapply(Condition c, Character other) {
		// Automated Unapply of Conditions that unapplies all its Status Effects using "applySE" assuming they all directly affect the current Character
		for (StatusEffect se : c.getStatusEffects()) {
			if (se.affectsSelf()) {
				this.unapplySE(se);
			}
			else {
				if (other.equals(Character.EMPTY)) {
					return;
				}
				other.unapplySE(se);
			}
		}
	}
	protected void unapply(Condition c) {
		this.unapply(c, Character.EMPTY);
	}
	
	// Methods for a Character's basic turn (beginning-end)
	// For list of commands:
	protected void addCommand(Command added) {
		// Added commands are added to the end of the list, but before the last two commands (Alter Character and End Turn)
		this.commands.add(this.commands.size() - 2, added);
	}
	protected void alterBasicAttack(BasicAttackCommand altered) {
		this.commands.remove(0);
		this.commands.add(0, altered);
	}
	protected void restoreBasicAttack() {
		this.alterBasicAttack(new BasicAttackCommand(this, this.baseDmgType, this.baseRangeType));
	}
	
	// Prompts for altering a Character
	// Add Condition Functions (and Prompt for Add Conditions)
	// Adding Crowd Control Conditions
	private void promptConditionAddCrowdControl() {
		// Make a parallel String list for printing
		LinkedList<String> ccStrings = new LinkedList<>();
		for (CrowdControl cc : CrowdControl.CCLIST) {
			ccStrings.add(cc.getName());
		}
		
		// Add chosen Condition to Character
		int choice = BattleSimulator.getInstance().promptSelect(ccStrings);
		if (choice == 0) {
			return;
		}
		this.addCondition(CrowdControl.CCLIST.get(choice-1));
		
		ccStrings.clear();
	}
	// Adding Terrain Conditions
	private void promptConditionAddTerrain() {
		// Make a parallel String list for printing
		LinkedList<String> terrainStrings = new LinkedList<>();
		for (Terrain t : Terrain.TERRAINLIST) {
			terrainStrings.add(t.getName());
		}
		
		// Add chosen Condition to Character
		int choice = BattleSimulator.getInstance().promptSelect(terrainStrings);
		if (choice == 0) {
			return;
		}
		this.addCondition(Terrain.TERRAINLIST.get(choice-1));
		
		terrainStrings.clear();
	}
	
	// Overwritten in other classes, used to apply a Class Condition from this Character to a different Character
	public void promptClassConditionGive(Character other) {
		System.out.println(this.getName() + " has no Class Conditions to add.");
	}
	
	// The Add Condition Prompt
	protected void promptConditionAdd() {
		String choice;
		System.out.println("Choose Type of Condition to Add:");
		System.out.println("0. None (Go back)");
		System.out.println("1. Crowd Control");
		System.out.println("2. Terrain");
		System.out.println("3. Class Condition");
		while (true) {
			System.out.print("Choice? ");
			choice = BattleSimulator.getInstance().getPrompter().nextLine();
			switch(choice)
	        {
	            case "0": // None
	                return;
	            case "1": // Crowd Control
	            	System.out.println();
	            	this.promptConditionAddCrowdControl();
	                return;
	            case "2": // Terrain
	            	System.out.println();
	            	this.promptConditionAddTerrain();
	                return;
	            case "3": // Class Condition
	            	System.out.println();
	            	Character chosenClass = BattleSimulator.getInstance().targetSingle();
	                if (chosenClass.equals(Character.EMPTY)) {
	                	break;
	                }
	                chosenClass.promptClassConditionGive(this);
	                return;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
	}
	
	
	// Used to remove a chosen Condition from a Character (Remove Condition Prompt)
	protected void promptConditionRemove() {
		// Make a parallel String list for printing
		LinkedList<String> conditionStrings = new LinkedList<>();
		for (Condition c : this.getAllConditions()) {
			conditionStrings.add(c.toString());
		}
		
		// Remove chosen Condition from Character
		int choice = BattleSimulator.getInstance().promptSelect(conditionStrings);
		if (choice == 0) {
			return;
		}
		this.removeCondition(this.getAllConditions().get(choice-1));
		
		conditionStrings.clear();
	}
	
	// Prompts for how to alter this Character when chosen
	protected void promptAlterCharacter() {
		String choice;
		System.out.println("Select how to alter " + this.getName() + ":");
		System.out.println("0. None (Go back)");
		System.out.println("1. Alter Current Health");
		System.out.println("2. Add Condition");
		System.out.println("3. Remove Condition");
		while (true) {
			System.out.print("Choice? ");
			choice = BattleSimulator.getInstance().getPrompter().nextLine();
			switch(choice)
	        {
	            case "0": // None
	                return;
	            case "1": // Alter Current Health
	            	System.out.println();
	            	System.out.println(this.getName() + "'s Current Health: " + this.getCurrentHealth());
	            	System.out.print("Enter the amount to add (negative to subtract): ");
	            	while (true) {
	            		if (BattleSimulator.getInstance().getPrompter().hasNextInt()) {
		    				int amount = BattleSimulator.getInstance().getPrompter().nextInt();
		    				BattleSimulator.getInstance().getPrompter().nextLine();
		    				if (amount < 0) {
		    					this.setCurrentHealth(this.getCurrentHealth() + amount);
		    				}
		    				else {
		    					this.restoreHealth(amount);
		    				}
		    				System.out.println(this.getName() + "'s new Current Health: " + this.getCurrentHealth());
		    				return;
		    			}
		            	else {
		    				String responce = BattleSimulator.getInstance().getPrompter().nextLine();
		    				System.out.println("\""+responce+"\" is not a valid responce. Please enter a valid responce.");
		    				System.out.print("Choice? ");
		    			}
	            	}
	            case "2": // Add Condition
	            	System.out.println();
	            	this.promptConditionAdd();
	                return;
	            case "3": // Remove Condition
	            	System.out.println();
	            	this.promptConditionRemove();
	                return;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
	}
	
	// Start of turn
	protected void beginTurnSetup() {
		// Designate the Character is in its turn
		this.inTurn = true;
		
		// Make turn actions available
		this.turnActionsSpent = false;
		
		// Conditions: Increment all non-source-incrementing, non-permanent, non-end-of-turn conditions for this Character and remove respective expired conditions
		LinkedList<Condition> toRemove = new LinkedList<>();
		for (Condition con : this.getActiveConditions()) {
			// Increment non-source-incrementing and non-permanent Conditions
			if (!con.isSourceIncrementing() && !con.isPermanent() && !con.isEndOfTurn()) {
				con.incrementTurn();
			}
			if (con.isExpired()) {
				toRemove.add(con);
			}
		}
		for (Condition c : toRemove) {
				this.removeCondition(c);
		}
		
		// Increment all source-incrementing, non-end-of-turn conditions for all Characters in battle with this Character as the source and remove respecive expired conditions
		// Afterwards, while we're at it, goes ahead and applies all basic status effects to the respective combatants
		for (Character combatant : BattleSimulator.getInstance().getCombatants()) {
			toRemove.clear();
			for (Condition con : combatant.getActiveConditions()) {
				if (con.isSourceIncrementing() && con.getSource().equals(this) && !con.isEndOfTurn()) {
					con.incrementTurn();
				}
				// Also removes any other conditions that are expired
				if (con.isExpired()) {
					toRemove.add(con);
				}
			}
			for (Condition c : toRemove) {
				combatant.removeCondition(c);
			}
			
			combatant.applyBasicStatusEffects();
		}
	}
	protected void printTurnStats() {
		System.out.println("It is " + this.getName() + "'s turn.");
		System.out.println("Current Health: " + this.getCurrentHealth());
	}
	protected void printActiveConditions() {
		System.out.println("Current Conditions:");
		LinkedList<Condition> curConditions = this.getActiveConditions();
		for (Condition c : curConditions) {
			System.out.println("\t" + c.toString());
		}
		System.out.println();
	}
	protected void beginTurnDisplay() {
		this.printTurnStats();
		this.printActiveConditions();
		//DE Active Abilities (override in other classes?)
		Command.promptCommands(this.commands);
	}
	public void beginTurn() {
		// Setup
		this.beginTurnSetup();
		
		// State if Character is dead
		if (this.getCurrentHealth() < 0) {
			System.out.println(this.getName() + " is dead. Have turn anyway?");
			if (!BattleSimulator.getInstance().askYorN()) {
				System.out.println();
				this.endTurn();
				return;
			}
		}
		
		// Do action based on command given
		while (true) {
			// If turn actions are spent, ask to continue
			if (this.turnActionsSpent()) {
				System.out.println("\n" + this.getName() + "'s Turn Actions have been spent, End turn?");
				if (BattleSimulator.getInstance().askYorN()) {
					break;
				}
			}
			
			// Display stats and prompt for available commands
			this.beginTurnDisplay();
		}
		
		// End the turn
		System.out.println();
		this.endTurn();
	}
	// End of turn
	protected void endTurnSetup() {
		// Designate this Character's turn is over
		this.inTurn = false;
		
		// Increment all non-source-incrementing, non-permanent, end-of-turn conditions for this Character and remove respective expired conditions
		LinkedList<Condition> toRemove = new LinkedList<>();
		for (Condition con : this.getActiveConditions()) {
			// Increment non-source-incrementing and non-permanent Conditions
			if (!con.isSourceIncrementing() && !con.isPermanent() && con.isEndOfTurn()) {
				con.incrementTurn();
			}
			if (con.isExpired()) {
				toRemove.add(con);
			}
		}
		for (Condition c : toRemove) {
				this.removeCondition(c);
		}
		
		// Increment all source-incrementing, end-of-turn conditions for all Characters in battle with this Character as the source and remove respective expired conditions
		// Afterwards, while we're at it, goes ahead and unapplies all basic status effects to the respective combatants
		for (Character combatant : BattleSimulator.getInstance().getCombatants()) {
			toRemove.clear();
			for (Condition con : combatant.getActiveConditions()) {
				if (con.isSourceIncrementing() && con.getSource().equals(this) && con.isEndOfTurn()) {
					con.incrementTurn();
				}
				// Also removes any other conditions that are expired
				if (con.isExpired()) {
					toRemove.add(con);
				}
			}
			for (Condition c : toRemove) {
				combatant.removeCondition(c);
			}
			
			combatant.unapplyBasicStatusEffects();
		}
	}
	public void endTurn() {
		// Setup
		this.endTurnSetup();
		
		// State facts
		System.out.println(this.getName() + "'s turn is over.");
		
		// Return (though basic, this is a model to be overwritten)
		System.out.println("Enter something then press enter to continue.");
		BattleSimulator.getInstance().getPrompter().nextLine();
		System.out.println("\n-----------------------------------------------------------------------------");
	}
	
	
	// Get method for previous attack made (for convenience)
	protected AttackResult previousAttack() {
		if (!this.AttacksMade.isEmpty()) {
			return this.AttacksMade.getLast();
		}
		return AttackResult.EMPTY;
	}
	
	
	// Direct combat methods (attacking, healing, dealing damage...)
	// Restores the health of the target by a certain amount, then returns the amount actually healed (if someone tried to heal above the maximum)
	protected void restoreHealth(int amount) {
		if (amount <= 0) {
			return;
		}
		
		int healingReceived = amount;
		this.CurrentHealth += amount;
		if (this.CurrentHealth > this.getHealth()) {
			healingReceived = amount - (this.CurrentHealth - this.getHealth());
			this.CurrentHealth = this.getHealth();
		}
		if (healingReceived == 0) {
			System.out.println(this.getName() + " healed at maximum health.");
			return;
		}
		System.out.println(this.getName() + " healed for " + healingReceived + " Health for a new total of " + this.getCurrentHealth());
	}
	// Takes damage by numerical amount
	protected int takeDamage(int damageDealt, Attack.DmgType aType) {
		// Checks for invincibility or stasis
		for (Condition c : this.getActiveConditions()) {
			if (c instanceof Invincible || c instanceof Stasis) {
				damageDealt = 0;
			}
		}
		
		// Applies resistance/vulnerability effects if present
		if (this.getResistances().containsKey(aType)) {
			damageDealt = (int)Math.round(damageDealt * (1.0 - this.getResistances().get(aType)/100.0));
		}
		if (this.getVulnerabilities().containsKey(aType)) {
			damageDealt = (int)Math.round(damageDealt * (1.0 + this.getVulnerabilities().get(aType)/100.0));
		}
		
		// Deals the amount of damage to this Character
		int shieldDamage = 0;
		if (this.Shields > 0) {
			int remainingShields = this.Shields - damageDealt;
			if (remainingShields > 0) {
				this.Shields = remainingShields;
				return damageDealt;
			}
			else {
				shieldDamage = this.Shields;
				this.Shields = 0;
			}
		}
		this.setCurrentHealth(this.getCurrentHealth() - (damageDealt - shieldDamage));
		return damageDealt;
	}
	
	// Stores the information from an AttackResult
	protected void storeAttack(AttackResult atkRes) {
		// If this character was the attacker, store in attacks made
		if (this.equals(atkRes.getAttacker())) {
			this.AttacksMade.add(atkRes);
		}
		// If this character was the defender, store in attacks defended
		else if (this.equals(atkRes.getDefender())) {
			this.AttacksDefended.add(atkRes);
		}
	}
	
	// Functions to apply and unapply the correct attack Conditions (Incoming or Outgoing) for the Characters in an Attack
	protected void applyAttackConditions(Attack atk) {
		// If attacking, apply outgoing status effects
		if (this.equals(atk.getAttacker())) {
			this.applyOutgoingStatusEffects(atk.getDefender());
		}
		// If defending, apply incoming status effects
		else if (this.equals(atk.getDefender())) {
			this.applyIncomingStatusEffects(atk.getAttacker());
		}
	}
	protected void unapplyAttackConditions(AttackResult atkRes) {
		// If attacking, unapply outgoing status effects, then store the attack result
		if (this.equals(atkRes.getAttacker())) {
			this.unapplyOutgoingStatusEffects(atkRes.getDefender());
		}
		// If defending, unapply incoming status effects, then store the attack result
		else if (this.equals(atkRes.getDefender())) {
			this.unapplyIncomingStatusEffects(atkRes.getAttacker());
		}
	}
	
	// These functions apply the pre-attack and post-attack effects for the Characters (can be overridden by each class for additional effects, default of no effects)
	protected void applyPreAttackEffects(Attack atk) {}
	protected void applyPostAttackEffects(AttackResult atkRes) {}
	
	
	
	// Damages a player knocked into an object or another Character
	public String knockBackDamage(Character enemy, Obstacle obj, int extraSpaces) {
		String ret = "";
		
		// Calculate the damage the Character will take.
		int percentage = obj.calculatePercentage(extraSpaces);
		int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		double armorEffect = 1.0 * obj.getArmorScore() / enemy.getArmor();
		int damage = (int)Math.round(damageMax * armorEffect);
		
		// Deal the damage to the enemy
		enemy.takeDamage(damage, Attack.DmgType.TRUE);
		ret += "The " + obj.getName() + " dealt " + damage + " to " + enemy.getName() + "!";
		if (enemy.isDead()) {
			ret += "\n" + enemy.getName() + " was defeated from the damage taken from the " + obj.getName() + "!";
		}
		
		return ret;
	}
	public void knockBackDamage(Character enemy, Character collided, int extraSpaces) {
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
			
			// Calculate and apply damage
			int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
			double armorEffect = 1.0 * collided.getArmor() / enemy.getArmor();
			int damage = (int)Math.round(damageMax * armorEffect);
			System.out.println(collided.getName() + " successfully blocked " + enemy.getName() + "!");
			Attack collidedAttack = new AttackBuilder()
					.attacker(collided)
					.defender(enemy)
					.type(Attack.DmgType.TRUE)
					.usesFlatDamage()
					.flatDamage(damage)
					.hasDeviation(false)
					.build();
			collidedAttack.execute();
		}
		
		// Otherwise, the Character was not blocked, and both parties take various damage
		
		// First, damage is dealt to the Character knocked-back (enemy)
		// Calculate percentage of Health taken
		int percentage = 5 + 2 * extraSpaces;
		if (percentage > 15) {
			percentage = 15;
		}
		
		// Add Accuracy Reduction Effect
		StatusEffect enemyAccReductionEffect = new StatusEffect(Stat.Version.ACCURACY, -75, StatusEffect.Type.BASIC);
		Condition enemyAccReduction = new Condition("Kock-back Accuracy Reduction", 1);
		enemyAccReduction.addStatusEffect(enemyAccReductionEffect);
		enemy.addCondition(enemyAccReduction);
		
		// Calculate and apply damage
		int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		double armorEffect = 1.0 * collided.getArmor() / enemy.getArmor();
		int damage = (int)Math.round(damageMax * armorEffect);
		Attack collidedAttack = new AttackBuilder()
				.attacker(collided)
				.defender(enemy)
				.type(Attack.DmgType.TRUE)
				.usesFlatDamage()
				.flatDamage(damage)
				.hasDeviation(false)
				.build();
		collidedAttack.execute();
		
		
		// Second, damage is dealt to the Character knocked into (collided)
		// Calculate percentage of Health taken
		percentage = 2 + 2 * extraSpaces;
		if (percentage > 10) {
			percentage = 10;
		}
		
		// Add Accuracy Reduction Effect
		StatusEffect collidedAccReductionEffect = new StatusEffect(Stat.Version.ACCURACY, -25);
		Condition collidedAccReduction = new Condition("Knock-back Accuracy Reduction", 1);
		collidedAccReduction.addStatusEffect(collidedAccReductionEffect);
		collided.addCondition(collidedAccReduction);
		
		// Calculate and apply damage
		damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		armorEffect = 1.0 * enemy.getArmor() / collided.getArmor();
		damage = (int)Math.round(damageMax * armorEffect);
		Attack enemyAttack = new AttackBuilder()
				.attacker(enemy)
				.defender(collided)
				.type(Attack.DmgType.TRUE)
				.usesFlatDamage()
				.flatDamage(damage)
				.hasDeviation(false)
				.build();
		enemyAttack.execute();
	}
	
	
	// Prints the Character, listing the stats after the name.
	public String getStatStrings() {
		String ret = "Level: " + this.getLevel();
		for(Stat s : stats) {
			ret += "\n" + s.toString();
		}
		return ret;
	}
	@Override
	public String toString() {
		return this.getName() + "\n" + this.getStatStrings();
	}
	// Does a full display of the Character (subclasses override to include Abilities)
	public String getDescription() {
		String ret = this.toString();
		ret += "\nBase Damage Type: " + this.getBaseDmgType();
		ret += "\nBase Range Type: " + this.getRangeType();
		ret += "\nResistances: " + this.getResistances().toString();
		ret += "\nVulnerabilities: " + this.getVulnerabilities().toString();
		return ret;
	}
}

