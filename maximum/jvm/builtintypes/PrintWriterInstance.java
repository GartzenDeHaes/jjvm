/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.utility.Integers;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.Frame;
import maximum.jvm.ClassDef;
import maximum.jvm.Type;


public class PrintWriterInstance extends Mutex implements ReferenceType, Dispatch
{
	private static final int M_INIT = 0;
	private static final int M_INIT_PS = 1;
	private static final int M_INIT_PSZ = 2;
	private static final int M_INIT_W = 3;
	private static final int M_INIT_WZ = 4;
	private static final int M_CHECKERROR = 5;
	private static final int M_CLOSE = 6;
	private static final int M_FLUSH = 7;
	private static final int M_PRINT_B = 8;
	private static final int M_PRINT_C = 9;
	private static final int M_PRINT_CA = 10;
	private static final int M_PRINT_D = 11;
	private static final int M_PRINT_F = 12;
	private static final int M_PRINT_I = 13;
	private static final int M_PRINT_J = 14;
	private static final int M_PRINT_O = 15;
	private static final int M_PRINT_S = 16;
	private static final int M_PRINTLN = 17;
	private static final int M_PRINTLN_B = 18;
	private static final int M_PRINTLN_C = 19;
	private static final int M_PRINTLN_CA = 20;
	private static final int M_PRINTLN_D = 21;
	private static final int M_PRINTLN_F = 22;
	private static final int M_PRINTLN_I = 23;
	private static final int M_PRINTLN_J = 24;
	private static final int M_PRINTLN_O = 25;
	private static final int M_PRINTLN_S = 26;
	private static final int M_WRITE_CA = 28;
	private static final int M_WRITE_CAII = 29;
	private static final int M_WRITE_I = 30;
	private static final int M_WRITE_S = 31;
	private static final int M_WRITE_SII = 32;

	private static final Hashtable methodMap;
	
	static 
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt ( M_INIT ) );
		methodMap.put ( "(Ljava/io/PrintStream;)V<init>", Integers.getInt ( M_INIT_PS ) );
		methodMap.put ( "(Ljava/io/PrintStream;Z)V<init>", Integers.getInt ( M_INIT_PSZ ) );
		methodMap.put ( "(Ljava/io/Writer;)V<init>", Integers.getInt ( M_INIT_W ) );
		methodMap.put ( "(Ljava/io/Writer;Z)V<init>", Integers.getInt ( M_INIT_WZ ) );
		methodMap.put ( "()ZcheckError", Integers.getInt ( M_CHECKERROR ) );
		methodMap.put ( "()Vflush", Integers.getInt ( M_FLUSH ) );
		methodMap.put ( "(B)Vprint", Integers.getInt ( M_PRINT_B ) );
		methodMap.put ( "(C)Vprint", Integers.getInt ( M_PRINT_C ) );
		methodMap.put ( "([C)Vprint", Integers.getInt ( M_PRINT_CA ) );
		methodMap.put ( "(D)Vprint", Integers.getInt ( M_PRINT_D ) );
		methodMap.put ( "(F)Vprint", Integers.getInt ( M_PRINT_F ) );
		methodMap.put ( "(I)Vprint", Integers.getInt ( M_PRINT_I ) );
		methodMap.put ( "(J)Vprint", Integers.getInt ( M_PRINT_J ) );
		methodMap.put ( "(Ljava/lang/Object;)Vprint", Integers.getInt ( M_PRINT_O ) );
		methodMap.put ( "(Ljava/lang/String;)Vprint", Integers.getInt ( M_PRINT_S ) );
		methodMap.put ( "()Vprintln", Integers.getInt ( M_PRINTLN ) );
		methodMap.put ( "(B)Vprintln", Integers.getInt ( M_PRINTLN_B ) );
		methodMap.put ( "(C)Vprintln", Integers.getInt ( M_PRINTLN_C ) );
		methodMap.put ( "([C)Vprintln", Integers.getInt ( M_PRINTLN_CA ) );
		methodMap.put ( "(D)Vprintln", Integers.getInt ( M_PRINTLN_D ) );
		methodMap.put ( "(F)Vprintln", Integers.getInt ( M_PRINTLN_F ) );
		methodMap.put ( "(I)Vprintln", Integers.getInt ( M_PRINTLN_I ) );
		methodMap.put ( "(J)Vprintln", Integers.getInt ( M_PRINTLN_J ) );
		methodMap.put ( "(Ljava/lang/Object;)Vprintln", Integers.getInt ( M_PRINTLN_O ) );
		methodMap.put ( "(Ljava/lang/String;)Vprintln", Integers.getInt ( M_PRINTLN_S ) );
		methodMap.put ( "([C)Vwrite", Integers.getInt ( M_WRITE_CA ) );
		methodMap.put ( "([CII)Vwrite", Integers.getInt ( M_WRITE_CAII ) );
		methodMap.put ( "(I)Vwrite", Integers.getInt ( M_WRITE_I ) );
		methodMap.put ( "(Ljava/lang/String;)Vwrite", Integers.getInt ( M_WRITE_S ) );
		methodMap.put ( "(Ljava/lang/String;II)Vwrite", Integers.getInt ( M_WRITE_SII ) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	PrintWriterInstance ( int refId, ClassInstance cls, HeapManager heap )
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
			ASSERT.fatal ( false, "PrintWriterInstance", 103, "Can't happen" );
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
		return "java/io/PrintWriter";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Object") || className.equals ( "java/io/PrintWriter") )
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
		DispatchFrame dm = new DispatchFrame ( heap, ((Integer)methodMap.get(desc)).intValue(), this, args, pos, len, myClass );
		return dm;
	}
	
	StringBuffer output = new StringBuffer();
	
	public String getOutput()
	{
		return output.toString();
	}
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		ArrayInstance ai;
		StringInstance si;
		
		switch ( method )
		{
			case M_INIT:
				break;
			case M_INIT_PS:
				break;
			case M_INIT_PSZ:
				break;
			case M_INIT_W:
				break;
			case M_INIT_WZ:
				break;
			case M_CHECKERROR:
				return 0;
			case M_CLOSE:
				break;
			case M_FLUSH:
				break;
			case M_PRINT_B:
				output.append ( (byte)args[0] );
				break;
			case M_PRINT_C:
				output.append ( (char)args[0] );
				break;
			case M_WRITE_CA:
			case M_PRINT_CA:
				ai = (ArrayInstance)heap.get ( (int)args[0] );
				output.append(ai.arrayToString ());
				break;
			case M_PRINT_D:
				output.append ( Double.longBitsToDouble ( args[0] ));
				break;
			case M_PRINT_F:
				output.append ( Float.intBitsToFloat ( (int) args[0] ));
				break;
			case M_WRITE_I:
			case M_PRINT_I:
				output.append ( (int)args[0] );
				break;
			case M_PRINT_J:
				output.append ( args[0] );
				break;
			case M_PRINT_O:
				break;
			case M_WRITE_S:
			case M_PRINT_S:
				si = (StringInstance)heap.get ( (int)args[0] );
				output.append(si.data);
				break;
			case M_PRINTLN:
				output.append ("\r\n");
				break;
			case M_PRINTLN_B:
				output.append ( (byte)args[0] + "\r\n" );
				break;
			case M_PRINTLN_C:
				output.append ( (char)args[0] + "\r\n" );
				break;
			case M_PRINTLN_CA:
				ai = (ArrayInstance)heap.get ( (int)args[0] );
				output.append(ai.arrayToString () + "\r\n");
				break;
			case M_PRINTLN_D:
				output.append ( Double.longBitsToDouble(args[0]) + "\r\n" );
				break;
			case M_PRINTLN_F:
				output.append ( Float.intBitsToFloat( (int)args[0] ) + "\r\n" );
				break;
			case M_PRINTLN_I:
				output.append ( (int)args[0] + "\r\n" );
				break;
			case M_PRINTLN_J:
				output.append ( args[0] + "\r\n" );
				break;
			case M_PRINTLN_O:
				break;
			case M_PRINTLN_S:
				si = (StringInstance)heap.get ( (int)args[0] );
				output.append ( si.data + "\r\n" );
				break;
			case M_WRITE_CAII:
			case M_WRITE_SII:
		}
	return 0;
	}
}
