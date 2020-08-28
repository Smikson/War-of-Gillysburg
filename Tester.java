package WyattWitemeyer.WarOfGillysburg;
import java.io.*;
import java.util.*;

public class Tester {
	public static void main(String args[]) throws IOException {
		// Creates lists for the allies and enemies
		//List<Character> allies = new LinkedList<>();
		List<Enemy> enemies = new LinkedList<>();
		
		/*
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
		*/
		
		// Creates Enemies with respective Data and adds them to the enemies list
		Enemy spi1 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Giant Spider 1").build();
		Enemy spi2 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Giant Spider 2").build();
		Enemy spi3 = new EnemyBuilder(EnemyDataBase.GIANT_SPIDER).Name("Giant Spider 3").build();
		
		enemies.add(spi1);
		enemies.add(spi2);
		enemies.add(spi3);
		
		
		// THE ACTION TAKES PLACE HERE
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
		
	}
}
