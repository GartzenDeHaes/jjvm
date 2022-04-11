/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.utility.Integers;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.Type;

public class StringBufferInstance extends Mutex implements ReferenceType, Dispatch
{
	public static final int M_INIT = 0;
	public static final int M_INIT_I = 1;
	public static final int M_INIT_S = 2;
	public static final int M_APPEND_Z = 3;
	public static final int M_APPEND_C = 4;
	public static final int M_APPEND_CA = 5;
	public static final int M_APPEND_CAII = 6;
	public static final int M_APPEND_D = 7;
	public static final int M_APPEND_F = 8;
	public static final int M_APPEND_I = 9;
	public static final int M_APPEND_J = 10;
	public static final int M_APPEND_O = 11;
	public static final int M_APPEND_S = 12;
	public static final int M_CAPACITY = 13;
	public static final int M_CHARAT = 14;
	public static final int M_ENSURECAPACITY = 15;
	public static final int M_GETCHARS_IICAI = 16;
	public static final int M_INSERT_IZ = 17;
	public static final int M_INSERT_IC = 18;
	public static final int M_INSERT_ICA = 19;
	public static final int M_INSERT_ID = 20;
	public static final int M_INSERT_IF = 21;
	public static final int M_INSERT_II = 22;
	public static final int M_INSERT_IJ = 23;
	public static final int M_INSERT_IO = 24;
	public static final int M_INSERT_IS = 25;
	public static final int M_LENGTH = 26;
	public static final int M_REVERSE = 27;
	public static final int M_SETCHARAT = 28;
	public static final int M_SETLEN = 29;
	public static final int M_TOSTRING = 30;
	
	public static final Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt(M_INIT) );
		methodMap.put ( "(I)V<init>", Integers.getInt(M_INIT_I) );
		methodMap.put ( "(Ljava/lang/String;)V<init>", Integers.getInt(M_INIT_S) );
		methodMap.put ( "(Z)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_Z) );
		methodMap.put ( "(C)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_C) );
		methodMap.put ( "([C)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_CA) );
		methodMap.put ( "([CII)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_CAII) );
		methodMap.put ( "(D)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_D) );
		methodMap.put ( "(F)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_F) );
		methodMap.put ( "(I)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_I) );
		methodMap.put ( "(J)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_J) );
		methodMap.put ( "(Ljava/lang/Object;)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_O) );
		methodMap.put ( "(Ljava/lang/String;)Ljava/lang/StringBuffer;append", Integers.getInt(M_APPEND_S) );
		methodMap.put ( "()capacity", Integers.getInt(M_CAPACITY) );
		methodMap.put ( "(I)CcharAt", Integers.getInt(M_CHARAT) );
		methodMap.put ( "(I)VensureCapacity", Integers.getInt(M_ENSURECAPACITY) );
		methodMap.put ( "(II[CI)VgetChars", Integers.getInt(M_GETCHARS_IICAI) );
		methodMap.put ( "(IZ)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IZ) );
		methodMap.put ( "(IC)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IC) );
		methodMap.put ( "(I[C)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_ICA) );
		methodMap.put ( "(ID)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_ID) );
		methodMap.put ( "(IF)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IF) );
		methodMap.put ( "(II)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_II) );
		methodMap.put ( "(IJ)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IJ) );
		methodMap.put ( "(ILjava/lang/Object;)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IO) );
		methodMap.put ( "(ILjava/lang/String;)Ljava/lang/StringBuffer;insert", Integers.getInt(M_INSERT_IS) );
		methodMap.put ( "()Vlength", Integers.getInt(M_LENGTH) );
		methodMap.put ( "()Ljava/lang/StringBuffer;reverse", Integers.getInt(M_REVERSE) );
		methodMap.put ( "(IC)VsetCharAt", Integers.getInt(M_SETCHARAT) );
		methodMap.put ( "(I)VsetLength", Integers.getInt(M_SETLEN) );
		methodMap.put ( "()Ljava/lang/String;toString", Integers.getInt(M_TOSTRING) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	StringBufferInstance ( HeapManager heap, int refId, ClassInstance cls )
	{
		id = refId;
		refCount = 0;
		myClass = cls;
		try
		{
			mySuper = heap.get ( heap.newInstance("java/lang/Object") );
		}
		catch ( Exception cnfe )
		{
			ASSERT.fatal ( false, "StringBufferInstance", 104, "Can't happen" );
		}
	}
	
	public ReferenceType getSuper()
	{
		return mySuper;
	}
	
	public void addRef ()
	{
		refCount++;
	}
	
	public void release ()
	{
		refCount--;
	}

	public int getRefCount ()
	{
		return refCount;
	}

	public int getRefId ()
	{
		return id;
	}
	
	public String getQName ()
	{
		return "java/lang/StringBuffer";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/StringBuffer" ) || className.equals ( "java/lang/Object") )
		{
			return 1;
		}
		return 0;
	}
	
	public long getField ( String name )
	{
		return 0;
	}
	
	public void setField ( String name, long value )
	{
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len )
	{
		return new DispatchFrame ( heap, ((Integer)methodMap.get(desc)).intValue(), this, args, pos, len, myClass );
	}

	StringBuffer data = new StringBuffer();

	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		switch ( method )
		{
			case M_INIT:
				break;
			case M_INIT_I:
				data = new StringBuffer ( (int)args[0] );
				break;
			case M_INIT_S:
				data = new StringBuffer ( ((StringInstance)heap.get((int)args[0])).data );
				break;
			case M_APPEND_Z:
				data.append ( args[0] != 0 );
				return id;
			case M_APPEND_C:
				data.append ( (char)args[0] );
				return id;
			case M_APPEND_CA:
				System.out.println ("StringBuffer.append([C) called");
				return id;
			case M_APPEND_CAII:
				System.out.println ("StringBuffer.append([CII) called");
				return id;
			case M_APPEND_D:
				data.append ( Double.longBitsToDouble(args[0]) );
				return id;
			case M_APPEND_F:
				data.append ( Float.intBitsToFloat((int)args[0]) );
				return id;
			case M_APPEND_I:
				data.append ((int)args[0]);
				return id;
			case M_APPEND_J:
				data.append ( args[0] );
				return id;
			case M_APPEND_O:
				System.out.println ("StringBuffer.append(Object) called");
				return id;
			case M_APPEND_S:
				data.append ( ((StringInstance)heap.get((int)args[0])).data );
				return id;
			case M_CAPACITY:
				return data.capacity();
			case M_CHARAT:
				return data.charAt ( (int)args[0] );
			case M_ENSURECAPACITY:
				data.ensureCapacity ( (int)args[0] );
				break;
			case M_GETCHARS_IICAI:
				System.out.println ("StringBuffer.getChars(II[CI) called");
				break;
			case M_INSERT_IZ:
				data.insert ( (int)args[0], args[1] != 0 );
				return id;
			case M_INSERT_IC:
				data.insert ( (int)args[0], (char)args[1] );
				return id;
			case M_INSERT_ICA:
				System.out.println ("StringBuffer.append(I[C) called");
				return id;
			case M_INSERT_ID:
				data.insert ( (int)args[0], Double.longBitsToDouble(args[1]) );
				return id;
			case M_INSERT_IF:
				data.insert ( (int)args[0], Float.intBitsToFloat ((int)args[1]) );
				return id;
			case M_INSERT_II:
				data.insert ( (int)args[0], (int)args[1] );
				return id;
			case M_INSERT_IJ:
				data.insert ( (int)args[0], args[1] );
				return id;
			case M_INSERT_IO:
				System.out.println ("StringBuffer.insert(Object) called");
				return id;
			case M_INSERT_IS:
				data.insert ( (int)args[0], ((StringInstance)heap.get((int)args[1])).data );
				return id;
			case M_LENGTH:
				return data.length();
			case M_REVERSE:
				data.reverse();
				return id;
			case M_SETCHARAT:
				data.setCharAt ((int)args[0], (char)args[1] );
				break;
			case M_SETLEN:
				data.setLength ( (int)args[0] );
				break;
			case M_TOSTRING:
				return heap.getRefToConstString ( data.toString() );
		}
		return 0;
	}
}
