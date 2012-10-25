import java.util.*;
import java.lang.*;
import java.awt.*;

/**
 * Stage is a template class (not abstract) for each of the five pipeline stages.
 * @see Fetch
 * @see Decode
 * @see Execute
 * @see Memory
 * @see WriteBack
 */
class Stage {
	/**
	 * current instruction for the stage.
	 * @see Instruction
	 */
   protected Instruction myInstruction;      // instruction object
	/**
	 * program counter for the current instruction.
	 */
   protected int PC;                         // program counter ( which
                                             //   is index of instruction vector)
    
    /**
	 * initialzes the fields.
	 */
   public Stage() {
      // default (initialized) instruction is a NOP
      myInstruction = new Instruction("NOP");
      PC = -9;  // initialize program counter 
  }

}
