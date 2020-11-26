package WyattWitemeyer.WarOfGillysburg;
import java.util.*;
import java.io.*;

public class Demo {
	public static int getIntResponse(Scanner sc, int lowerbound, int upperbound) {
		int choice;
		while (true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				sc.nextLine();
				if (choice >= lowerbound && choice <= upperbound) {
					break;
				}
				else {
					System.out.print("Please enter an integer within the requested bound: ");
				}
			}
			else {
				sc.nextLine();
				System.out.print("Please enter an integer as a response: ");
			}
		}
		return choice;
	}
	
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		SteelLegionTankBuilder playerBuilder = new SteelLegionTankBuilder().Name("You, Steel Legion Tank");
		
		System.out.println("Welcome to the War of Gillysburg Code Demo!");
		System.out.println("This is a test envirnoment with limited view of the code as a whole, but is good for understanding how the code interacts with gameplay and testing the Abilities and functionality of each class.");
		System.out.println("This \"Demo\" is a work in progress and will be added to as classes are finished and edited");
		System.out.println("Currently there is only 1 completely finished and working player class, the Steel Legion Tank.");
		System.out.println();
		
		System.out.println("Very short Ability descriptions for refernce (does not cover all effects of Abilities):");
		System.out.println("Unique Passive Ability - \"Hold It Right There!\" - Halts enemies and grants the ability to block for allies. Increased Block stat when doing so.");
		System.out.println("Passive Ability - \"Enchanted Armor\" - Increases Armor and Block stats. Heals the Steel Legion Warrior at the end of each of his/her turn.");
		System.out.println("Passive Ability - \"Shield Skills\" - Improves \"Shield Bash\" with Accuracy and Cooldown reduction after missing and \"Shield Reflection\" with healing for effecting multiple targets.");
		System.out.println("Passive Ability - \"Professional Laughter\" - Increases Threat and Health stats..");
		System.out.println("Ability 1 - \"Shield Bash\" - Targeted - Deals low-average damage and stuns target.");
		System.out.println("Ability 2 - \"Shield Reflection\" - AOE - Deals very low damage and blinds near targets.");
		System.out.println("Ability 3 - \"Taunting Attack\" - Targeted - Deals average damage and taunts target.");
		System.out.println("Ability 4 - \"Leader Strike\" - Targeted - Deals low-average damage and heals/buffs allies.");
		System.out.println("ULTIMATE Ability - \"HahaHa You Can\'t Kill Me!\" - Fully heals self. Increases Armor. Taunts all enemies.");
		System.out.println();
		
		System.out.print("Enter a level for the Steel Legion Tank (1-100): ");
		playerBuilder.Level(getIntResponse(sc, 1, 100));
		
		System.out.println("Enter the rank of each Ability as prompted.");
		System.out.print("Enter a rank for the Unique Passive Ability: \"Hold It Right There!\" (1-5): ");
		playerBuilder.setHoldItRightThereRank(getIntResponse(sc, 1, 5));
		
		System.out.print("Enter a rank for the Passive Ability: Enchanted Armor (0-15): ");
		playerBuilder.setEnchantedArmorRank(getIntResponse(sc, 0, 15));
		
		System.out.print("Enter a rank for the Passive Ability: Shield Skills (0-15): ");
		playerBuilder.setShieldSkillsRank(getIntResponse(sc, 0, 15));
		
		System.out.print("Enter a rank for the Passive Ability: Professional Laughter (0-15): ");
		playerBuilder.setProfessionalLaughterRank(getIntResponse(sc, 0, 15));
		
		System.out.print("Enter a rank for Ability 1: Shield Bash (1-10): ");
		playerBuilder.setShieldBashRank(getIntResponse(sc, 1, 10));
		
		System.out.print("Enter a rank for Ability 2: Shield Reflection (0-10): ");
		playerBuilder.setShieldReflectionRank(getIntResponse(sc, 0, 10));
		
		System.out.print("Enter a rank for Ability 3: Taunting Attack (0-10): ");
		playerBuilder.setTauntingAttackRank(getIntResponse(sc, 0, 10));
		
		System.out.print("Enter a rank for Ability 4: Leader Strike (0-10): ");
		playerBuilder.setShieldBashRank(getIntResponse(sc, 0, 10));
		
		System.out.print("Enter a rank for the ULTIMATE Ability: HaHaHa You Can\'t Kill Me! (0-3): ");
		playerBuilder.setShieldBashRank(getIntResponse(sc, 0, 3));
		
		Character player = playerBuilder.build();
		BattleSimulator.getInstance().addAlly(player);
		BattleSimulator.getInstance().playDemo(player);
		
		sc.close();
	}
}
