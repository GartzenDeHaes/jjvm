/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.utility.Integers;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;


public class StringClass implements ClassDef, Dispatch
{
	public static final int M_COPY_AC = 1;
	public static final int M_COPY_ACII = 2;
	public static final int M_VALUEOF_Z = 3;
	public static final int M_VALUEOF_C = 4;
	public static final int M_VALUEOF_AC = 5;
	public static final int M_VALUEOF_ACII = 6;
	public static final int M_VALUEOF_D = 7;
	public static final int M_VALUEOF_F = 8;
	public static final int M_VALUEOF_I = 9;
	public static final int M_VALUEOF_J = 10;
	public static final int M_VALUEOF_Lobj = 11;

	public static final Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "([C)Ljava/lang/String;copyValueOf", Integers.getInt ( M_COPY_AC ) );
		methodMap.put ( "([CII)Ljava/lang/String;copyValueOf", Integers.getInt ( M_COPY_ACII ) );
		methodMap.put ( "(Z)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_Z ) );
		methodMap.put ( "(C)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_C ) );
		methodMap.put ( "([C)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_AC ) );
		methodMap.put ( "([CII)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_ACII ) );
		methodMap.put ( "(D)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_D ) );
		methodMap.put ( "(F)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_F ) );
		methodMap.put ( "(I)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_I ) );
		methodMap.put ( "(J)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_J ) );
		methodMap.put ( "(Ljava/lang/Object;)Ljava/lang/String;valueOf", Integers.getInt ( M_VALUEOF_Lobj ) );
	}
	
	ClassDef superClass;
	
	public StringClass ( ObjectClass sup )
	{
		superClass = sup;
	}
	
	public boolean implementsInterface( String clsName )
	{
		return clsName.equals ( "java/io/Serializable" );
	}
	
	/**  Builtins don't need to be reloaded.
	 */
	public void reload ( DataInputStream dis ) throws IOException
	{
	}
	
	/** builtins don't care about references */
	public void addRef ()
	{
	}
	
	public void release ()
	{
	}
	
	public ClassDef getSuperClass ()
	{
		return superClass;
	}

	/**
	 *  Get the fully qualified name
	 */
	public String getQName ()
	{
		return "java/lang/String";
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		return new DispatchFrame( heap, ((Integer)methodMap.get (desc)).intValue(), this, args, pos, len, ci );
	}
	
	public int getFieldIndex ( String name )
	{
		return 0;
	}
	
	public int getStaticDataSize ( )
	{
		return 0;
	}
	
	public ClassInstance createClassInstance ( int refId, HeapManager heap )
	{
		try
		{
			return new ClassInstance ( heap, refId, this );
		}
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "StringClass", 118, "Internal Error" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		ReferenceType rt = new StringInstance ( heap, refId, ci );
		return rt;
	}
	
	public long call ( HeapManager heap, int methodId, int[] args, ClassInstance ci )
	{
		switch ( methodId )
		{
			case M_COPY_AC:
				return heap.getRefToConstString (((ArrayInstance)heap.get((int)args[0]) ).arrayToString());
			case M_COPY_ACII:
				return heap.getRefToConstString (((ArrayInstance)heap.get((int)args[0]) ).arrayToString());
			case M_VALUEOF_ACII:
				return heap.getRefToConstString ( String.valueOf(((ArrayInstance)heap.get((int)args[0]) ).arrayToString() ));				
			case M_VALUEOF_AC:
				return heap.getRefToConstString ( String.valueOf(((ArrayInstance)heap.get((int)args[0]) ).arrayToString() ));				
			
			case M_VALUEOF_Z:
				if ( args[0] == 0 )
				{
					return heap.getRefToConstString ( String.valueOf(true) );
				}
				return heap.getRefToConstString ( String.valueOf(false) );
			case M_VALUEOF_C:
				return heap.getRefToConstString ( String.valueOf((char)args[0]) );
			case M_VALUEOF_D:
				return heap.getRefToConstString ( String.valueOf(Double.longBitsToDouble( args[0] )) );
			case M_VALUEOF_F:
				return heap.getRefToConstString ( String.valueOf(Float.intBitsToFloat( (int)args[0] )) );
			case M_VALUEOF_I:
				return heap.getRefToConstString ( String.valueOf((int)args[0]) );
			case M_VALUEOF_J:
				return heap.getRefToConstString ( String.valueOf(args[0]) );
			case M_VALUEOF_Lobj:
				return heap.getRefToConstString ( String.valueOf(heap.get( (int)args[0]) ) );
		}
		return 0;
	}	
}
