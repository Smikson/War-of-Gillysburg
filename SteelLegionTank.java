package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SteelLegionTank extends Character {
	// Passive Abilities
	private HoldItRightThere HoldItRightThere; // Unique Passive Ability (UPA)
	private EnchantedArmor EnchantedArmor;
	private ShieldSkills ShieldSkills;
	private ProfessionalLaughter ProfessionalLaughter = new ProfessionalLaughter(5);
	
	// Base Abilities
	private ShieldBash ShieldBash;
	private ShieldReflection ShieldReflection;
	private TauntingAttack TauntingAttack;
	private LeaderStrike LeaderStrike;
	private HaHaHaYouCantKillMe HaHaHaYouCantKillMe;
	
	// A List of all Abilities so all Cooldowns can be reduced at once
	private LinkedList<Ability> abilities;
	
	// These first two methods help set up the Steel Legion Tank subclass.
	public SteelLegionTank(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType, int upaRank, int eArmorRank, int sSkillsRank, int profLaughRank, int sBashRank, int sReflectRank, int tAttackRank, int lStrikeRank, int haRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
		this.HoldItRightThere = new HoldItRightThere(this, upaRank);
		this.EnchantedArmor = new EnchantedArmor(this, eArmorRank);
		this.ShieldSkills = new ShieldSkills(this, sSkillsRank);
		this.ProfessionalLaughter = new ProfessionalLaughter(this, profLaughRank);
		this.ShieldBash = new ShieldBash(this, sBashRank, sSkillsRank);
		this.ShieldReflection = new ShieldReflection(this, sReflectRank, sSkillsRank);
		this.TauntingAttack = new TauntingAttack(this, tAttackRank, sSkillsRank);
		this.LeaderStrike = new LeaderStrike(this, lStrikeRank, sSkillsRank);
		this.HaHaHaYouCantKillMe = new HaHaHaYouCantKillMe(this, haRank);
		
		// Add Abilities to a list for Cooldown purposes
		this.abilities = new LinkedList<>();
		this.abilities.add(this.HoldItRightThere);
		this.abilities.add(this.EnchantedArmor);
		this.abilities.add(this.ShieldSkills);
		this.abilities.add(this.ProfessionalLaughter);
		this.abilities.add(this.ShieldBash);
		this.abilities.add(this.ShieldReflection);
		this.abilities.add(this.TauntingAttack);
		this.abilities.add(this.LeaderStrike);
		this.abilities.add(this.HaHaHaYouCantKillMe);
		
		// Add new commands for Abilities
		this.addCommand(1, "Shield Bash");
		this.addCommand(2, "Shield Reflection");
		this.addCommand(3, "Taunting Attack");
		this.addCommand(4, "Leader Strike");
		this.addCommand(5, "HaHaHaYouCantKillMe");
	}
	public SteelLegionTank(SteelLegionTank copy) {
		super(copy);
		this.HoldItRightThere = new HoldItRightThere(this, copy.HoldItRightThere.rank());
		this.EnchantedArmor = new EnchantedArmor(this, copy.EnchantedArmor.rank());
		this.ShieldSkills = new ShieldSkills(this, copy.ShieldSkills.rank());
		this.ProfessionalLaughter = new ProfessionalLaughter(this, copy.ProfessionalLaughter.rank());
		this.ShieldBash = new ShieldBash(this, copy.ShieldBash.rank(), copy.ShieldSkills.rank());
		this.ShieldReflection = new ShieldReflection(this, copy.ShieldReflection.rank(), copy.ShieldSkills.rank());
		this.TauntingAttack = new TauntingAttack(this, copy.TauntingAttack.rank(), copy.ShieldSkills.rank());
		this.LeaderStrike = new LeaderStrike(this, copy.LeaderStrike.rank(), copy.ShieldSkills.rank());
		this.HaHaHaYouCantKillMe = new HaHaHaYouCantKillMe(this, copy.HaHaHaYouCantKillMe.rank());
		
		// Add Abilities to a list for Cooldown purposes
		this.abilities = new LinkedList<>();
		this.abilities.add(this.HoldItRightThere);
		this.abilities.add(this.EnchantedArmor);
		this.abilities.add(this.ShieldSkills);
		this.abilities.add(this.ProfessionalLaughter);
		this.abilities.add(this.ShieldBash);
		this.abilities.add(this.ShieldReflection);
		this.abilities.add(this.TauntingAttack);
		this.abilities.add(this.LeaderStrike);
		this.abilities.add(this.HaHaHaYouCantKillMe);
		
		// Add new commands for Abilities
		this.addCommand(1, "Shield Bash");
		this.addCommand(2, "Shield Reflection");
		this.addCommand(3, "Taunting Attack");
		this.addCommand(4, "Leader Strike");
		this.addCommand(5, "HaHaHaYouCantKillMe");
	}
	
	// Get methods for ranks for Abilities (sometimes assists in Character creation or testing)
	public int getHoldItRightThereRank() {
		return this.HoldItRightThere.rank();
	}
	public int getEnchantedArmorRank() {
		return this.EnchantedArmor.rank();
	}
	public int getShieldSkillsRank() {
		return this.ShieldSkills.rank();
	}
	public int getProfessionalLaughterRank() {
		return this.ProfessionalLaughter.rank();
	}
	public int getShieldBashRank() {
		return this.ShieldBash.rank();
	}
	public int getShieldReflectionRank() {
		return this.ShieldReflection.rank();
	}
	public int getTauntingAttackRank() {
		return this.TauntingAttack.rank();
	}
	public int getLeaderStrikeRank() {
		return this.LeaderStrike.rank();
	}
	public int getHaHaHaYouCantKillMeRank() {
		return this.HaHaHaYouCantKillMe.rank();
	}
	
	
	// Overrides the begin and end turn function of Character to include reducing the Cooldowns of Abilities.
	// Start of Turn override
	@Override
	public void beginTurn() {
		// Base Setup
		this.beginTurnSetup();
		
		// State if Character is dead
		if (this.getCurrentHealth() < 0) {
			System.out.println(this.getName() + " is dead. Have turn anyway? Y or N");
			if (!BattleSimulator.getInstance().askYorN()) {
				this.endTurn();
				return;
			}
		}
		
		// Setup for Class
		// Checks to see if it is the beginning of the round (Rank 15 of Professional Laughter gives Taunt)
		if (BattleSimulator.getInstance().getRound() == 1 && this.ProfessionalLaughter.rank() >= 15) {
			System.out.println("You have \"Taunt\" for 2 rounds.\n");
		}
		
		// Reduces the Cooldown of all Abilities that need it.
		for (Ability a : abilities) {
			if (a.onCooldown()) {
				a.incrementTurn();
			}
		}
		
		// Do action based on command given
		boolean flag = true;
		while (flag) {
			// Display available actions
			this.beginTurnDisplay();
			
			System.out.print("Choice? ");
			String responce = BattleSimulator.getInstance().getPrompter().nextLine();
			Character target;
			switch(responce)
	        {
	            case "1": // Basic Attack
	                target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                System.out.println(this.attack(target));
	                flag = false;
	                break;
	            case "2": // Shield Bash
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                System.out.println(this.useShieldBash(target));
	                flag = false;
	                break;
	            case "3": // Shield Reflection
	            	System.out.println("Choose enemies hit by attack:");
	            	LinkedList<Character> attackTargets = BattleSimulator.getInstance().targetMultiple();
	                if (attackTargets.isEmpty()) {
	                	break;
	                }
	                System.out.println("Choose enemies blinded (0 no longer takes back, will remain empty list):");
	                LinkedList<Character> blindedTargets = BattleSimulator.getInstance().targetMultiple();
	                System.out.println(this.useShieldReflection(attackTargets, blindedTargets));
	                flag = false;
	                break;
	            case "4": // Taunting Attack
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                System.out.println(this.useTauntingAttack(target));
	                flag = false;
	                break;
	            case "5": // Leader Strike
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                System.out.println(this.useLeaderStrike(target));
	                flag = false;
	                break;
	            case "6": // HaHaHaYouCantKillMe
	            	System.out.println(this.useHahahaYouCantKillMe());
	                flag = false;
	                break;
	            case "7": // Add Condition
	            	Character chosen = BattleSimulator.getInstance().targetSingle();
	                if (chosen.equals(Character.EMPTY)) {
	                	break;
	                }
	                chosen.promptConditionAdd();
	                break;
	            case "8": // Remove Condition
	            	Character choice = BattleSimulator.getInstance().targetSingle();
	                if (choice.equals(Character.EMPTY)) {
	                	break;
	                }
	                choice.promptConditionRemove();
	                break;
	            case "9": // End Turn
	                flag = false;
	                break;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
		
		this.endTurn();
	}
	// End of Turn Override
	@Override
	public void endTurn() {
		// Setup
		this.endTurnSetup();
		
		// Use Enchanted Armor Healing (end of turn effect)
		System.out.println(this.useEnchantedArmorHealing());
		
		// State facts
		System.out.println(this.getName() + "'s turn is over.");
		
		// Return
		System.out.println("Enter something the press enter to continue.");
		BattleSimulator.getInstance().getPrompter().nextLine();
	}
	
	// Overrides "avoidAttack" in order to also store the fact that an attack was blocked in "Shield Bash" and "Shield Reflection"
	@Override
	protected void avoidAttack(Attack atk) {
		super.avoidAttack(atk);
		this.ShieldBash.didBlock = true;
		this.ShieldReflection.didBlock = true;
	}
	// Overrides "receivedAttack" for death effect 
	@Override
	protected void receivedAttack(Attack atk) {
		super.receivedAttack(atk);
		if (this.isDead() && this.HaHaHaYouCantKillMe.rank() >= 3) {
			this.useDeathHaHaHaYouCantKillMe(BattleSimulator.getInstance().getAllies());
		}
	}
	
	// Methods to use "Hold It Right There" Unique Passive
	public void addHoldItRightThereBlockBonus() {
		// Do not add Condition if already present
		for (Condition c : this.getAllConditions()) {
			if (c.getName() == this.HoldItRightThere.getSelfBlockCondition().getName()) {
				return;
			}
		}
		this.addCondition(this.HoldItRightThere.getSelfBlockCondition());
	}
	public void useHoldItRightThereHaltCondition(Character enemy) {
		// If condition is present, remove it first
		for (Condition c : enemy.getAllConditions()) {
			if (c.getName() == this.HoldItRightThere.getEnemyHaltCondition().getName()) {
				enemy.removeCondition(c);
			}
		}
		enemy.addCondition(this.HoldItRightThere.getEnemyHaltCondition());
	}
	
	// Deals with the healing from the "Enchanted Armor" Passive
	public String useEnchantedArmorHealing() {
		// Calculates the amount of healing based on Maximum Health
		int healing = (int) Math.round(this.EnchantedArmor.getScaler() * this.getHealth());
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	
	// Deals with the healing (and rank 15 Cooldown) portion of the "Shield Skills" Passive from hitting multiple enemies with "Shield Reflection"
	private String useShieldSkillsHealing(int numBlinded) {
		// First, non-healing-wise, if 7 enemies are hit at rank 15, the Cooldown of Shield Bash is refreshed.
		if (this.ShieldSkills.rank() >= 15 && numBlinded >= 7) {
			this.ShieldBash.turnCount = ShieldBash.cooldown();
		}
		
		// Calculates the amount of healing based on missing Health and the number of enemies blinded
		int healing = 0;
		if ((this.ShieldSkills.rank() < 11 && numBlinded >= 3) || (this.ShieldSkills.rank() >= 11 && numBlinded >= 3 && numBlinded < 5)) {
			healing = (int) Math.round(this.ShieldSkills.getScalerBlind3() * (this.getHealth() - this.getCurrentHealth()));
		}
		else if (this.ShieldSkills.rank() >= 11 && numBlinded >= 5){
			healing = (int) Math.round((this.ShieldSkills.getScalerBlind3() + this.ShieldSkills.getScalerBlind5()) * (this.getHealth() - this.getCurrentHealth()));
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		if (healing == 0) {
			return "";
		}
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	
	// Deals the Damage from the "Shield Bash" Ability (Ability 1)
	public String useShieldBash(Character enemy) {
		// Before anything, put Shield Bash "on Cooldown"
		this.ShieldBash.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.ShieldBash.getSelfPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		ret += this.attack(enemy, this.ShieldBash.getScaler());
		
		// Unapply the bonus pre-conditions
		this.unapply(preCondition);
		
		// Change the didCrit of "Shield Bash" to match if the Ability critically struck (this may affect the effects below when received)
		this.ShieldBash.didCrit = this.previousAttack().didCrit();
		
		// If the attack hit, apply all the conditions (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add all conditions to enemy hit
			Stun stunEffect = this.ShieldBash.getStunEffect();
			enemy.addCondition(stunEffect);
			Condition accuracyReduction = this.ShieldBash.getEnemyAccuracyReduction();
			enemy.addCondition(accuracyReduction);
			
			// Change numMisses back to 0
			this.ShieldBash.numMisses = 0;
		}
		// If the attack missed, check to see if its Cooldown is reduced by "Shield Skills" and increment numMisses
		else {
			// Each point in "Shield Skills" causes a reduction of 1 up to a maximum of the Cooldown itself
			int cdr = this.ShieldSkills.rank();
			if (cdr > this.ShieldBash.cooldown()) {
				cdr = this.ShieldBash.cooldown();
			}
			this.ShieldBash.turnCount = cdr;
			
			// Increment numMisses
			this.ShieldBash.numMisses++;
		}
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") and didCrit to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
		this.ShieldBash.didCrit = false;
		
		// Return results
		return ret;
	}
	
	// Deals the Damage from the "Shield Reflection" Ability (Ability 2) to multiple enemies
	public String useShieldReflection(LinkedList<Character> enemies, LinkedList<Character> blinded) {
		// Before anything, put Shield Reflection "on Cooldown"
		this.ShieldReflection.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Make the attack against all enemies affected
		for (Character enemy : enemies) {
			ret += this.attack(enemy, this.ShieldReflection.getScaler(), false) + "\n"; // false - indicates an AOE attack
		}
		
		// Blind all enemies affected
		for (Character enemy : blinded) {
			enemy.addCondition(this.ShieldReflection.getBlindEffect());
		}
		
		// Add the possible healing string based on bonus effects from the "Shield Skills" Ability (will be empty String if nothing happens)
		ret += this.useShieldSkillsHealing(blinded.size());
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
		
		// Return the result
		return ret;
	}
	public String useShieldReflection(LinkedList<Character> enemies) {
		return this.useShieldReflection(enemies, enemies);
	}
	
	// Deals the Damage from the "Taunting Attack" Ability (Ability 3)
	public String useTauntingAttack(Character enemy) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.TauntingAttack.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.TauntingAttack.getPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		ret += this.attack(enemy, this.TauntingAttack.getScaler());
		
		// Unapply the bonus accuracy pre-condition
		this.unapply(preCondition);
		
		// If the attack hit, apply the taunt condition (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectHit();
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				ret += "\n" + enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!\n";
			}
			
			// Change numMisses back to 0
			this.TauntingAttack.numMisses = 0;
		}
		// If the attack missed, still apply the taunt condition if it is effective (will be 0 if not) and increment numMisses
		else {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectMiss(enemy);
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				ret += "\n" + enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!\n";
			}
			
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.ShieldSkills.rank() >= 15) {
				this.TauntingAttack.numMisses++;
			}
		}
		
		// Return the result
		return ret;
	}
	
	// Deals the Damage from the "Leader Strike" Ability (Ability 4) and Calculates the amount healed for allies.
	public String useLeaderStrike(Character enemy) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.LeaderStrike.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.LeaderStrike.getPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		ret += this.attack(enemy, this.LeaderStrike.getScaler());
		
		// Unapply the bonus accuracy pre-condition
		this.unapply(preCondition);
		
		// If the attack hit revert numMisses to 0, if it missed, increment numMisses
		if (this.previousAttack().didHit()) {
			this.LeaderStrike.numMisses = 0;
		}
		else {
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.ShieldSkills.rank() >= 15) {
				this.TauntingAttack.numMisses++;
			}
		}
		
		
		// Past rank 3, this Character is included for the buffs in "allies", either way, create a copy of the list so the original is unchanged (extra safety net)
		LinkedList<Character> alliesCopy = new LinkedList<>();
		for (Character ally : BattleSimulator.getInstance().getAllies()) {
			alliesCopy.add(ally);
		}
		if (!alliesCopy.contains(this) && this.LeaderStrike.rank() >= 3) {  // Adds this if not present and should be
			alliesCopy.add(this);
		}
		if (alliesCopy.contains(this) && this.LeaderStrike.rank() < 3) {  // Removes this if present and should not be
			alliesCopy.remove(this);
		}
		
		// Calculates and adds the amount of Healing received for each ally affected by the Ability (in the list) and apply the damage boost
		for (Character ally : alliesCopy) {
			// Calculates the healing amount for the ally in the list
			int healing = (int)Math.round(ally.getHealth() * this.LeaderStrike.getHealingScaler());
			
			healing = ally.restoreHealth(healing);
			ret += ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth() + "\n";
			
			// Applies the damage boost to each ally affected
			ally.addCondition(this.LeaderStrike.getAllyDamageBonus());
			
			// Checks to see if each ally's attack will stun the next target, and adds it to the return if so.
			if (this.LeaderStrike.willStun()) {
				ret += ally.getName() + " will also stun the next target hit\n";
			}
		}
		
		return ret;
	}
	
	
	// Restores the Character to full health and grants bonuses from the ULTIMATE Ability "HaHaHa You Can't Kill Me!"
	public String useHahahaYouCantKillMe() {
		// Initialize the return String
		String ret = "";
		
		// Heal to full Health
		int healing = this.getHealth() - this.getCurrentHealth();
		healing = this.restoreHealth(healing);
		ret += this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth() + "\n";
		
		// Apply Additional Conditions
		this.addCondition(this.HaHaHaYouCantKillMe.getSelfArmorBonus());
		for (Character enemy : BattleSimulator.getInstance().getEnemies()) {
			enemy.addCondition(this.HaHaHaYouCantKillMe.getEnemyTauntEffect(enemy));
		}
		
		return ret;
	}
	
	// Gives all allies the appropriate buffs if you die while "HaHaHa You Can't Kill Me!" is active
	public String useDeathHaHaHaYouCantKillMe(List<Character> allies) {
		// Initialize the return String
		String ret = "";
		
		// All this only happens at rank 3
		if (this.HaHaHaYouCantKillMe.rank() >= 3) {
			// Restore each ally for 25% of their max Health and give each ally the damage buff from the Ability
			for (Character ally : allies) {
				if (!ally.equals(this)) {
					// Calculates the healing amount for the ally in the list
					int healing = (int)Math.round(ally.getHealth() * .25);
					
					healing = ally.restoreHealth(healing);
					ret += ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth() + "\n";
					
					// Gives each ally the Damage buff and Invincibility for 1 turn
					ally.addCondition(this.HaHaHaYouCantKillMe.getAllyDamageBonus());
					ally.addCondition(new Invincible("HaHaHa You Can't Kill Me: Invincibility", 1));
				}
			}
		}
		
		// Return the result
		return ret;
	}
}
