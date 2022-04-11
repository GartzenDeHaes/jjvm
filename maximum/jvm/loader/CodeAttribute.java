/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.FastVector;


class CodeAttribute
{
	private int stackSize;//    	u2 max_stack;
	private int localSize;//	 	u2 max_locals;
    //	u4 code_length;
	private byte[] code;	//    	u1 code[code_length];
    //	u2 exception_table_length;
    //	{    	u2 start_pc;
    //	      	u2 end_pc;
    //	      	u2  handler_pc;
    //	      	u2  catch_type;
    // 	}	exception_table[exception_table_length];
	private FastVector catches;
	
    //	u2 attributes_count;
    //	attribute_info attributes[attributes_count];
    private FastVector attributes;
	
	CodeAttribute ( DataInputStream dis, FastVector cp ) throws IOException
	{
		stackSize = dis.readShort();
		localSize = dis.readShort();
		int codeLen = dis.readInt ();
		code = new byte[codeLen];
		dis.read ( code );
		
		int numExceptions = dis.readShort();
		catches = new FastVector ( numExceptions );
		
		for ( int x = 0; x < numExceptions; x++ )
		{
			catches.addElement ( new CatchInfo ( dis ) );
		}
		int attrCount = dis.readShort();
		attributes = new FastVector ( attrCount );
		
		for ( int x = 0; x < attrCount; x++ )
		{
			attributes.addElement ( new Attribute_Info ( dis, cp ) );
		}
	}
	
	int getStackSize()
	{
		return stackSize;
	}
	
	int getLocalSize()
	{
		return localSize;
	}
	
	byte[] getCode ()
	{
		return code;
	}
	
	CatchInfo getCatch ( int pc )
	{
		CatchInfo ci;
		
		for ( int x = 0; x < catches.size(); x++ )
		{
			ci = (CatchInfo)catches.elementAt ( x );
			if ( pc >= ci.startPc && pc <= ci.endPc )
			{
				return ci;
			}
		}
		return null;
	}
	
	class CatchInfo
	{
		int startPc;
		int endPc;
		int handlerPc;
		int catchType;
		
		CatchInfo( DataInputStream dis ) throws IOException
		{
			startPc = dis.readShort();
			endPc = dis.readShort();
			handlerPc = dis.readShort();
			catchType = dis.readShort();
		}
		
		CatchInfo ( int s, int e, int h, int t )
		{
			startPc = s;
			endPc = e;
			handlerPc = h;
			catchType = t;
		}
	}
	
	//********************************
	// Write interface
	//********************************
	int codeUtfIndex;
	
	public CodeAttribute ( int codeUtfIndex, int locSize )
	{
		this.codeUtfIndex = codeUtfIndex;
		localSize = locSize;
		stackSize = 10;
		catches = new FastVector();
		attributes = new FastVector();
	}
	
	public void setCode ( byte[] ba )
	{
		code = ba;
	}
	
	public void addCatch ( int startPc, int endPc, int handlerPc, int catchType )
	{
		catches.addElement ( new CatchInfo ( startPc, endPc, handlerPc, catchType ) );
	}
}
