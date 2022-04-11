/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;

import maximum.utility.Integers;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.Frame;
import maximum.jvm.ClassDef;
import maximum.jvm.Type;

												 
public class ObjectInstance extends Mutex implements ReferenceType, Dispatch
{
	private static final int M_INIT = 0;
	private static final int M_CLONE = 1;
	private static final int M_EQUALS_L = 2;
	private static final int M_FINIALIZE = 3;
	private static final int M_GETCLASS = 4;
	private static final int M_HASHCODE = 5;
	private static final int M_NOTIFY = 6;
	private static final int M_NOTIFYALL = 7;
	private static final int M_TOSTRING = 8;
	private static final int M_WAIT = 9;
	private static final int M_WAIT_J = 10;
	private static final int M_WAIT_JI = 11;
	
	private static final Hashtable methodMap;
	
	static 
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt ( M_INIT ) );
		methodMap.put ( "()Vclone", Integers.getInt ( M_CLONE ) );
		methodMap.put ( "(Ljava/lang/Object;)Bequals", Integers.getInt ( M_EQUALS_L ) );
		methodMap.put ( "()Vfinalize", Integers.getInt ( M_FINIALIZE ) );
		methodMap.put ( "()Ljava/lang/Class;getClass", Integers.getInt ( M_GETCLASS ) );
		methodMap.put ( "()Ihashcode", Integers.getInt ( M_HASHCODE ) );
		methodMap.put ( "()Vnotify", Integers.getInt ( M_NOTIFY ) );
		methodMap.put ( "()VnotifyAll", Integers.getInt ( M_NOTIFYALL ) );
		methodMap.put ( "()Ljava/lang/String;toString", Integers.getInt ( M_TOSTRING ) );
		methodMap.put ( "()Vwait", Integers.getInt ( M_WAIT ) );
		methodMap.put ( "(J)Vwait", Integers.getInt ( M_WAIT_J ) );
		methodMap.put ( "(JI)", Integers.getInt ( M_WAIT_JI ) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	
	ObjectInstance ( int refId, ClassInstance cls )
	{
		id = refId;
		refCount = 0;
		myClass = cls;
	}
	
	public ReferenceType getSuper()
	{
		return null;
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
		return "java/lang/Object";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Object") )
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
		DispatchFrame dm = new DispatchFrame ( heap, 0, this, args, pos, len, myClass );
		return dm;
	}
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		try
		{
			switch ( method )
			{
				case M_INIT:
					return 0;
				case M_CLONE:
					return 0;
				case M_EQUALS_L:
					if ( id == args[0] )
					{
						return 1;
					}
					return 0;
				case M_FINIALIZE:
					return 0;
				case M_GETCLASS:
					return heap.getRefToConstString("java/lang/String");
				case M_HASHCODE:
					return id;
				case M_NOTIFY:
					notify();
					break;
				case M_NOTIFYALL:
					notifyAll();
					break;
				case M_WAIT:
					wait ();
					break;
				case M_WAIT_J:
					wait ( args[0] );
					break;
				case M_WAIT_JI:
					wait ( args[0], (int)args[1] );
			}
		}
		catch ( InterruptedException ie )
		{
			return -1;
		}
	return 0;
	}
}
