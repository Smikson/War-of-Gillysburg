package WyattWitemeyer.WarOfGillysburg;
import java.util.*;
public class Dice {
	// Variable
	private int sides;
	
	// Constructors
	public Dice(int sides) {
		this.sides = sides;
	}
	public Dice() {
		this.sides = 6;
	}
	
	// Returns a random number between 1 and the number of sides
	public int roll() {
		Random rd = new Random();
		return rd.nextInt(this.sides) + 1;
	}
	
	// Returns the sum of an integer number of rolls
	public int roll(int numTimes) {
		int sum = 0;
		for (int x = 0; x<numTimes; x++) {
			sum += this.roll();
		}
		return sum;
	}
}
