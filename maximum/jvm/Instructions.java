/*
 *  This software is Public Domain.
 */
package maximum.jvm;


public interface Instructions
{
	/** do nothing
	 * 		
	 *  ARGS:
	 *  STACK: ... => ... 
	 */
	public static final byte NOP = 0;
	
	/** push null on operand stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,null 
	 */
	public static final byte ACONST_NULL = 1;
	
	/** push byte -1 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,-1 
	 */
	public static final byte ICONST_M1 = 2;

	/** push byte 0 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,0 
	 */
	public static final byte ICONST_0 = 3;

	/** push byte 1 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,1 
	 */
	public static final byte ICONST_1 = 4;

	/** push byte 2 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,2 
	 */
	public static final byte ICONST_2 = 5;

	/** push byte 3 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,3 
	 */
	public static final byte ICONST_3 = 6;

	/** push byte 4 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,4 
	 */
	public static final byte ICONST_4 = 7;

	/** push byte 5 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,5 
	 */
	public static final byte ICONST_5 = 8;

	/** push long 0 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,0L 
	 */
	public static final byte LCONST_0 = 9;

	/** push long 1 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,1L 
	 */
	public static final byte LCONST_1 = 10;

	/** push float 0 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,0F 
	 */
	public static final byte FCONST_0 = 11;

	/** push float 1 on the op stack
	 * 
	 *  ARGS:
	 *	STACK: ... => ...,1F 
	 */
	public static final byte FCONST_1 = 12;

	/** push float 2 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,2F 
	 */
	public static final byte FCONST_2 = 13;

	/** push double 0 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,0D 
	 */
	public static final byte DCONST_0 = 14;

	/** push double 1 on the op stack
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,1D 
	 */
	public static final byte DCONST_1 = 15;

	/** push byte on the op stack
	 * 
	 *  ARGS: byte
	 *  STACK: ... => ...,<b> 
	 */
	public static final byte BIPUSH = 16;

	/** push short on the op stack
	 * 
	 *  ARGS: byte1, byte2
	 *  STACK: ... => ...,(byte1 << 8) | byte2
	 */
	public static final byte SIPUSH = 17;

	/** Push item from runtime constant pool.  The constant
	 *  must be an int, float, or string reference.
	 * 
	 *  ARGS: index
	 *  STACK: ... => ...,value
	 */
	public static final byte LDC = 18;
	
	/** Push an int, float, or string reference from runtime 
	 *  constant pool (wide index).  
	 *  Index = (indexbyte1 << 8) | indexbyte2
	 * 
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK: ... => ...,value
	 */
	public static final byte LDC_W = 19;
	
	/** Push a long or double reference from runtime 
	 *  constant pool (wide index).  
	 *  Index = (indexbyte1 << 8) | indexbyte2
	 * 
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK: ... => ...,value
	 */
	public static final byte LDC2_W = 20;
	
	/** Load byte from local variable.
	 * 
	 *  ARGS: index
	 *  STACK: ... => ...,value
	 */
	public static final byte ILOAD = 21;
	
	/** Load long from local variable.  The index is an unsigned 
	 *  byte. Both index and index + 1 must be indices into the 
	 *  local variable array of the current frame (§3.6). The 
	 *  value of the local variable at index is pushed onto the 
	 *  operand stack. The lload opcode can be used in 
	 *  conjunction with the wide instruction to access a local 
	 *  variable using a two-byte unsigned index.
	 * 
	 *  ARGS: index
	 *  STACK: ... => ...,value
	 */
	public static final byte LLOAD = 22;
	
	/**  Load float from local variable.
	 * 
	 *   ARGS: index
	 *   STACK: ... => ...,value
	 */
	public static final byte FLOAD = 23;
	
	/** Load double from local variable.The index is an 
	 *  unsigned byte. Both index and index + 1 must be 
	 *  indices into the local variable array of the current 
	 *  frame (§3.6). 
	 * 
	 *  ARGS: index
	 *  STACK: ... => ...,value
	 */
	public static final byte DLOAD = 24;
	
	/**  Load a refid from a local variable onto the stack.
	 * 
	 *   ARGS: index
	 *   STACK: ... => ...,objectref
	 */
	public static final byte ALOAD = 25;
	
	/** Load byte from the first local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,i
	 */
	public static final byte ILOAD_0 = 26;
	
	/** Load byte from the second local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,i
	 */
	public static final byte ILOAD_1 = 27;
	
	/** Load byte from the thrid local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,i
	 */
	public static final byte ILOAD_2 = 28;
	
	/** Load byte from the fourth local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,i
	 */
	public static final byte ILOAD_3 = 29;
	
	/** Load long from the first local variable.  Both <n> and 
	 *  <n> + 1 must be indices into the local variable array 
	 *  of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,l
	 */
	public static final byte LLOAD_0 = 30;

	/** Load long from the second local variable.  Both <n> and 
	 *  <n> + 1 must be indices into the local variable array 
	 *  of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,l
	 */
	public static final byte LLOAD_1 = 31;

	/** Load long from the second third variable.  Both <n> and 
	 *  <n> + 1 must be indices into the local variable array 
	 *  of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,l
	 */
	public static final byte LLOAD_2 = 32;

	/** Load long from the fourth local variable.  Both <n> and 
	 *  <n> + 1 must be indices into the local variable array 
	 *  of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,l
	 */
	public static final byte LLOAD_3 = 33;

	/** Load float from the first local variable.  
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,f
	 */
	public static final byte FLOAD_0 = 34;

	/** Load float from the second local variable.  
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,f
	 */
	public static final byte FLOAD_1 = 35;

	/** Load float from the third local variable.  
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,f
	 */
	public static final byte FLOAD_2 = 36;

	/** Load float from the fourth local variable.  
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,f
	 */
	public static final byte FLOAD_3 = 37;
	
	/** Load double from the first local variable.  Both 
	 *  <n> and <n> + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,d
	 */
	public static final byte DLOAD_0 = 38;

	/** Load double from the second local variable.  Both 
	 *  <n> and <n> + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,d
	 */
	public static final byte DLOAD_1 = 39;

	/** Load double from the third local variable.  Both 
	 *  <n> and <n> + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,d
	 */
	public static final byte DLOAD_2 = 40;

	/** Load double from the fourth local variable.  Both 
	 *  <n> and <n> + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). 
	 * 
	 *  ARGS: 
	 *  STACK: ... => ...,d
	 */
	public static final byte DLOAD_3 = 41;
	
	/** Load reference from first local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,objectref
	 */
	public static final byte ALOAD_0 = 42;
	
	/** Load reference from second local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,objectref
	 */
	public static final byte ALOAD_1 = 43;
	
	/** Load reference from third local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,objectref
	 */
	public static final byte ALOAD_2 = 44;
	
	/** Load reference from fourth local variable.
	 * 
	 *  ARGS:
	 *  STACK: ... => ...,objectref
	 */
	public static final byte ALOAD_3 = 45;
	
	/** Load byte from array.
	 * 
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte IALOAD = 46;
	
	/** Load long from array.
	 * 
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte LALOAD = 47;
	
	/** Load float from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte FALOAD = 48;
	
	/** Load double from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte DALOAD = 49;
	
	/** Load objectref from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,objectref
	 */
	public static final byte AALOAD = 50;
	
	/** Load byte from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte BALOAD = 51;
	
	/** Load char from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte CALOAD = 52;
	
	/** Load short from array.
	 *  ARGS:
	 *  STACK: ...,arrayref,index => ...,value
	 */
	public static final byte SALOAD = 53;
	
	/** Store byte into local variable
	 *  ARGS: index
	 *  STACK: ...,value => ...
	 */
	public static final byte ISTORE = 54;
	
	/** Store long into local variable.  Both index and 
	 *  index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: index
	 *  STACK: ...,value => ...
	 */
	public static final byte LSTORE = 55;
	
	/** Store float into local variable
	 *  ARGS: index
	 *  STACK: ...,value => ...
	 */
	public static final byte FSTORE = 56;
	
	/** Store double into local variable.  Both index and 
	 *  index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: index
	 *  STACK: ...,value => ...
	 */
	public static final byte DSTORE = 57;
	
	/** Store objectref into local variable
	 *  ARGS: index
	 *  STACK: ...,objectref => ...
	 */
	public static final byte ASTORE = 58;

	/** Store byte into the first local variable
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte ISTORE_0 = 59;

	/** Store byte into the second local variable
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte ISTORE_1 = 60;

	/** Store byte into the thrid local variable
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte ISTORE_2 = 61;

	/** Store byte into the fourth local variable
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte ISTORE_3 = 62;
	
	/** Store long into the first local variable.  Both index 
	 *  and index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte LSTORE_0 = 63;
	
	/** Store long into the second local variable.  Both index 
	 *  and index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte LSTORE_1 = 64;
	
	/** Store long into the third local variable.  Both index 
	 *  and index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte LSTORE_2 = 65;
	
	/** Store long into the fourth local variable.  Both index 
	 *  and index + 1 must be indices into the local variable 
	 *  array of the current frame (§3.6). The local 
	 *  variables at index and index + 1 are set to value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte LSTORE_3 = 66;
	
	/** Store float into the first local variable.
	 *  ARGS:
	 *  STACK: ...,value => ...
	 */
	public static final byte FSTORE_0 = 67;
	
	/** Store float into the second local variable.
	 *  ARGS:
	 *  STACK: ...,value => ...
	 */
	public static final byte FSTORE_1 = 68;
	
	/** Store float into the third local variable.
	 *  ARGS:
	 *  STACK: ...,value => ...
	 */
	public static final byte FSTORE_2 = 69;
	
	/** Store float into the fourth local variable.
	 *  ARGS:
	 *  STACK: ...,value => ...
	 */
	public static final byte FSTORE_3 = 70;
	
	/** Store double into the first local variable.  Both 
	 *  index and index + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). The 
	 *  local variables at index and index + 1 are set to
	 *  value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte DSTORE_0 = 71;
	
	/** Store double into the second local variable.  Both 
	 *  index and index + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). The 
	 *  local variables at index and index + 1 are set to
	 *  value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte DSTORE_1 = 72;
	
	/** Store double into the third local variable.  Both 
	 *  index and index + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). The 
	 *  local variables at index and index + 1 are set to
	 *  value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte DSTORE_2 = 73;
	
	/** Store double into the fourth local variable.  Both 
	 *  index and index + 1 must be indices into the local 
	 *  variable array of the current frame (§3.6). The 
	 *  local variables at index and index + 1 are set to
	 *  value. 
	 *  ARGS: 
	 *  STACK: ...,value => ...
	 */
	public static final byte DSTORE_3 = 74;
	
	/** Store objectref into the first local variable.
	 *  ARGS: 
	 *  STACK: ...,objectref => ...
	 */
	public static final byte ASTORE_0 = 75;
		
	/** Store objectref into the second local variable.
	 *  ARGS: 
	 *  STACK: ...,objectref => ...
	 */
	public static final byte ASTORE_1 = 76;
	
	/** Store objectref into the third local variable.
	 *  ARGS: 
	 *  STACK: ...,objectref => ...
	 */
	public static final byte ASTORE_2 = 77;
	
	/** Store objectref into the fourth local variable.
	 *  ARGS: 
	 *  STACK: ...,objectref => ...
	 */
	public static final byte ASTORE_3 = 78;
	
	/** Store into byte array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte IASTORE = 79;
	
	/** Store into long array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte LASTORE = 80;
	
	/** Store into float array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte FASTORE = 81;
	
	/** Store into double array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte DASTORE = 82;
	
	/** Store into reference array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte AASTORE = 83;
	
	/** Store into byte array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte BASTORE = 84;
	
	/** Store into char array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte CASTORE = 85;
	
	/** Store into short array.  
	 *  ARGS:
	 *  STACK: ...,arrayref,index,value => ...
	 */
	public static final byte SASTORE = 86;
	
	/** Pop the top operand stack value.
	 *  ARGS:
	 *  STACK: ...,value => ...
	 */
	public static final byte POP = 87;
	
	/** Pop two ints off the stack ( 2 ints or 1 long )
	 *  ARGS:
	 *  STACK: ..., value2, value1  ...
	 */
	public static final byte POP2 = 88;
	
	/** DUP
	 *  ARGS:
	 *  STACK: ...,value => ...,value,value
	 */
	public static final byte DUP = 89;
	
	/** Duplicate the top operand stack value and insert 
	 *  two values down.
	 *  ARGS:
	 *  STACK: ..., value2, value1  => ..., value1, value2, value1
	 */
	public static final byte DUP_X1 = 90;
	
	/** Duplicate the top operand stack value and insert 
	 *  three (int) values down.
	 *  ARGS:
	 *  STACK: ...,value3,value2,value1  => ..., value1,value3,value2,value1
	 */
	public static final byte DUP_X2 = 91;
	
	/** Duplicate the top two (int) operand stack values.
	 *  ARGS:
	 *  STACK: ...,value2,value1 => ...
	 */
	public static final byte DUP2 = 92;
	
	/** Duplicate the two (int) operand stack values 
	 *  and insert three (int) values down
	 *  ARGS:
	 *  STACK: ...,value3,value2,value1 => ...,value2,value1,value3,value2,value1
	 */
	public static final byte DUP2_X1 = 93;
	
	/** Duplicate the top two (int) operand stack values and insert four
	 *  (int) values down.
	 *  ARGS:
	 *  STACK: ...,value4,value3,value2,value1 => ...,value2,value1,value4,value3,value2,value1
	 */
	public static final byte DUP2_X2 = 94;
	
	/** Swap the top two operand stack values.
	 *  ARGS:
	 *  STACK: ..., value2, value1 => ..., value1, value2
	 */
	public static final byte SWAP = 95;
	
	/** Add ints
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte IADD = 96;
	
	/** Add long
	 *  ARGS:
	 *  STACK: ..., value1, value2  ..., result
	 */
	public static final byte LADD = 97;
	
	/** Add float
	 *  ARGS:
	 *  STACK: ..., value1, value2  ..., result
	 */
	public static final byte FADD = 98;
	
	/** Add double
	 *  ARGS:
	 *  STACK: ..., value1, value2  ..., result
	 */
	public static final byte DADD = 99;
	
	/** Subtract ints
	 *  ARGS:
	 *  STACK: ..., value1, value2  ..., value1 - value2
	 */
	public static final byte ISUB = 100;
	
	/** Subtract longs. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 - value2
	 */
	public static final byte LSUB = 101;
	
	/** Subtract floats. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 - value2
	 */
	public static final byte FSUB = 102;
	
	/** Subtract doubles. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 - value2
	 */
	public static final byte DSUB = 103;
	
	/** Mult ints. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte IMUL = 104;
	
	/** Mult longs. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte LMUL = 105;
	
	/** Mult ints. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte FMUL = 106;
	
	/** Mult doubles. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte DMUL = 107;
	
	/** Divide ints. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 / value2
	 */
	public static final byte IDIV = 108;
	
	/** Divide longs. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 / value2
	 */
	public static final byte LDIV = 109;
	
	/** Divide floats. 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 / value2
	 */
	public static final byte FDIV = 110;
	
	/** Divide doubles. 
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., value1 / value2
	 */
	public static final byte DDIV = 111;
	
	/** Remainder int. The byte result is 
	 *  value1 - (value1 / value2) * value2
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte IREM = 112;
	
	/** Remainder long. The byte result is 
	 *  value1 - (value1 / value2) * value2
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 => ..., result
	 */
	public static final byte LREM = 113;
	
	/** The floating-pobyte remainder result from a dividend 
	 *  value1' and a divisor value2' is defined by the mathematical 
	 *  relation result = value1' - (value2' * q), where q is an 
	 *  integer that is negative only if value1' / value2' is negative 
	 *  and positive only if value1' / value2' is positive, and whose 
	 *  magnitude is as large as possible without exceeding the 
	 *  magnitude of the true mathematical quotient of value1' and 
	 *  value2'. 
	 * 
	 *  ARGS:
	 *  STACK: ...,value1,value2 => ...,result
	 */
	public static final byte FREM = 114;
	
	/** The floating-pobyte remainder result from a dividend 
	 *  value1' and a divisor value2' is defined by the mathematical 
	 *  relation result = value1' - (value2' * q), where q is an 
	 *  integer that is negative only if value1' / value2' is negative 
	 *  and positive only if value1' / value2' is positive, and whose 
	 *  magnitude is as large as possible without exceeding the 
	 *  magnitude of the true mathematical quotient of value1' and 
	 *  value2'. 
	 * 
	 *  ARGS:
	 *  STACK: ...,value1,value2 => ...,result
	 */
	public static final byte DREM = 115;
	
	/** Negate int
	 * 
	 *  ARGS:
	 *  STACK: ...,value1 => ...,-value1
	 */
	public static final byte INEG = 116;
	
	/** Negate long
	 * 
	 *  ARGS:
	 *  STACK: ...,value1 => ...,-value1
	 */
	public static final byte LNEG = 117;
	
	/** Negate float
	 * 
	 *  ARGS:
	 *  STACK: ...,value1 => ...,-value1
	 */
	public static final byte FNEG = 118;
	
	/** Negate double
	 * 
	 *  ARGS:
	 *  STACK: ...,value1 => ...,-value1
	 */
	public static final byte DNEG = 119;
	
	/** Shift left int.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte ISHL = 120;
	
	/** Shift left long.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte LSHL = 121;
	
	/** Shift right int.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte ISHR = 122;
	
	/** Shift right long.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte LSHR = 123;
	
	/** Logical shift right int.  The values are popped from 
	 *  the operand stack. An byte result is calculated by shifting 
	 *  value1 right by s bit positions, with zero extension, where 
	 *  s is the value of the low 5 bits of value2. The result is 
	 *  pushed onto the operand stack.  If value1 is positive and s 
	 *  is value2 & 0x1f, the result is the same as that of 
	 *  value1 >> s; if value1 is negative, the result is equal to 
	 *  the value of the expression (value1 >> s) + (2 << ~s). The 
	 *  addition of the (2 << ~s) term cancels out the propagated sign 
	 *  bit. The shift distance actually used is always in the range 
	 *  0 to 31, inclusive.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result
	 */
	public static final byte IUSHR = 124;
	
	/** Logical shift right long.  The value1 must be of type long, 
	 *  and value2 must be of type int. The values are popped from 
	 *  the operand stack. A long result is calculated by shifting 
	 *  value1 right logically (with zero extension) by the amount 
	 *  indicated by the low 6 bits of value2. The result is pushed 
	 *  onto the operand stack.  If value1 is positive and s is 
	 *  value2 & 0x3f, the result is the same as that of value1 >> s; 
	 *  if value1 is negative, the result is equal to the value of 
	 *  the expression (value1 >> s) + (2L << ~s). The addition of 
	 *  the (2L << ~s) term cancels out the propagated sign bit. The 
	 *  shift distance actually used is always in the range 0 to 63, 
	 *  inclusive.

	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte LUSHR = 125;
	
	/** Boolean AND int.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte IAND = 126;
	
	/** Boolean AND long.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte LAND = 127;
	
	/** Boolean OR int.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte IOR = (byte)128;
	
	/** Boolean OR long.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte LOR = (byte)129;
	
	/** Boolean XOR int.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte IXOR = (byte)130;
	
	/** Boolean XOR long.
	 * 
	 *  ARGS:
	 *  STACK: ..., value1, value2 =>  ..., result  
	 */
	public static final byte LXOR = (byte)131;
	
	/** Increment local variable by constant.  The index is 
	 *  an unsigned byte that must be an index into the 
	 *  local variable array of the current frame (§3.6). 
	 *  The const is an immediate signed byte. The local 
	 *  variable at index must contain an int. The value const 
	 *  is first sign-extended to an int, and then the local 
	 *  variable at index is incremented by that amount.
	 * 
	 *  ARGS: index, constant
	 *  STACK: ... => ...
	 */
	public static final byte IINC = (byte)132;
	
	/** Convert byte to long.  It is popped from the operand 
	 *  stack and sign-extended to a long result.
	 * 
	 *  ARGS:
	 *  STACK: ...,value => ...,result
	 */
	public static final byte I2L = (byte)133;
	
	/** Convert byte to float.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte I2F = (byte)134;
	
	/** Convert byte to double
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte I2D = (byte)135;
	
	/** Long to int.  The value on the top of the operand 
	 *  stack must be of type long. It is popped from the 
	 *  operand stack and converted to an byte result by taking 
	 *  the low-order 32 bits of the long value and discarding 
	 *  the high-order 32 bits. The result is pushed onto the 
	 *  operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte L2I = (byte)136;
	
	/** Long to float
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte L2F = (byte)137;
	
	/** Long to double
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte L2D = (byte)138;
	
	/** float to int
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte F2I = (byte)139;
	
	/** float to long
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte F2L = (byte)140;
	
	/** float to double
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte F2D = (byte)141;
	
	/** double to int
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte D2I = (byte)142;
	
	/** double to long
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte D2L = (byte)143;
	
	/** double to float
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte D2F = (byte)144;
	
	/** byte to byte
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte I2B = (byte)145;
	
	/** byte to char
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte I2C = (byte)146;
	
	/** byte to short
	 * 
	 *  ARGS:
	 *  STACK:  ...,value => ...,result
	 */
	public static final byte I2S = (byte)147;
	
	/** Compare long.  If value1 is greater than value2, the byte 
	 *  value 1 is pushed onto the operand stack. If value1 is 
	 *  equal to value2, the byte value 0 is pushed onto the operand 
	 *  stack. If value1 is less than value2, the byte value -1 is 
	 *  pushed onto the operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value1,value2 => ...,result
	 */
	public static final byte LCMP = (byte)148;
	
	/**  Compare float.  If value1' is greater than value2', the 
	 *   byte value 1 is pushed onto the operand stack. Otherwise, 
	 *   if value1' is equal to value2', the byte value 0 is pushed 
	 *   onto the operand stack.  Otherwise, if value1' is less 
	 *   than value2', the byte value -1 is pushed onto the operand 
	 *   stack.  For NaN the fcmpl instruction pushes the byte value 
	 *   -1 onto the operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value1,value2 => ...,result
	 */
	public static final byte FCMPL = (byte)149;
	
	/**  Compare float.  If value1' is greater than value2', the 
	 *   byte value 1 is pushed onto the operand stack. Otherwise, 
	 *   if value1' is equal to value2', the byte value 0 is pushed 
	 *   onto the operand stack.  Otherwise, if value1' is less 
	 *   than value2', the byte value -1 is pushed onto the operand 
	 *   stack.  For NaN hhe fcmpg instruction pushes the byte value 1 
	 *   onto the operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value1,value2 => ...,result
	 */
	public static final byte FCMPG = (byte)150;
	/**  Compare double.  If value1' is greater than value2', the 
	 *   byte value 1 is pushed onto the operand stack. Otherwise, 
	 *   if value1' is equal to value2', the byte value 0 is pushed 
	 *   onto the operand stack.  Otherwise, if value1' is less 
	 *   than value2', the byte value -1 is pushed onto the operand 
	 *   stack.  For NaN hhe dcmpl instruction pushes the byte value -1 
	 *   onto the operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value1,value2 => ...,result
	 */
	public static final byte DCMPL = (byte)151;
	/**  Compare double.  If value1' is greater than value2', the 
	 *   byte value 1 is pushed onto the operand stack. Otherwise, 
	 *   if value1' is equal to value2', the byte value 0 is pushed 
	 *   onto the operand stack.  Otherwise, if value1' is less 
	 *   than value2', the byte value -1 is pushed onto the operand 
	 *   stack.  For NaN hhe fcmpg instruction pushes the byte value 1 
	 *   onto the operand stack.
	 * 
	 *  ARGS:
	 *  STACK:  ...,value1,value2 => ...,result
	 */
	public static final byte DCMPG = (byte)152;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFEQ = (byte)153;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFNE = (byte)154;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFLT = (byte)155;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFGE = (byte)156;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFGT = (byte)157;
	
	/** Branch if byte comparison with zero succeeds.  
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value => ...
	 */
	public static final byte IFLE = (byte)158;
	
	/** Branch if byte comparison succeeds.  (value1 = value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPEQ = (byte)159;
	
	/** Branch if byte comparison succeeds.  (value1 != value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPNE = (byte)160;
	
	/** Branch if byte comparison succeeds.  (value1 < value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPLT = (byte)161;
	
	/** Branch if byte comparison succeeds.  (value1 >= value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPGE = (byte)162;
	
	/** Branch if byte comparison succeeds.  (value1 > value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPGT = (byte)163;
	
	/** Branch if byte comparison succeeds.  (value1 <= value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ICMPLE = (byte)164;
	
	/** Branch if reference comparison succeeds.  (value1 == value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ACMPEQ = (byte)165;
	
	/** Branch if reference comparison succeeds.  (value1 != value2)
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ...,value1,value2 => ...
	 */
	public static final byte IF_ACMPNE = (byte)166;
	
	/** Branch.
	 *  PC = (branchbyte1 << 8) | branchbyte2
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ... => ...
	 */
	public static final byte GOTO = (byte)167;
	
	/** The address of the opcode of the instruction immediately 
	 *  following this jsr instruction is pushed onto the operand 
	 *  stack as a value of type returnAddress. The unsigned 
	 *  branchbyte1 and branchbyte2 are used to construct a 
	 *  signed 16-bit offset, where the offset is 
	 *  (branchbyte1 << 8) | branchbyte2. Execution proceeds at 
	 *  that offset from the address of this jsr instruction. The 
	 *  target address must be that of an opcode of an instruction 
	 *  within the method that contains this jsr instruction.
	 * 
	 *  ARGS:  branchbyte1, branchbyte2
	 *  STACK:  ... => ...,address
	 */
	public static final byte JSR = (byte)168;
	
	/** Return from subroutine.  The local variable at index in 
	 *  the current frame (§3.6) must contain a value of type 
	 *  returnAddress. The contents of the local variable are 
	 *  written into the Java virtual machine's pc register, and 
	 *  execution continues there.
	 * 
	 *  ARGS: index
	 *  STACK: ... => ...
	 */
	public static final byte RET = (byte)169;
	
	/** Access jump table by index and jump.  A tableswitch is a 
	 *  variable-length instruction. Immediately after the tableswitch 
	 *  opcode, between 0 and 3 null bytes (zeroed bytes, not the null 
	 *  object) are inserted as padding. The number of null bytes is 
	 *  chosen so that the following byte begins at an address that is 
	 *  a multiple of 4 bytes from the start of the current method (the 
	 *  opcode of its first instruction). Immediately after the padding 
	 *  follow bytes constituting three signed 32-bit values: default, 
	 *  low, and high. Immediately following those bytes are bytes 
	 *  constituting a series of high - low + 1 signed 32-bit offsets. 
	 *  The value low must be less than or equal to high. The 
	 *  high - low + 1 signed 32-bit offsets are treated as a 0-based 
	 *  jump table. Each of these signed 32-bit values is constructed 
	 *  as (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4.  The 
	 *  index must be of type byte and is popped from the operand stack. 
	 *  If index is less than low or index is greater than high, then a 
	 *  target address is calculated by adding default to the address of 
	 *  the opcode of this tableswitch instruction. Otherwise, the offset 
	 *  at position index - low of the jump table is extracted. The target 
	 *  address is calculated by adding that offset to the address of the 
	 *  opcode of this tableswitch instruction. Execution then continues at 
	 *  the target address.  The target address that can be calculated from 
	 *  each jump table offset, as well as the ones that can be calculated 
	 *  from default, must be the address of an opcode of an instruction 
	 *  within the method that contains this tableswitch instruction.
	 * 
	 *  ARGS: <0-3 byte pad\>   
	 *		defaultbyte1,defaultbyte2,defaultbyte3,defaultbyte4,
	 *		lowbyte1,lowbyte2,lowbyte3,lowbyte4,
	 *		highbyte1, highbyte2, highbyte3, highbyte4,
	 *		jump offsets...
	 * 
	 *  STACK: ...,index => ...  
	 */
	public static final byte TABLESWITCH = (byte)170;
	
	/** Access jump table by key match and jump.  A lookupswitch is a 
	 *  variable-length instruction. Immediately after the lookupswitch 
	 *  opcode, between zero and three null bytes (zeroed bytes, not 
	 *  the null object) are inserted as padding. The number of null 
	 *  bytes is chosen so that the defaultbyte1 begins at an address 
	 *  that is a multiple of four bytes from the start of the current 
	 *  method (the opcode of its first instruction). Immediately after 
	 *  the padding follow a series of signed 32-bit values: default, 
	 *  npairs, and then npairs pairs of signed 32-bit values. The 
	 *  npairs must be greater than or equal to 0. Each of the npairs 
	 *  pairs consists of an byte match and a signed 32-bit offset. Each 
	 *  of these signed 32-bit values is constructed from four unsigned 
	 *  bytes as (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4.  
	 *  The table match-offset pairs of the lookupswitch instruction 
	 *  must be sorted in increasing numerical order by match.  The key 
	 *  must be of type byte and is popped from the operand stack. The 
	 *  key is compared against the match values. If it is equal to one 
	 *  of them, then a target address is calculated by adding the 
	 *  corresponding offset to the address of the opcode of this 
	 *  lookupswitch instruction. If the key does not match any of the 
	 *  match values, the target address is calculated by adding default 
	 *  to the address of the opcode of this lookupswitch instruction. 
	 *  Execution then continues at the target address.  The target 
	 *  address that can be calculated from the offset of each 
	 *  match-offset pair, as well as the one calculated from default, 
	 *  must be the address of an opcode of an instruction within the 
	 *  method that contains this lookupswitch instruction.
	 * 
	 *  ARGS:  lookupswitch   
	 *		<0-3 byte pad\>
	 *		defaultbyte1,defaultbyte2,defaultbyte3,defaultbyte4,
	 *		npairs1, npairs2, npairs3, npairs4,
	 *		match-offset pairs...  
	 * 
	 *  STACK:  ...,key => ...
	 */
	public static final byte LOOKUPSWITCH = (byte)171;
	
	/** Return byte from method.  The current method must have return 
	 *  type boolean, byte, short, char, or int. The value must be of 
	 *  type int. If the current method is a synchronized method, the 
	 *  monitor acquired or reentered on invocation of the method is 
	 *  released or exited (respectively) as if by execution of a 
	 *  monitorexit instruction. If no exception is thrown, value is 
	 *  popped from the operand stack of the current frame (§3.6) and 
	 *  pushed onto the operand stack of the frame of the invoker. 
	 *  Any other values on the operand stack of the current method 
	 *  are discarded.
	 *  
	 *  The interpreter then returns control to the invoker of the 
	 *  method, reinstating the frame of the invoker.
	 *  
	 * Runtime Exceptions
	 * 
	 *  If the current method is a synchronized method and the current 
	 *  thread is not the owner of the monitor acquired or reentered 
	 *  on invocation of the method, ireturn throws an 
	 *  IllegalMonitorStateException. This can happen, for example, if 
	 *  a synchronized method contains a monitorexit instruction, but 
	 *  no monitorenter instruction, on the object on which the method 
	 *  is synchronized.
	 * 
	 *  Otherwise, if the virtual machine implementation enforces the 
	 *  rules on structured use of locks described in Section 8.13 and 
	 *  if the first of those rules is violated during invocation of the 
	 *  current method, then ireturn throws an 
	 *  IllegalMonitorStateException.
	 * 
	 *  ARGS:
	 *  STACK:  ..., value => [empty]
	 */
	public static final byte IRETURN = (byte)172;
	
	/** Return long from method.
	 * 
	 *  ARGS:
	 *  STACK:  ..., value => [empty]
	 */
	public static final byte LRETURN = (byte)173;
	
	/** Return float from method.
	 * 
	 *  ARGS:
	 *  STACK:  ..., value => [empty]
	 */
	public static final byte FRETURN = (byte)174;
	
	/** Return double from method.
	 * 
	 *  ARGS:
	 *  STACK:  ..., value => [empty]
	 */
	public static final byte DRETURN = (byte)175;
	
	/** Return reference from method.
	 * 
	 *  ARGS:
	 *  STACK:  ..., value => [empty]
	 */
	public static final byte ARETURN = (byte)176;
	
	/** Return from void method.  The current method must have return 
	 *  type void. If the current method is a synchronized method, the 
	 *  monitor acquired or reentered on invocation of the method is 
	 *  released or exited (respectively) as if by execution of a 
	 *  monitorexit instruction. If no exception is thrown, any values 
	 *  on the operand stack of the current frame (§3.6) are discarded.
	 *  The interpreter then returns control to the invoker of the 
	 *  method, reinstating the frame of the invoker.
	 * 
	 *  ARGS:
	 *  STACK:  ... => [empty]
	 */
	public static final byte RETURN = (byte)177;
	
	/** Get static field from class.  The value of the index is 
	 *  (indexbyte1 << 8) | indexbyte2. After linking, this is
	 *  an index to a ClassDef field.
	 *  
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK:  ... => ..., value 
	 */
	public static final byte GETSTATIC = (byte)178;
	
	/** Store a static field in a class.  The value of the index 
	 *  is (indexbyte1 << 8) | indexbyte2. After linking, this is
	 *  an index to a ClassDef field.
	 *  
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK:  value,... => ...
	 */
	public static final byte PUTSTATIC = (byte)179;
	
	/** Get a field from a class.  The value of the index is 
	 *  (indexbyte1 << 8) | indexbyte2. After linking, this is
	 *  an index to a ReferenceType field.
	 *  
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK:  ..., objectref => ..., value 
	 */
	public static final byte GETFIELD = (byte)180;
	
	/** Store a field in a class.  The value of the index is 
	 *  (indexbyte1 << 8) | indexbyte2. After linking, this is
	 *  an index to a ReferenceType field.
	 *  
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK:  ..., objectref, value => ...
	 */
	public static final byte PUTFIELD = (byte)181;
	
	/** Invoke a method on a class instance.  If the method is 
	 *  synchronized, the monitor associated with objectref is 
	 *  acquired or reentered.  If the method is not native, the 
	 *  nargs argument values and objectref are popped from the 
	 *  operand stack. A new frame is created on the Java virtual 
	 *  machine stack for the method being invoked. The objectref 
	 *  and the argument values are consecutively made the values 
	 *  of local variables of the new frame, with objectref in 
	 *  local variable 0, arg1 in local variable 1 (or, if arg1 
	 *  is of type long or double, in local variables 1 and 2), 
	 *  and so on. Any argument value that is of a floating-pobyte 
	 *  type undergoes value set conversion (§3.8.3) prior to being 
	 *  stored in a local variable. The new frame is then made 
	 *  current, and the Java virtual machine pc is set to the 
	 *  opcode of the first instruction of the method to be invoked. 
	 *  Execution continues with the first instruction of the method.
	 * 
	 *  ARGS:  indexbyte1, indexbyte2
	 *  STACK:  ..., objectref, [arg1, [arg2 ...]] => ...
	 */
	public static final byte INVOKEVIRTUAL = (byte)182;
	
	/** Invoke instance method; special handling for superclass, 
	 *  private, and instance initialization method invocations.
	 * 
	 *   ARGS:  indexbyte1, indexbyte2
	 *   STACK: ..., objectref, [arg1, [arg2 ...]] => ...
	 */
	public static final byte INVOKESPECIAL = (byte)183;
	
	/** Invoke a class (static) method.
	 * 
	 *   ARGS:  indexbyte1, indexbyte2
	 *   STACK: ..., [arg1, [arg2 ...]] => ...
	 */
	public static final byte INVOKESTATIC = (byte)184;
	
	/** Invoke interface method.  The unsigned indexbyte1 and 
	 *  indexbyte2 are used to construct an index into the runtime 
	 *  constant pool of the current class (§3.6), where the value 
	 *  of the index is (indexbyte1 << 8) | indexbyte2. The runtime 
	 *  constant pool item at that index must be a symbolic reference 
	 *  to an interface method (§5.1), which gives the name and 
	 *  descriptor (§4.3.3) of the interface method as well as a 
	 *  symbolic reference to the interface in which the interface 
	 *  method is to be found. The named interface method is resolved 
	 *  (§5.4.3.4). The interface method must not be an instance 
	 *  initialization method (§3.9) or the class or interface 
	 *  initialization method (§3.9).  The count operand is an unsigned 
	 *  byte that must not be zero. The objectref must be of type 
	 *  reference and must be followed on the operand stack by nargs 
	 *  argument values, where the number, type, and order of the 
	 *  values must be consistent with the descriptor of the resolved 
	 *  interface method. The value of the fourth operand byte must 
	 *  always be zero.
	 * 
	 *  ARGS: indexbyte1,indexbyte2,count,0  
	 *  STACK:  ..., objectref, [arg1, [arg2 ...]] => ...
	 */
	public static final byte INVOKEINTERFACE = (byte)185;
	
	/** Create new object
	 * 
	 *   ARGS:  indexbyte1, indexbyte2
	 *   STACK:  ... => ...,objectref
	 */
	public static final byte NEW = (byte)187;
	
	/** Create new array.  The count represents the number of 
	 *  elements in the array to be created. The atype is a code 
	 *  that indicates the type of array to create. It must take one 
	 *  of the following values:
	 *		Array Type  atype  
	 *		T_BOOLEAN	4 
	 *		T_CHAR		5  
	 *		T_FLOAT		6  
	 *		T_DOUBLE	7  
	 *		T_BYTE		8  
	 *		T_SHORT		9  
	 *		T_INT		10	
	 *		T_LONG		11  
	 * 
	 *   ARGS:  atype
	 *   STACK:  ...,count => ...,arrayref
	 */
	public static final byte NEWARRAY = (byte)188;

	/** Create new reference array.  The count represents the number of 
	 *  elements in the array to be created.
	 * 
	 *   ARGS:  index1byte,indexbyte2
	 *   STACK:  ...,count => ...,arrayref
	 */
	public static final byte ANEWARRAY = (byte)189;
	
	/**  Get length of array.
	 * 
	 *    ARGS:
	 *    STACK:  ..., arrayref => ..., length
	 */
	public static final byte ARRAYLENGTH = (byte)190;
	
	/** Throw exception or error.  The objectref must be of type 
	 *  reference and must refer to an object that is an instance 
	 *  of class Throwable or of a subclass of Throwable. It is 
	 *  popped from the operand stack. The objectref is then 
	 *  thrown by searching the current method (§3.6) for the ]
	 *  first exception handler that matches the class of objectref, 
	 *  as given by the algorithm in §3.10.  If an exception handler 
	 *  that matches objectref is found, it contains the location 
	 *  of the code intended to handle this exception. The pc 
	 *  register is reset to that location, the operand stack of 
	 *  the current frame is cleared, objectref is pushed back onto 
	 *  the operand stack, and execution continues. If no matching 
	 *  exception handler is found in the current frame, that frame 
	 *  is popped. If the current frame represents an invocation of 
	 *  a synchronized method, the monitor acquired or reentered on 
	 *  invocation of the method is released or exited (respectively) 
	 *  as if by execution of a monitorexit instruction. Finally, the 
	 *  frame of its invoker is reinstated, if such a frame exists, 
	 *  and the objectref is rethrown. If no such frame exists, the 
	 *  current thread exits.
	 * 
	 *    ARGS:
	 *    STACK:  ...,objectref => objectref
	 */
	public static final byte ATHROW = (byte)191;
	
	/** Check whether object is of given type.  The unsigned indexbyte1 
	 *  and indexbyte2 are used to construct an index into the runtime 
	 *  constant pool of the current class (§3.6), where the value of the 
	 *  index is (indexbyte1 << 8) | indexbyte2. The runtime constant 
	 *  pool item at the index must be a symbolic reference to a class, 
	 *  array, or interface type. The named class, array, or interface 
	 *  type is resolved (§5.4.3.1).  If objectref is null or can be cast 
	 *  to the resolved class, array, or interface type, the operand 
	 *  stack is unchanged; otherwise, the checkcast instruction throws 
	 *  a ClassCastException.
	 * 
	 *    ARGS:  indexbyte1,indexbyte2
	 *    STACK:  ...,objectref => ...,objectref
	 */
	public static final byte CHECKCAST = (byte)192;
	
	/** Determine if object is of given type.  The unsigned indexbyte1 
	 *  and indexbyte2 are used to construct an index into the runtime 
	 *  constant pool of the current class (§3.6), where the value of the 
	 *  index is (indexbyte1 << 8) | indexbyte2. The runtime constant 
	 *  pool item at the index must be a symbolic reference to a class, 
	 *  array, or interface type. The named class, array, or interface 
	 *  type is resolved (§5.4.3.1).  If objectref is not null and is an 
	 *  instance of the resolved class or array or implements the resolved 
	 *  interface, the instanceof instruction pushes an byte result of 1 as 
	 *  an byte on the operand stack. Otherwise, it pushes an byte result of 0.
	 * 
	 *   ARGS:  indexbyte1,indexbyte2
	 *   STACK:  ..., objectref => ..., result
	 */
	public static final byte INSTANCEOF = (byte)193;
	
	/** Enter monitor for object 
	 * 
	 *   ARGS:  
	 *   STACK: ..., objectref => ...  
	 */
	public static final byte MONITORENTER = (byte)194;
	
	/** Exit monitor for object 
	 * 
	 *   ARGS:  
	 *   STACK: ..., objectref => ...  
	 */
	public static final byte MONITOREXIT = (byte)195;
	
	/** In either case, the wide opcode itself is followed in the 
	 *  compiled code by the opcode of the instruction wide modifies. 
	 *  In either form, two unsigned bytes indexbyte1 and indexbyte2 
	 *  follow the modified opcode and are assembled into a 16-bit 
	 *  unsigned index to a local variable in the current frame (§3.6).
	 *  <opcode> is one of iload, fload, aload, lload, dload, istore, 
	 *  fstore, astore, lstore, dstore, or ret.
	 * 
	 *   ARGS: <OPCODE>,indexbyte1,indexbyte2 
	 *			OR
	 *			IINC,indexbyte1,indexbyte2,constbyte1,constbyte2
	 * 
	 *   STACK: See OPCODE
	 */
	public static final byte WIDE = (byte)196;
	
	/**  Create new multidimensional array.
	 * 
	 *   ARGS:  indexbyte1,indexbyte2,dimensions
	 *   STACK:  ..., count1, [count2, ...] => ..., arrayref
	 */
	public static final byte MULTIANEWARRAY = (byte)197;
	
	/**  Branch if reference is null.
	 * 
	 *   ARGS: branchbyte1, branchbyte2
	 *   STACK:  ...,value => ...
	 */
	public static final byte IFNULL = (byte)198;
	
	/**  Branch if reference is not null.
	 * 
	 *   ARGS:  branchbyte1,branchbyte2
	 *   STACK:  ...,value => ...
	 */
	public static final byte IFNONNULL = (byte)199;
	
	/**  Branch always, wide index
	 * 
	 *   ARGS:  branchbyte1,branchbyte2,branchbyte3,branchbyte3
	 *   STACK:
	 */
	public static final byte GOTO_W = (byte)200;
		
	/** The address of the opcode of the instruction immediately 
	 *  following this jsr instruction is pushed onto the operand 
	 *  stack as a value of type returnAddress. The unsigned 
	 *  branchbyte1 and branchbyte2 are used to construct a 
	 *  signed 16-bit offset, where the offset is 
	 *  (branchbyte1 << 8) | branchbyte2. Execution proceeds at 
	 *  that offset from the address of this jsr instruction. The 
	 *  target address must be that of an opcode of an instruction 
	 *  within the method that contains this jsr instruction.
	 * 
	 *  ARGS:  branchbyte1,branchbyte2,branchbyte3,branchbyte3
	 *  STACK:  ... => ...,address
	 */
	public static final byte JSR_W = (byte)201;
	
	/** ???
	 */
	public static final byte BREAKPOINT = (byte)202;
}
