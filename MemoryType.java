import java.util.*;
import java.lang.*;

/**
 * MemoryType implements hardware memory as either a register file or main memory.
 */
class MemoryType {
	/**
	 * vector of memory locations.
	 */
   private Vector memory;                

	/**
	 * creates a MemoryType object and initializes each location to zero.
	 */
   public MemoryType() {
        super();
        memory = new Vector(32);          // 32 words of memory
        int i;
        for(i=0; i<32;i++)                // initialize to all 0's
            memory.addElement( new Integer(0) );
   }

	/**
	 * returns the integer value from the location of the address supplied.
	 */
   public int getValue( int address ) {
       return ((Integer) memory.elementAt( address ) ).intValue();
   } 

	/**
	 * returns a boolean indicating whether value was placed in the memory location specified by
	 * address was successful.
	 */
   public boolean putValue( int value, int address ) {
       if ((address >= 0) && (address< 32)) {
           memory.setElementAt((new Integer(value)), address); 
           return true;
       } else
           return false;
   }

}
