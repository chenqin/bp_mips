import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Get ride of GUI interface for now
 */

/**
 * @author chen
 *
 */
public class Console {

	static ReservationStation station;
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		File instructionfile = new File(args[0]);
		if(instructionfile.exists()){
			station = ReservationStation.getStation();
			Scanner sc = new Scanner(instructionfile);
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

}
