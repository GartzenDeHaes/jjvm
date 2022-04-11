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


public class SystemClass implements ClassDef, Dispatch
{
	private static final int M_ARRAYCOPY = 0;
	private static final int M_CURRENTTIME = 1;
	private static final int M_GC = 2;
	
	private static final int F_OUT = 0;
	private static final int F_ERR = 1;
	
	private static Hashtable methodMap;
	
	static 
	{
		methodMap = new Hashtable();
		methodMap.put ( "([Ljava/lang/Object;[Ljava/lang/Object;III)Varraycopy", Integers.getInt(M_ARRAYCOPY) );
		methodMap.put ( "()JcurrentTimeMillis", Integers.getInt ( M_CURRENTTIME ) );
		methodMap.put ( "()Vgc", Integers.getInt(M_GC) );
	}
	
	int refid;
	ClassDef superClass;
	
	public SystemClass ( ObjectClass sup )
	{
		superClass = sup;
	}
	
	public boolean implementsInterface( String clsName )
	{
		return false;
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
		return "java/lang/System";
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
	}
	
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		return new DispatchFrame(heap, ((Integer)methodMap.get(desc)).intValue(), this, args, pos, len, ci );
	}
	
	public int getFieldIndex ( String name )
	{
		if ( name.equals ( "out" ) )
		{
			return F_OUT;
		}
		else if ( name.equals ( "err" ) )
		{
			return F_ERR;
		}
		return -1;
	}
	
	public int getStaticDataSize ( )
	{
		return 2;
	}
	
	public ClassInstance createClassInstance ( int refId, HeapManager heap )
	{
		ClassInstance ci = null;
		try
		{
			ci = new ClassInstance ( heap, refId, this );
			ci.classData[0] = heap.newInstance ( "java/io/PrintWriter" );
			ci.classData[1] = heap.newInstance ( "java/io/PrintWriter" );
		}
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "SystemClass", 121, "Internal Error" );
		}
		return ci;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		return null;
	}
	
	public long call ( HeapManager heap, int methodId, int[] args, ClassInstance ci )
	{
		switch (methodId)
		{
			case M_ARRAYCOPY:
				break;
			case M_CURRENTTIME:
				return System.currentTimeMillis();
			case M_GC:
				System.gc();
				break;
		}
		return 0;
	}
}
