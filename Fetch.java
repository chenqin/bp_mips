import java.util.*;
import java.lang.*;

/**
 * Fetch handles the fetch stage of execution.
 */
class Fetch extends Stage {
	/**
	 * the number of instructions in the program.
	 */
   private int instructionSize;
    
	/**
	 * creates a Fetch object and initializes its fields.
	 */
   public Fetch() {
        super();
        PC = -1;  // set program counter so first instruction is PC=0
        instructionSize = 0;
   }

	/**
	 * process the current instruction.  Implements the fetch stage of a pipeline.
	 * Uses an instruction vector as well as the Decode stage.
	 * @see Instruction
	 * @see Decode
	 */
   public void step(Vector Instructions, Decode myDecode) {
      instructionSize = Instructions.size();       

      // if no stall at decode, move PC to next instruction

      if (myDecode.isStall == false) {
          if ((PC < Instructions.size()) && (Instructions.size()>0) )
            PC++;
          // load next instruction; if no more instructions, issue NOP
          if ((PC >= 0) && (PC < Instructions.size()) )
              myInstruction = (Instruction) Instructions.elementAt(PC);
          else {
              myInstruction = new Instruction("NOP");   
          }
          myInstruction.flush = false;  // valid instruction  
       }

   }

	/**
	 * returns a string representation of the current instruction and the results of its processing.
	 */
   public String toString() {
      String temp;

      if (myInstruction.flush == true) {
           temp = "FLUSHED: \n" + myInstruction + "\n";
           return temp;
      }
      if ((PC >= 0) && (PC < instructionSize) )
        temp = Integer.toString(PC) + ":\n" + myInstruction + "\n";
      else
        temp = myInstruction + "\n";
      return temp;
   }

}
