import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ReservationStation.ReservationEntry;


/**
 * Esentially, tomasulo algorithm issues and stalls
 * instructions based on reservation station condition check
 * so it will read instructions and do couple of optimization 
 * filling into registeration station and then execute 
 * @author chen
 *
 */
/**
 * @author chen
 *
 */
public class ReservationStation {
	int pc = 0;
	/**
	 * indicates if register in use
	 * */
	/**
	 * Reservation Entry
	 * qj and qk is the entry that produce source 
	 * rj and rk indicates whether source are avalaible
	 * result is produced when current insturction finished executed
	 * */
	
	class ReservationEntry{
		int opcode;
		Instruction Instr;
		int qj;
		int qk;
		boolean rj;
		boolean rk;
		boolean isdone;
		Object result;
		
		public ReservationEntry(Instruction instroc){
			Instr = instroc;
			opcode = Instr.opcode;
			this.qj = Instr.rs;
			this.qk = Instr.rt;
			isdone = false;
		}
		
		public boolean isInstalled(){
			if(rj && rk) return true;
			else return false;
		}
		
		public void update(){
			ReservationEntry entry = ReservationStation.getStation()._regmap.get(Integer.valueOf(this.qj));
			if(entry != null && entry.isdone == false){
				//install a cycle here
			}
			entry = ReservationStation.getStation()._regmap.get(Integer.valueOf(this.qk));
			if(entry != null && entry.isdone == false){
				//install a cycle here
			}
			
			//execute here assume infinite ALU
			
		}
	}
	
	/**
	 * entries in reservation station
	 * each map to one instruction
	 */
	List<ReservationEntry> _resentries;
	
	/**
	 * usage of each register
	 */
	Map<Integer,ReservationEntry> _regmap;
	List<Instruction> _instructions;

	protected static ReservationStation instance;
	
	/**
	 * @return reservation station singleton instance
	 */
	public static ReservationStation getStation(){
		if(instance == null)
			instance = new ReservationStation();
		return instance;
	}

	private ReservationStation() {
		_resentries = new ArrayList<ReservationEntry>();
		_instructions = new ArrayList<Instruction>();
		_regmap = new HashMap<Integer, ReservationEntry>();
	}
	
	public void add(Instruction newinst) throws Exception{
		if(newinst.valid()){
			_instructions.add(newinst);
			_resentries.add(new ReservationEntry(newinst));
		}else
			throw new Exception("Instruction not valid");
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

	/**
	 * check if all instructions has been executed
	 * if so, exit
	 * @return
	 */
	public boolean isrunning() {
		for(ReservationEntry rentry : _resentries)
			if(!rentry.isdone) return false;
		return true;
	}
}
