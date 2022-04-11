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


public class ObjectClass implements ClassDef, Dispatch
{	
	/**  Builtins don't need to be reloaded.
	 */
	public void reload ( DataInputStream dis ) throws IOException
	{
	}
	
	/** builtins don't care about references */
	public void addRef ()
	{
	}
	
	public void release ()
	{
	}
	
	public ClassDef getSuperClass ()
	{
		return null;
	}

	/**
	 *  Get the fully qualified name
	 */
	public String getQName ()
	{
		return "java/lang/Object";
	}
	
	public boolean implementsInterface( String clsName )
	{
		return false;
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
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
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "ObjectClass", 73, "Internal Error" );
		}
		return null;
	}

	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		return new DispatchFrame ( heap, 0, this, args, pos, len, ci );
	}
	
	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		ReferenceType rt = new ObjectInstance ( refId, ci );
		return rt;
	}

	public long call ( HeapManager heap, int methodId, int[] args, ClassInstance ci )
	{
		return 0;
	}
}
