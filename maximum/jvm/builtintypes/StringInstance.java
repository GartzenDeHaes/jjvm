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


public class StringInstance extends Mutex implements ReferenceType, Dispatch
{
	public static final int M_INIT = 0;
	public static final int M_INIT_AB = 1;
	public static final int M_INIT_ABII = 2;
	public static final int M_INIT_ABIIS = 3;
	public static final int M_INIT_ABS = 4;
	public static final int M_INIT_AC = 5;
	public static final int M_INIT_ACII = 6;
	public static final int M_INIT_S = 7;
	public static final int M_INIT_STRINGBUF = 52;
	public static final int M_CHARAT = 8;
	public static final int M_COMPARETO = 9;
	public static final int M_COMPNOCASE = 10;
	public static final int M_CAT = 11;
	public static final int M_ENDSWITH = 14;
	public static final int M_EQUALS = 15;
	public static final int M_EQUALSNOCASE = 16;
	public static final int M_GETBYTES = 17;
	public static final int M_GETBYTES_S = 18;
	public static final int M_GETCHARS = 19;
	public static final int M_HASHCODE = 20;
	public static final int M_INDEXOF_C = 21;
	public static final int M_INDEXOF_CI = 22;
	public static final int M_INDEXOF_S = 23;
	public static final int M_INDEXOF_SI = 24;
	public static final int M_INTERN = 25;
	public static final int M_LASTINDEXOF_C = 26;
	public static final int M_LASTINDEXOF_CI = 27;
	public static final int M_LASTINDEXOF_S = 28;
	public static final int M_LASTINDEXOF_SI = 29;
	public static final int M_LEN = 30;
	public static final int M_REGIONMATCH_ZISII = 31;
	public static final int M_REGIONMATCH_ISII = 32;
	public static final int M_REPLACE = 33;
	public static final int M_STARTSWITH_S = 34;
	public static final int M_STARTSWITH_SI = 35;
	public static final int M_SUBSTRING_I = 36;
	public static final int M_SUBSTRING_II = 37;
	public static final int M_TOCHARARRAY = 38;
	public static final int M_TOLOWER = 39;
	public static final int M_TOSTRING = 40;
	public static final int M_TOUPPER = 41;
	public static final int M_TRIM = 42;
	
	public static final Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt(M_INIT) );
		methodMap.put ( "([B)V<init>", Integers.getInt(M_INIT_AB) );
		methodMap.put ( "([BII)V<init>", Integers.getInt(M_INIT_ABII) );
		methodMap.put ( "([BIILjava/lang/String;)V<init>", Integers.getInt(M_INIT_ABIIS) );
		methodMap.put ( "([BLjava/lang/String;)V<init>", Integers.getInt(M_INIT_ABS) );
		methodMap.put ( "([C)V<init>", Integers.getInt(M_INIT_AC) );
		methodMap.put ( "([CII)V<init>", Integers.getInt(M_INIT_ACII) );
		methodMap.put ( "(Ljava/lang/String;)V<init>", Integers.getInt(M_INIT_S) );
		methodMap.put ( "(Ljava/lang/StringBuffer;)V<init>", Integers.getInt(M_INIT_STRINGBUF) );
		methodMap.put ( "(I)CcharAt", Integers.getInt(M_CHARAT) );
		methodMap.put ( "(Ljava/lang/String;)ZcompareTo", Integers.getInt(M_COMPARETO) );
		methodMap.put ( "(Ljava/lang/String;)ZcompareNoCase", Integers.getInt(M_COMPNOCASE) );
		methodMap.put ( "(Ljava/lang/String;)Ljava/lang/String;concat", Integers.getInt(M_CAT) );
		methodMap.put ( "(Ljava/lang/String;)ZendsWith", Integers.getInt(M_ENDSWITH) );
		methodMap.put ( "(Ljava/lang/String;)Zequals", Integers.getInt(M_EQUALS) );
		methodMap.put ( "(Ljava/lang/String;)ZequalsNoCase", Integers.getInt(M_EQUALSNOCASE) );
		methodMap.put ( "()[BgetBytes", Integers.getInt(M_GETBYTES) );
		methodMap.put ( "(Ljava/lang/String;)[BgetBytes", Integers.getInt(M_GETBYTES_S) );
		methodMap.put ( "()[CgetChars", Integers.getInt(M_GETCHARS) );
		methodMap.put ( "()IhashCode", Integers.getInt(M_HASHCODE) );
		methodMap.put ( "(C)IindexOf", Integers.getInt(M_INDEXOF_C) );
		methodMap.put ( "(CI)IindexOf", Integers.getInt(M_INDEXOF_CI) );
		methodMap.put ( "(Ljava/lang/String;)IindexOf", Integers.getInt(M_INDEXOF_S) );
		methodMap.put ( "(Ljava/lang/String;I)IindexOf", Integers.getInt(M_INDEXOF_SI) );
		methodMap.put ( "()Ljava/lang/String;intern", Integers.getInt(M_INTERN) );
		methodMap.put ( "(C)IlastIndexOf", Integers.getInt(M_LASTINDEXOF_C) );
		methodMap.put ( "(CI)IlastIndexOf", Integers.getInt(M_LASTINDEXOF_CI) );
		methodMap.put ( "(Ljava/lang/String;)IlastIndexOf", Integers.getInt(M_LASTINDEXOF_S) );
		methodMap.put ( "(Ljava/lang/String;I)IlastIndexOf", Integers.getInt(M_LASTINDEXOF_SI) );
		methodMap.put ( "()Ilength", Integers.getInt(M_LEN) );
		methodMap.put ( "(ZILjava/lang/String;II)IregionMatch", Integers.getInt(M_REGIONMATCH_ZISII) );
		methodMap.put ( "(ILjava/lang/String;II)IregionMatch", Integers.getInt(M_REGIONMATCH_ISII) );
		methodMap.put ( "(CC)Vreplace", Integers.getInt(M_REPLACE) );
		methodMap.put ( "(Ljava/lang/String;)ZstartsWith", Integers.getInt(M_STARTSWITH_S) );
		methodMap.put ( "(Ljava/lang/String;I)ZstartsWith", Integers.getInt(M_STARTSWITH_SI) );
		methodMap.put ( "(I)Ljava/lang/String;substring", Integers.getInt(M_SUBSTRING_I) );
		methodMap.put ( "(II)Ljava/lang/String;substring", Integers.getInt(M_SUBSTRING_II) );
		methodMap.put ( "()[CtoCharArray", Integers.getInt(M_TOCHARARRAY) );
		methodMap.put ( "()Ljava/lang/String;toLowerCase", Integers.getInt(M_TOLOWER) );
		methodMap.put ( "()Ljava/lang/String;toString", Integers.getInt(M_TOSTRING) );
		methodMap.put ( "()Ljava/lang/String;toUpperCase", Integers.getInt(M_TOUPPER) );
		methodMap.put ( "()Ljava/lang/String;trim", Integers.getInt(M_TRIM) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	StringInstance ( HeapManager heap, int refId, ClassInstance cls )
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
			ASSERT.fatal ( false, "StringInstance", 127, "Can't happen" );
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
		return "java/lang/String";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/String" ) || className.equals ( "java/lang/Object") )
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

	String data;
	
	public void set (String s)
	{
		data = s;
	}
	
	public String get ( )
	{
		return data;
	}
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		int inst = 0;
		
		switch ( method )
		{
			case M_INIT:
				break;
			case M_INIT_AB:
			case M_INIT_ABII:
			case M_INIT_ABIIS:
			case M_INIT_ABS:
			case M_INIT_AC:
			case M_INIT_ACII:
				data = ((ArrayInstance)heap.get((int)args[0]) ).arrayToString();
				break;
			case M_INIT_S:
				data = ((StringInstance)heap.get ((int)args[0])).data;
				break;
			case M_INIT_STRINGBUF:
				throw new Error();
			case M_CHARAT:
				return data.charAt((int)args[0]);
			case M_COMPARETO:
				return data.compareTo (((StringInstance)heap.get ((int)args[0])).data);
			case M_COMPNOCASE:
				return data.compareTo (((StringInstance)heap.get ((int)args[0])).data);
			case M_CAT:
				try
				{
					inst = heap.newInstance( "java/lang/String" );
				}
				catch ( Exception cfne )
				{
					cfne.printStackTrace();
					ASSERT.fatal ( false, "StringInstance", 207, "Impossible condition" );
				}
				((StringInstance)heap.get(inst)).data = data + ((StringInstance)heap.get((int)args[0])).data;
				return inst;
			case M_ENDSWITH:
				if ( data.endsWith( ((StringInstance)heap.get((int)args[0]) ).data) )
				{
					return 1;
				}
				return 0;
			case M_EQUALS:
				if ( data.equals( ((StringInstance)heap.get((int)args[0]) ).data) )
				{
					return 1;
				}
				return 0;
			case M_EQUALSNOCASE:
				if ( data.equals( ((StringInstance)heap.get((int)args[0]) ).data) )
				{
					return 1;
				}
				return 0;
			case M_GETBYTES:
			case M_GETBYTES_S:
				try
				{
					byte[] ba = data.getBytes();
					inst = heap.newArray( "[B", 1, Type.TYPE_BYTE, ba.length, 0, 0 );
					ArrayInstance ai = (ArrayInstance)heap.get(inst);
					for ( int x = 0; x < ba.length; x ++ )
					{
						ai.array[x] = ba[x];
					}
				}
				catch ( Exception ioe )
				{
					ioe.printStackTrace();
					ASSERT.fatal ( false, "StringInstance", 238, "Impossible condition" );
				}
				return inst;
			case M_GETCHARS:
				try
				{
					char[] ca = new char[data.length()];
					data.getChars( (int)args[0], (int)args[1], ca, (int)args[3]);
					ArrayInstance ai = (ArrayInstance)heap.get((int)args[2]);
					for ( int x = 0; x < ca.length; x ++ )
					{
						ai.array[x] = ca[x];
					}
				}
				catch ( Exception ioe )
				{
					ioe.printStackTrace();
					ASSERT.fatal ( false, "StringInstance", 238, "Impossible condition" );
				}
				return inst;
			case M_HASHCODE:
				return data.hashCode();
			case M_INDEXOF_C:
				return data.indexOf( (char)args[0] );
			case M_INDEXOF_CI:
				return data.indexOf( (char)args[0], (int)args[1] );
			case M_INDEXOF_S:
				StringInstance si = (StringInstance)heap.get ( (int)args[0] );
				return data.indexOf( si.data );
			case M_INDEXOF_SI:
				si = (StringInstance)heap.get ( (int)args[0] );
				return data.indexOf( si.data, (int)args[1] );
			case M_INTERN:
				return id;
			case M_LASTINDEXOF_C:
				return data.lastIndexOf ( (char)args[0] );
			case M_LASTINDEXOF_CI:
				return data.lastIndexOf ( (char)args[0], (int)args[1] );
			case M_LASTINDEXOF_S:
				si = (StringInstance)heap.get ( (int)args[0] );
				return data.lastIndexOf( si.data );				
			case M_LASTINDEXOF_SI:
				si = (StringInstance)heap.get ( (int)args[0] );
				return data.lastIndexOf( si.data, (int)args[1] );	
			case M_LEN:
				return data.length();
			case M_REGIONMATCH_ZISII:
				si = (StringInstance)heap.get ( (int)args[2] );
				if ( data.regionMatches( args[0] != 0, (int)args[1], si.data, (int)args[3], (int)args[4] ) )
				{
					return 1;
				}
				return 0;
			case M_REGIONMATCH_ISII:
				si = (StringInstance)heap.get ( (int)args[1] );
				if ( data.regionMatches( (int)args[0], si.data, (int)args[2], (int)args[3] ) )
				{
					return 1;
				}
				return 0;
			case M_REPLACE:
				data.replace( (char)args[0], (char)args[1] );
				break;
			case M_STARTSWITH_S:
				si = (StringInstance)heap.get ( (int)args[0] );
				if ( data.startsWith ( si.data ) )
				{
					return 1;
				}
				return 0;
			case M_STARTSWITH_SI:
				si = (StringInstance)heap.get ( (int)args[0] );
				if ( data.startsWith ( si.data, (int)args[1] ) )
				{
					return 1;
				}
				return 0;
			case M_SUBSTRING_I:
				return heap.getRefToConstString ( data.substring ( (int)args[0] ) );
			case M_SUBSTRING_II:
				return heap.getRefToConstString ( data.substring ( (int)args[0], (int)args[1] ) );
			case M_TOCHARARRAY:
				char[] ca = data.toCharArray();
				try
				{
					inst = heap.newArray( "[C", 1, Type.TYPE_CHAR, ca.length, 0, 0 );
				}
				catch ( Exception ioe )
				{
					ioe.printStackTrace();
				}
				ArrayInstance ai = (ArrayInstance)heap.get(inst);
				for ( int x = 0; x < ca.length; x ++ )
				{
					ai.array[x] = ca[x];
				}
			case M_TOLOWER:
				return heap.getRefToConstString( data.toLowerCase() );
			case M_TOSTRING:
				return id;
			case M_TOUPPER:
				return heap.getRefToConstString ( data.toUpperCase() );
			case M_TRIM:
		}
	return 0;
	}
}
