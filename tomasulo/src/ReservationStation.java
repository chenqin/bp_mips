import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	
	public enum STATUS {
	    UNISSUED, ISSUE, EXECUTE, WRITEBACK, DONE
	}
	
	int pc = 0;

	
	static class Execution{
		public static void issue(Instruction inst){
			
		}
		public static void execute(Instruction inst){
			
		}
		public static void writeback(Instruction inst){
			
		}
	}
	
	class ReservationEntry{
		int opcode;
		Instruction Instr;
		int qj;
		int qk;
		public STATUS status;
		boolean rj;
		boolean rk;
		
		public ReservationEntry(Instruction instroc){
			Instr = instroc;
			opcode = Instr.opcode;
			this.qj = Instr.rs;
			this.qk = Instr.rt;
			this.status = STATUS.UNISSUED;
		}
		
		public boolean isInstalled(){
			if(rj && rk) return true;
			else return false;
		}
		
		public void update(){
			switch(this.status){
				case UNISSUED:
					this.status = STATUS.ISSUE;
				case ISSUE:
					ReservationEntry entry = ReservationStation.getStation()._regmap.get(Integer.valueOf(this.qj));
					if(entry != null && entry.status == STATUS.DONE){
						//install a cycle here
						break;
					}
					entry = ReservationStation.getStation()._regmap.get(Integer.valueOf(this.qk));
					if(entry != null && entry.status == STATUS.DONE){
						//install a cycle here
						break;
					}
					this.status = STATUS.EXECUTE;
				case EXECUTE:
					Execution.execute(this.Instr);
					this.status = STATUS.WRITEBACK;
				case WRITEBACK:
					Execution.writeback(this.Instr);
					this.status = STATUS.DONE;
			}
				
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
