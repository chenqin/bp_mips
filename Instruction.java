import java.util.*;
import java.awt.*;
import java.lang.*;

/**
 * The Instruction class is used by the simulator to
 * hold all information needed for an instruction.
 * As of 8/26/97 this includes branch prediction
 * although it is not yet implemented.
 */
public class Instruction
{
	/**
	 * opcode stores an integer value for an instruction's opcode.
	 * These integer values were determined arbitrarily and are
	 * defined in the constructor. 
	 */
	public int opcode;
	/**
	 * rs hold the source1 register number.
	 */
	public int rs;
	/**
	 * rt holds the source2 register number.
	 */
	public int rt;
	/**
	 * rd holds the destination register number.
	 */
	public int rd;
	/**
	 * immediate holds the immediate value for I-type instructions.
	 */
	public int immediate;
	/**
	 * rdValue holds the value stored in the register specified by rd.
	 */
	public int rdValue;
	/**
	 * rsValue holds the value stored in the register specified by rs.
	 */
	public int rsValue;
	/**
	 * rtValue holds the value stored in the register specified by rt.
	 */
	public int rtValue;
	/**
	 * instructionString is a String representation of the instruction.
	 */
    public String instructionString;
   	/**
	 * ok is an internal boolean used for checking the validity of an entered instruction.
	 */
	private boolean ok;
	/**
	 * flush indicates if the instruction has been flushed.
	 */
	public boolean flush;
	/**
	 * branchTaken indicates if a branch instruction was taken last time (currently unused).
	 */
	public boolean branchTaken;
	/**
	 * wbFlush indicates if an instruction has been flushed.  This additional flush variable was
	 * included because of the case when an instruction is in WriteBack as well as Fetch.  This
	 * case only occurs for an instruction following a branch in a one instruction loop.
	 */
	public boolean wbFlush;
	/**
	 * used to indicate that the Rs value of a previous instruction is needed before this instruction
	 * can continue execution.
	 */
    public boolean forwardRsFlag;
    /**
	 * used to indicate that the Rt value of a previous instruction is needed before this instruction
	 * can continue execution.
	 */
    public boolean forwardRtFlag;
    /**
	 * indicates whether the instruction is an I-type instruction (and that the Rt value in not needed).
	 */
    private boolean isImmediateInstruction;
    /**
	 * internal string used to indicate an error with an entered instruction.
	 */
	private String error = "The entered instruction is invalid.";
	
	/**
	 * takes a string and parses it to fill the fields of the object.
	 */
	Instruction(String temp)
	{
		StringTokenizer tokens = new StringTokenizer(temp, " ");
		String op = "", t1 = "", t2 = "", t3 = "";
		int switcher;
	
	        rdValue = 0;              // initial operand values
	        rsValue = 0;
	        rtValue = 0;              
	        isImmediateInstruction = false;  // assumes no immediate operand	
	        ok = true;                // instruction is recognized by parser
              instructionString = temp; // the full instruction as a string
              flush = false;            // instruction will be executed
              branchTaken = true;       // assume branch will be taken initially,
                                        //   if this instruction is a branch
              forwardRsFlag = false;    // assume not forwarded data
              forwardRtFlag = false;

                // if no-op instruction (aka a stall)
                if (temp.equals("NOP")) {
                  instructionString = "NOP";
                  rd = 0;
                  rt = 0;
                  rs = 0;
                  immediate = 0;
                  opcode = 0;
                  return;
                }

trying:		
		try
		{
			op = tokens.nextToken();
			t1 = tokens.nextToken();
			t2 = tokens.nextToken();
			
			if(t1.length() < 2 || t2.length() < 2)
			{
				ok = false;
				break trying;
			}
			
			if (!op.equals("LW") && !op.equals("SW"))
			{
				t3 = tokens.nextToken();
				if(t3.length() == 0)
				{
					ok = false;
					break trying;
				}
			}
			
			if(op.equals("ADD") || op.equals("SUB") || op.equals("MULT") ||
				op.equals("DIV") || op.equals("AND") || op.equals("OR") ||
                op.equals("XOR") || op.equals("SLL") || op.equals("SRL") || 
                op.equals("SLT") || op.equals("SLE") || op.equals("SGT") || 
                op.equals("SGE") || op.equals("SEQ") )
				switcher = 0;  // R-type instructions
			else if(op.equals("ADDI") || op.equals("ANDI") || op.equals("ORI") || 
                      op.equals("XORI") || op.equals("SUBI") || op.equals("SLLI") ||
                      op.equals("SRLI") )
				switcher = 1;  // I-type instructions
			else if(op.equals("LW") || op.equals("SW"))
				switcher = 2;  // load/store word instructions
            else if(op.equals("BEQ") || op.equals("BNE") )
                switcher = 3;  // branch instructions
			else
			{
				ok = false;
				break trying;
			}
			
			switch (switcher)
			{
				case 0:
					if(!t1.startsWith("$") || !t2.startsWith("$") ||
					    !t1.startsWith("$"))
					{
						ok = false;
						break trying;
					}
					else
					{
						rd = Integer.parseInt(t1.substring(1));
						rs = Integer.parseInt(t2.substring(1));
						rt = Integer.parseInt(t3.substring(1));
						if (rt < 0 || rt > 31 || rs < 0 || rs > 31 || rd < 1 || rd > 31)
						{
							error = "Invalid Instr: register out of bounds";
							ok = false;
							break trying;
						}
						if (op.equals("ADD"))
							opcode = 32;
						else if (op.equals("SUB"))
							opcode = 34;
						else if (op.equals("MULT"))
							opcode = 24;
						else if (op.equals("DIV"))
							opcode = 26;
						else if (op.equals("AND"))
	                        opcode = 36;
                        else if (op.equals("XOR"))
                            opcode = 38;
                        else if (op.equals("SLL"))
                            opcode = 50;
                        else if (op.equals("SRL"))
                            opcode = 51;
                        else if (op.equals("SLT"))
                            opcode = 60;
                        else if (op.equals("SLE"))
                            opcode = 61;
                        else if (op.equals("SEQ"))
                            opcode = 62;
                        else if (op.equals("SGT"))
                            opcode = 63;
                        else if (op.equals("SGE"))
                            opcode = 64;
						else
							opcode = 37; // OR is the only one left
					}
					break;
				case 1:
					if(!t1.startsWith("$") || !t2.startsWith("$"))
					{
						ok = false;
						break trying;
					}
					else
					{
						rd = Integer.parseInt(t1.substring(1));
						rs = Integer.parseInt(t2.substring(1));
						if (rd < 1 || rd > 31 || rs < 0 || rs > 31)
						{
							error = "Invalid Instr: register out of bounds";
							ok = false;
							break trying;
						}
						isImmediateInstruction = true;
						immediate = Integer.parseInt(t3);
						if(op.equals("ADDI")) 
							opcode = 32;
						else if (op.equals("ANDI"))
							opcode = 36;
						else if (op.equals("SUBI"))
							opcode = 34;
						else if (op.equals("XORI"))
							opcode = 38;
						else if (op.equals("SLLI"))
							opcode = 50;
						else if (op.equals("SRLI"))
							opcode = 51;
						else
							opcode = 37; // ORI is the only one left
					}
					break;
				case 2:
					if(!t1.startsWith("$"))
					{
						ok = false;
						break trying;
					}
					else
					{
						rt = Integer.parseInt(t1.substring(1));
						rs = Integer.parseInt(t2.substring(t2.indexOf("$")+1,t2.indexOf(")")));
						if (rt < 1 || rt > 31 || rs < 0 || rs > 31)
						{
							error = "Invalid Instr: register out of bounds";
							ok = false;
							break trying;
						}
						immediate = Integer.parseInt(t2.substring(0,t2.indexOf("(")));
						if (op.equals("LW")) {
							opcode = 35;
                            rd = rt;
						} else
							opcode = 43; // SW is the only one left
					}
					break;
                case 3:
					if(!t1.startsWith("$") || !t2.startsWith("$"))
					{
						ok = false;
						break trying;
					}
					else
					{
						rs = Integer.parseInt(t1.substring(1));
						rt = Integer.parseInt(t2.substring(1));
						if (rs < 1 || rs > 31 || rt < 0 || rt > 31)
						{
							error = "Invalid Instr: register out of bounds";
							ok = false;
							break trying;
						}
						isImmediateInstruction = false;
						immediate = Integer.parseInt(t3);
						if(op.equals("BEQ")) 
							opcode = 70;
						else
							opcode = 71; // BNE is the only one left
					}
					break;                                   
				default:
					ok = false;
					break trying;
			}
		}
		catch(NumberFormatException e)
		{
			ok = false;
		}
		catch(NoSuchElementException e2)
		{
			ok = false;
		}
		catch(StringIndexOutOfBoundsException e3)
		{
			ok = false;
		}
	}
	
	/**
	 * returns a boolean indicating whether the instruction is valid.
	 */
	public boolean valid()
	{
		return ok;
	}
	
	/**
	 * returns a string indicating the error generated from the entered instruction.
	 */
	public String theError()
	{
		return error;
	}
    
    /**
	 * returns a boolean indicating whether the instruction is an I-type.
	 */
    public boolean isImmediate()
    {
        return isImmediateInstruction;
    }
	
	/**
	 * returns a string representation of the instruction.
	 */
    public String toString()
    {
        return (instructionString+"\n");
    }
}
