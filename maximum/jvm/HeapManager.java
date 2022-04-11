/*
 *  This software is Public Domain.
 */
package maximum.jvm;

import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.IOException;

import maximum.utility.ASSERT;
import maximum.utility.FastVector;
import maximum.jvm.VProcess;
import maximum.jvm.builtintypes.ArrayInstance;
import maximum.jvm.builtintypes.ArrayClass;
import maximum.jvm.builtintypes.ClassInstance;
import maximum.jvm.builtintypes.StringClass;
import maximum.jvm.builtintypes.StringInstance;


/**
 *  This is used to locate instances by refid and for
 *  object pooling.
 */
public class HeapManager
{	
	private ClassFileDb classDb;
	private Hashtable loadedClasses = new Hashtable();
													
	private FastVector objs = new FastVector ( );
	private Hashtable strings = new Hashtable();
	private ReferenceType NULL;
	private ClassInstance classClass;
	private VProcess process;
	private String document;
	
	HeapManager ( ClassFileDb db, VProcess proc ) throws FileNotFoundException, IOException
	{
		this.document = document;
		classDb = db;
		process = proc;
		
		db.loadClassInstances ( this );
		
		try
		{
			NULL = getOrLoadClass( "java/lang/null" ).createInstance(0, this);
			classClass = getOrLoadClass ( "java/lang/Class" );
		}
		catch ( ProgramException pe )
		{
			pe.printStackTrace();
			ASSERT.fatal ( false, "HeapManager", 41, "Internal Error" );
		}
		objs.addElement ( NULL );
	}
	
	public ClassInstance getClassClass()
	{
		return classClass;
	}
	
	void addClassInstance ( ClassDef cd )
	{
		int id = objs.size();
		objs.addElement ( null );
		try
		{
			ClassInstance ci = cd.createClassInstance ( id, this );
			objs.setElementAt ( id, ci );
			loadedClasses.put ( cd.getQName(), ci );
		}
		catch ( Exception te )
		{
			te.printStackTrace();
			ASSERT.fatal ( false, "HeapManager", 75, "Internal Error" );
		}
	}
	
	public VProcess getProcess()
	{
		return process;
	}
		
	public int getRefToConstString ( String s )
	{
		StringInstance i = (StringInstance)strings.get ( s );
		if ( i == null )
		{
			try
			{
				ClassInstance sc = forName( "java/lang/String" );
				int id = objs.size();
				objs.addElement (null);
				i = (StringInstance)sc.createInstance( id, this );
				i.set(s);
				strings.put ( s, i );
				objs.setElementAt ( id, i );
			}
			catch ( ProgramException cnfe )
			{
				cnfe.printStackTrace();
				ASSERT.fatal ( false, "HeapManager", 43, "Can't happen" );
			}
			catch ( IOException ioe )
			{
				ioe.printStackTrace();
				ASSERT.fatal ( false, "HeapManager", 43, "Can't happen" );
			}
			catch ( Exception te )
			{
				te.printStackTrace();
				ASSERT.fatal ( false, "HeapManager", 109, "Can't happen" );				
			}
		}
		return i.getRefId();
	}
	
	/**
	 *  Get an instance of class Class.
	 */
	public synchronized ClassInstance forName ( String qname ) throws ProgramException
	{
		try
		{
			return getOrLoadClass ( qname );
		}
		catch ( Exception e )
		{
			try
			{
				throw new ProgramException( newInstance("java/lang/ClassNotFoundException") );
			}
			catch ( IOException ioe )
			{
				ioe.printStackTrace();
				ASSERT.fatal ( false, "HeapManager", 95, "Internal Error" );
			}
		}
		return null;
	}
	
	/**
	 *  Get a reference type.  The reference count of
	 *  the object is incremented.
	 */
	public synchronized ReferenceType get ( int refid )
	{
		ReferenceType rt = (ReferenceType)objs.elementAt ( refid );
		rt.addRef ();
		return rt;
	}
		
	/**
	 *  Call this when the ref count of an instance 
	 *  goes to zero.
	 */
	public synchronized void release ( int refid )
	{
		ReferenceType rt = (ReferenceType)objs.elementAt ( refid );
		rt.release ();
		if ( rt.getRefCount() == 0 && refid != 0 )
		{
			objs.setElementAt ( refid, NULL );
		}
	}
	
	public synchronized int newInstance ( String clsName ) throws ProgramException, IOException
	{
		ReferenceType rt;
		int refId = objs.size();
		objs.addElement ( null );
		try
		{
			rt = getOrLoadClass( clsName ).createInstance( refId, this );
		}
		catch ( FileNotFoundException fnfe )
		{
			throw new ProgramException( newInstance( "java/lang/ClassNotFoundException" ) );
		}
		objs.setElementAt ( refId, rt );
		return refId;
	}
	
	public synchronized int newArray ( String desc, int dimensions, int basetype, int sized1, int sized2, int sized3 )
	{
		ArrayInstance ai;
		int refId = objs.size();
		objs.addElement ( null );
		
		try
		{
			ArrayClass ac = (ArrayClass)classDb.get( "", desc, 0 );
			ClassInstance ci = getOrLoadClass ( desc );
			ai = (ArrayInstance)ac.createInstance( refId, this, ci, sized1, sized2, sized3 );
			objs.setElementAt ( refId, ai );
		}
		catch ( Exception fnfe )
		{
			//throw new ProgramException( newInstance("java/lang/FileNotFoundException") );
			fnfe.printStackTrace();
			ASSERT.fatal ( false, "HeapManager", 183, "Internal Error" );
		}
		return refId;
	}
	
	private ClassInstance getOrLoadClass ( String clsName ) throws ProgramException, IOException
	{
		try
		{
			ClassInstance ci = (ClassInstance)loadedClasses.get ( clsName );
			if ( ci != null )
			{
				return ci;
			}
			int id = objs.size();
			objs.addElement ( null );
			ci = classDb.get ( document, clsName, 5000 ).createClassInstance ( id, this );
			objs.setElementAt ( id, ci );
			loadedClasses.put ( clsName, ci );
			return ci;
		}
		catch ( FileNotFoundException fnfe )
		{
			throw new ProgramException ( this.newInstance ( "java/lang/ClassNotFoundException" ) );
		}
	}
}
