/*
 *  This software is Public Domain.
 */
package maximum.jvm.builtintypes;

import java.io.DataInputStream;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.utility.Mutex;
import maximum.jvm.ReferenceType;
import maximum.jvm.ClassDef;
import maximum.jvm.HeapManager;
import maximum.jvm.Frame;
import maximum.jvm.ProgramException;


public class NullClass implements ClassDef
{
	NullInstance NULL = new NullInstance ( null );
	
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
		return null;
	}

	public String getQName ()
	{
		return "java/lang/null";
	}
	
	public boolean implementsInterface( String clsName )
	{
		return false;
	}
	
	public long getConstant ( int index, HeapManager heap )
	{
		return 0;
	}
		
	public Frame callMethod ( HeapManager heap, String desc, int[] args, int pos, int len, ClassInstance ci  )
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
			ASSERT.fatal ( false, "NullClass", 75, "Internal Error" );
		}
		return null;
	}

	public ReferenceType createInstance ( int refId, HeapManager heap, ClassInstance ci )
	{
		return NULL;
	}
	
	class NullInstance extends Mutex implements ReferenceType
	{
		ClassInstance cls;
		
		NullInstance ( ClassInstance ncls )
		{
			cls = ncls;
		}
		
		public ReferenceType getSuper()
		{
			return null;
		}
	
		public void addRef ()
		{
		}
		
		public void release ()
		{
		}

		public int getRefCount ()
		{
			return 1;
		}

		public int getRefId ()
		{
			return 0;
		}
	
		public String getQName ()
		{
			return "java/lang/null";
		}
	
		public ClassInstance getClassDef ()
		{
			return cls;
		}
		
		public int instanceOf ( String className )
		{
			return 0;
		}
	
		public long getContstant ( String name )
		{
			return 0;
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
			return null;
		}
	}
}
