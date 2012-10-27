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

	/**
	 * 
	 */
	public Console() {
		// TODO Auto-generated constructor stub
	}


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
				if(insttemp.valid())
					station.add(insttemp);
				else
					System.out.println("instruction format error");
			}
			sc.close();
		}
	}

}
