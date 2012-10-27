import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * Esentially, tomasulo algorithm issues and stalls
 * instructions based on reservation station condition check
 * so it will read instructions and do couple of optimization 
 * filling into registeration station and then execute 
 * @author chen
 *
 */
public class ReservationStation {
	int pc = 0;
	/**
	 * indicates if register in use
	 * */
	
	class RegisterInfo{
		boolean isBusy;
		Instruction instr;
	}
	/**
	 * qj and qk value can check status of 
	 * instruction vj and vk
	 * */
	
	class ReservationEntry{
		boolean isALUInUse;
		Instruction Instr;
		Instruction vj;
		Instruction vk;
		public void update(){
			
		}
	}
	
	List<RegisterInfo> _regsinfo;
	List<ReservationEntry> _resentries;
	List<Instruction> _instructions;

	protected static ReservationStation instance;
	public static ReservationStation getStation(){
		if(instance == null)
			instance = new ReservationStation();
		return instance;
	}

	private ReservationStation() {
		_regsinfo = new ArrayList<RegisterInfo>();
		_resentries = new ArrayList<ReservationEntry>();
		_instructions = new ArrayList<Instruction>();
	}
	
	public void add(Instruction newinst){
		_instructions.add(newinst);
	}
	
	protected void unrolling(){
		//identify loop and unroll by renaming varables
		//when it meets a branch it go 
	}
	
	public void execute(){
		
	}
	/**
	 * called by simulator to trigger reservation internal instructions
	 * stages change
	 */
	public void step(){
		pc++;
		for(ReservationEntry rentry : _resentries)
			rentry.update();
	}
}
