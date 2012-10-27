import java.awt.*;
import java.util.*;

/**
 * Simulator is a class that handles the operation of each of the five stages of pipelined execution.
 */
public class Simulator
{
	/**
	 * object that implements the fetch stage of pipelined execution.
	 * @see Fetch
	 */
    public static Fetch myFetch;
    /**
	 * object that implements the decode stage of pipelined execution.
	 * @see  Decode
	 */
    public static Decode myDecode;
    /**
	 * object that implements the execute stage of pipelined execution.
	 * @see Execute
	 */
    public static Execute myExecute;
    /**
	 * object that implements the memory stage of pipelined execution.
	 * @see Memory
	 */
    public static Memory myMemory;
    /**
	 * object that implements the writeback stage of pipelined execution.
	 * @see WriteBack
	 */
    public static WriteBack myWriteBack;
    /**
	 * object that implements memory.
	 * @see MemoryType
	 */
    public static MemoryType mainMemory;
    /**
	 * object that implements the register file.
	 * @see MemoryType
	 */
    public static MemoryType regFile;
    /**
	 * the listboxes used in the GUI.
	 * @see ListBoxes
	 */
    public static ListBoxes lb;
    /**
	 * the vector of instructions.
	 * @see Instruction
	 */
    public static Vector Instructions;
    /**
	 * the Stages object used in the GUI.
	 * @see Stages
	 */
    public static Stages stg;
    
    /**
     * the reservation station in simulator
     */
    public static ReservationStation rs;

    /**
	 * creates a Simulator object and initialzes each of its components.
	 * Uses an instructions vector, the GUI's ListBoxes, and the GUI's Stages.
	 * @see Instruction
	 * @see ListBoxes
	 * @see Stages
	 */
    public Simulator(Vector _Instructions,
    	ListBoxes _lb,
    	Stages _stg) {
        Instructions = _Instructions;
        lb = _lb;
        stg = _stg;
        mainMemory = new MemoryType();
        regFile = new MemoryType();
        myFetch = new Fetch();
        myDecode = new Decode();
        myExecute = new Execute();
        myMemory = new Memory();
        myWriteBack = new WriteBack();
    }

    /**
	 * handles one cycle of program execution.  Returns a boolean indicating whether the program
	 * has completed execution.
	 */
    public boolean step() {
    	rs.step();
		/**
		 * indicates whether the first four stages of the pipeline all contain NOPs.
		 */
    	boolean allNOPs = false;
        // move instructions the pipeline by one stage
        myWriteBack.myInstruction = myMemory.myInstruction;
        myMemory.myInstruction = myExecute.myInstruction;
        myExecute.myInstruction = myDecode.myInstruction;
        myDecode.myInstruction = myFetch.myInstruction;
        myWriteBack.PC = myMemory.PC;
        myMemory.PC = myExecute.PC;
        myExecute.PC = myDecode.PC;
        if (myDecode.isStall == false)
          myDecode.PC = myFetch.PC;
       
        // load next instruction
        myFetch.step( Instructions, myDecode );           
        myDecode.step( Instructions, regFile, myMemory, 
                       myWriteBack, myFetch, myExecute, lb );
        myExecute.step( Instructions, myFetch, myDecode, lb );
        myMemory.step( mainMemory, lb );
        allNOPs = (myFetch.myInstruction.opcode == 0) &&
        		  (myDecode.myInstruction.opcode == 0) &&
        		  (myExecute.myInstruction.opcode == 0) &&
        		  (myMemory.myInstruction.opcode == 0);
        myWriteBack.step( regFile, Instructions, lb, allNOPs );

        // show status of stages
        stg.clearAll();
        stg.fetch.setText( myFetch.toString() );
        stg.decode.setText( myDecode.toString() );
        stg.execute.setText( myExecute.toString() );
        stg.memory.setText( myMemory.toString() );
        stg.wb.setText( myWriteBack.toString() );

        return myWriteBack.programDone();
    }
    
    /**
	 * sets the state of forwarding to cond.
	 */
    public void enableForwarding(boolean cond)
    {
    	myDecode.forwarding = cond;
    }
    
    /**
	 * sets the state of branch prediction to cond (unused).
	 */
    public void enableBP(boolean cond)
    {
    	myDecode.branchPrediction = cond;
    }
        
}
