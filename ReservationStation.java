
/**
 * Esentially, tomasulo algorithm issues and stalls
 * instructions based on reservation station condition check
 * so it will read instructions and do couple of optimization 
 * filling into registeration station and then execute 
 * @author chen
 *
 */
public class ReservationStation {
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
	}
	
	RegisterInfo[] _regsinfo;
	ReservationEntry[] _resentries;
	
	public ReservationStation(int regnum, int alunum) {
		_regsinfo = new RegisterInfo[regnum];
		_resentries = new ReservationEntry[alunum];
	}
	
	void reNaming(Instruction cur_in){
		
	}
	
	/**
	 * control if instruction fetch could be issued
	 * if so, fill available entry in reservation station
	 * @param cur_in
	 */
	void fillEntry(Instruction cur_in){
		for(ReservationEntry re : _resentries){
			
		}
		//stall pc
	}
	
	public void step(Instruction cur_instruct){
		//check register renaming if there is unrolling possibility
		reNaming(cur_instruct);
		fillEntry(cur_instruct);
	}

}
