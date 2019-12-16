package WyattWitemeyer.WarOfGillysburg;
import java.util.*;
public class DiceRoller {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int dicenum = 0;
		int dicetype = 0;
		
		while (dicenum <= 0) {
			System.out.println("How many dice do you want to roll? ");
			if (sc.hasNextInt()) {
				dicenum = sc.nextInt();
			}
		}
		
		while (dicetype <= 0) {
			System.out.println("How many sides does the dice have? ");
			if (sc.hasNextInt()) {
				dicetype = sc.nextInt();
			}
		}
		
		System.out.println(dicenum + "d" + dicetype);
		int sum = 0;
		Random rd = new Random();
		for (int x = 0; x<dicenum; x++) {
			sum += rd.nextInt(dicetype) + 1;
		}
		
		System.out.println("You rolled a " + sum + "!");
		
		sc.close();
		
	}
}
