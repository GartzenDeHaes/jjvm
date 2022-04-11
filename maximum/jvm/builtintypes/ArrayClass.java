/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.Type;
import maximum.jvm.loader.DescriptorParser;


public class ArrayClass implements ClassDef
{
	ClassDef superClass;
	String desc;
	int dimensions;
	int basetype;
	
	public ArrayClass ( ObjectClass sup, String desc )
	{
		superClass = (ClassDef)sup;
		this.desc = desc;
		Type t = DescriptorParser.parse1 ( desc );
		dimensions = t.arraydims;
		basetype = t.type;
	}
	
	public void reload ( DataInputStream dis ) throws IOException
	{
	}
	
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

	public String getQName ()
	{
		return "java/lang/Array";
	}

	public boolean implementsInterface( String clsName )
	{
		return false;
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
	}
			
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		return null;
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
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		try
		{
			return new ArrayInstance( heap, refId, ci, 1, 0, 0, 0, 0 );
		}
		catch ( Exception ioe )
		{
			ioe.printStackTrace();
			ASSERT.fatal ( false, "ArrayClass", 72, "Instance creation failed" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci, int sized1, int sized2, int sized3 )
	{
		try
		{
			return new ArrayInstance( heap, refId, ci, dimensions, basetype, sized1, sized2, sized3 );
		}
		catch ( Exception ioe )
		{
			ioe.printStackTrace();
			ASSERT.fatal ( false, "ArrayClass", 86, "Instance creation failed" );
		}
		return null;
	}
}
