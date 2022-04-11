/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.util.Hashtable;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.utility.Mutex;
import maximum.utility.Integers;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;


/**
 *  ClassInstance is an instance of class Class, NOT an instance of a class.
 */
public class ClassInstance extends Mutex implements ReferenceType, Dispatch
{
	private static final int M_GETNAME = 999;
	
	private static Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()Ljava/lang/String;getName", Integers.getInt ( M_GETNAME ) );
	}
	
	int refCount;
	int id;
	//ClassInstance myClass;
	ClassDef refClass;
	ClassInstance refSuper;
	
	//ReferenceType mySuper;
	
	long[] classData;
	
	public ClassInstance ( HeapManager heap, int refId, ClassDef wrapped ) throws Exception
	{
		refClass = wrapped;
		id = refId;
		refCount = 0;
		//myClass = clsCls;
		try
		{
			//mySuper = heap.get ( heap.newInstance("java/lang/Object") );
			if ( wrapped.getSuperClass() != null )
			{
				// Object and null have null super classes
				refSuper = heap.forName ( wrapped.getSuperClass().getQName() );
			}
		}
		catch ( Exception cnfe )
		{
			ASSERT.fatal ( false, "ClassInstance", 127, "Can't happen" );
		}
		classData = new long[refClass.getStaticDataSize()];
	}
	
	public ReferenceType getSuper()
	{
		return refSuper;
	}
	
	public boolean implementsInterface( String clsName )
	{
		return refClass.implementsInterface( clsName );
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
		return refClass.getQName();
	}
	
	public ClassInstance getClassDef ()
	{
		return null;//myClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/Class" ) || className.equals ( "java/lang/Object") )
		{
			return 1;
		}
		return 0;
	}
	
	public long getField ( String name )
	{
		return classData[refClass.getFieldIndex(name)];
	}
	
	public void setField ( String name, long value )
	{
		classData[refClass.getFieldIndex(name)] = value;
	}
	
	public String getName ()
	{
		return refClass.getQName();
	}
	
	public ReferenceType createInstance ( int id, HeapManager heap ) throws IOException, ProgramException
	{
		return refClass.createInstance (id, heap, this );
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len )
	{
		Integer i = (Integer)methodMap.get ( desc );
		if ( i == null )
		{
			return refClass.callMethod ( heap, desc, args, pos, len, this );
		}
		else
		{
			return new DispatchFrame ( heap, i.intValue(), this, args, pos, len, this );
		}
	}
	
	public long call ( HeapManager heap, int method, int[] args, ClassInstance ci )
	{
		switch ( method )
		{
			case M_GETNAME:
				return 0;
		}
	return 0;
	}
}
