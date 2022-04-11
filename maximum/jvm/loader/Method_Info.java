/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.io.IOException;
import java.io.DataInputStream;

import maximum.utility.ByteVector;
import maximum.utility.FastVector;
import maximum.jvm.Frame;
import maximum.jvm.loader.CodeAttribute;
import maximum.jvm.loader.ExceptionAttribute;
import maximum.jvm.Type;

/**
 *  Definition for a method in a class.
 */
public class Method_Info
{
	public static final int ACC_PUBLIC = 0x0001;  //Declared public; may be accessed from outside its package.  
	public static final int ACC_PRIVATE = 0x0002;  //Declared private; accessible only within the defining class.  
	public static final int ACC_PROTECTED = 0x0004;  //Declared protected; may be accessed within subclasses.  
	public static final int ACC_STATIC = 0x0008;  //Declared static.  
	public static final int ACC_FINAL = 0x0010;  //Declared final; may not be overridden.  
	public static final int ACC_SYNCHRONIZED = 0x0020;  //Declared synchronized; invocation is wrapped in a monitor lock.  
	public static final int ACC_NATIVE = 0x0100;  //Declared native; implemented in a language other than Java.  
	public static final int ACC_ABSTRACT = 0x0400;  //Declared abstract; no implementation is provided.  
	public static final int ACC_STRICT = 0x0800;  //Declared strictfp; floating-point mode is FP-strict  

	int accessFlags;
	FastVector attribs;
	
	String methodName;
	String descriptor;
	
	CodeAttribute code;
	ExceptionAttribute excpt;
	FastVector args = new FastVector();
	Type retType;
	
	Method_Info ( DataInputStream dis, FastVector cp ) throws IOException
	{		
		accessFlags = dis.readShort();
		methodName = ((Utf8_Info)cp.elementAt ( dis.readShort() )).str;
		descriptor = ((Utf8_Info)cp.elementAt ( dis.readShort() )).str;
		
		int attrCount = dis.readShort ();
		attribs = new FastVector ( attrCount );
		
		for ( int x = 0; x < attrCount; x++ )
		{
			Attribute_Info ai = new Attribute_Info ( dis, cp );
			attribs.addElement ( ai );
			if ( ai.name.equals ( "Code" ) )
			{
				code = (CodeAttribute)ai.data;
			}
			else if ( ai.name.equals ( "Exceptions" ) )
			{
				excpt = (ExceptionAttribute)ai.data;
			}
		}
		DescriptorParser.parse ( args, descriptor );
		retType = (Type)args.pop ();
	}
		
	public String getName ()
	{
		return methodName;
	}
	
	/**
	 *  Create a runtime stack frame.
	 */
	public Frame createFrame ()
	{
		return null;
	}
	
	/**
	 *  Returns true if this is a synchronized method.
	 */
	public boolean isSynchronized ()
	{
		return (accessFlags & ACC_SYNCHRONIZED) != 0;
	}
	
	/**
	 *   Get the number of arguments that should
	 *   be on the operand stack when method is 
	 *   called.
	 */
	public int getArgCount ()
	{
		return args.size ();
	}
	
	/**
	 *  Get the number of integers used by the argument
	 */
	public int getArgSize ( )
	{
		int size = 0;
		
		for ( int x = 0; x < args.size(); x++ )
		{
			size += ((Type)args.elementAt(x)).intsize;
		}
		return size;
	}

	//****************************************
	// WRITE interface
	//****************************************
	
	int methodNameIdx, descIdx;
	ByteVector codeaccum;
	
	public Method_Info ( int numLocals, int nameIdx, int descIdx, int codeIndex, int exceptIndex, FastVector cp )
	{
		codeaccum = new ByteVector();
		methodNameIdx = nameIdx;
		this.descIdx = descIdx;
		methodName = ((Utf8_Info)cp.elementAt ( nameIdx )).str;
		descriptor = ((Utf8_Info)cp.elementAt ( descIdx )).str;		
		DescriptorParser.parse ( args, descriptor );
		retType = (Type)args.pop ();
		
		code = new CodeAttribute ( codeIndex, getArgSize()+numLocals );
		excpt = new ExceptionAttribute ( );
	}
	
	public int appendCode ( byte op )
	{
		return codeaccum.addElement ( op );
	}
	
	public int appendCode ( byte op, byte arg1 )
	{
		int ret = codeaccum.addElement ( op );
		codeaccum.addElement ( arg1 );
		return ret;
	}
	
	public int appendCode ( byte op, byte arg1, byte arg2 )
	{
		int ret = codeaccum.addElement ( op );
		codeaccum.addElement ( arg1 );
		codeaccum.addElement ( arg2 );
		return ret;
	}
	
	public void writeByte ( int pos, byte b )
	{
		codeaccum.setElementAt ( pos, b );
	}

	public int getCodePos (  )
	{
		return codeaccum.size();
	}
}
