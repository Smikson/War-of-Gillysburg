package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

enum CharacterType {
	NONE, PLAYER, FIRE, ICE, HAIRY, VERMIN, DRAGON
}

public class Character {
	// Static Characters to aid with Character building and leveling up from a base level.
	public static final Character EMPTY = new Character("Null",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.NONE);
	public static final Character STEEL_LEGION_TANK = new Character("Tank",0,750,70,185,100,125,0,38,2,3,1,1,20,3,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character STEEL_LEGION_WARRIOR = new Character("Warrior",0,635,85,162,118,125,0,30,5,6,5,1,15,5,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character STEEL_LEGION_BARBARIAN = new Character("Barbarian",0,530,125,158,124,100,0,28,7,6,6,1,12,8,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SENTINEL_SNIPER = new Character("Sniper",0,400,140,118,146,160,27,0,8,5,8,8,5,20,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SENTINEL_SPECIALIST = new Character("Specialist",0,500,100,138,140,140,25,0,5,5,7,5,7,18,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SENTINEL_ARC_ARCHER = new Character("Arc Archer",0,350,85,125,130,150,43,0,7,6,35,6,6,19,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SILENT_DEATH_SHADOW = new Character("Shadow",0,375,150,117,147,118,33,0,10,7,5,1,2,25,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SILENT_DEATH_POISON_SPECIALIST = new Character("Poison Specialist",0,480,110,140,128,115,24,0,5,4,4,1,6,15,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character SILENT_DEATH_HUNTER = new Character("Hunter",0,520,100,146,126,125,23,0,6,4,5,4,4,16,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character KINITCHU_ORDER_DRAGON_FIRE_WIZARD = new Character("Pyromancer",0,370,155,121,142,120,20,0,8,4,4,5,4,23,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character KINITCHU_ORDER_THAUMRATURGE = new Character("Ice Wizard",0,510,105,150,121,122,21,0,6,4,4,5,17,7,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character KINITCHU_ORDER_ARCANA = new Character("Arcana",0,325,165,115,152,140,18,0,9,4,6,5,6,17,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character KINITCHU_ORDER_LUMINESCENT_WIZARD = new Character("Luminescent Wizard",0,425,120,120,134,135,21,0,8,4,5,5,3,24,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	public static final Character KINITCHU_ORDER_NECROMANCER = new Character("Necromancer",0,410,130,119,138,125,20,0,5,4,4,5,6,19,90,110, new HashMap<AttackType,Double>(), new HashMap<AttackType,Double>(), CharacterType.PLAYER);
	
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
	
	private HashMap<AttackType,Double> resistances;
	private HashMap<AttackType,Double> vulnerabilities;
	
	private CharacterType Type;
	
	// Contains a list of the basic stats (everything but STDup/down and Current Health), conditions, and damage over time effects for looping through
	protected LinkedList<Stat> stats;
	protected LinkedList<Condition> conditions;
	protected LinkedList<DamageOverTime> dotEffects;
	
	// Holds the possible actions a Character can take on their turn
	private LinkedList<String> commands;
	
	// Safety measure to have a way to clear the conditions if necessary (If works consistently, REMOVE THIS)
	public void clearConditions() {
		this.conditions.clear();
	}
	
	// Store the previous attack made (for use in Character Abilities sometimes)
	protected LinkedList<AttackResult> AttacksMade;
	protected LinkedList<AttackResult> AttacksDefended;
	
	// Constructor (sets each stat variable)
	public Character(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls, CharacterType type) {
		this.name = nam;
		this.Level = lvl;
		this.Health = new Stat(hp, StatVersion.HEALTH);
		this.Damage = new Stat(dmg, StatVersion.DAMAGE);
		this.Armor = new Stat(arm, StatVersion.ARMOR);
		this.ArmorPiercing = new Stat(armp, StatVersion.ARMOR_PIERCING);
		this.Accuracy = new Stat(acc, StatVersion.ACCURACY);
		this.Dodge = new Stat(dod, StatVersion.DODGE);
		this.Block = new Stat(blk, StatVersion.BLOCK);
		this.CriticalChance = new Stat(crit, StatVersion.CRITICAL_CHANCE);
		this.Speed = new Stat(spd, StatVersion.SPEED);
		this.AttackSpeed = new Stat(atkspd, StatVersion.ATTACK_SPEED);
		this.Range = new Stat(range, StatVersion.RANGE);
		this.Threat = new Stat(thrt, StatVersion.THREAT);
		this.TacticalThreat = new Stat(tactthrt, StatVersion.TACTICAL_THREAT);
		this.STDdown = new Stat(stdDown, StatVersion.STANDARD_DEVIATION_DOWN);
		this.STDup = new Stat(stdUp, StatVersion.STANDARD_DEVIATION_UP);
		
		this.CurrentHealth = this.Health.getTotal();
		this.Shields = 0;
		
		this.resistances = resis;
		this.vulnerabilities = vuls;
		
		this.Type = type;
		
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
		this.dotEffects = new LinkedList<>();
		this.AttacksMade = new LinkedList<>();
		this.AttacksDefended = new LinkedList<>();
		
		// Initializes the possible commands for a basic Character
		this.commands = new LinkedList<>();
		this.commands.add("Basic Attack");
		this.commands.add("Alter Character");
		this.commands.add("End Turn");
	}
	public Character(Character copy) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getResistances(), copy.getVulnerabilities(), copy.getType());
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
	
	public HashMap<AttackType,Double> getResistances() {
		HashMap<AttackType,Double> copy = this.resistances;
		return copy;
	}
	public HashMap<AttackType,Double> getVulnerabilities() {
		HashMap<AttackType,Double> copy =  this.vulnerabilities;
		return copy;
	}
	
	public CharacterType getType() {
		return this.Type;
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
	
	
	// Condition/Stat methods
	// Helpful method for checking stats
	private Stat getStat(StatVersion type) {
		// Find the matching Stat for this Character
		for (Stat s : this.stats) {
			if (s.getVersion().equals(type)) {
				return s;
			}
		}
		
		// Return the matching stat, or an empty stat if none were found
		return new Stat();
	}
	public int getStatValue(StatVersion type) {
		Stat requested = this.getStat(type);
		return requested.getTotal() + requested.bonus;
	}
	
	// To add conditions to this Character
	protected void addCondition(Condition c) {
		this.conditions.add(c);
	}
	
	// To remove conditions from this Character
	protected void removeCondition(Condition removed) {
		// If the Condition is already removed (or wasn't there in the first place), we are done.
		if (!this.getAllConditions().contains(removed)) {
			return;
		}
		
		// Unapply condition if need be
		this.unapply(removed);
		
		// Remove condition
		this.conditions.remove(removed);
		
		// Removes linked conditions
		for (Condition c : removed.getLinkedConditions()) {
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
				if (se.getType().equals(StatusEffectType.BASIC)) {
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
				if (se.getType().equals(StatusEffectType.INCOMING)) {
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
				if (se.getType().equals(StatusEffectType.OUTGOING)) {
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
	protected void apply(Condition c) {
		// Automated Apply of Conditions that applies all its Status Effects using "applySE" assuming they all directly affect the current Character
		// Make a note that each time this function is called, "unapply" should be called for the same condition when finished.
		for (StatusEffect se : c.getStatusEffects()) {
			this.applySE(se);
		}
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
	protected void unapply(Condition c) {
		// Automated Unapply of Conditions that unapplies all its Status Effects using "applySE" assuming they all directly affect the current Character
		for (StatusEffect se : c.getStatusEffects()) {
			this.unapplySE(se);
		}
	}
	
	
	// Methods for Damage over Time effects
	protected void addDoT(DamageOverTime effect) {
		this.dotEffects.add(effect);
	}
	protected void removeDot(DamageOverTime effect) {
		this.dotEffects.remove(effect);
	}
	protected void displayDoT() {
		if (this.dotEffects.isEmpty()) {
			return;
		}
		System.out.println("Damage Over Time Effects:");
		for (DamageOverTime dot : this.dotEffects) {
			System.out.print("\t" + dot.displayString());
		}
	}
	
	
	
	// Methods for a Character's basic turn (beginning-end)
	// For list of commands:
	protected void addCommand(int i, String added) {
		this.commands.add(i, added);
	}
	protected void addCommand(String added) {
		this.commands.add(added);
	}
	protected void displayCommands() {
		System.out.println("Possible Actions:");
		for (int i = 0; i < this.commands.size(); i++) {
			System.out.println("" + (i+1) + ". " + this.commands.get(i));
		}
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
	
	// Used to remove a chosen Damage over Time effect from a Character
	protected void promptDoTRemove() {
		// Make a parallel String list for printing
		LinkedList<String> dotStrings = new LinkedList<>();
		for (DamageOverTime dot : this.dotEffects) {
			dotStrings.add(dot.displayString());
		}
		
		// Remove chosen Damage Over Time Effect from Character
		int choice = BattleSimulator.getInstance().promptSelect(dotStrings);
		if (choice ==  0) {
			return;
		}
		this.removeDot(this.dotEffects.get(choice-1));
	}
	
	
	// Prompts for how to alter this Character when chosen
	protected void promptAlterCharacter() {
		String choice;
		System.out.println("Select how to alter " + this.getName() + ":");
		System.out.println("0. None (Go back)");
		System.out.println("1. Alter Current Health");
		System.out.println("2. Add Condition");
		System.out.println("3. Remove Condition");
		System.out.println("4. Remove Damage over Time Effect");
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
		    				this.restoreHealth(amount);
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
	            case "4": // Remove DoT
	            	System.out.println();
	            	this.promptDoTRemove();
	            	return;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
	}
	
	// Start of turn
	protected void beginTurnSetup() {
		// Apply a "tick" to any damage over time effects
		for (DamageOverTime dot : this.dotEffects) {
			if (dot.isExpired()) {
				this.removeDot(dot);
			}
			else {
				dot.activate();
			}
		}
		
		// Conditions: Increment all non-source-incrementing, non-permanent, non-end-of-turn conditions for this Character and remove respective expired conditions
		LinkedList<Condition> toRemove = new LinkedList<>();
		for (Condition con : this.getActiveConditions()) {
			// Increment non-source-incrementing and non-permanent Conditions
			if (!con.isSourceIncrementing() && !con.isPermanent() && !con.isEndOfTurn()) {
				con.turnCount++;
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
					con.turnCount++;
				}
				// Also removes any other conditions that are expired
				if (con.isExpired()) {
					toRemove.add(con);
				}
				for (Condition c : toRemove) {
						this.removeCondition(c);
				}
			}
			
			combatant.applyBasicStatusEffects();
		}
	}
	protected void beginTurnDisplay() {
		System.out.println("It is " + this.getName() + "'s turn.");
		System.out.println("Current Health: " + this.getCurrentHealth());
		System.out.println("Current Conditions:");
		LinkedList<Condition> curConditions = this.getActiveConditions();
		for (Condition c : curConditions) {
			System.out.println("\t" + c.toString());
		}
		System.out.println();
		this.displayCommands();
	}
	public void beginTurn() {
		// Setup
		this.beginTurnSetup();
		
		// State if Character is dead
		if (this.getCurrentHealth() < 0) {
			System.out.println(this.getName() + " is dead. Have turn anyway? Y or N");
			if (!BattleSimulator.getInstance().askYorN()) {
				this.endTurn();
				return;
			}
		}
		
		// Do action based on command given
		boolean flag = true;
		while (flag) {
			// Display available actions
			this.beginTurnDisplay();
			
			System.out.print("Choice? ");
			String responce = BattleSimulator.getInstance().getPrompter().nextLine();
			switch(responce)
	        {
	            case "1": // Basic Attack
	                Character enemy = BattleSimulator.getInstance().targetSingle();
	                if (enemy.equals(Character.EMPTY)) {
	                	break;
	                }
	                this.attack(enemy);
	                flag = false;
	                break;
	            case "2": // Alter Character
	            	Character chosen = BattleSimulator.getInstance().targetSingle();
	            	if (chosen.equals(Character.EMPTY)) {
	            		break;
	            	}
	            	chosen.promptAlterCharacter();
	            	break;
	            case "3": // End Turn
	                flag = false;
	                break;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
		
		this.endTurn();
	}
	// End of turn
	protected void endTurnSetup() {
		// Increment all non-source-incrementing, non-permanent, end-of-turn conditions for this Character and remove respective expired conditions
		LinkedList<Condition> toRemove = new LinkedList<>();
		for (Condition con : this.getActiveConditions()) {
			// Increment non-source-incrementing and non-permanent Conditions
			if (!con.isSourceIncrementing() && !con.isPermanent() && con.isEndOfTurn()) {
				con.turnCount++;
			}
			if (con.isExpired()) {
				toRemove.add(con);
			}
		}
		for (Condition c : toRemove) {
				this.removeCondition(c);
		}
		
		// Increment all source-incrementing, end-of-turn conditions for all Characters in battle with this Character as the source and remove respecive expired conditions
		// Afterwards, while we're at it, goes ahead and unapplies all basic status effects to the respective combatants
		for (Character combatant : BattleSimulator.getInstance().getCombatants()) {
			toRemove.clear();
			for (Condition con : combatant.getActiveConditions()) {
				if (con.isSourceIncrementing() && con.getSource().equals(this) && con.isEndOfTurn()) {
					con.turnCount++;
				}
				// Also removes any other conditions that are expired
				if (con.isExpired()) {
					toRemove.add(con);
				}
				for (Condition c : toRemove) {
						this.removeCondition(c);
				}
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
		System.out.println("Enter something the press enter to continue.");
		BattleSimulator.getInstance().getPrompter().nextLine();
		System.out.println("\n-----------------------------------------------------------------------------");
	}
	
	
	// Methods used to store attacks made/defended (used in subclasses for interactions)
	protected void hitAttack(AttackResult atk) {
		this.AttacksMade.add(atk);
	}
	protected void missAttack(AttackResult atk) {
		this.AttacksMade.add(atk);
	}
	protected void receivedAttack(AttackResult atk) {
		this.AttacksDefended.add(atk);
	}
	protected void avoidAttack(AttackResult atk) {
		this.AttacksDefended.add(atk);
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
	protected void takeDamage(int damageDealt, AttackType aType) {
		// Checks for invincibility or stasis
		for (Condition c : this.getActiveConditions()) {
			if (c instanceof Invincible || c instanceof Stasis) {
				damageDealt = 0;
			}
		}
		
		if (this.getResistances().containsKey(aType)) {
			damageDealt *= 1 - this.getResistances().get(aType)/100.0;
		}
		if (this.getVulnerabilities().containsKey(aType)) {
			damageDealt *= 1 + this.getVulnerabilities().get(aType)/100.0;
		}
		
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
	
	// Calculates the deviated Damage done using the ranges from STDup and STDdown
	protected int calcDeviatedDamage(Character enemy, int finalDamageStat, double finalScaler, boolean didCrit) {
		// Calculate the totalDamage before STD is applied
		int totalDamage = (int)Math.round(finalDamageStat*finalScaler);
		
		// Calculates the minimum and maximum Damage possible due to Standard Deviation (minimum is removed if you didCrit)
		int minDamage;
		int maxDamage = (int)(1.0 * totalDamage * this.getSTDup() / 100);
		
		if (didCrit) {
			minDamage = totalDamage;
		}
		else {
			minDamage = (int)(1.0 * totalDamage * this.getSTDdown() / 100);
		}
		
		// Determines where on the Damage spectrum created the Ability landed, and calculates the final Damage done
		Dice vary = new Dice(maxDamage-minDamage+1);
		return minDamage + vary.roll() -1;
	}
	
	// Deals a flat amount of damage to another Character and returns the output string based on damage and stating if they died.
	protected void dealDamage(Character enemy, int damageDealt, AttackType aType, boolean didCrit) {
		
		enemy.takeDamage(damageDealt, aType);
		
		// Attack output
		if (didCrit) {
			System.out.println(this.getName() + " scored a CRITCAL HIT against " + enemy.getName() + " for a total of " + damageDealt + " " + aType.toString() + " damage!");
		}
		else {
			System.out.println(this.getName() + " hit " + enemy.getName() + " for a total of " + damageDealt + " " + aType.toString() + " damage!");
		}
		
		if (enemy.isDead()) {
			Dice funDie = new Dice(6);
			String funWord = "";
			
			switch(funDie.roll()) {
				case 1: {
					funWord = "utterly annihilated ";
					break;
				}
				case 2: {
					funWord = "defeated ";
					break;
				}
				case 3: {
					funWord = "obliterated ";
					break;
				}
				case 4: {
					funWord = "purged the universe of ";
					break;
				}
				case 5: {
					funWord = "destroyed ";
					break;
				}
				case 6: {
					funWord = "slew ";
					break;
				}
			}
			
			System.out.println(this.getName() + " has " + funWord + enemy.getName() + "!");
		}
		else {
			System.out.println(enemy.getName() + " has " + enemy.getCurrentHealth() + " Health remaining.");
		}
		
		// Unapply the Incoming/Outgoing Status Effects
		enemy.unapplyIncomingStatusEffects(this);
		this.unapplyOutgoingStatusEffects(enemy);
		
		// Store the attack made
		AttackResult atk = new AttackResultBuilder()
				.attacker(this)
				.defender(enemy)
				.type(aType)
				.didHit(true)
				.didCrit(didCrit)
				.damageDealt(damageDealt)
				.didKill(enemy.isDead())
				.build();
		this.hitAttack(atk);
		enemy.receivedAttack(atk);
	}
	protected void dealDamage(Character enemy, int damageDealt, AttackType aType) {
		this.dealDamage(enemy, damageDealt, aType, false);
	}
	
	public void attack(Character enemy, double scaler, AttackType aType, boolean isTargeted, boolean canMiss, boolean armorApplies) {
		// Add: Check for being attacked conditions (Steel Legion Tank: Hold It Right There)
		
		// Make sure neither target is dead.
		if (this.isDead()) {
			System.out.println(this.getName() + " is dead. Thus, " + this.getName() + " is incapable of attacking.");
		}
		if (enemy.isDead()) {
			System.out.println(enemy.getName() + " is already dead. The attack had no effect.");
		}
		
		// Apply Incoming/Outgoing Status Effects
		enemy.applyIncomingStatusEffects(this);
		this.applyOutgoingStatusEffects(enemy);
		
		// Attack always hits unless it is a Targeted attack and can miss (some targeted attacks cannot miss)
		boolean didHit = true;
		
		if (isTargeted && canMiss) {
			// Finds if the attack landed
			didHit = this.landAttack(enemy);
		}
		
		// If the attack missed
		if (!didHit) {
			// Unapply the Incoming/Outgoing Status Effects
			enemy.unapplyIncomingStatusEffects(this);
			this.unapplyOutgoingStatusEffects(enemy);
			
			// Store the attack attempt, then return
			AttackResult atk = new AttackResultBuilder()
					.attacker(this)
					.defender(enemy)
					.type(aType)
					.didHit(false)
					.didCrit(false)
					.damageDealt(0)
					.didKill(false)
					.build();
			this.missAttack(atk);
			enemy.avoidAttack(atk);
			
			// Print the result
			System.out.println(this.getName() + " missed " + enemy.getName() + "!");
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
			
			// Calculates the damage dealt over the deviation range
			int damageDealt = this.calcDeviatedDamage(enemy, this.getDamage(), scaler, didCrit);
			
			// Damages the enemy and determines whether enemy died (Storing of attacks that hit occur in the "dealDamage" function)
			this.dealDamage(enemy, damageDealt, aType, didCrit);
		}
	}
	// Normal Attack Abbreviations
	public void attack(Character enemy, double scaler, AttackType aType, boolean armorApplies) {
		this.attack(enemy, scaler, aType, true, true, armorApplies);
	}
	public void attack(Character enemy, double scaler, AttackType aType) {
		this.attack(enemy, scaler, aType, true);
	}
	public void attack(Character enemy, double scaler, boolean armorApplies) {
		this.attack(enemy, scaler, AttackType.TRUE, armorApplies);
	}
	public void attack(Character enemy, AttackType aType, boolean armorApplies) {
		this.attack(enemy, 1, aType, armorApplies);
	}
	public void attack(Character enemy, double scaler) {
		this.attack(enemy, scaler, true);
	}
	public void attack(Character enemy, AttackType aType) {
		this.attack(enemy, aType, true);
	}
	public void attack(Character enemy) {
		this.attack(enemy, 1);
	}
	// AOE Attack Abbreviations (when isTargeted is false)
	public void attackAOE(Character enemy, double scaler, AttackType aType, boolean armorApplies) {
		this.attack(enemy, scaler, aType, false, false, armorApplies);
	}
	public void attackAOE(Character enemy, double scaler, AttackType aType) {
		this.attackAOE(enemy, scaler, aType, true);
	}
	public void attackAOE(Character enemy, double scaler, boolean armorApplies) {
		this.attackAOE(enemy, scaler, AttackType.TRUE, armorApplies);
	}
	public void attackAOE(Character enemy, AttackType aType, boolean armorApplies) {
		this.attackAOE(enemy, 1, aType, armorApplies);
	}
	public void attackAOE(Character enemy, double scaler) {
		this.attackAOE(enemy, scaler, true);
	}
	public void attackAOE(Character enemy, AttackType aType) {
		this.attackAOE(enemy, aType, true);
	}
	public void attackAOE(Character enemy) {
		this.attackAOE(enemy, 1);
	}
	// Cannot Miss Attack Abbreviations (when canMiss is false, but isTargeted is true)
	public void attackNoMiss(Character enemy, double scaler, AttackType aType, boolean armorApplies) {
		this.attack(enemy, scaler, aType, true, false, armorApplies);
	}
	public void attackNoMiss(Character enemy, double scaler, AttackType aType) {
		this.attackNoMiss(enemy, scaler, aType, true);
	}
	public void attackNoMiss(Character enemy, double scaler, boolean armorApplies) {
		this.attackNoMiss(enemy, scaler, AttackType.TRUE, armorApplies);
	}
	public void attackNoMiss(Character enemy, AttackType aType, boolean armorApplies) {
		this.attackNoMiss(enemy, 1, aType, armorApplies);
	}
	public void attackNoMiss(Character enemy, double scaler) {
		this.attackNoMiss(enemy, scaler, true);
	}
	public void attackNoMiss(Character enemy, AttackType aType) {
		this.attackNoMiss(enemy, aType, true);
	}
	public void attackNoMiss(Character enemy) {
		this.attackNoMiss(enemy, 1);
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
		enemy.takeDamage(damage, AttackType.TRUE);
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
			collided.dealDamage(enemy, damage, AttackType.TRUE);
		}
		
		// Otherwise, the Character was not blocked, and both parties take various damage
		
		// First, damage is dealt to the Character knocked-back (enemy)
		// Calculate percentage of Health taken
		int percentage = 5 + 2 * extraSpaces;
		if (percentage > 15) {
			percentage = 15;
		}
		
		// Add Accuracy Reduction Effect
		StatusEffect enemyAccReductionEffect = new StatusEffect(StatVersion.ACCURACY, -75, StatusEffectType.BASIC);
		Condition enemyAccReduction = new Condition("Kock-back Accuracy Reduction", 1);
		enemyAccReduction.addStatusEffect(enemyAccReductionEffect);
		enemy.addCondition(enemyAccReduction);
		
		// Calculate and apply damage
		int damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		double armorEffect = 1.0 * collided.getArmor() / enemy.getArmor();
		int damage = (int)Math.round(damageMax * armorEffect);
		collided.dealDamage(enemy, damage, AttackType.TRUE);
		
		
		// Second, damage is dealt to the Character knocked into (collided)
		// Calculate percentage of Health taken
		percentage = 2 + 2 * extraSpaces;
		if (percentage > 10) {
			percentage = 10;
		}
		
		// Add Accuracy Reduction Effect
		StatusEffect collidedAccReductionEffect = new StatusEffect(StatVersion.ACCURACY, -25);
		Condition collidedAccReduction = new Condition("Knock-back Accuracy Reduction", 1);
		collidedAccReduction.addStatusEffect(collidedAccReductionEffect);
		collided.addCondition(collidedAccReduction);
		
		// Calculate and apply damage
		damageMax = (int)Math.round(1.0 * enemy.getHealth() * percentage / 100);
		armorEffect = 1.0 * enemy.getArmor() / collided.getArmor();
		damage = (int)Math.round(damageMax * armorEffect);
		enemy.dealDamage(collided, damage, AttackType.TRUE);
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

