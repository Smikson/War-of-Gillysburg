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
	
	// Abstract display and execute functions that need to be specified in each Command class.
	public abstract String display();
	public abstract void execute();
	
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
// Basic Attack Class: Allows for the creation of unique basic attacks that take a built attack or can create the default basic attack
class BasicAttack extends Command {
	// Variables to hold if the basic attack targets only a single person, and the Attack to execute
	private boolean singleAttack;
	private Attack attack;
	
	// Constructors:
	// Constructor for unique basic attacks based on a pre-built attack
	public BasicAttack(Character owner, boolean singleAttack, Attack atk) {
		// First, call the super constructor specifying the command is usable (true)
		super(owner, true);
		
		// Set the values passed
		this.singleAttack = singleAttack;
		this.attack = atk;
	}
	// Constructor for default basic attacks
	public BasicAttack(Character owner, AttackType aType) {
		// By default, a basic attack is a single attack (will all default values and) with the specified attack type
		this(owner, true, new AttackBuilder().attacker(owner).type(aType).build());
	}
	
	// Overrides the abstract display function to show the text: "Basic Attack" when choosing
	@Override
	public String display() {
		return "Basic Attack";
	}
	
	// Overrides the abstract execute function to execute the basic attack
	@Override
	public void execute() {
		// If the basic attack only hits one target, do a target single attack
		if (this.singleAttack) {
			// Get the target
			Character chosen = BattleSimulator.getInstance().targetSingle();
			
			// If they responded requesting to go back (Character.EMPTY was returned), end the current function
            if (chosen.equals(Character.EMPTY)) {
            	return;
            }
            
            // Add the target as the defender of the attack and execute the attack
            this.attack = new AttackBuilder(this.attack).defender(chosen).build();
			this.attack.execute();
			
			// Using a basic attack uses the character's turn actions, then end the current function
			this.getOwner().useTurnActions();
			return;
		}
		
		// Otherwise do a target multiple attack. First get all the target enemies
		LinkedList<Character> chosen = BattleSimulator.getInstance().targetMultiple();
		
		// Loop through the list of targets
		for (Character choice : chosen) {
			// For each character, make the targeted choice the defender of the attack and execute the attack
            this.attack = new AttackBuilder(this.attack).defender(choice).build();
			this.attack.execute();
		}
		
		// Using a basic attack uses the character's turn actions
		this.getOwner().useTurnActions();
	}
}

// Alter Character: Contains the code to display and execute the "Alter Character" command. Currently no special objects with additions can be made
class AlterCharacter extends Command {
	// Constructor
	public AlterCharacter(Character owner) {
		// Calls the super (Command) constructor specifying the command is usable (true)
		super(owner, true);
	}
	
	// Overrides the abstract display function to show the text: "Alter Character" when choosing
	@Override
	public String display() {
		return "Alter Character";
	}
	
	// Overrides the abstract execute function to allow for the altering of a Character
	@Override
	public void execute() {
		Character chosen = BattleSimulator.getInstance().targetSingle();
    	if (chosen.equals(Character.EMPTY)) {
    		return;
    	}
		//this.getOwner().promptAlterCharacter();
	}
}

// End Turn: Contains the code to display and execute the "End Turn" command. Currently no special objects with additions can be made
class EndTurn extends Command {
	// Constructor
	public EndTurn(Character owner) {
		// Calls the super (Command) constructor specifying the command is usable (true)
		super(owner, true);
	}

	// Overrides the abstract display function to show the text: "End Turn" when choosing
	@Override
	public String display() {
		return "End Turn";
	}
	
	// Overrides the abstract execute function to simply call the end turn function for the owner
	@Override
	public void execute() {
		this.getOwner().endTurn();
	}
}

// Display Threat Order: Contains the code to display and execute the "Display Threat Order" command. Currently no special objects with additions can be made
class DisplayThreatOrder extends Command {
	// Variable to hold the owner as an Enemy
	private Enemy owner;
	
	// Constructor: Must be an Enemy
	public DisplayThreatOrder(Enemy owner) {
		// Calls the super (Command) constructor specifying the command is usable (true)
		super(owner, true);
		this.owner = owner;
	}
	
	// Overrides the abstract display function to show the text: "Display Threat Order" when choosing
	@Override
	public String display() {
		return "Display Threat Order";
	}
	
	// Overrides the abstract execute function to call the displayThreatOrder function of the Enemy owner
	@Override
	public void execute() {
		this.owner.displayThreatOrder();
	}
}

// Ability Command: Allows for the creation of the command for unique abilities based off their respective use(1) function.
class AbilityCommand extends Command {
	// A Variable to hold the Ability used
	private Ability ability;
	
	// Constructor: Just takes the Ability
	public AbilityCommand(Ability ability) {
		// Calls the super (Command) constructor specifying the command is usable only when the rank is above 0
		super(ability.getOwner(), ability.rank() > 0);
		
		// Sets the ability variable
		this.ability = ability;
	}
	
	// Overrides the abstract display function to show the name of the Ability when choosing
	@Override
	public String display() {
		return this.ability.getName();
	}
	
	// Overrides the abstract execute function to call the use(1) function of the ability
	@Override
	public void execute() {
		this.ability.use(1);
	}
}



