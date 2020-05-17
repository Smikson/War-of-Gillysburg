package WyattWitemeyer.WarOfGillysburg;
import java.io.*;
import java.util.*;

public class Tester {
	public static void main(String args[]) throws IOException {
		// Creates lists for the allies and enemies
		List<Character> allies = new LinkedList<>();
		List<Enemy> enemies = new LinkedList<>();
		
		
		// Creates Players with respective classes and adds them to the allies list
		SteelLegionWarrior Ian = PlayerDataBase.IAN;
		SentinelArcArcher Tyler = PlayerDataBase.TYLER;
		SilentDeathPoisonSpecialist Wyatt = PlayerDataBase.WYATTWIL;
		Character Lesmik = AllyDataBase.LESMIK_SILOLAS;
		Character Stellov = AllyDataBase.STELLOV_ALDECEPT;
		Character Leya = AllyDataBase.LEYA_MENDACE;
		Character Prince = AllyDataBase.PRINCE_JAPHETH;
		
		allies.add(Ian);
		allies.add(Tyler);
		allies.add(Wyatt);
		allies.add(Lesmik);
		allies.add(Stellov);
		allies.add(Leya);
		allies.add(Prince);
		
		// Creates Enemies with respective Data and adds them to the enemies list
		Enemy spi1 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Gaint Spider 1").build();
		Enemy spi2 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Gaint Spider 2").build();
		Enemy spi3 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Gaint Spider 3").build();
		
		enemies.add(spi1);
		enemies.add(spi2);
		enemies.add(spi3);
		
		/*
		// Gets all characters CurrentHealth from the file BattleHealthData.txt and stores in the CurrentHealthStatus List
		Scanner sc = new Scanner(new File("BattleHealthData.txt"));
		LinkedList<String> currentHealthStatus = new LinkedList<>();
		while (sc.hasNextLine()) {
			currentHealthStatus.add(sc.nextLine());
		}
		sc.close();
		
		// Puts data in a map with the name as the key and the CurrentHealth as the value
		HashMap<String, Integer> currentHealth = new HashMap<>();
		for (String line : currentHealthStatus) {
			try {
				currentHealth.put((line.split(": ")[0]), Integer.parseInt(line.split(": ")[1]));
			}
			catch (Exception e) {
				System.out.println("File is not in correct format.");
			}
		}
		
		// Sets the CurrentHealth of the combatants to the data in the file
		for (Character ally : allies) {
			if (currentHealth.containsKey(ally.getName())) {
				ally.setCurrentHealth(currentHealth.get(ally.getName()));
			}
		}
		for (Enemy enemy : enemies) {
			if (currentHealth.containsKey(enemy.getName())) {
				enemy.setCurrentHealth(currentHealth.get(enemy.getName()));
			}
		}
		*/
		List<Character> group = new LinkedList<Character>();
		group.add(spi3);
		
		// THE ACTION TAKES PLACE HERE
		//LinkedList<Character> listed = new LinkedList<>();
		SteelLegionTank ally1 = new SteelLegionTankBuilder(PlayerDataBase.TEST).Name("Ally 1").build();
		SteelLegionTank ally2 = new SteelLegionTankBuilder(PlayerDataBase.TEST).Name("Ally 2").build();
		SteelLegionTank ally3 = new SteelLegionTankBuilder(PlayerDataBase.TEST).Name("Ally 3").build();
		BattleSimulator.getInstance().addAlly(ally1);
		BattleSimulator.getInstance().addAlly(ally2);
		BattleSimulator.getInstance().addAlly(ally3);
		BattleSimulator.getInstance().addEnemy(spi1);
		BattleSimulator.getInstance().addEnemy(spi2);
		BattleSimulator.getInstance().addEnemy(spi3);
		BattleSimulator.getInstance().initiate();
		
		/*
		// Writes CurrentHealth of all characters to the BattleHealthData.txt File.
		BufferedWriter writer = new BufferedWriter(new FileWriter("BattleHealthData.txt"));
		for (Character ally : allies) {
			writer.write(ally.getName() + ": " + ally.getCurrentHealth() + "\n");
		}
		for (Enemy enemy : enemies) {
			writer.write(enemy.getName() + ": " + enemy.getCurrentHealth() + "\n");
		}
		writer.close();*/
		
	}
}
