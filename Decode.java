import java.util.Vector;

/**
 * Decode handles the operation for the decode stage of pipelined execution.
 */
class Decode extends Stage {
	/**
	 * holds the Rd of the last three instruction to detect hazards
	 */
    private Vector hazardList;
    /**
	 * placeholder for an instruction that is stalled.
	 */
    private Instruction instructionSave;
    /**
	 * saves the program counter for an instruction that is stalled.
	 */
    private int savePC;
    /**
	 * indicates that a stall has been issued.
	 */
    public boolean isStall;
    /**
	 * indicates that a stall has been detected.
	 */
    public boolean stallflag;
    /**
	 * indicates whether branches are assumed taken (not used).
	 */
    public boolean assumeBranchTaken;
    /**
	 * indicates whether a branch instruction was taken on its last execution.
	 */
    public boolean branchPrediction;
    /**
	 * indicates whether forwarding is used.
	 */
    public boolean forwarding;
    /**
	 * table used to predict branches (unused).
	 */
    public Vector branchTable; 

    /**
	 * initialzes the fields of the object
	 */
    public Decode() {
       super();
       instructionSave = new Instruction("NOP");
       isStall = false;
       assumeBranchTaken = true;   // assume branches always taken (when prediction disabled)
       hazardList = new Vector(3);
       branchTable = new Vector();
       hazardList.addElement(new Integer(0));
       hazardList.addElement(new Integer(0));
       hazardList.addElement(new Integer(0));
    }

    /**
	 * process the current instruction.  Implements the functionality of the decode stage of a pipeline.
	 * Uses a vector of instructions, each of the other pipeline stages, the register file, and the
	 * ListBoxes object.
	 * @see Instruction
	 * @see Fetch
	 * @see Execute
	 * @see Memory
	 * @see WriteBack
	 * @see MemoryType
	 * @see Listboxes
	 */
    public void step(Vector Instructions, MemoryType regFile, 
                     Memory myMemory, WriteBack myWriteBack,
                     Fetch myFetch, Execute myExecute, ListBoxes lb) {

         String str;

        // if last instruction was stalled, recall stalled instruction

        if (PC==Instructions.size())
           PC = -9;  // this is actually a NOP received from Fetch

        if (isStall == true) {                             
            myInstruction = instructionSave;   
            PC = savePC;
        }
        
        //--Check for RAW hazards in pipeline; ignore NOP instructions-----

        // check RAW hazard for rs

        stallflag = false;
        if ( ( ( (myExecute.myInstruction.opcode != 0) && 
               (myExecute.myInstruction.flush == false) &&
               (myInstruction.rs==
                ((Integer) hazardList.elementAt(0) ).intValue() )) ||  
              ( (myMemory.myInstruction.opcode != 0) && 
               (myMemory.myInstruction.flush == false) &&
               (myInstruction.rs==
                ((Integer) hazardList.elementAt(1) ).intValue() )) ||     
              ( (myWriteBack.myInstruction.opcode != 0) && 
               (myWriteBack.myInstruction.flush == false) &&
               (myInstruction.rs==
                ((Integer) hazardList.elementAt(2) ).intValue() )) ) &&
             (myInstruction.opcode != 0) && (myInstruction.flush == false) )
        {
            stallflag = true;
        }

        // check for forwarding if hazard detected for rs

        if ((forwarding==true) && (stallflag==true)) {
           // see if value ready at end of last execute stage (now memory stage)
           if ((myInstruction.rs == myMemory.myInstruction.rd) &&
           		(myInstruction.rs != myExecute.myInstruction.rd)) {
               switch(myMemory.myInstruction.opcode) {
                 case 0:   // NOP
                 case 35:  // LW
                 case 43:  // SW
                 case 70:  // BEQ
                 case 71:  // BNE
                     break;      // ignore these instructions
                 default:
                     myInstruction.rsValue = myMemory.myInstruction.rdValue; // forwarded
                     stallflag = false;
                     myInstruction.forwardRsFlag = true;
                     str = "Result forwarded from ALU: $" + myInstruction.rs+"="+
                           myInstruction.rsValue+".";
                     lb.Messages.add(str, 0);
               }
           } else if ((myInstruction.rs == myWriteBack.myInstruction.rd) &&
           		(myInstruction.rs != myExecute.myInstruction.rd)) {
                 myInstruction.rsValue = myWriteBack.myInstruction.rdValue;  // forwarded
                 stallflag = false;
                 myInstruction.forwardRsFlag = true;
                 str = "Result forwarded from MEM: $" + myInstruction.rs+"="+
                           myInstruction.rsValue+".";
                 lb.Messages.add(str, 0);
           }            
        }
           

        // check for RAW hazard for rt

        if ( (myInstruction.isImmediate()==false) && (myInstruction.opcode != 35) &&
             ( ( (myExecute.myInstruction.opcode != 0) && 
               (myExecute.myInstruction.flush == false) &&
               (myInstruction.rt==
                    ((Integer) hazardList.elementAt(0) ).intValue() )) ||  
             ( (myMemory.myInstruction.opcode != 0) && 
               (myMemory.myInstruction.flush == false) &&
               (myInstruction.rt==
                    ((Integer) hazardList.elementAt(1) ).intValue() )) ||     
             ( (myWriteBack.myInstruction.opcode != 0) && 
               (myWriteBack.myInstruction.flush == false) &&
               (myInstruction.rt==
                    ((Integer) hazardList.elementAt(2) ).intValue() )) ) && 
             (myInstruction.opcode != 0) && (myInstruction.flush == false) )
        {
            stallflag = true;
        }


        // check for forwarding if hazard detected for rt

        if ((!myInstruction.isImmediate())&&(forwarding==true)&&(stallflag==true)) {
           // see if value ready at end of last execute stage (now memory stage)
           if ((myInstruction.rt == myMemory.myInstruction.rd) &&
           		(myInstruction.rt != myExecute.myInstruction.rd)) {
               switch(myMemory.myInstruction.opcode) {
                 case 0:   // NOP
                 case 35:  // LW
                 case 43:  // SW
                 case 70:  // BEQ
                 case 71:  // BNE
                     break;      // ignore these instructions
                 default:
                     myInstruction.rtValue = myMemory.myInstruction.rdValue; // forwarded
                     stallflag = false;
                     myInstruction.forwardRtFlag = true;
                     str = "Result forwarded from ALU: $" + myInstruction.rt+"="+
                           myInstruction.rtValue+".";
                     lb.Messages.add(str, 0);
               }
           } else if ((myInstruction.rt == myWriteBack.myInstruction.rd) &&
           		(myInstruction.rt != myExecute.myInstruction.rd)) {
                 myInstruction.rtValue = myWriteBack.myInstruction.rdValue;  // forwarded
                 stallflag = false;
                 myInstruction.forwardRtFlag = true;
                 str = "Result forwarded from MEM: $" + myInstruction.rt+"="+
                        myInstruction.rtValue+".";
                 lb.Messages.add(str, 0);
 
           }            
        }
             

        if (stallflag == true) {
            // RAW hazard found, so issue a stall
            if (isStall == false) {
                isStall = true;
                instructionSave = myInstruction;
                savePC = PC;
            }
            myInstruction = new Instruction("NOP");     
            myInstruction.rsValue = 0;
            myInstruction.rtValue = 0; 
            PC = -9;
        } else {            
            // no new hazards; check to see if last instruction stalled, and clean up if so
            if (isStall == true) {
                 isStall = false;
                 myInstruction = instructionSave;
            }
            
            // if a branch op, and prediction is on, then do what prediction bit says

//            if ((branchPrediction == true) && 
//                ((myInstruction.opcode == 70) || (myInstruction.opcode == 71)) ) {
//                if ( ((Instruction)Instructions.elementAt(PC)).branchTaken == true) {
//                     myFetch.PC = ((Instruction)Instructions.elementAt(PC)).immediate - 1;
//                     lb.Messages.add("Branch predicted at "+PC+" and taken.", 0);
//              }
//            }
       }        


       // assume R-type--read source operand values from Reg File
       
        if (myInstruction.forwardRsFlag == false) 
            myInstruction.rsValue = regFile.getValue( myInstruction.rs );
        if (myInstruction.forwardRtFlag == false) {       
            if (myInstruction.isImmediate())  
                  // assign immediate value
                  myInstruction.rtValue = myInstruction.immediate;
            else
                  // read from register
                  myInstruction.rtValue = regFile.getValue( myInstruction.rt );
        }



       // update hazard list (the destination register number at each remaining stage)
       // to match instructions as they propagate through the stages    

       hazardList.setElementAt( hazardList.elementAt(1), 2);
       hazardList.setElementAt( hazardList.elementAt(0), 1);
       hazardList.setElementAt( new Integer(myInstruction.rd), 0);      

       if (isStall == true) {
             // throw event to tell user a stall has been issued
             str = "Stall issued for instruction "+savePC+".";
             lb.Messages.add(str, 0);
       }
    }

   /**
	 * returns a string representation of the stage's current instruction and the results of its processing.
	 */
   public String toString() {
      String temp;
 
      if (myInstruction.flush == true) {
           temp = "FLUSHED: \n" + myInstruction + "\n";
           return temp;
      }
 
      if (PC >= 0) { 
         temp = Integer.toString(PC) + ":\n" + myInstruction + "\n" +
             "ALUop1= " + Integer.toString(myInstruction.rsValue) + "\n" +
             "ALUop2= " + Integer.toString(myInstruction.rtValue) + "\n";
      } else {
         temp = myInstruction + "\n";
         if (isStall)
            temp += "Stalled:\n" + Integer.toString(savePC) + ":  " +
                    instructionSave + "\n";
      }

      return temp;
   }
   
}

