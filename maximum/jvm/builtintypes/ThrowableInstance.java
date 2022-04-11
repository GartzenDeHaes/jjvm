/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;

import maximum.utility.Integers;
import maximum.utility.ASSERT;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;


public class ThrowableInstance extends Mutex implements ReferenceType, Dispatch
{
	static final int M_INIT = 0;
	static final int M_INIT_S = 1;
	static final int M_FILLINSTACKTRACE = 2;
	static final int M_GETLOCAL = 3;
	static final int M_GETMESSAGE = 4;
	static final int M_PRINTSTACKTRACE = 5;
	static final int M_TOSTRING = 6;
	
	static Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()V<init>", Integers.getInt ( M_INIT ) );
		methodMap.put ( "(Ljava/lang/String;)V<init>", Integers.getInt ( M_INIT_S ) );
		methodMap.put ( "()VfillInStackTrace", Integers.getInt ( M_FILLINSTACKTRACE ) );
		methodMap.put ( "()VgetLocalizedMessages", Integers.getInt ( M_GETLOCAL ) );
		methodMap.put ( "()Ljava/lang/String;getMessage", Integers.getInt ( M_GETMESSAGE ) );
		methodMap.put ( "()VprintStackTrace", Integers.getInt ( M_PRINTSTACKTRACE ) );
		methodMap.put ( "()Ljava/lang/String;toString", Integers.getInt ( M_TOSTRING ) );
	}
	
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	ThrowableInstance ( HeapManager heap, int refId, ClassInstance cls )
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
			ASSERT.fatal ( false, "ExceptionInstance", 29, "Can't happen" );
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
		return "java/lang/Throwable";
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Throwable" ) || 
			 className.equals ( "java/lang/Object") ||
			 className.equals ( "java/lang/Serializable") )
		{
			return 1;
		}
		return mySuper.instanceOf( className );
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
		if ( true )
		{
			DispatchFrame dm = new DispatchFrame ( heap, len, this, args, pos, len, myClass );
			return dm;
		}
		return mySuper.callMethod ( heap, desc, args, pos, len );
	}
	
	int message;
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		switch ( method )
		{
			case M_INIT:
				break;
			case M_INIT_S:
				message = (int)args[0];
				break;
			case M_FILLINSTACKTRACE:
				break;
			case M_GETLOCAL:
				break;
			case M_GETMESSAGE:
				return message;
			case M_PRINTSTACKTRACE:
				break;
			case M_TOSTRING:
				return message;
		}
	return 0;
	}
}
