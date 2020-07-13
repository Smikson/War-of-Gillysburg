package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public abstract class Command {
	// Variables all commands must have: A character that owns the command, and whether that command is usable
	private Character owner;
	private boolean isUsable;
	
	// Constructor that must be called by subclasses to initialize required variables
	public Command(Character owner, boolean isUsable) {
		this.owner = owner;
		this.isUsable = isUsable;
	}
	
	// Abstract execute and display functions that need to be specified in each Command class.
	public abstract void execute();
	public abstract String display();
	
	// Get functions to return required variables
	public Character getOwner() {
		return this.owner;
	}
	public boolean isUsable() {
		return this.isUsable;
	}
	
	// Static function that displays choices and executes each command
	public static void promptCommands(LinkedList<Command> commands) {
		// Create a linked list holding all the Commands that are usable and their display Strings
		LinkedList<Command> usableCommands = new LinkedList<>();
		LinkedList<String> displays = new LinkedList<>();
		for (Command c : commands) {
			if (c.isUsable()) {
				usableCommands.add(c);
				displays.add(c.display());
			}
		}
		
		// Prompt to select a command from the usable Commands and their displays (No none option)
		int choice = BattleSimulator.getInstance().promptSelect("Possible Actions:", false, displays);
		
		// Execute the command chosen
		usableCommands.get(choice - 1).execute();
	}
}


// Create an additional class for Basic Attack, Alter Character, End Turn, Display Threat Order that override the execute command function?
class BasicAttack extends Command {
	private boolean singleAttack;
	private Attack attack;
	
	public BasicAttack(Character owner) {
		super(owner, true);
		this.singleAttack = true;
		this.attack = new AttackBuilder().attacker(owner).build();
	}
	public BasicAttack(Character owner, boolean singleAttack, Attack atk) {
		this(owner);
		this.singleAttack = singleAttack;
		this.attack = atk;
	}
	
	public boolean isSingle() {
		return this.singleAttack;
	}
	
	@Override
	public void execute() {
		this.attack.execute();
	}

	@Override
	public String display() {
		return "Basic Attack";
	}
	
	
}


