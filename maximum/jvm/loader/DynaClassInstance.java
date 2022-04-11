/*
 *  This software is Public Domain.
 */
package maximum.jvm.loader;

import java.util.Hashtable;
import java.io.IOException;

import maximum.utility.Mutex;
import maximum.utility.FastVector;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.ReferenceType;
import maximum.jvm.builtintypes.ClassInstance;

/**
 *  Instance of a loaded class
 */
public class DynaClassInstance extends Mutex implements ReferenceType
{
	int refCount;
	int id;
	ClassFile myClassFile;
	ClassInstance myClass;
	ReferenceType superClass;
	Hashtable methods;
	Hashtable fields = new Hashtable();
	
	DynaClassInstance ( ClassFile cf, int refId, HeapManager heap, ClassInstance cd, Hashtable methods, FastVector fieldTemplates ) throws ProgramException, IOException
	{
		myClassFile = cf;
		myClass = cd;
		refCount = 0;
		id = refId;
		this.methods = methods;
		
		for ( int x = 0; x < fieldTemplates.size(); x++ )
		{
			Field_Info fi = (Field_Info)fieldTemplates.elementAt ( x );
			fields.put ( fi.fieldName, new Field ( fi.type ) );
		}
		superClass = heap.get( heap.newInstance( myClass.getSuper().getQName() ) );
	}
	
	public int getRefId ()
	{
		return id;
	}
	
	public String getQName ()
	{
		return myClass.getQName();
	}
	
	public void addRef ()
	{
		refCount++;
	}
	
	public int getRefCount ()
	{
		return refCount;
	}

	public void release ()
	{
		refCount--;
	}
	
	public ClassInstance getClassDef ()
	{
		return myClass;
	}

	public ReferenceType getSuper()
	{
		return superClass;
	}
	
	public int instanceOf ( String className )
	{
		if ( myClass.getQName().equals ( className ) )
		{
			return 1;
		}
		if ( myClass.implementsInterface ( className ) )
		{
			return 1;
		}
		return superClass.instanceOf ( className );
	}
	
	public long getField ( String name )
	{
		return ((Field)fields.get ( name )).data;
	}
	
	public void setField ( String name, long value )
	{
		((Field)fields.get ( name )).data = value;
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len )
	{
		Method_Info mi = (Method_Info)methods.get ( desc );
		if ( mi == null )
		{
			return superClass.callMethod ( heap, desc, args, pos, len );
		}
		DynaFrame dm = new DynaFrame ( myClassFile, this, heap, mi , args, pos, len );
		return dm;
	}
}
