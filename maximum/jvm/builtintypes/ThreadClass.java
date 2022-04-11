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


public class ThreadClass implements ClassDef, Dispatch
{
	private static final int M_ACTIVECOUNT = 0;
	private static final int M_CURRENTTHREAD = 1;
	private static final int M_SLEEP_L = 2;
	private static final int M_SLEEP_LI = 3;
	private static final int M_DUMPSTACK = 4;
	
	private static final int F_MAX_PRIORITY = 0;
	private static final int F_MIN_PRIORITY = 1;
	private static final int F_NORM_PRIORITY = 2;
	
	private static Hashtable methodMap;
	
	static
	{
		methodMap = new Hashtable();
		methodMap.put ( "()IactiveCount", Integers.getInt(M_ACTIVECOUNT) );
		methodMap.put ( "()Ljava/lang/Thread;currentThread", Integers.getInt(M_CURRENTTHREAD) );
		methodMap.put ( "(J)Vsleep", Integers.getInt(M_SLEEP_L) );
		methodMap.put ( "(JI)Vsleep", Integers.getInt(M_SLEEP_LI) );
		methodMap.put ( "()VdumpStack", Integers.getInt(M_DUMPSTACK) );
	}
	
	int refid;
	ClassDef superClass;
	
	public ThreadClass ( ObjectClass sup )
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
		return "java/lang/Thread";
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
	}
		
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci )
	{
		return new DispatchFrame( heap, ((Integer)methodMap.get(desc)).intValue(), this, args, pos, len, ci );
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
			ASSERT.fatal ( false, "ThreadClass", 111, "Internal Error" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		ReferenceType rt = new ThreadInstance ( heap, refId, ci );
		return rt;
	}
	
	public long call ( HeapManager heap, int methodId, int[] args, ClassInstance ci )
	{
		switch (methodId)
		{
			case M_ACTIVECOUNT:
			case M_CURRENTTHREAD:
			case M_SLEEP_L:
			case M_SLEEP_LI:
			case M_DUMPSTACK:
		}
		return 0;
	}
}
