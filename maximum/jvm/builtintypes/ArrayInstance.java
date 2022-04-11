/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.IOException;

import maximum.utility.Mutex;
import maximum.utility.ASSERT;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.Type;

public class ArrayInstance extends Mutex implements ReferenceType, Dispatch
{
	int refCount;
	int id;
	ClassInstance myClass;
	ReferenceType mySuper;
	
	long[] array;
	int type;
	
	ArrayInstance ( HeapManager heap, int refId, ClassInstance cls, int dimensions, int basetype, int sized1, int sized2, int sized3 ) throws IOException
	{
		id = refId;
		refCount = 0;
		myClass = cls;
		
		array = new long[sized1];
		if ( dimensions > 1 )
		{
			type = Type.TYPE_REF;
			String desc = cls.getName().substring ( 1 );
			for ( int x = 0; x < sized1; x++ )
			{
				array[x] = heap.newArray ( desc, dimensions-1, basetype, sized2, sized3, 0 );
			}
		}
		else
		{
			type = basetype;
		}
		try
		{
			mySuper = heap.get ( heap.newInstance("java/lang/Object") );
		}
		catch ( ProgramException pe )
		{
			pe.printStackTrace();
			ASSERT.fatal ( false, "ArrayInstance", 52, "Internal Error" );
		}
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "ArrayInstance", 52, "Internal Error" );
		}
	}
	
	public void store ( int index, long value )
	{
		array[index] = value;
	}
	
	public long load ( int index )
	{
		return array[index];
	}
	
	public int getLength ()
	{
		return array.length;
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
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}
	
	public ReferenceType getSuper()
	{
		return mySuper;
	}
	
	public String getQName ()
	{
		return myClass.getQName();
	}
	
	public int instanceOf ( String className )
	{
		if ( className.equals ( "java/lang/array" ) || className.equals ( "java/lang/Object") )
		{
			return 1;
		}
		return 0;
	}
	
	public long getField ( String name )
	{
		return array.length;
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
		switch ( method )
		{
		}
		return 0;
	}
	
	String arrayToString ()
	{
		StringBuffer sb = new StringBuffer("");
		
		for ( int x = 0; x < array.length; x++ )
		{
			sb.append ((char)array[x]);
		}
		return sb.toString();
	}
}
