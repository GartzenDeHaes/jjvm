/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.Instructions;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.ReferenceType;
import maximum.jvm.Type;

import maximum.jvm.builtintypes.ArrayInstance;
import maximum.jvm.builtintypes.ClassInstance;

/**
 *  Dynamic frame for application defined method calls.
 */
public class DynaFrame implements Frame, Instructions
{
	private ClassFile cls;
	private DynaClassInstance obj;
	private HeapManager heap;
	private Method_Info method;
	
	private boolean isSynchronized;
	private byte [] code;

	private int pc;
	private int argCount;
	private long returnValue;
	private int[] localVars;
	private int[] operandStack;
	private int top;
	private boolean inExcept;
	
	
	DynaFrame ( ClassFile cf, DynaClassInstance rt, HeapManager hm, Method_Info mi, int[] args, int pos, int len )
	{
		ASSERT.fatal ( mi != null, "DynaFrame", 32, "Method not found" );
		cls = cf;
		obj = rt;
		heap = hm;
		method = mi;
		
		this.code = mi.code.getCode();
		localVars = new int [ mi.code.getLocalSize() ];
		operandStack = new int [ mi.code.getStackSize() ];
		pc = 0;
		this.isSynchronized = mi.isSynchronized();
		
		if ( rt != null )
		{
			pushArg ( rt.getRefId() );
		}
		for ( int x = pos; x < pos+len; x++ )
		{
			if ( mi.getArgSize() == 2 )
			{
				pushArg ( args[x] );
			}
			else
			{
				pushArg ( (int)args[x] );
			}
		}
	}

	private void pushArg ( long arg )
	{
		localVars[ argCount++ ] = (int)(arg & 0xFFFFFFFF);
		localVars[ argCount++ ] = (int)(arg >> 32);
	}
	
	private void pushArg ( int arg )
	{
		localVars[ argCount++ ] = arg;
	}

	public boolean unhandledException ()
	{
		return inExcept;
	}

	public long getRet ( )
	{
		return returnValue;
	}
	
	public void call () throws IOException
	{
		int tmp, tmp2;
		long ltmp, ltmp2;
		double dtmp, dtmp2;
		float ftmp, ftmp2;
		
		Fieldref_Info fldInfo;
		Methodref_Info methodrefInfo;
		ClassDef clsdef;
		ReferenceType inst;
		Frame frame;
		
		if ( isSynchronized )
		{
			try
			{
				obj.lock ();
			}
			catch ( Exception te )
			{
				// need to figure out exception processing
			}
		}
		while ( true )
		{
		try
		{
		switch ( code[pc++] )
		{
			/** do nothing
			 * 		
			 *  ARGS:
			 *  STACK: ... => ... 
			 */
			case NOP:
				break;
			
			/** push null on operand stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,null 
			 */
			case ACONST_NULL:
				operandStack[top++] = heap.get ( 0 ).getRefId();
				break;
				
			/** push int -1 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,-1 
			 */
			case ICONST_M1:
				operandStack[top++] = -1;
				break;

			/** push int 0 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,0 
			 */
			case ICONST_0:
				operandStack[top++] = 0;
				break;

			/** push int 1 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,1 
			 */
			case ICONST_1:
				operandStack[top++] = 1;
				break;

			/** push int 2 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,2 
			 */
			case ICONST_2:
				operandStack[top++] = 2;
				break;

			/** push int 3 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,3 
			 */
			case ICONST_3:
				operandStack[top++] = 3;
				break;
			
			/** push int 4 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,4 
			 */
			case ICONST_4:
				operandStack[top++] = 4;
				break;

			/** push int 5 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,5 
			 */
			case ICONST_5:
				operandStack[top++] = 5;
				break;

			/** push long 0 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,0L 
			 */
			case LCONST_0:
				operandStack[top++] = 0;
				break;

			/** push long 1 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,1L 
			 */
			case LCONST_1:
				operandStack[top++] = 1;
				break;

			/** push float 0 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,0F 
			 */
			case FCONST_0:
				operandStack[top++] = Float.floatToIntBits ( 0F );
				break;

			/** push float 1 on the op stack
			 * 
			 *  ARGS:
			 *	STACK: ... => ...,1F 
			 */
			case FCONST_1:
				operandStack[top++] = Float.floatToIntBits ( 1F );
				break;

			/** push float 2 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,2F 
			 */
			case FCONST_2:
				operandStack[top++] = Float.floatToIntBits ( 2F );
				break;

			/** push double 0 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,0D 
			 */
			case DCONST_0:
				ltmp = Double.doubleToLongBits ( 0.0 );
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;

			/** push double 1 on the op stack
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,1D 
			 */
			case DCONST_1:
				ltmp = Double.doubleToLongBits ( 1.0 );
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;

			/** push byte on the op stack
			 * 
			 *  ARGS: byte
			 *  STACK: ... => ...,<b> 
			 */
			case BIPUSH:
				operandStack[top++] = (code[pc++]&0xFF);
				break;

			/** push short on the op stack
			 * 
			 *  ARGS: byte1, byte2
			 *  STACK: ... => ...,(byte1 << 8) | byte2
			 */
			case SIPUSH:
				operandStack[top++] = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				break;

			/** Push item from runtime constant pool.  The constant
			 *  must be an int, float, or string reference.
			 * 
			 *  ARGS: index
			 *  STACK: ... => ...,value
			 */
			case LDC:
				operandStack[top++] = (int)cls.getConstant( (code[pc++]&0xFF), heap );
				break;
	
			/** Push an int, float, or string reference from runtime 
			 *  constant pool (wide index).  
			 *  Index = (indexbyte1 << 8) | indexbyte2
			 * 
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK: ... => ...,value
			 */
			case LDC_W:
				operandStack[top++] = (int)cls.getConstant( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF), heap );
				break;
	
			/** Push a long or double reference from runtime 
			 *  constant pool (wide index).  
			 *  Index = (indexbyte1 << 8) | indexbyte2
			 * 
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK: ... => ...,value
			 */
			case LDC2_W:
				ltmp = cls.getConstant( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF), heap );
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Load int from local variable.
			 * 
			 *  ARGS: index
			 *  STACK: ... => ...,value
			 */
			case ILOAD:
				operandStack[top++] = localVars[(code[pc++]&0xFF)];
				break;
	
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
			case LLOAD:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[tmp];
				operandStack[top++] = localVars[tmp+1];
				break;
	
			/**  Load float from local variable.
			 * 
			 *   ARGS: index
			 *   STACK: ... => ...,value
			 */
			case FLOAD:
				operandStack[top++] = localVars[(code[pc++]&0xFF)];
				break;
	
			/** Load double from local variable.The index is an 
			 *  unsigned byte. Both index and index + 1 must be 
			 *  indices into the local variable array of the current 
			 *  frame (§3.6). 
			 * 
			 *  ARGS: index
			 *  STACK: ... => ...,value
			 */
			case DLOAD:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[tmp];
				operandStack[top++] = localVars[tmp+1];
				break;
	
			/**  Load a refid from a local variable onto the stack.
			 * 
			 *   ARGS: index
			 *   STACK: ... => ...,objectref
			 */
			case ALOAD:
				operandStack[top++] = localVars[(code[pc++]&0xFF)];
				break;
	
			/** Load int from the first local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,i
			 */
			case ILOAD_0:
				operandStack[top++] = localVars[0];
				break;
	
			/** Load int from the second local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,i
			 */
			case ILOAD_1:
				operandStack[top++] = localVars[1];
				break;
	
			/** Load int from the thrid local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,i
			 */
			case ILOAD_2:
				operandStack[top++] = localVars[2];
				break;
	
			/** Load int from the fourth local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,i
			 */
			case ILOAD_3:
				operandStack[top++] = localVars[3];
				break;
	
			/** Load long from the first local variable.  Both <n> and 
			 *  <n> + 1 must be indices into the local variable array 
			 *  of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,l
			 */
			case LLOAD_0:
				operandStack[top++] = localVars[0];
				operandStack[top++] = localVars[1];
				break;

			/** Load long from the second local variable.  Both <n> and 
			 *  <n> + 1 must be indices into the local variable array 
			 *  of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,l
			 */
			case LLOAD_1:
				operandStack[top++] = localVars[1];
				operandStack[top++] = localVars[2];
				break;

			/** Load long from the second third variable.  Both <n> and 
			 *  <n> + 1 must be indices into the local variable array 
			 *  of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,l
			 */
			case LLOAD_2:
				operandStack[top++] = localVars[2];
				operandStack[top++] = localVars[3];
				break;

			/** Load long from the fourth local variable.  Both <n> and 
			 *  <n> + 1 must be indices into the local variable array 
			 *  of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,l
			 */
			case LLOAD_3:
				operandStack[top++] = localVars[3];
				operandStack[top++] = localVars[4];
				break;

			/** Load float from the first local variable.  
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,f
			 */
			case FLOAD_0:
				operandStack[top++] = localVars[0];
				break;

			/** Load float from the second local variable.  
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,f
			 */
			case FLOAD_1:
				operandStack[top++] = localVars[1];
				break;

			/** Load float from the third local variable.  
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,f
			 */
			case FLOAD_2:
				operandStack[top++] = localVars[2];
				break;

			/** Load float from the fourth local variable.  
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,f
			 */
			case FLOAD_3:
				operandStack[top++] = localVars[3];
				break;
	
			/** Load double from the first local variable.  Both 
			 *  <n> and <n> + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,d
			 */
			case DLOAD_0:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[0];
				operandStack[top++] = localVars[1];
				break;

			/** Load double from the second local variable.  Both 
			 *  <n> and <n> + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,d
			 */
			case DLOAD_1:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[1];
				operandStack[top++] = localVars[2];
				break;
			
			/** Load double from the third local variable.  Both 
			 *  <n> and <n> + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,d
			 */
			case DLOAD_2:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[2];
				operandStack[top++] = localVars[3];
				break;

			/** Load double from the fourth local variable.  Both 
			 *  <n> and <n> + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). 
			 * 
			 *  ARGS: 
			 *  STACK: ... => ...,d
			 */
			case DLOAD_3:
				tmp = (code[pc++]&0xFF);
				operandStack[top++] = localVars[3];
				operandStack[top++] = localVars[4];
				break;
	
			/** Load reference from first local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,objectref
			 */
			case ALOAD_0:
				operandStack[top++] = localVars[0];
				break;
	
			/** Load reference from second local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,objectref
			 */
			case ALOAD_1:
				operandStack[top++] = localVars[1];
				break;
	
			/** Load reference from third local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,objectref
			 */
			case ALOAD_2:
				operandStack[top++] = localVars[2];
				break;
	
			/** Load reference from fourth local variable.
			 * 
			 *  ARGS:
			 *  STACK: ... => ...,objectref
			 */
			case ALOAD_3:
				operandStack[top++] = localVars[3];
				break;
	
			/** Load int from array.
			 * 
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case IALOAD:
				tmp = operandStack[--top];
				tmp2 = top-1;
				operandStack[tmp2] = (int)((ArrayInstance)heap.get ( operandStack[tmp2] )).load ( tmp );
				break;
	
			/** Load long from array.
			 * 
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case LALOAD:
				tmp = operandStack[top-1];
				ltmp = ((ArrayInstance)heap.get ( operandStack[top-2] )).load ( tmp );
				operandStack[top-2] = (int)ltmp;
				operandStack[top-1] = (int)(ltmp>>32);
				break;
	
			/** Load float from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case FALOAD:
				tmp = operandStack[--top];
				tmp2 = top-1;
				operandStack[tmp2] = (int)((ArrayInstance)heap.get ( operandStack[tmp2] )).load ( tmp );
				break;
	
			/** Load double from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case DALOAD:
				tmp = operandStack[--top];
				tmp2 = top-1;
				operandStack[tmp2] = (int)((ArrayInstance)heap.get ( operandStack[tmp2] )).load ( tmp );
				break;
	
			/** Load objectref from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,objectref
			 */
			case AALOAD:
				tmp = operandStack[--top];
				tmp2 = top-1;
				operandStack[tmp2] = (int)((ArrayInstance)heap.get ( operandStack[tmp2] )).load ( tmp );
				break;
	
			/** Load byte from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case BALOAD:
				tmp = operandStack[--top];
				tmp2 = top-1;
				operandStack[tmp2] = (int)((ArrayInstance)heap.get ( operandStack[tmp2] )).load ( tmp );
				break;
	
			/** Load char from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case CALOAD:
				tmp = operandStack[--top];
				operandStack[top-1] = (int)((ArrayInstance)heap.get ( operandStack[top-1] )).load ( tmp );
				break;
	
			/** Load short from array.
			 *  ARGS:
			 *  STACK: ...,arrayref,index => ...,value
			 */
			case SALOAD:
				tmp = operandStack[--top];
				tmp2 = operandStack[top-1];
				inst = heap.get ( tmp2 );
				operandStack[top-1] = (int)((ArrayInstance)inst).load ( tmp );
				break;
	
			/** Store int into local variable
			 *  ARGS: index
			 *  STACK: ...,value => ...
			 */
			case ISTORE:
				localVars[(code[pc++]&0xFF)] = operandStack[--top];
				break;
	
			/** Store long into local variable.  Both index and 
			 *  index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: index
			 *  STACK: ...,value => ...
			 */
			case LSTORE:
				tmp = (code[pc++]&0xFF);
				localVars[tmp+1] = operandStack[--top];
				localVars[tmp] = operandStack[--top];
				break;
	
			/** Store float into local variable
			 *  ARGS: index
			 *  STACK: ...,value => ...
			 */
			case FSTORE:
				localVars[(code[pc++]&0xFF)] = operandStack[--top];
				break;
	
			/** Store double into local variable.  Both index and 
			 *  index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: index
			 *  STACK: ...,value => ...
			 */
			case DSTORE:
				tmp = (code[pc++]&0xFF);
				localVars[tmp] = operandStack[--top];
				localVars[tmp+1] = operandStack[top];
				break;
	
			/** Store objectref into local variable
			 *  ARGS: index
			 *  STACK: ...,objectref => ...
			 */
			case ASTORE:
				localVars[(code[pc++]&0xFF)] = operandStack[--top];
				break;

			/** Store int into the first local variable
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case ISTORE_0:
				localVars[0] = operandStack[--top];
				break;

			/** Store int into the second local variable
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case ISTORE_1:
				localVars[1] = operandStack[--top];
				break;

			/** Store int into the thrid local variable
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case ISTORE_2:
				localVars[2] = operandStack[--top];
				break;

			/** Store int into the fourth local variable
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case ISTORE_3:
				localVars[3] = operandStack[--top];
				break;
	
			/** Store long into the first local variable.  Both index 
			 *  and index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case LSTORE_0:
				localVars[1] = operandStack[--top];
				localVars[0] = operandStack[--top];
				break;
	
			/** Store long into the second local variable.  Both index 
			 *  and index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case LSTORE_1:
				localVars[2] = operandStack[--top];
				localVars[1] = operandStack[--top];
				break;
	
			/** Store long into the third local variable.  Both index 
			 *  and index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case LSTORE_2:
				localVars[3] = operandStack[--top];
				localVars[2] = operandStack[--top];
				break;
	
			/** Store long into the fourth local variable.  Both index 
			 *  and index + 1 must be indices into the local variable 
			 *  array of the current frame (§3.6). The local 
			 *  variables at index and index + 1 are set to value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case LSTORE_3:
				localVars[4] = operandStack[--top];
				localVars[3] = operandStack[--top];
				break;
	
			/** Store float into the first local variable.
			 *  ARGS:
			 *  STACK: ...,value => ...
			 */
			case FSTORE_0:
				localVars[0] = operandStack[--top];
				break;
	
			/** Store float into the second local variable.
			 *  ARGS:
			 *  STACK: ...,value => ...
			 */
			case FSTORE_1:
				localVars[1] = operandStack[--top];
				break;
	
			/** Store float into the third local variable.
			 *  ARGS:
			 *  STACK: ...,value => ...
			 */
			case FSTORE_2:
				localVars[2] = operandStack[--top];
				break;
	
			/** Store float into the fourth local variable.
			 *  ARGS:
			 *  STACK: ...,value => ...
			 */
			case FSTORE_3:
				localVars[3] = operandStack[--top];
				break;
	
			/** Store double into the first local variable.  Both 
			 *  index and index + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). The 
			 *  local variables at index and index + 1 are set to
			 *  value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case DSTORE_0:
				localVars[1] = operandStack[--top];
				localVars[0] = operandStack[--top];
				break;
	
			/** Store double into the second local variable.  Both 
			 *  index and index + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). The 
			 *  local variables at index and index + 1 are set to
			 *  value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case DSTORE_1:
				localVars[2] = operandStack[--top];
				localVars[1] = operandStack[--top];
				break;
	
			/** Store double into the third local variable.  Both 
			 *  index and index + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). The 
			 *  local variables at index and index + 1 are set to
			 *  value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case DSTORE_2:
				localVars[3] = operandStack[--top];
				localVars[2] = operandStack[--top];
				break;
	
			/** Store double into the fourth local variable.  Both 
			 *  index and index + 1 must be indices into the local 
			 *  variable array of the current frame (§3.6). The 
			 *  local variables at index and index + 1 are set to
			 *  value. 
			 *  ARGS: 
			 *  STACK: ...,value => ...
			 */
			case DSTORE_3:
				localVars[4] = operandStack[--top];
				localVars[3] = operandStack[--top];
				break;
	
			/** Store objectref into the first local variable.
			 *  ARGS: 
			 *  STACK: ...,objectref => ...
			 */
			case ASTORE_0:
				localVars[0] = operandStack[--top];
				break;
				
			/** Store objectref into the second local variable.
			 *  ARGS: 
			 *  STACK: ...,objectref => ...
			 */
			case ASTORE_1:
				localVars[1] = operandStack[--top];
				break;
	
			/** Store objectref into the third local variable.
			 *  ARGS: 
			 *  STACK: ...,objectref => ...
			 */
			case ASTORE_2:
				localVars[2] = operandStack[--top];
				break;
	
			/** Store objectref into the fourth local variable.
			 *  ARGS: 
			 *  STACK: ...,objectref => ...
			 */
			case ASTORE_3:
				localVars[3] = operandStack[--top];
				break;
	
			/** Store into int array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case IASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Store into long array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case LASTORE:
				ltmp = (operandStack[--top]<<32L) | operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, ltmp );
				break;
	
			/** Store into float array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case FASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Store into double array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case DASTORE:
				ltmp = (operandStack[--top]<<32L) | operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, ltmp );
				break;
	
			/** Store into reference array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case AASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Store into byte array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case BASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Store into char array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case CASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Store into short array.  
			 *  ARGS:
			 *  STACK: ...,arrayref,index,value => ...
			 */
			case SASTORE:
				tmp = operandStack[--top];
				tmp2 = operandStack[--top];
				inst = heap.get ( operandStack[--top] );
				((ArrayInstance)inst).store( tmp2, tmp );
				break;
	
			/** Pop the top operand stack value.
			 *  ARGS:
			 *  STACK: ...,value => ...
			 */
			case POP:
				top--;
				break;
				
			/** Pop two ints off the stack ( 2 ints or 1 long )
			 *  ARGS:
			 *  STACK: ..., value2, value1  ...
			 */
			case POP2:
				top -= 2;
				break;
				
			/** DUP
			 *  ARGS:
			 *  STACK: ...,value => ...,value,value
			 */
			case DUP:
				operandStack[top] = operandStack[top-1];				
				top++;
				break;
	
			/** Duplicate the top operand stack value and insert 
			 *  two values down.
			 *  ARGS:
			 *  STACK: ..., value2, value1  => ..., value1, value2, value1
			 */
			case DUP_X1:
				operandStack[top] = operandStack[top-1];
				
				operandStack[top-1] = operandStack[top-2];
				
				operandStack[top-2] = operandStack[top];
				
				top++;
				break;
	
			/** Duplicate the top operand stack value and insert 
			 *  three (int) values down.
			 *  ARGS:
			 *  STACK: ...,value3,value2,value1  => ..., value1,value3,value2,value1
			 */
			case DUP_X2:
				operandStack[top] = operandStack[top-1];
				
				operandStack[top-1] = operandStack[top-2];
				
				operandStack[top-2] = operandStack[top-3];
				
				operandStack[top-3] = operandStack[top];
				
				top++;
				break;
	
			/** Duplicate the top two (int) operand stack values.
			 *  ARGS:
			 *  STACK: ...,value2,value1 => ...
			 */
			case DUP2:
				operandStack[top] = operandStack[top-2];
				
				operandStack[top+1] = operandStack[top-1];
				
				top += 2;
				break;
	
			/** Duplicate the two (int) operand stack values 
			 *  and insert three (int) values down
			 *  ARGS:
			 *  STACK: ...,value3,value2,value1 => ...,value2,value1,value3,value2,value1
			 */
			case DUP2_X1:
				operandStack[top+1] = operandStack[top-1];
				
				operandStack[top] = operandStack[top-2];
				
				operandStack[top-1] = operandStack[top-3];
				
				operandStack[top-2] = operandStack[top+1];
				
				operandStack[top-1] = operandStack[top];
				
				top+=2;
				break;
	
			/** Duplicate the top two (int) operand stack values and insert four
			 *  (int) values down.
			 *  ARGS:
			 *  STACK: ...,value4,value3,value2,value1 => ...,value2,value1,value4,value3,value2,value1
			 */
			case DUP2_X2:
				operandStack[top+1] = operandStack[top-1];
				
				operandStack[top] = operandStack[top-2];
				
				operandStack[top-1] = operandStack[top-3];
				
				operandStack[top-2] = operandStack[top-4];
								
				operandStack[top-3] = operandStack[top+1];
				
				operandStack[top-4] = operandStack[top];
				
				top+=2;
				break;
	
			/** Swap the top two operand stack values.
			 *  ARGS:
			 *  STACK: ..., value2, value1 => ..., value1, value2
			 */
			case SWAP:
				tmp = operandStack[top-1];
				operandStack[top-1] = operandStack[top-2];
				operandStack[top-2] = tmp;				
				break;
	
			/** Add ints
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result
			 */
			case IADD:
				top--;
				operandStack[top-1] += operandStack[top];
				break;
	
			/** Add long
			 *  ARGS:
			 *  STACK: ..., value1, value2  ..., result
			 */
			case LADD:
				top--;
				ltmp = (operandStack[top-1]<<32L) | operandStack[top-2];
				ltmp += (operandStack[top-3]<<32L) | operandStack[top-4];
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Add float
			 *  ARGS:
			 *  STACK: ..., value1, value2  ..., result
			 */
			case FADD:
				ftmp = Float.intBitsToFloat ( operandStack[--top] );
				ftmp += Float.intBitsToFloat ( operandStack[top-1] );
				operandStack[top-1] = Float.floatToIntBits ( ftmp );
				break;
	
			/** Add double
			 *  ARGS:
			 *  STACK: ..., value1, value2  ..., result
			 */
			case DADD:
				dtmp = Double.longBitsToDouble ( (operandStack[--top]<<32L) | operandStack[--top] );
				dtmp += Double.longBitsToDouble ( (operandStack[--top]<<32L) | operandStack[--top] );
				ltmp = Double.doubleToLongBits ( dtmp );
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Subtract ints
			 *  ARGS:
			 *  STACK: ..., value1, value2  ..., value1 - value2
			 */
			case ISUB:
				operandStack[top-2] = operandStack[top-1] - operandStack[top-2];
				top--;
				break;
	
			/** Subtract longs. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 - value2
			 */
			case LSUB:
				operandStack[top-2] = operandStack[top-1] - operandStack[top-2];
				top--;
				break;
	
			/** Subtract floats. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 - value2
			 */
			case FSUB:
				operandStack[top-2] = Float.floatToIntBits( Float.intBitsToFloat( operandStack[top-2] ) - Float.intBitsToFloat ( (int)operandStack[top-1] ) );
				top--;
				break;
	
			/** Subtract doubles. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 - value2
			 */
			case DSUB:
				ltmp = Double.doubleToLongBits( Double.longBitsToDouble( (operandStack[top-3]<<32L) | operandStack[top-4] ) - Double.longBitsToDouble ( (operandStack[top-1]<<32L) | operandStack[top-2] ) );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Mult ints. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case IMUL:
				operandStack[top-2] *= operandStack[top-1];
				top--;
				break;
	
			/** Mult longs. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case LMUL:
				operandStack[top-2] *= operandStack[top-1];
				top--;
				break;
	
			/** Mult ints. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case FMUL:
				operandStack[top-2] = Float.floatToIntBits( Float.intBitsToFloat( operandStack[top-1] ) * Float.intBitsToFloat ( operandStack[top-2] ) );
				top--;
				break;
	
			/** Mult doubles. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case DMUL:
				ltmp = Double.doubleToLongBits( Double.longBitsToDouble( (operandStack[top-1]<<32L) | operandStack[top-2] ) * Double.longBitsToDouble ( (operandStack[top-3]<<32L) | operandStack[top-4] ) );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Divide ints. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 / value2
			 */
			case IDIV:
				operandStack[top-2] /= operandStack[top-1];
				top--;
				break;
	
			/** Divide longs. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 / value2
			 */
			case LDIV:
				operandStack[top-2] /= operandStack[top-1];
				top--;
				break;
	
			/** Divide floats. 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 / value2
			 */
			case FDIV:
				operandStack[top-2] = Float.floatToIntBits( Float.intBitsToFloat( operandStack[top-2] ) / Float.intBitsToFloat ( operandStack[top-1] ) );
				top--;
				break;
	
			/** Divide doubles. 
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., value1 / value2
			 */
			case DDIV:
				ltmp = Double.doubleToLongBits( Double.longBitsToDouble( (operandStack[top-3]<<32L) | operandStack[top-4] ) / Double.longBitsToDouble ( (operandStack[top-1]<<32L) | operandStack[top-2] ) );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Remainder int. The int result is 
			 *  value1 - (value1 / value2) * value2
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case IREM:
				operandStack[top-2] = operandStack[top-2] % operandStack[top-1];
				top--;
				break;
	
			/** Remainder long. The int result is 
			 *  value1 - (value1 / value2) * value2
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 => ..., result
			 */
			case LREM:
				operandStack[top-2] = operandStack[top-2] % operandStack[top-1];
				top--;
				break;
	
			/** The floating-point remainder result from a dividend 
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
			case FREM:
				operandStack[top-2] = Float.floatToIntBits( Float.intBitsToFloat( operandStack[top-2] ) % Float.intBitsToFloat ( operandStack[top-1] ) );
				top--;
				break;
	
			/** The floating-point remainder result from a dividend 
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
			case DREM:
				ltmp = Double.doubleToLongBits( Double.longBitsToDouble( (operandStack[top-3]<<32L) | operandStack[top-4] ) % Double.longBitsToDouble ( (operandStack[top-1]<<32L) | operandStack[top-2] ) );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Negate int
			 * 
			 *  ARGS:
			 *  STACK: ...,value1 => ...,-value1
			 */
			case INEG:
				operandStack[top-1] = -operandStack[top-1];
				break;
	
			/** Negate long
			 * 
			 *  ARGS:
			 *  STACK: ...,value1 => ...,-value1
			 */
			case LNEG:
				operandStack[top-1] = -operandStack[top-1];
				break;
	
			/** Negate float
			 * 
			 *  ARGS:
			 *  STACK: ...,value1 => ...,-value1
			 */
			case FNEG:
				operandStack[top-1] = Float.floatToIntBits( -Float.intBitsToFloat( operandStack[top-1] ) );
				break;
	
			/** Negate double
			 * 
			 *  ARGS:
			 *  STACK: ...,value1 => ...,-value1
			 */
			case DNEG:
				ltmp = Double.doubleToLongBits( -Double.longBitsToDouble( (operandStack[top-1]<<32L) | operandStack[top-2] ) );
				operandStack[top-1] = (int)(ltmp>>32);
				operandStack[top-2] = (int)ltmp;
				break;
	
			/** Shift left int.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result
			 */
			case ISHL:
				operandStack[top-2] = (int)(operandStack[top-2]<<operandStack[top-1]);
				top--;
				break;
	
			/** Shift left long.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result
			 */
			case LSHL:
				ltmp = ((operandStack[top-3]<<32L)|operandStack[top-4]) << ((operandStack[top-1]<<32L) | operandStack[top-2] );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Shift right int.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result
			 */
			case ISHR:
				operandStack[top-2] = (operandStack[top-2]>>operandStack[top-1]);
				top--;
				break;
	
			/** Shift right long.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result
			 */
			case LSHR:
				ltmp = ((operandStack[top-3]<<32L)|operandStack[top-4]) >> ((operandStack[top-1]<<32L) | operandStack[top-2] );
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Logical shift right int.  The values are popped from 
			 *  the operand stack. An int result is calculated by shifting 
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
			case IUSHR:
				tmp = (int)operandStack[top-2];
				if ( tmp < 0 )
				{
					operandStack[top-2] = (tmp>>operandStack[top-1]) + (2 << ~operandStack[top-1]);
				}
				else
				{
					operandStack[top-2] = tmp>>operandStack[top-1];
				}
				top--;
				break;
	
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
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case LUSHR:
				ltmp = (operandStack[top-3]<<32L) | operandStack[top-4];
				if ( ltmp < 0 )
				{
					ltmp = (ltmp >> ((operandStack[top-1]<<32L) | operandStack[top-2] )) + (2 << ~((operandStack[top-1]<<32L) | operandStack[top-2] ));
				}
				else
				{
					ltmp = ltmp >> ((operandStack[top-1]<<32L) | operandStack[top-2] );
				}
				top -= 3;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Boolean AND int.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case IAND:
				operandStack[top-2] &= operandStack[top-1];
				top--;
				break;
	
			/** Boolean AND long.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case LAND:
				ltmp = (operandStack[top-1]<<32L) | operandStack[top-2];
				ltmp &= (operandStack[top-3] << 32L) | operandStack[top-4];
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Boolean OR int.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case IOR:
				operandStack[top-2] |= operandStack[top-1];
				top--;
				break;
	
			/** Boolean OR long.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case LOR:
				ltmp = (operandStack[top-1]<<32L) | operandStack[top-2];
				ltmp |= (operandStack[top-3]<<32L) | operandStack[top-4];
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Boolean XOR int.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case IXOR:
				operandStack[top-2] ^= operandStack[top-1];
				top--;
				break;
	
			/** Boolean XOR long.
			 * 
			 *  ARGS:
			 *  STACK: ..., value1, value2 =>  ..., result  
			 */
			case LXOR:
				ltmp = (operandStack[top-3]<<32L) | operandStack[top-4];
				ltmp ^= (operandStack[top-1]<<32L) | operandStack[top-2];
				top -= 4;
				operandStack[top++] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
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
			case IINC:
				localVars[(code[pc++]&0xFF)] += (code[pc++]&0xFF);
				break;
	
			/** Convert int to long.  It is popped from the operand 
			 *  stack and sign-extended to a long result.
			 * 
			 *  ARGS:
			 *  STACK: ...,value => ...,result
			 */
			case I2L:
				operandStack[top++] = 0;
				break;
	
			/** Convert int to float.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case I2F:
				operandStack[top-1] = Float.floatToIntBits ( (float)operandStack[top-1] );
				break;
	
			/** Convert int to double
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case I2D:
				ltmp = Double.doubleToLongBits ( (double)operandStack[top-1] );
				operandStack[top-1] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** Long to int.  The value on the top of the operand 
			 *  stack must be of type long. It is popped from the 
			 *  operand stack and converted to an int result by taking 
			 *  the low-order 32 bits of the long value and discarding 
			 *  the high-order 32 bits. The result is pushed onto the 
			 *  operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case L2I:
				operandStack[top-2] = (int)((operandStack[top-1]<<32L) | operandStack[top-2] );
				break;
	
			/** Long to float
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case L2F:
				operandStack[top-2] = Float.floatToIntBits ( (float) ((operandStack[top-1]<<32L) | operandStack[top-2]) );
				break;
				
			/** Long to double
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case L2D:
				ltmp = Double.doubleToLongBits ( (double)((operandStack[top-1]<<32L)|operandStack[top-2]  ) );
				operandStack[top-2] = (int)ltmp;
				operandStack[top-1] = (int)(ltmp>>32);
				break;
	
			/** float to int
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case F2I:
				operandStack[top-1] = (int)Float.intBitsToFloat ( (int)operandStack[top-1] );
				break;
				
			/** float to long
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case F2L:
				ltmp = (long)Float.intBitsToFloat ( operandStack[top-1] );
				operandStack[top-1] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** float to double
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case F2D:
				ltmp = Double.doubleToLongBits(Float.intBitsToFloat ( operandStack[top-1] ));
				operandStack[top-1] = (int)ltmp;
				operandStack[top++] = (int)(ltmp>>32);
				break;
	
			/** double to int
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case D2I:
				operandStack[top-2] = (int)((operandStack[top-1]<<32L)|operandStack[top-2]);
				top--;
				break;
	
			/** double to long
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case D2L:
				ltmp = (operandStack[top-1]<<32L)|operandStack[top-2];
				ltmp = Double.doubleToLongBits ( (double)ltmp );
				operandStack[top-1] = (int)(ltmp>>32);
				operandStack[top-2] = (int)ltmp;
				break;
	
			/** double to float
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case D2F:
				operandStack[top-2] = Float.floatToIntBits ( (float)Double.longBitsToDouble ((operandStack[top-1]<<32L)|operandStack[top-2] ) );
				top--;
				break;
				
			/** Int to byte
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case I2B:
				operandStack[top-1] &= 0xFF;
				break;
	
			/** Int to char
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case I2C:
				operandStack[top-1] &= 0xFFFF;
				break;
	
			/** Int to short
			 * 
			 *  ARGS:
			 *  STACK:  ...,value => ...,result
			 */
			case I2S:
				operandStack[top-1] &= 0xFFFF;
				break;
	
			/** Compare long.  If value1 is greater than value2, the int 
			 *  value 1 is pushed onto the operand stack. If value1 is 
			 *  equal to value2, the int value 0 is pushed onto the operand 
			 *  stack. If value1 is less than value2, the int value -1 is 
			 *  pushed onto the operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value1,value2 => ...,result
			 */
			case LCMP:
				ltmp = (operandStack[top-1]<<32L) | operandStack[top-2];
				ltmp2 = (operandStack[top-3]<<32L) | operandStack[top-4];
				top -= 4;
				if ( ltmp > ltmp2 )
				{
					operandStack[top++] = 1;
				}
				else if ( ltmp == ltmp2 )
				{
					operandStack[top++] = 0;
				}
				else
				{
					operandStack[top++] = -1;
				}
				break;
	
			/**  Compare float.  If value1' is greater than value2', the 
			 *   int value 1 is pushed onto the operand stack. Otherwise, 
			 *   if value1' is equal to value2', the int value 0 is pushed 
			 *   onto the operand stack.  Otherwise, if value1' is less 
			 *   than value2', the int value -1 is pushed onto the operand 
			 *   stack.  For NaN the fcmpl instruction pushes the int value 
			 *   -1 onto the operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value1,value2 => ...,result
			 */
			case FCMPL:
				ftmp = Float.intBitsToFloat ( (int)operandStack[top-2] );
				ftmp2 = Float.intBitsToFloat ( (int)operandStack[top-1] );
				if ( ftmp > ftmp2 )
				{
					operandStack[top-2] = 1;
				}
				else if ( ftmp == ftmp2 )
				{
					operandStack[top-2] = 0;
				}
				else
				{
					operandStack[top-2] = -1;
				}
				top--;
				break;
	
			/**  Compare float.  If value1' is greater than value2', the 
			 *   int value 1 is pushed onto the operand stack. Otherwise, 
			 *   if value1' is equal to value2', the int value 0 is pushed 
			 *   onto the operand stack.  Otherwise, if value1' is less 
			 *   than value2', the int value -1 is pushed onto the operand 
			 *   stack.  For NaN hhe fcmpg instruction pushes the int value 1 
			 *   onto the operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value1,value2 => ...,result
			 */
			case FCMPG:
				ftmp = Float.intBitsToFloat ( (int)operandStack[top-2] );
				ftmp2 = Float.intBitsToFloat ( (int)operandStack[top-1] );
				if ( ftmp > ftmp2 )
				{
					operandStack[top-2] = 1;
				}
				else if ( ftmp == ftmp2 )
				{
					operandStack[top-2] = 0;
				}
				else
				{
					operandStack[top-2] = -1;
				}
				top--;
				break;
							
			/**  Compare double.  If value1' is greater than value2', the 
			 *   int value 1 is pushed onto the operand stack. Otherwise, 
			 *   if value1' is equal to value2', the int value 0 is pushed 
			 *   onto the operand stack.  Otherwise, if value1' is less 
			 *   than value2', the int value -1 is pushed onto the operand 
			 *   stack.  For NaN hhe dcmpl instruction pushes the int value -1 
			 *   onto the operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value1,value2 => ...,result
			 */
			case DCMPL:
				dtmp = Double.longBitsToDouble ( (operandStack[top-3]<<32L) | operandStack[top-4] );
				dtmp2 = Double.longBitsToDouble ( (operandStack[top-1]<<32L)|operandStack[top-2] );
				top -= 4;
				if ( dtmp > dtmp2 )
				{
					operandStack[top++] = 1;
				}
				else if ( dtmp == dtmp2 )
				{
					operandStack[top++] = 0;
				}
				else
				{
					operandStack[top++] = -1;
				}
				break;
			
			/**  Compare double.  If value1' is greater than value2', the 
			 *   int value 1 is pushed onto the operand stack. Otherwise, 
			 *   if value1' is equal to value2', the int value 0 is pushed 
			 *   onto the operand stack.  Otherwise, if value1' is less 
			 *   than value2', the int value -1 is pushed onto the operand 
			 *   stack.  For NaN hhe fcmpg instruction pushes the int value 1 
			 *   onto the operand stack.
			 * 
			 *  ARGS:
			 *  STACK:  ...,value1,value2 => ...,result
			 */
			case DCMPG:
				dtmp = Double.longBitsToDouble ( (operandStack[top-3]<<32L) | operandStack[top-4] );
				dtmp2 = Double.longBitsToDouble ( (operandStack[top-1]<<32L)|operandStack[top-2] );
				top -= 4;
				if ( dtmp > dtmp2 )
				{
					operandStack[top-2] = 1;
				}
				else if ( dtmp == dtmp2 )
				{
					operandStack[top-2] = 0;
				}
				else
				{
					operandStack[top-2] = -1;
				}
				break;
	
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFEQ:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] == 0 )
				{
					pc += tmp;
				}
				break;
				
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFNE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] != 0 )
				{
					pc += tmp;
				}
				break;
	
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFLT:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] < 0 )
				{
					pc += tmp;
				}
				break;
	
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFGE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] >= 0 )
				{
					pc += tmp;
				}
				break;
	
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFGT:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] > 0 )
				{
					pc += tmp;
				}
				break;
	
			/** Branch if int comparison with zero succeeds.  
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value => ...
			 */
			case IFLE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[--top] <= 0 )
				{
					pc += tmp;
				}
				break;
	
			/** Branch if int comparison succeeds.  (value1 = value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPEQ:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] == operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if int comparison succeeds.  (value1 != value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPNE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] != operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if int comparison succeeds.  (value1 < value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPLT:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] < operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if int comparison succeeds.  (value1 >= value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPGE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] >= operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if int comparison succeeds.  (value1 > value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPGT:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] > operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if int comparison succeeds.  (value1 <= value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ICMPLE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] <= operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if reference comparison succeeds.  (value1 == value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ACMPEQ:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] == operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch if reference comparison succeeds.  (value1 != value2)
			 *  PC = (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ...,value1,value2 => ...
			 */
			case IF_ACMPNE:
				tmp = (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-3;
				if ( operandStack[top-2] != operandStack[top-1] )
				{
					pc += tmp;
				}
				top -= 2;
				break;
	
			/** Branch.
			 *  PC += (branchbyte1 << 8) | branchbyte2
			 * 
			 *  ARGS:  branchbyte1, branchbyte2
			 *  STACK:  ... => ...
			 */
			case GOTO:
				pc += (((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF))-1;
				break;
				
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
			case JSR:
				operandStack[top++] = pc+2;
				pc = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				break;
	
			/** Return from subroutine.  The local variable at index in 
			 *  the current frame (§3.6) must contain a value of type 
			 *  returnAddress. The contents of the local variable are 
			 *  written into the Java virtual machine's pc register, and 
			 *  execution continues there.
			 * 
			 *  ARGS: index
			 *  STACK: ... => ...
			 */
			case RET:
				pc = localVars[code[pc]];
				break;
	
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
			 *  index must be of type int and is popped from the operand stack. 
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
			case TABLESWITCH:
				tableswitch ();
				break;
	
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
			 *  pairs consists of an int match and a signed 32-bit offset. Each 
			 *  of these signed 32-bit values is constructed from four unsigned 
			 *  bytes as (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4.  
			 *  The table match-offset pairs of the lookupswitch instruction 
			 *  must be sorted in increasing numerical order by match.  The key 
			 *  must be of type int and is popped from the operand stack. The 
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
			case LOOKUPSWITCH:
				lookupswitch ();
				break;
	
			/** Return int from method.  The current method must have return 
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
			case IRETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = operandStack[--top];
				return;

	
			/** Return long from method.
			 * 
			 *  ARGS:
			 *  STACK:  ..., value => [empty]
			 */
			case LRETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = (operandStack[--top]<<32L)|operandStack[--top];
				return;
	
			/** Return float from method.
			 * 
			 *  ARGS:
			 *  STACK:  ..., value => [empty]
			 */
			case FRETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = operandStack[--top];
				return;
	
			/** Return double from method.
			 * 
			 *  ARGS:
			 *  STACK:  ..., value => [empty]
			 */
			case DRETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = (operandStack[--top]<<32L)|operandStack[--top];
				return;
	
			/** Return reference from method.
			 * 
			 *  ARGS:
			 *  STACK:  ..., value => [empty]
			 */
			case ARETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = operandStack[--top];
				return;
	
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
			case RETURN:
				if ( isSynchronized )
				{
					obj.unlock ();
				}
				returnValue = 0L;
				return;
	
			/** Get static field from class.  The value of the index is 
			 *  (indexbyte1 << 8) | indexbyte2. After linking, this is
			 *  an index to a ClassDef field.
			 *  
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK:  ... => ..., value 
			 */
			case GETSTATIC:
				fldInfo = cls.getFieldRef ( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF) );
				if ( fldInfo.type.type == Type.TYPE_DOUBLE || fldInfo.type.type == Type.TYPE_LONG )
				{
					ltmp = heap.forName(fldInfo.className).getField ( fldInfo.fieldName );
					operandStack[top++] = (int)ltmp;
					operandStack[top++] = (int)(ltmp>>32);
				}
				else
				{
					operandStack[top++] = (int)heap.forName(fldInfo.className).getField ( fldInfo.fieldName );
				}
				break;
	
			/** Store a static field in a class.  The value of the index 
			 *  is (indexbyte1 << 8) | indexbyte2. After linking, this is
			 *  an index to a ClassDef field.
			 *  
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK:  value,... => ...
			 */
			case PUTSTATIC:
				fldInfo = cls.getFieldRef(((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF));
				if ( fldInfo.type.type == Type.TYPE_DOUBLE || fldInfo.type.type == Type.TYPE_LONG )
				{
					heap.forName(fldInfo.className).setField ( fldInfo.fieldName, (operandStack[--top]<<32L)|operandStack[--top] );					
				}
				else
				{
					heap.forName(fldInfo.className).setField ( fldInfo.fieldName, operandStack[--top] );
				}
				break;
	
			/** Get a field from a class.  The value of the index is 
			 *  (indexbyte1 << 8) | indexbyte2. 
			 *  
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK:  ..., objectref => ..., value 
			 */
			case GETFIELD:
				fldInfo = cls.getFieldRef(((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF));
				if ( fldInfo.type.type == Type.TYPE_DOUBLE || fldInfo.type.type == Type.TYPE_LONG )
				{
					ltmp = heap.get( operandStack[top-1] ).getField ( fldInfo.fieldName );
					operandStack[top-1] = (int)ltmp;
					operandStack[top++] = (int)(ltmp>>32);
				}
				else
				{
					operandStack[top-1] = (int)heap.get( operandStack[top-1] ).getField ( fldInfo.fieldName );
				}
				break;
	
			/** Store a field in a class.  The value of the index is 
			 *  (indexbyte1 << 8) | indexbyte2. 
			 *  
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK:  ..., objectref, value => ...
			 */
			case PUTFIELD:
				fldInfo = cls.getFieldRef(((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF));
				if ( fldInfo.type.type == Type.TYPE_DOUBLE || fldInfo.type.type == Type.TYPE_LONG )
				{
					ltmp = (operandStack[--top]<<32L) | operandStack[--top];
				}
				else
				{
					ltmp = operandStack[--top];
				}
				inst = heap.get ( (int)operandStack[--top] );
				inst.setField ( fldInfo.fieldName, ltmp );
				break;
	
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
			 *  and so on. Any argument value that is of a floating-point 
			 *  type undergoes value set conversion (§3.8.3) prior to being 
			 *  stored in a local variable. The new frame is then made 
			 *  current, and the Java virtual machine pc is set to the 
			 *  opcode of the first instruction of the method to be invoked. 
			 *  Execution continues with the first instruction of the method.
			 * 
			 *  ARGS:  indexbyte1, indexbyte2
			 *  STACK:  ..., objectref, [arg1, [arg2 ...]] => ...
			 */
			case INVOKEVIRTUAL:
				methodrefInfo = cls.getMethodRef ( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF) );
				tmp = methodrefInfo.args.size();
				top = (top-1)-tmp;
				inst = heap.get ( operandStack[top] );
				frame = inst.callMethod ( heap, methodrefInfo.descriptor + methodrefInfo.methodName, operandStack, top+1, tmp );
				frame.call ();
				if ( frame.unhandledException() )
				{
					operandStack[top++] = (int)frame.getRet();
					if ( ! processException () )
					{
						return;
					}
					break;
				}
				switch ( methodrefInfo.retType.type )
				{
					case Type.TYPE_VOID:
						break;
					case Type.TYPE_LONG:
					case Type.TYPE_DOUBLE:
						operandStack[top++] = (int)frame.getRet();
						operandStack[top++] = (int)(frame.getRet()>>32);
						break;
					default:
						operandStack[top++] = (int)frame.getRet();
						break;
				}
				break;
	
			/** Invoke instance method; special handling for superclass, 
			 *  private, and instance initialization method invocations.
			 * 
			 *   ARGS:  indexbyte1, indexbyte2
			 *   STACK: ..., objectref, [arg1, [arg2 ...]] => ...
			 */
			case INVOKESPECIAL:
				// this probably isn't handling the "unless" section
				// of the VM spec correctly.
				
				methodrefInfo = cls.getMethodRef ( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF) );
				tmp = methodrefInfo.args.size();
				top = (top-1)-tmp;
				inst = heap.get ( (int)operandStack[top] );
				if ( cls.getQName().equals( inst.getQName() ) && method.getName().startsWith("<") )
				{
					// class super class
					if ( !cls.isAccessSuperSet() )
					{
						continue;
					}
					frame = inst.getSuper().callMethod ( heap, methodrefInfo.descriptor + methodrefInfo.methodName, operandStack, top+1, tmp );
				}
				else
				{
					frame = inst.callMethod ( heap, methodrefInfo.descriptor + methodrefInfo.methodName, operandStack, top+1, tmp );
				}
				frame.call ();
				if ( frame.unhandledException() )
				{
					operandStack[top++] = (int)frame.getRet();
					if ( ! processException () )
					{
						return;
					}
					break;
				}
				switch ( methodrefInfo.retType.type )
				{
					case Type.TYPE_VOID:
						break;
					case Type.TYPE_LONG:
					case Type.TYPE_DOUBLE:
						operandStack[top++] = (int)frame.getRet();
						operandStack[top++] = (int)(frame.getRet()>>32);
						break;
					default:
						operandStack[top++] = (int)frame.getRet();
						break;
				}
				break;
				
			/** Invoke a class (static) method.
			 * 
			 *   ARGS:  indexbyte1, indexbyte2
			 *   STACK: ..., [arg1, [arg2 ...]] => ...
			 */
			case INVOKESTATIC:
				methodrefInfo = cls.getMethodRef ( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF) );
				tmp = methodrefInfo.args.size();
				top -= tmp;
				frame = cls.callMethod ( heap, methodrefInfo.descriptor + methodrefInfo.methodName, operandStack, top, tmp, obj.getClassDef() );
				top--;
				frame.call ();
				if ( frame.unhandledException() )
				{
					operandStack[top++] = (int)frame.getRet();
					if ( ! processException () )
					{
						return;
					}
					break;
				}
				switch ( methodrefInfo.retType.type )
				{
					case Type.TYPE_VOID:
						break;
					case Type.TYPE_LONG:
					case Type.TYPE_DOUBLE:
						operandStack[top++] = (int)frame.getRet();
						operandStack[top++] = (int)(frame.getRet()>>32);
						break;
					default:
						operandStack[top++] = (int)frame.getRet();
						break;
				}
				break;
				
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
			case INVOKEINTERFACE:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				tmp2 = (code[pc++]&0xFF);
				ASSERT.fatal ( code[pc] == 0, "DynaFrame", 2291, "INVOKEINTERFACE bad" );
				pc++;
				// untested!
				methodrefInfo = cls.getMethodRef ( tmp );
				tmp = methodrefInfo.args.size();
				top = (top-1)-tmp;
				inst = heap.get ( (int)operandStack[top] );
				frame = inst.callMethod ( heap, methodrefInfo.descriptor + methodrefInfo.methodName, operandStack, top+1, tmp );
				frame.call ();
				if ( frame.unhandledException() )
				{
					operandStack[top++] = (int)frame.getRet();
					if ( ! processException () )
					{
						return;
					}
					break;
				}
				switch ( methodrefInfo.retType.type )
				{
					case Type.TYPE_VOID:
						break;
					case Type.TYPE_LONG:
					case Type.TYPE_DOUBLE:
						operandStack[top++] = (int)frame.getRet();
						operandStack[top++] = (int)(frame.getRet()>>32);
						break;
					default:
						operandStack[top++] = (int)frame.getRet();
						break;
				}
				break;
				
			/** Create new object
			 * 
			 *   ARGS:  indexbyte1, indexbyte2
			 *   STACK:  ... => ...,objectref
			 */
			case NEW:
				operandStack[top++] = heap.newInstance ( cls.getRefClassName( ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF) ) );
				break;
	
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
			case NEWARRAY:
				tmp = (code[pc++]&0xFF);
				operandStack[top-1] = heap.newArray ( "[" + Type.typeToChar( tmp ), 1, tmp, operandStack[top-1], 0, 0 );
				break;

			/** Create new reference array.  The count represents the number of 
			 *  elements in the array to be created.
			 * 
			 *   ARGS:  index1byte,indexbyte2
			 *   STACK:  ...,count => ...,arrayref
			 */
			case ANEWARRAY:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				operandStack[top-1] = heap.newArray ( cls.getRefClassName( tmp) , 1, Type.TYPE_REF, operandStack[top-1], 0, 0 );
				break;
				
			/**  Get length of array.
			 * 
			 *    ARGS:
			 *    STACK:  ..., arrayref => ..., length
			 */
			case ARRAYLENGTH:
				operandStack[top-1] = ((ArrayInstance)heap.get ( operandStack[top-1] )).getLength();
				break;
				
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
			case ATHROW:
				if (! processException() )
				{
					return;
				}
				break;
	
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
			case CHECKCAST:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
//
//  Unimplemented
//
				break;
	
			/** Determine if object is of given type.  The unsigned indexbyte1 
			 *  and indexbyte2 are used to construct an index into the runtime 
			 *  constant pool of the current class (§3.6), where the value of the 
			 *  index is (indexbyte1 << 8) | indexbyte2. The runtime constant 
			 *  pool item at the index must be a symbolic reference to a class, 
			 *  array, or interface type. The named class, array, or interface 
			 *  type is resolved (§5.4.3.1).  If objectref is not null and is an 
			 *  instance of the resolved class or array or implements the resolved 
			 *  interface, the instanceof instruction pushes an int result of 1 as 
			 *  an int on the operand stack. Otherwise, it pushes an int result of 0.
			 * 
			 *   ARGS:  indexbyte1,indexbyte2
			 *   STACK:  ..., objectref => ..., result
			 */
			case INSTANCEOF:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				operandStack[top-1] = heap.get ( operandStack[top-1] ).instanceOf ( cls.getRefClassName ( tmp ) );
				break;
	
			/** Enter monitor for object 
			 * 
			 *   ARGS:  
			 *   STACK: ..., objectref => ...  
			 */
			case MONITORENTER:
				try
				{
					heap.get ( operandStack[--top] ).lock();
				}
				catch ( Exception te )
				{
					// exception processing
					te.printStackTrace();
				}
				break;
	
			/** Exit monitor for object 
			 * 
			 *   ARGS:  
			 *   STACK: ..., objectref => ...  
			 */
			case MONITOREXIT:
				heap.get ( operandStack[--top] ).unlock();
				break;
				
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
			case WIDE:
				wide ();
				break;
	
			/**  Create new multidimensional array.
			 * 
			 *   ARGS:  indexbyte1,indexbyte2,dimensions
			 *   STACK:  ..., count1, [count2, ...] => ..., arrayref
			 */
			case MULTIANEWARRAY:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				tmp2 = (code[pc++]&0xFF);
				if ( tmp2 > 3 )
					throw new Error ( "MULTIANEWARRAY: Array too large" );
				if ( tmp2 == 3 )
				{
					ltmp = operandStack[top-1];
				}
				else //if ( tmp2 == 2 )
				{
					ltmp = 0;
				}
				operandStack[top-tmp2] = heap.newArray ( cls.getRefClassName(tmp) , tmp2, Type.TYPE_REF, (int)operandStack[top-tmp2], (int)operandStack[top-tmp2], (int)ltmp );
				top = (top - tmp2) + 1;
				break;
	
			/**  Branch if reference is null.
			 * 
			 *   ARGS: branchbyte1, branchbyte2
			 *   STACK:  ...,value => ...
			 */
			case IFNULL:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				top--;
				if ( operandStack[top] == 0 )
				{
					pc += tmp;
				}
				break;
	
			/**  Branch if reference is not null.
			 * 
			 *   ARGS:  branchbyte1,branchbyte2
			 *   STACK:  ...,value => ...
			 */
			case IFNONNULL:
				tmp = ((code[pc++]&0xFF) << 8) | (code[pc++]&0xFF);
				if ( operandStack[--top] != 0 )
				{
					pc += tmp;
				}
				break;
	
			/**  Branch always, wide index
			 * 
			 *   ARGS:  branchbyte1,branchbyte2,branchbyte3,branchbyte3
			 *   STACK:
			 */
			case GOTO_W:
				pc += ((((code[pc++]&0xFF) << 24) | ((code[pc++]&0xFF) << 16)) | ((code[pc++]&0xFF) << 8)) | (code[pc++]&0xFF);
				break;
				
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
			case JSR_W:
				operandStack[top++] = pc;
				pc += ((((code[pc++]&0xFF) << 24) | ((code[pc++]&0xFF) << 16)) | ((code[pc++]&0xFF) << 8)) | (code[pc++]&0xFF);
				break;
	
			/** ???
			 */
			case BREAKPOINT:
				break;
				
			default:
				break;
		
		} // switch
		} // try
		catch ( ProgramException pe )
		{
			operandStack[top++] = pe.excptRefId;
			if ( ! processException () )
			{
				return;
			}
		}
		} // while ( true )
	}
	
	private final void tableswitch ()
	{
		int high, low, def, idx, pad = 0;
		
		pc--;
		while ( ((pc+1 + pad) % 4) != 0)
		{
			pad++;
		}
		// extract default
		def = (code[pc+1+pad]<<24) | (code[pc+2+pad]<<16) | (code[pc+3+pad]<<8) | code[pc+4+pad];
		// extact low
		low = (code[pc+5+pad]<<24) | (code[pc+6+pad]<<16) | (code[pc+7+pad]<<8) | code[pc+8+pad];
		// extract high
		high = (code[pc+5+pad]<<24) | (code[pc+6+pad]<<16) | (code[pc+7+pad]<<8) | code[pc+8+pad];
		
		idx = (int)operandStack[--top];
		
		if ( idx > high || idx < low )
		{
			pc += def;
			return;
		}
		pc = code[idx-low + pc+9+pad];
	}
	
	private final void lookupswitch ()
	{
		int pad = 0;
		pc--;
		
		while ( ((pc+1 + pad) % 4) != 0)
		{
			pad++;
		}
		int key = (int)operandStack[--top];
		
		// extract default
		int def = (code[pc+1+pad]<<24) | (code[pc+2+pad]<<16) | (code[pc+3+pad]<<8) | code[pc+4+pad];
		// extact npairs
		int npairs = (code[pc+5+pad]<<24) | (code[pc+6+pad]<<16) | (code[pc+7+pad]<<8) | code[pc+8+pad];
		
		int pos = pc + pad + 9;
		for ( int x = 0; x < npairs; x++ )
		{
			int match = (code[pos]<<24) | (code[pos+1]<<16) | (code[pos+2]<<8) | code[pos+3];
			pos += 4;
			if ( key == match )
			{
				pc += (code[pos]<<24) | (code[pos+1]<<16) | (code[pos+2]<<8) | code[pos+3];
				return;
			}
		}
		pc += def;
	}
	
	private final void wide ()
	{
	}
	
	private final boolean processException( )
	{
		int exceptInst = (int)operandStack[top-1];
		CodeAttribute.CatchInfo ci = method.code.getCatch ( pc );
		if ( ci == null )
		{
			returnValue = exceptInst;
			return false;
		}
		pc = ci.handlerPc;
		return true;
	}
}
