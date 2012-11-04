import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class Tomasulo {
	static ReservationStation station;
	static File instructionfile;
	static File simpleloopfile;
	static Scanner sc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		instructionfile = new File("C:\\Users\\chen\\bp_mips\\bp_mips\\tomasulo\\src\\instructions.dat");
		simpleloopfile = new File("C:\\Users\\chen\\bp_mips\\bp_mips\\tomasulo\\src\\simpleloop.dat");
		station = ReservationStation.getStation();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void simpleforloop() throws FileNotFoundException{
		sc = new Scanner(simpleloopfile);
		while(sc.hasNextLine()){
			String temp = sc.nextLine();
			System.out.println(temp);
			Instruction insttemp = new Instruction(temp);
			try {
				station.add(insttemp);
			} catch (Exception e) {
				System.out.println("instruction format error");
			}
		}
		
		while(station.isrunning())
			station.step();
		sc.close();
	}
}
