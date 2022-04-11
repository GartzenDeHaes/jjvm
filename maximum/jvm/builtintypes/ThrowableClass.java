/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.Type;
import maximum.jvm.ProgramException;


public class ThrowableClass implements ClassDef
{
	private Hashtable classesByName = new Hashtable ();
	private int refid;
	private ClassDef superClass;
	
	
	public ThrowableClass ( ObjectClass obj )
	{
		superClass = obj;
	}
		
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
		return superClass;
	}

	/**
	 *  Get the fully qualified name
	 */
	public String getQName ()
	{
		return "java/lang/Throwable";
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
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "ThrowableClass", 91, "Internal Error" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		ReferenceType rt = new ThrowableInstance ( heap, refId, ci );
		return rt;
	}
}
