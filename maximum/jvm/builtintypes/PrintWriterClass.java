/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.Hashtable;

import maximum.utility.ASSERT;
import maximum.utility.Integers;
import maximum.jvm.ClassDef;
import maximum.jvm.Frame;
import maximum.jvm.ReferenceType;
import maximum.jvm.HeapManager;
import maximum.jvm.ProgramException;
import maximum.jvm.Type;


public class PrintWriterClass implements ClassDef, Dispatch
{
	int refid;
	ClassDef superClass;
	
	public PrintWriterClass ( ObjectClass sup )
	{
		superClass = sup;
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
	
	public boolean implementsInterface( String clsName )
	{
		return false;
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
		return "java/io/PrintWriter";
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
			ASSERT.fatal ( false, "PrintWriterClass", 90, "Internal Error" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		return new PrintWriterInstance ( refId, ci, heap );
	}
	
	public long call ( HeapManager heap, int methodId, int[] args, ClassInstance ci )
	{
		return 0;
	}
}
