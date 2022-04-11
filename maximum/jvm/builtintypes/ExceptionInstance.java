/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import maximum.utility.ASSERT;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;


public class ExceptionInstance extends Mutex implements ReferenceType, Dispatch
{
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	ExceptionInstance ( HeapManager heap, int refId, ClassInstance cls )
	{
		id = refId;
		refCount = 0;
		myClass = cls;
		try
		{
			mySuper = heap.get ( heap.newInstance("java/lang/Throwable") );
		}
		catch ( Exception cnfe )
		{
			cnfe.printStackTrace();
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
		return myClass.getQName();
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Exception" ) || className.equals ( "java/lang/Object") )
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
		if ( desc.endsWith("<init>") )
		{
			DispatchFrame dm = new DispatchFrame ( heap, len, this, args, pos, len, myClass );
			return dm;
		}
		return mySuper.callMethod ( heap, desc, args, pos, len );
	}

	static final int M_INIT = 0;
	static final int M_INIT_S = 1;
		
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		switch ( method )
		{
			case M_INIT:
				return 0;
			case M_INIT_S:
				((ThrowableInstance)mySuper).call ( heap, method, args, null );
				return 0;
		}
	return 0;
	}
}
